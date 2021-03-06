package at.tugraz.netguard;

import java.util.HashMap;
import java.util.Map;

public class CipherSuiteLookupTable {

    public enum Insecurity {
        NONE("none"),
        UNKNOWN("unknown"),
        FEW_BIT("uses keys smaller than 128 bits in its encryption"),
        NULL("specifies no encryption at all for the connection"),
        NULL_AUTH("is open to man-in-the-middle attacks because it does not authenticate the server"),
        WEIRD_NSS("was meant to die with SSL 3.0 and is of unknown safety"),
        RC4("uses RC4 which has insecure biases in its output"),
        SWEET32("uses 3DES which is vulnerable to the Sweet32 attack");

        private String reason;

        Insecurity(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return this.reason;
        }
    }

    public static String getCipherSuiteName(int cipherSuiteId) {
        if(cipherSuiteId <= 0x001B) {
            return CipherSuiteIdLookup[cipherSuiteId];
        } else if(0x001E <= cipherSuiteId && cipherSuiteId <= 0x0046) {
            int offset = 2;
            return CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0x0067 <= cipherSuiteId && cipherSuiteId <= 0x006D) {
            int offset = 34;
            return CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0x0084 <= cipherSuiteId && cipherSuiteId <= 0x00C5) {
            int offset = 56;
            return CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0x00FF == cipherSuiteId) {
            int offset = 113;
            return CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0x5600 == cipherSuiteId) {
            int offset = 21873;
            return CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0xC001 <= cipherSuiteId && cipherSuiteId <= 0xC0B3) {
            int offset = 49009;
            return CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0xCCA8 <= cipherSuiteId && cipherSuiteId <= 0xCCAE) {
            int offset = 52069;
            return CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0xD001 <= cipherSuiteId && cipherSuiteId <= 0xD003) {
            int offset = 52919;
            return CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0xD005 == cipherSuiteId) {
            int offset = 52920;
            return CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0x001C <= cipherSuiteId && cipherSuiteId <= 0x001D) {
            return "Reserved to avoid conflicts with SSLv3";
        } else if((0x0047 <= cipherSuiteId && cipherSuiteId <= 0x004F) ||
                (0x0059 <= cipherSuiteId && cipherSuiteId <= 0x005C)) {
            return "Reserved to avoid conflicts with deployed implementations";
        } else if(0x0050 <= cipherSuiteId && cipherSuiteId <= 0x0058) {
            return "Reserved to avoid conflicts";
        } else if((0x0060 <= cipherSuiteId && cipherSuiteId <= 0x0066) ||
                (0xFEFE <= cipherSuiteId && cipherSuiteId <= 0xFEFF)) {
            return "Reserved to avoid conflicts with widely deployed implementations";
        } else if(0xFF00 <= cipherSuiteId && cipherSuiteId <= 0xFFFF) {
            return "Reserved for Private Use";
        } else {
            return "Unassigned";
        }
    }

    public static Insecurity getCipherSuiteInsecurity(int cipherSuiteId) {
        String name;
        if(0x0000 <= cipherSuiteId && cipherSuiteId <= 0x001B) {
            name = CipherSuiteIdLookup[cipherSuiteId];
        } else if(0x001E <= cipherSuiteId && cipherSuiteId <= 0x0046) {
            int offset = 2;
            name =  CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0x0067 <= cipherSuiteId && cipherSuiteId <= 0x006D) {
            int offset = 34;
            name =  CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0x0084 <= cipherSuiteId && cipherSuiteId <= 0x00C5) {
            int offset = 56;
            name =  CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0x00FF == cipherSuiteId) {
            int offset = 113;
            name = CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0x5600 == cipherSuiteId) {
            int offset = 21873;
            name = CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0xC001 <= cipherSuiteId && cipherSuiteId <= 0xC0B3) {
            int offset = 49009;
            name = CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0xCCA8 <= cipherSuiteId && cipherSuiteId <= 0xCCAE) {
            int offset = 52069;
            name = CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0xD001 <= cipherSuiteId && cipherSuiteId <= 0xD003) {
            int offset = 52919;
            name = CipherSuiteIdLookup[cipherSuiteId - offset];
        } else if(0xD005 == cipherSuiteId) {
            int offset = 52920;
            name = CipherSuiteIdLookup[cipherSuiteId - offset];
        } else {
            return Insecurity.UNKNOWN;
        }

        if(InsecurityMap.containsKey(name)) {
            return InsecurityMap.get(name);
        }
        return Insecurity.NONE;
    }

    private static final Map<String, Insecurity> InsecurityMap = new HashMap<String, Insecurity>() {{
            put("TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_DHE_DSS_WITH_DES_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_DHE_RSA_WITH_DES_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_DH_DSS_EXPORT_WITH_DES40_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_DH_DSS_WITH_DES_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_DH_RSA_EXPORT_WITH_DES40_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_DH_RSA_WITH_DES_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_DH_anon_EXPORT_WITH_RC4_40_MD5", Insecurity.FEW_BIT);
            put("TLS_DH_anon_WITH_DES_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_RSA_EXPORT_WITH_DES40_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_RSA_EXPORT_WITH_RC2_CBC_40_MD5", Insecurity.FEW_BIT);
            put("TLS_RSA_EXPORT_WITH_RC4_40_MD5", Insecurity.FEW_BIT);
            put("TLS_RSA_WITH_DES_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5", Insecurity.FEW_BIT);
            put("TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA", Insecurity.FEW_BIT);
            put("TLS_KRB5_EXPORT_WITH_RC4_40_MD5", Insecurity.FEW_BIT);
            put("TLS_KRB5_EXPORT_WITH_RC4_40_SHA", Insecurity.FEW_BIT);
            put("TLS_KRB5_WITH_DES_CBC_MD5", Insecurity.FEW_BIT);
            put("TLS_KRB5_WITH_DES_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_RSA_EXPORT1024_WITH_DES_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_DHE_DSS_EXPORT1024_WITH_DES_CBC_SHA", Insecurity.FEW_BIT);
            put("TLS_RSA_EXPORT1024_WITH_RC4_56_SHA", Insecurity.FEW_BIT);
            put("TLS_DHE_DSS_EXPORT1024_WITH_RC4_56_SHA", Insecurity.FEW_BIT);
            put("TLS_RSA_EXPORT1024_WITH_RC4_56_MD5", Insecurity.FEW_BIT);
            put("TLS_RSA_EXPORT1024_WITH_RC2_CBC_56_MD5", Insecurity.FEW_BIT);
            put("TLS_DHE_PSK_WITH_NULL_SHA", Insecurity.NULL);
            put("TLS_DHE_PSK_WITH_NULL_SHA256", Insecurity.NULL);
            put("TLS_DHE_PSK_WITH_NULL_SHA384", Insecurity.NULL);
            put("TLS_ECDHE_ECDSA_WITH_NULL_SHA", Insecurity.NULL);
            put("TLS_ECDHE_PSK_WITH_NULL_SHA", Insecurity.NULL);
            put("TLS_ECDHE_PSK_WITH_NULL_SHA256", Insecurity.NULL);
            put("TLS_ECDHE_PSK_WITH_NULL_SHA384", Insecurity.NULL);
            put("TLS_ECDHE_RSA_WITH_NULL_SHA", Insecurity.NULL);
            put("TLS_ECDH_ECDSA_WITH_NULL_SHA", Insecurity.NULL);
            put("TLS_ECDH_RSA_WITH_NULL_SHA", Insecurity.NULL);
            put("TLS_ECDH_anon_WITH_NULL_SHA", Insecurity.NULL);
            put("TLS_NULL_WITH_NULL_NULL", Insecurity.NULL);
            put("TLS_PSK_WITH_NULL_SHA", Insecurity.NULL);
            put("TLS_PSK_WITH_NULL_SHA256", Insecurity.NULL);
            put("TLS_PSK_WITH_NULL_SHA384", Insecurity.NULL);
            put("TLS_RSA_PSK_WITH_NULL_SHA", Insecurity.NULL);
            put("TLS_RSA_PSK_WITH_NULL_SHA256", Insecurity.NULL);
            put("TLS_RSA_PSK_WITH_NULL_SHA384", Insecurity.NULL);
            put("TLS_RSA_WITH_NULL_MD5", Insecurity.NULL);
            put("TLS_RSA_WITH_NULL_SHA", Insecurity.NULL);
            put("TLS_RSA_WITH_NULL_SHA256", Insecurity.NULL);
            put("TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_EXPORT_WITH_RC4_40_MD5", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_3DES_EDE_CBC_SHA", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_AES_128_CBC_SHA", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_AES_128_CBC_SHA256", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_AES_128_GCM_SHA256", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_AES_256_CBC_SHA", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_AES_256_CBC_SHA256", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_AES_256_GCM_SHA384", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_ARIA_128_CBC_SHA256", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_ARIA_128_GCM_SHA256", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_ARIA_256_CBC_SHA384", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_ARIA_256_GCM_SHA384", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_CAMELLIA_128_GCM_SHA256", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_CAMELLIA_256_GCM_SHA384", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_DES_CBC_SHA", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_RC4_128_MD5", Insecurity.NULL_AUTH);
            put("TLS_DH_anon_WITH_SEED_CBC_SHA", Insecurity.NULL_AUTH);
            put("TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA", Insecurity.NULL_AUTH);
            put("TLS_ECDH_anon_WITH_AES_128_CBC_SHA", Insecurity.NULL_AUTH);
            put("TLS_ECDH_anon_WITH_AES_256_CBC_SHA", Insecurity.NULL_AUTH);
            put("TLS_ECDH_anon_WITH_NULL_SHA", Insecurity.NULL_AUTH);
            put("TLS_ECDH_anon_WITH_RC4_128_SHA", Insecurity.NULL_AUTH);
            put("TLS_RSA_EXPORT_WITH_RC4_40_MD5", Insecurity.RC4);
            put("TLS_RSA_WITH_RC4_128_MD5", Insecurity.RC4);
            put("TLS_RSA_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_DH_anon_EXPORT_WITH_RC4_40_MD5", Insecurity.RC4);
            put("TLS_DH_anon_WITH_RC4_128_MD5", Insecurity.RC4);
            put("TLS_KRB5_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_KRB5_WITH_RC4_128_MD5", Insecurity.RC4);
            put("TLS_KRB5_EXPORT_WITH_RC4_40_SHA", Insecurity.RC4);
            put("TLS_KRB5_EXPORT_WITH_RC4_40_MD5", Insecurity.RC4);
            put("TLS_PSK_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_DHE_PSK_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_RSA_PSK_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_ECDH_ECDSA_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_ECDHE_ECDSA_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_ECDH_RSA_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_ECDHE_RSA_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_ECDH_anon_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_ECDHE_PSK_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_RSA_EXPORT1024_WITH_RC4_56_SHA", Insecurity.RC4);
            put("TLS_DHE_DSS_EXPORT1024_WITH_RC4_56_SHA", Insecurity.RC4);
            put("TLS_DHE_DSS_WITH_RC4_128_SHA", Insecurity.RC4);
            put("TLS_RSA_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_DH_DSS_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_DH_RSA_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_DH_anon_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_KRB5_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_KRB5_WITH_3DES_EDE_CBC_MD5", Insecurity.SWEET32);
            put("TLS_PSK_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_DHE_PSK_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_RSA_PSK_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_SRP_SHA_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_SRP_SHA_RSA_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_SRP_SHA_DSS_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("TLS_ECDHE_PSK_WITH_3DES_EDE_CBC_SHA", Insecurity.SWEET32);
            put("SSL_RSA_FIPS_WITH_DES_CBC_SHA", Insecurity.WEIRD_NSS);
            put("SSL_RSA_FIPS_WITH_3DES_EDE_CBC_SHA", Insecurity.WEIRD_NSS);
        }};

    private static final String[] CipherSuiteIdLookup = {
            "TLS_NULL_WITH_NULL_NULL",
            "TLS_RSA_WITH_NULL_MD5",
            "TLS_RSA_WITH_NULL_SHA",
            "TLS_RSA_EXPORT_WITH_RC4_40_MD5",
            "TLS_RSA_WITH_RC4_128_MD5",
            "TLS_RSA_WITH_RC4_128_SHA",
            "TLS_RSA_EXPORT_WITH_RC2_CBC_40_MD5",
            "TLS_RSA_WITH_IDEA_CBC_SHA",
            "TLS_RSA_EXPORT_WITH_DES40_CBC_SHA",
            "TLS_RSA_WITH_DES_CBC_SHA",
            "TLS_RSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_DH_DSS_EXPORT_WITH_DES40_CBC_SHA",
            "TLS_DH_DSS_WITH_DES_CBC_SHA",
            "TLS_DH_DSS_WITH_3DES_EDE_CBC_SHA",
            "TLS_DH_RSA_EXPORT_WITH_DES40_CBC_SHA",
            "TLS_DH_RSA_WITH_DES_CBC_SHA",
            "TLS_DH_RSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA",
            "TLS_DHE_DSS_WITH_DES_CBC_SHA",
            "TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA",
            "TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
            "TLS_DHE_RSA_WITH_DES_CBC_SHA",
            "TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_DH_anon_EXPORT_WITH_RC4_40_MD5",
            "TLS_DH_anon_WITH_RC4_128_MD5",
            "TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA",
            "TLS_DH_anon_WITH_DES_CBC_SHA",
            "TLS_DH_anon_WITH_3DES_EDE_CBC_SHA",
            "TLS_KRB5_WITH_DES_CBC_SHA",
            "TLS_KRB5_WITH_3DES_EDE_CBC_SHA",
            "TLS_KRB5_WITH_RC4_128_SHA",
            "TLS_KRB5_WITH_IDEA_CBC_SHA",
            "TLS_KRB5_WITH_DES_CBC_MD5",
            "TLS_KRB5_WITH_3DES_EDE_CBC_MD5",
            "TLS_KRB5_WITH_RC4_128_MD5",
            "TLS_KRB5_WITH_IDEA_CBC_MD5",
            "TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA",
            "TLS_KRB5_EXPORT_WITH_RC2_CBC_40_SHA",
            "TLS_KRB5_EXPORT_WITH_RC4_40_SHA",
            "TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5",
            "TLS_KRB5_EXPORT_WITH_RC2_CBC_40_MD5",
            "TLS_KRB5_EXPORT_WITH_RC4_40_MD5",
            "TLS_PSK_WITH_NULL_SHA",
            "TLS_DHE_PSK_WITH_NULL_SHA",
            "TLS_RSA_PSK_WITH_NULL_SHA",
            "TLS_RSA_WITH_AES_128_CBC_SHA",
            "TLS_DH_DSS_WITH_AES_128_CBC_SHA",
            "TLS_DH_RSA_WITH_AES_128_CBC_SHA",
            "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",
            "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
            "TLS_DH_anon_WITH_AES_128_CBC_SHA",
            "TLS_RSA_WITH_AES_256_CBC_SHA",
            "TLS_DH_DSS_WITH_AES_256_CBC_SHA",
            "TLS_DH_RSA_WITH_AES_256_CBC_SHA",
            "TLS_DHE_DSS_WITH_AES_256_CBC_SHA",
            "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
            "TLS_DH_anon_WITH_AES_256_CBC_SHA",
            "TLS_RSA_WITH_NULL_SHA256",
            "TLS_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_RSA_WITH_AES_256_CBC_SHA256",
            "TLS_DH_DSS_WITH_AES_128_CBC_SHA256",
            "TLS_DH_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_DHE_DSS_WITH_AES_128_CBC_SHA256",
            "TLS_RSA_WITH_CAMELLIA_128_CBC_SHA",
            "TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA",
            "TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA",
            "TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA",
            "TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA",
            "TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA",
            "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_DH_DSS_WITH_AES_256_CBC_SHA256",
            "TLS_DH_RSA_WITH_AES_256_CBC_SHA256",
            "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256",
            "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256",
            "TLS_DH_anon_WITH_AES_128_CBC_SHA256",
            "TLS_DH_anon_WITH_AES_256_CBC_SHA256",
            "TLS_RSA_WITH_CAMELLIA_256_CBC_SHA",
            "TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA",
            "TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA",
            "TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA",
            "TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA",
            "TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA",
            "TLS_PSK_WITH_RC4_128_SHA",
            "TLS_PSK_WITH_3DES_EDE_CBC_SHA",
            "TLS_PSK_WITH_AES_128_CBC_SHA",
            "TLS_PSK_WITH_AES_256_CBC_SHA",
            "TLS_DHE_PSK_WITH_RC4_128_SHA",
            "TLS_DHE_PSK_WITH_3DES_EDE_CBC_SHA",
            "TLS_DHE_PSK_WITH_AES_128_CBC_SHA",
            "TLS_DHE_PSK_WITH_AES_256_CBC_SHA",
            "TLS_RSA_PSK_WITH_RC4_128_SHA",
            "TLS_RSA_PSK_WITH_3DES_EDE_CBC_SHA",
            "TLS_RSA_PSK_WITH_AES_128_CBC_SHA",
            "TLS_RSA_PSK_WITH_AES_256_CBC_SHA",
            "TLS_RSA_WITH_SEED_CBC_SHA",
            "TLS_DH_DSS_WITH_SEED_CBC_SHA",
            "TLS_DH_RSA_WITH_SEED_CBC_SHA",
            "TLS_DHE_DSS_WITH_SEED_CBC_SHA",
            "TLS_DHE_RSA_WITH_SEED_CBC_SHA",
            "TLS_DH_anon_WITH_SEED_CBC_SHA",
            "TLS_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_RSA_WITH_AES_256_GCM_SHA384",
            "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384",
            "TLS_DH_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_DH_RSA_WITH_AES_256_GCM_SHA384",
            "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256",
            "TLS_DHE_DSS_WITH_AES_256_GCM_SHA384",
            "TLS_DH_DSS_WITH_AES_128_GCM_SHA256",
            "TLS_DH_DSS_WITH_AES_256_GCM_SHA384",
            "TLS_DH_anon_WITH_AES_128_GCM_SHA256",
            "TLS_DH_anon_WITH_AES_256_GCM_SHA384",
            "TLS_PSK_WITH_AES_128_GCM_SHA256",
            "TLS_PSK_WITH_AES_256_GCM_SHA384",
            "TLS_DHE_PSK_WITH_AES_128_GCM_SHA256",
            "TLS_DHE_PSK_WITH_AES_256_GCM_SHA384",
            "TLS_RSA_PSK_WITH_AES_128_GCM_SHA256",
            "TLS_RSA_PSK_WITH_AES_256_GCM_SHA384",
            "TLS_PSK_WITH_AES_128_CBC_SHA256",
            "TLS_PSK_WITH_AES_256_CBC_SHA384",
            "TLS_PSK_WITH_NULL_SHA256",
            "TLS_PSK_WITH_NULL_SHA384",
            "TLS_DHE_PSK_WITH_AES_128_CBC_SHA256",
            "TLS_DHE_PSK_WITH_AES_256_CBC_SHA384",
            "TLS_DHE_PSK_WITH_NULL_SHA256",
            "TLS_DHE_PSK_WITH_NULL_SHA384",
            "TLS_RSA_PSK_WITH_AES_128_CBC_SHA256",
            "TLS_RSA_PSK_WITH_AES_256_CBC_SHA384",
            "TLS_RSA_PSK_WITH_NULL_SHA256",
            "TLS_RSA_PSK_WITH_NULL_SHA384",
            "TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256",
            "TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256",
            "TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256",
            "TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256",
            "TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256",
            "TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256",
            "TLS_EMPTY_RENEGOTIATION_INFO_SCSV",
            "TLS_FALLBACK_SCSV",
            "TLS_ECDH_ECDSA_WITH_NULL_SHA",
            "TLS_ECDH_ECDSA_WITH_RC4_128_SHA",
            "TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDHE_ECDSA_WITH_NULL_SHA",
            "TLS_ECDHE_ECDSA_WITH_RC4_128_SHA",
            "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDH_RSA_WITH_NULL_SHA",
            "TLS_ECDH_RSA_WITH_RC4_128_SHA",
            "TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDH_RSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_NULL_SHA",
            "TLS_ECDHE_RSA_WITH_RC4_128_SHA",
            "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
            "TLS_ECDH_anon_WITH_NULL_SHA",
            "TLS_ECDH_anon_WITH_RC4_128_SHA",
            "TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDH_anon_WITH_AES_128_CBC_SHA",
            "TLS_ECDH_anon_WITH_AES_256_CBC_SHA",
            "TLS_SRP_SHA_WITH_3DES_EDE_CBC_SHA",
            "TLS_SRP_SHA_RSA_WITH_3DES_EDE_CBC_SHA",
            "TLS_SRP_SHA_DSS_WITH_3DES_EDE_CBC_SHA",
            "TLS_SRP_SHA_WITH_AES_128_CBC_SHA",
            "TLS_SRP_SHA_RSA_WITH_AES_128_CBC_SHA",
            "TLS_SRP_SHA_DSS_WITH_AES_128_CBC_SHA",
            "TLS_SRP_SHA_WITH_AES_256_CBC_SHA",
            "TLS_SRP_SHA_RSA_WITH_AES_256_CBC_SHA",
            "TLS_SRP_SHA_DSS_WITH_AES_256_CBC_SHA",
            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384",
            "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384",
            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
            "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384",
            "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
            "TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384",
            "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
            "TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384",
            "TLS_ECDHE_PSK_WITH_RC4_128_SHA",
            "TLS_ECDHE_PSK_WITH_3DES_EDE_CBC_SHA",
            "TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA",
            "TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA",
            "TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA256",
            "TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA384",
            "TLS_ECDHE_PSK_WITH_NULL_SHA",
            "TLS_ECDHE_PSK_WITH_NULL_SHA256",
            "TLS_ECDHE_PSK_WITH_NULL_SHA384",
            "TLS_RSA_WITH_ARIA_128_CBC_SHA256",
            "TLS_RSA_WITH_ARIA_256_CBC_SHA384",
            "TLS_DH_DSS_WITH_ARIA_128_CBC_SHA256",
            "TLS_DH_DSS_WITH_ARIA_256_CBC_SHA384",
            "TLS_DH_RSA_WITH_ARIA_128_CBC_SHA256",
            "TLS_DH_RSA_WITH_ARIA_256_CBC_SHA384",
            "TLS_DHE_DSS_WITH_ARIA_128_CBC_SHA256",
            "TLS_DHE_DSS_WITH_ARIA_256_CBC_SHA384",
            "TLS_DHE_RSA_WITH_ARIA_128_CBC_SHA256",
            "TLS_DHE_RSA_WITH_ARIA_256_CBC_SHA384",
            "TLS_DH_anon_WITH_ARIA_128_CBC_SHA256",
            "TLS_DH_anon_WITH_ARIA_256_CBC_SHA384",
            "TLS_ECDHE_ECDSA_WITH_ARIA_128_CBC_SHA256",
            "TLS_ECDHE_ECDSA_WITH_ARIA_256_CBC_SHA384",
            "TLS_ECDH_ECDSA_WITH_ARIA_128_CBC_SHA256",
            "TLS_ECDH_ECDSA_WITH_ARIA_256_CBC_SHA384",
            "TLS_ECDHE_RSA_WITH_ARIA_128_CBC_SHA256",
            "TLS_ECDHE_RSA_WITH_ARIA_256_CBC_SHA384",
            "TLS_ECDH_RSA_WITH_ARIA_128_CBC_SHA256",
            "TLS_ECDH_RSA_WITH_ARIA_256_CBC_SHA384",
            "TLS_RSA_WITH_ARIA_128_GCM_SHA256",
            "TLS_RSA_WITH_ARIA_256_GCM_SHA384",
            "TLS_DHE_RSA_WITH_ARIA_128_GCM_SHA256",
            "TLS_DHE_RSA_WITH_ARIA_256_GCM_SHA384",
            "TLS_DH_RSA_WITH_ARIA_128_GCM_SHA256",
            "TLS_DH_RSA_WITH_ARIA_256_GCM_SHA384",
            "TLS_DHE_DSS_WITH_ARIA_128_GCM_SHA256",
            "TLS_DHE_DSS_WITH_ARIA_256_GCM_SHA384",
            "TLS_DH_DSS_WITH_ARIA_128_GCM_SHA256",
            "TLS_DH_DSS_WITH_ARIA_256_GCM_SHA384",
            "TLS_DH_anon_WITH_ARIA_128_GCM_SHA256",
            "TLS_DH_anon_WITH_ARIA_256_GCM_SHA384",
            "TLS_ECDHE_ECDSA_WITH_ARIA_128_GCM_SHA256",
            "TLS_ECDHE_ECDSA_WITH_ARIA_256_GCM_SHA384",
            "TLS_ECDH_ECDSA_WITH_ARIA_128_GCM_SHA256",
            "TLS_ECDH_ECDSA_WITH_ARIA_256_GCM_SHA384",
            "TLS_ECDHE_RSA_WITH_ARIA_128_GCM_SHA256",
            "TLS_ECDHE_RSA_WITH_ARIA_256_GCM_SHA384",
            "TLS_ECDH_RSA_WITH_ARIA_128_GCM_SHA256",
            "TLS_ECDH_RSA_WITH_ARIA_256_GCM_SHA384",
            "TLS_PSK_WITH_ARIA_128_CBC_SHA256",
            "TLS_PSK_WITH_ARIA_256_CBC_SHA384",
            "TLS_DHE_PSK_WITH_ARIA_128_CBC_SHA256",
            "TLS_DHE_PSK_WITH_ARIA_256_CBC_SHA384",
            "TLS_RSA_PSK_WITH_ARIA_128_CBC_SHA256",
            "TLS_RSA_PSK_WITH_ARIA_256_CBC_SHA384",
            "TLS_PSK_WITH_ARIA_128_GCM_SHA256",
            "TLS_PSK_WITH_ARIA_256_GCM_SHA384",
            "TLS_DHE_PSK_WITH_ARIA_128_GCM_SHA256",
            "TLS_DHE_PSK_WITH_ARIA_256_GCM_SHA384",
            "TLS_RSA_PSK_WITH_ARIA_128_GCM_SHA256",
            "TLS_RSA_PSK_WITH_ARIA_256_GCM_SHA384",
            "TLS_ECDHE_PSK_WITH_ARIA_128_CBC_SHA256",
            "TLS_ECDHE_PSK_WITH_ARIA_256_CBC_SHA384",
            "TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_CBC_SHA384",
            "TLS_ECDH_ECDSA_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_ECDH_ECDSA_WITH_CAMELLIA_256_CBC_SHA384",
            "TLS_ECDHE_RSA_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_ECDHE_RSA_WITH_CAMELLIA_256_CBC_SHA384",
            "TLS_ECDH_RSA_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_ECDH_RSA_WITH_CAMELLIA_256_CBC_SHA384",
            "TLS_RSA_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_RSA_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_DHE_RSA_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_DHE_RSA_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_DH_RSA_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_DH_RSA_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_DHE_DSS_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_DHE_DSS_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_DH_DSS_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_DH_DSS_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_DH_anon_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_DH_anon_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_ECDH_ECDSA_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_ECDH_ECDSA_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_ECDHE_RSA_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_ECDHE_RSA_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_ECDH_RSA_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_ECDH_RSA_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_PSK_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_PSK_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_DHE_PSK_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_DHE_PSK_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_RSA_PSK_WITH_CAMELLIA_128_GCM_SHA256",
            "TLS_RSA_PSK_WITH_CAMELLIA_256_GCM_SHA384",
            "TLS_PSK_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_PSK_WITH_CAMELLIA_256_CBC_SHA384",
            "TLS_DHE_PSK_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_DHE_PSK_WITH_CAMELLIA_256_CBC_SHA384",
            "TLS_RSA_PSK_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_RSA_PSK_WITH_CAMELLIA_256_CBC_SHA384",
            "TLS_ECDHE_PSK_WITH_CAMELLIA_128_CBC_SHA256",
            "TLS_ECDHE_PSK_WITH_CAMELLIA_256_CBC_SHA384",
            "TLS_RSA_WITH_AES_128_CCM",
            "TLS_RSA_WITH_AES_256_CCM",
            "TLS_DHE_RSA_WITH_AES_128_CCM",
            "TLS_DHE_RSA_WITH_AES_256_CCM",
            "TLS_RSA_WITH_AES_128_CCM_8",
            "TLS_RSA_WITH_AES_256_CCM_8",
            "TLS_DHE_RSA_WITH_AES_128_CCM_8",
            "TLS_DHE_RSA_WITH_AES_256_CCM_8",
            "TLS_PSK_WITH_AES_128_CCM",
            "TLS_PSK_WITH_AES_256_CCM",
            "TLS_DHE_PSK_WITH_AES_128_CCM",
            "TLS_DHE_PSK_WITH_AES_256_CCM",
            "TLS_PSK_WITH_AES_128_CCM_8",
            "TLS_PSK_WITH_AES_256_CCM_8",
            "TLS_PSK_DHE_WITH_AES_128_CCM_8",
            "TLS_PSK_DHE_WITH_AES_256_CCM_8",
            "TLS_ECDHE_ECDSA_WITH_AES_128_CCM",
            "TLS_ECDHE_ECDSA_WITH_AES_256_CCM",
            "TLS_ECDHE_ECDSA_WITH_AES_128_CCM_8",
            "TLS_ECDHE_ECDSA_WITH_AES_256_CCM_8",
            "TLS_ECCPWD_WITH_AES_128_GCM_SHA256",
            "TLS_ECCPWD_WITH_AES_256_GCM_SHA384",
            "TLS_ECCPWD_WITH_AES_128_CCM_SHA256",
            "TLS_ECCPWD_WITH_AES_256_CCM_SHA384",
            "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256",
            "TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256",
            "TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256",
            "TLS_PSK_WITH_CHACHA20_POLY1305_SHA256",
            "TLS_ECDHE_PSK_WITH_CHACHA20_POLY1305_SHA256",
            "TLS_DHE_PSK_WITH_CHACHA20_POLY1305_SHA256",
            "TLS_RSA_PSK_WITH_CHACHA20_POLY1305_SHA256",
            "TLS_ECDHE_PSK_WITH_AES_128_GCM_SHA256",
            "TLS_ECDHE_PSK_WITH_AES_256_GCM_SHA384",
            "TLS_ECDHE_PSK_WITH_AES_128_CCM_8_SHA256",
            "TLS_ECDHE_PSK_WITH_AES_128_CCM_SHA256",
    };
}
