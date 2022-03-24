/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

    private StreamUtils() {

    }

    public static InputStream toInputStream(OutputStream outputStream) {
        return new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());
    }
}
