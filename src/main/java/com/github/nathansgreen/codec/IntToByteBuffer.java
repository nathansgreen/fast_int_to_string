package com.github.nathansgreen.codec;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

/**
 * Writes integers to byte buffers.
 */
public class IntToByteBuffer {
    private static final byte[][] digitsInAscii = new byte[][]{
            {'0', '0'}, {'0', '1'}, {'0', '2'}, {'0', '3'}, {'0', '4'},
            {'0', '5'}, {'0', '6'}, {'0', '7'}, {'0', '8'}, {'0', '9'},
            {'1', '0'}, {'1', '1'}, {'1', '2'}, {'1', '3'}, {'1', '4'},
            {'1', '5'}, {'1', '6'}, {'1', '7'}, {'1', '8'}, {'1', '9'},
            {'2', '0'}, {'2', '1'}, {'2', '2'}, {'2', '3'}, {'2', '4'},
            {'2', '5'}, {'2', '6'}, {'2', '7'}, {'2', '8'}, {'2', '9'},
            {'3', '0'}, {'3', '1'}, {'3', '2'}, {'3', '3'}, {'3', '4'},
            {'3', '5'}, {'3', '6'}, {'3', '7'}, {'3', '8'}, {'3', '9'},
            {'4', '0'}, {'4', '1'}, {'4', '2'}, {'4', '3'}, {'4', '4'},
            {'4', '5'}, {'4', '6'}, {'4', '7'}, {'4', '8'}, {'4', '9'},
            {'5', '0'}, {'5', '1'}, {'5', '2'}, {'5', '3'}, {'5', '4'},
            {'5', '5'}, {'5', '6'}, {'5', '7'}, {'5', '8'}, {'5', '9'},
            {'6', '0'}, {'6', '1'}, {'6', '2'}, {'6', '3'}, {'6', '4'},
            {'6', '5'}, {'6', '6'}, {'6', '7'}, {'6', '8'}, {'6', '9'},
            {'7', '0'}, {'7', '1'}, {'7', '2'}, {'7', '3'}, {'7', '4'},
            {'7', '5'}, {'7', '6'}, {'7', '7'}, {'7', '8'}, {'7', '9'},
            {'8', '0'}, {'8', '1'}, {'8', '2'}, {'8', '3'}, {'8', '4'},
            {'8', '5'}, {'8', '6'}, {'8', '7'}, {'8', '8'}, {'8', '9'},
            {'9', '0'}, {'9', '1'}, {'9', '2'}, {'9', '3'}, {'9', '4'},
            {'9', '5'}, {'9', '6'}, {'9', '7'}, {'9', '8'}, {'9', '9'},
    };
    /**
     * index into the array to get the number of decimal digits
     */
    private static final byte[] leadingZeroesToDecimalDigits = new byte[]{
            // 0 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15
            10, 10, 9, 9, 9, 8, 8, 8, 7, 7, 7, 7, 6, 6, 6, 5,
            // 16 17 18 19 20  21  22  23  24  25  26  27  28  29  30  31
            5, 05, 04, 04, 04, 04, 03, 03, 03, 02, 02, 02, 01, 01, 01, 01
    };

    private static final long[] POWER_OF_10 = new long[]{
            // 0 1   2     3       4        5          6           7
            0, 10, 100, 1000, 10_000, 100_000, 1_000_000, 10_000_000,
            //        8              9               10
            100_000_000, 1_000_000_000, 10_000_000_000L
    };

    static int log10Characteristic(int x) {
        long ux = 0xffff_ffffL & x;
        int lz = Long.numberOfLeadingZeros(ux) - 32;
        int y = leadingZeroesToDecimalDigits[lz];
        if (ux < POWER_OF_10[y])
            y -= 1;
        return y;
    }

    static byte[] digitsInAscii(int offset) {
        assert offset > -1 : "offset must be >= 0";
        assert offset < 100 : "offset must be <= 99";
        return digitsInAscii[offset];
    }

    /**
     * Attempts to write {@code val} as an unsigned 32-bit integer in ascii digits
     * to the buffer.
     *
     * @param val    an unsigned integer
     * @param buffer a buffer with sufficient space between {@link ByteBuffer#position()}
     *               and {@link ByteBuffer#limit()} to hold the integer in ascii digits
     * @return {@code buffer}
     * @throws BufferOverflowException if a {@link ByteBuffer#put(byte) put} call fails
     */
    public static ByteBuffer uint32ToAscii(int val, ByteBuffer buffer) {
        if (val == 0) {
            return buffer.put((byte) '0');
        }
        int size = log10Characteristic(val) + 1;
        int p = buffer.position();
        if (buffer.limit() - p < size)
            throw new BufferOverflowException() {
                @Override
                public String getMessage() {
                    return "Need to write " + size + " bytes to buffer, but " +
                            (buffer.limit() - buffer.position()) + " is the limit";
                }
            };
        p += size;
        final int end = p;
        long uval = 0xffff_ffffL & val;
        while (uval >= 10) {
            buffer.position(p - 2);
            buffer.put(digitsInAscii((int) (uval % 100)));
            uval /= 100;
            p -= 2;
        }

        if (uval != 0) {
            buffer.put(p - 1, digitsInAscii((int) uval)[1]);
        }

        buffer.position(end);

        return buffer;
    }
}
