package at.tugraz.netguard;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import eu.faircode.netguard.DatabaseHelper;
import eu.faircode.netguard.R;
import eu.faircode.netguard.Rule;

public class ACNUtils {
    private static final String TAG = "NetGuard.ACNUtils";

    public static Context context = null;


    static {
        try {
            System.loadLibrary("netguard");
        } catch (UnsatisfiedLinkError ignored) {
            System.exit(1);
        }
    }

    public static native void enableSecurityAnalysis(boolean val);
    public static native void setIMEI(String imei);
    public static native void setPhoneNumber(String phoneNumber);
    public static native void updateKeywords(int uid, String[] keyword);

    public static String getIMEI() {
        if (context == null) return "";

        String imei = "";

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = tm.getImei();
            } else {
                imei = tm.getDeviceId();
            }
        } else {
            Log.e(TAG, "Permission READ_PHONE_STATE is missing");
        }

        if (imei.compareToIgnoreCase("000000000000000") == 0) // emulator = regex
            imei = "";

        return imei;
    }

    public static String getPhoneNumber() {
        if (context == null || getIMEI().isEmpty()) return ""; // no imei = use phone regex

        String number = "";

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            number = tm.getLine1Number();
        } else {
            Log.e(TAG, "Permission READ_PHONE_STATE is missing");
        }

        if (number == null || !number.matches("^\\d+$"))
            number = "";

        return number;
    }

    public static void prepareNativeSide(Context context) {
        // set imei and phone number
        ACNUtils.setIMEI(ACNUtils.getIMEI());
        ACNUtils.setPhoneNumber(ACNUtils.getPhoneNumber());

        //update native code keywords array for all apps
        List<Rule> apps =  Rule.getRules(false, context);
        for (Rule app : apps) {
            Cursor cursor = DatabaseHelper.getInstance(context).getKeywords(app.uid);
            final int colKeywords = cursor.getColumnIndex("keyword");
            List<String> keywords = new ArrayList<String>();

            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String keyword = cursor.getString(colKeywords);

                if (!keyword.equals(context.getResources().getString(R.string.keyword_imei)) &&
                        !keyword.equals(context.getResources().getString(R.string.keyword_phone_number))) {

                    keywords.add(keyword);
                }
                cursor.moveToNext();
            }

            ACNUtils.updateKeywords(app.uid, keywords.toArray(new String[0]));
        }
    }

    public static byte[] objectToByteArray(Object o)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] object_bytes = new byte[0];

        if (o == null) return object_bytes;

        try {
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(o);
                out.flush();
                object_bytes = bos.toByteArray();
            } finally {
                bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return object_bytes;
    }

    public static Object byteArrayToObject(byte[] object_bytes)
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(object_bytes);
        ObjectInput in = null;
        Object o = null;

        if (object_bytes == null || object_bytes.length == 0) return o;

        try {
            try {
                in = new ObjectInputStream(bis);
                o = in.readObject();
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return o;
    }

    public static String getTLSVersion(int version) {
        switch(version) {
            case 0x0300:
                return "SSL 3.0";
            case 0x0301:
                return "TLS 1.0";
            case 0x0302:
                return "TLS 1.1";
            case 0x0303:
                return "TLS 1.2";
            default:
                return "Unknown";
        }
    }
}
