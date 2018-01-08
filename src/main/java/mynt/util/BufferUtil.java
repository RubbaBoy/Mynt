package mynt.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class BufferUtil {

    private BufferUtil() {

    }

    public static void putString(ByteArrayOutputStream stream, String string) {
        putVarInt(stream, string.length());

        byte[] b = string.getBytes(Charset.defaultCharset());

        stream.write(b, 0, b.length);
    }

    public static void putVarInt(ByteArrayOutputStream stream, int value) {
        putVarLong(stream, value);
    }

    public static void putVarLong(ByteArrayOutputStream stream, long value) {
        do {
            byte temp = (byte) (value & 0b01111111);

            value >>>= 7;

            if (value != 0) {
                temp |= 0b10000000;
            }

            stream.write(temp);
        } while (value != 0);
    }

}
