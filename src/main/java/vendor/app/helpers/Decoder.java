package vendor.app.helpers;

import java.util.Base64;

public interface Decoder {
    default String decodeToBase64(byte[] hash) {
        return Base64.getEncoder().encodeToString(hash);
    }
}
