package utils;

import play.Logger;
import play.Play;

import java.math.BigInteger;

public class XorEncryption {
    private final static String CIPHER_KEY = "saveForLater.uuid.secret.key";
    private final static String PADDED_32_ZEROS = "00000000000000000000000000000000";
    private final static int HEX_RADIX = 16;
    private final static int UUID_LENGTH = 36;
    private final static int BIGINT_MINLENGTH = 36;


    private static BigInteger getCipherKey() {
        String CIPHER = Play.application().configuration().getString(CIPHER_KEY);
        if (CIPHER == null || CIPHER.length() != UUID_LENGTH || CIPHER.matches(".*[A-Z].*")) {
            Logger.error("ERROR - XorEncryption Invalid key - \"" + CIPHER + "\" for \"" + CIPHER_KEY + "\" from properties. Unable to encrypt/decrypt uuid for save-for-later");
            return null;
        } else {
            return uuidToBigInteger(CIPHER);
        }
    }

    private static BigInteger uuidToBigInteger(String uuid) {
        if (uuid == null || uuid.length() != UUID_LENGTH) {
            Logger.error("Unable to xor encrypt uuid:\"" + uuid + "\" which is null or not " + UUID_LENGTH + "bytes long");
            return null;
        }
        // Since we expect to use a lowercase uuid cipher key if we use an uppercase uuid it will come back as lowercase.
        // So we need to block this case.
        else if (uuid.matches(".*[A-Z].*")) {
            Logger.error("Unable to xor encrypt uppercase uuid:\"" + uuid + "\" using a lowercase key");
            return null;
        }
        String stripped = uuid.replace("-", "");
        return new BigInteger(stripped, HEX_RADIX);
    }

    private static String bigIntegerToUuid(BigInteger bigInt) {
        if (bigInt == null) {
            Logger.error("Unable to xor decrypt null key");
            return null;
        }
        String unformatted = bigInt.toString(HEX_RADIX);
        String padded = PADDED_32_ZEROS.substring(unformatted.length()) + unformatted;
        String formatted = padded.substring(0, 8)
                + "-" + padded.substring(8, 12)
                + "-" + padded.substring(12, 16)
                + "-" + padded.substring(16, 20)
                + "-" + padded.substring(20);
        return formatted;
    }

    /*
     * The decrypt process does the reverse of the encrypt
     * Expects a 40 long bigint as a string i.e. "174650142322392746796619227917559908601".
     * Which is bitwise xored with the secret key ( which has also converted to bigint )
     * Its converted to 32 byte hex BigInteger and "-" added back in after 8/12/16/20 chars.
     *
     * i.e. 174650142322392746796619227917559908601 decimal becomes hex 836464D5E9264BB49322BE67AE59C8F9
     * Which is xored with the secret key of say 88a976e1-e926-4bb4-9322-15aabc6d0516
     * 88a976e1e9264bb4932215aabc6d0516 xor 836464d5e9264bb49322be67ae59c8f9 = 0bcd1234000000000000abcd1234cdef
     * Which is back to the original ( unencrypted ) uuid
     */
    public static String decryptUuid(String encryptedUuid) {
        if (getCipherKey() == null) {
            Logger.error("Invalid CIPHER key unable to decrypt uuid");
            return "";
        }
        if (encryptedUuid == null || encryptedUuid.length() < BIGINT_MINLENGTH) {
            Logger.error("Unable to xor decrypt uuid:\"" + encryptedUuid + "\" which is " +
                    "null or less than " + BIGINT_MINLENGTH + "bytes long");
            return "";
        }
        BigInteger n = new BigInteger(encryptedUuid);
        if (n == null) {
            return "";
        } else {
            BigInteger decrypted = n.xor(getCipherKey());
            return (bigIntegerToUuid(decrypted));
        }
    }

    /*
    * Expects uuid format string i.e. "88a976e1-e926-4bb4-9322-15aabc6d0516"
    * It is stripped of "-" and converted to bigint with 16 radix ( hex )
    * And it is bitwise xored with the secret key ( which has also been converted to bigint )
    * The resultant bigint is then returned as the "encrypted" uuid.
    *
    * i.e. secretkey="88a976e1-e926-4bb4-9322-15aabc6d0516" and uuid="0bcd1234-0000-0000-0000-abcd1234cdef"
    * 88a976e1e9264bb4932215aabc6d0516 xor 0bcd1234000000000000abcd1234cdef = 836464d5e9264bb49322be67ae59c8f9
    * = -> 174650142322392746796619227917559908601 as a bigint
    */
    public static String encryptUuid(String uuid) {
        if (getCipherKey() == null) {
            Logger.error("Invalid CIPHER key unable to encrypt uuid");
            return "";
        }
        if (uuid == null || uuid.length() != UUID_LENGTH) {
            Logger.error("Unable to xor decrypt uuid:\"" + uuid + "\" which is null or not " + UUID_LENGTH + "bytes long");
            return "";
        }
        BigInteger n = uuidToBigInteger(uuid);
        if (n == null) {
            return "";
        } else {
            return (String.valueOf(n.xor(getCipherKey())));
        }
    }
}

