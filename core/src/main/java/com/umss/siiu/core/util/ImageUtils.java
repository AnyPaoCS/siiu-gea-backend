package com.umss.siiu.core.util;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    private ImageUtils () {}

    public static Byte[] inputStreamToByteArray(InputStream file) throws IOException {
        byte[] fileBytes = StreamUtils.copyToByteArray(file);
        var bytes = new Byte[fileBytes.length];
        var i = 0;
        for (Byte aByte : fileBytes) {
            bytes[i++] = aByte;
        }
        return bytes;
    }
}
