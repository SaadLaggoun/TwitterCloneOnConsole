package vendor.app.helpers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Encoder {
    default byte[] hash256(String string) {
        // Hashing with SHA-256
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;
        byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));

        return hash;
    }
}
