package com.github.nathansgreen.codec;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntToByteBufferTest {

    /**
     * Basic sanity check of internal static data.
     */
    @Test
    void digitsInAscii() {
        for (int i = 0; i < 10; i++) {
            assertArrayEquals(
                    ("0" + i).getBytes(US_ASCII),
                    IntToByteBuffer.digitsInAscii(i));
        }
        for (int i = 10; i < 100; i++) {
            assertArrayEquals(
                    Integer.toString(i).getBytes(US_ASCII),
                    IntToByteBuffer.digitsInAscii(i));
        }
    }

    /**
     * Basic sanity test of important implementation detail.
     */
    @Test
    void log10Characteristic() {
        assertEquals(2, IntToByteBuffer.log10Characteristic(999));
        assertEquals(3, IntToByteBuffer.log10Characteristic(1000));
        assertEquals(9, IntToByteBuffer.log10Characteristic(0xffffffff));
    }

    /**
     * Check all 1- and 2-digit numbers as well as a sample of 3-, 4-, 5-, 6-,
     * and 10-digit numbers.
     */
    @Test
    void uint32ToAscii() {
        Function<ByteBuffer, String> fn = (b) -> {
            String s = new String(b.array(), 0, b.position(), StandardCharsets.ISO_8859_1);
            b.clear();
            return s;
        };
        ByteBuffer buf = ByteBuffer.allocate(16);

        for (int i = 0; i < 9; i++)
            assertEquals("" + i, fn.apply(IntToByteBuffer.uint32ToAscii(i, buf)));

        for (int i = 10; i < 99; i++)
            assertEquals("" + i, fn.apply(IntToByteBuffer.uint32ToAscii(i, buf)));

        for (int i : IntStream.of(100, 199, 255, 256, 999, 1000, 12_345, 123_456, 0x7fffffff).toArray())
            assertEquals("" + i, fn.apply(IntToByteBuffer.uint32ToAscii(i, buf)));

        for (int i : IntStream.of(0x8000_0000, 0x89ab_cdef, 0xffff_ffff).toArray()) {
            long uint = 0xffff_ffffL & i;
            assertEquals("" + uint, fn.apply(IntToByteBuffer.uint32ToAscii(i, buf)));
        }
    }
}
