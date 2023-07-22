package com.rubisco.jttpd;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class MyURLDecoder {
    public static int hex2int(byte b1, byte b2) {
        int digit;
        if(b1 >= 'A') {
            // & 0xDF are used to convert lowercase to uppercase
            digit = (b1 & 0xDF) - 'A' + 10;
        } else {
            digit = (b1 - '0');
        }
        digit *= 16;
        if (b2 >= 'A') {
            digit += (b2 & 0xDF) - 'A' + 10;
        } else {
            digit += (b2 - '0');
        }
        return digit;
    }

    public static String decode(String src, String enc)
            throws UnsupportedEncodingException {
        byte[] srcBytes = src.getBytes("ISO_8859_1");
        // allocate srcBytes.length bytes because result will be smaller than
        byte[] destBytes = new byte[srcBytes.length];

        int destIdx = 0;
        for (int i = 0; i < srcBytes.length; i++) {
            if (srcBytes[i] == (byte)'%') {
                destBytes[destIdx] = (byte) hex2int(srcBytes[i + 1],
                        srcBytes[i + 2]);
                i += 2;
            } else {
                destBytes[destIdx] = srcBytes[i];
            }
            destIdx++;
        }
        byte[] destBytes2 = Arrays.copyOf(destBytes, destIdx);

        return new String(destBytes2, enc);
    }
}
