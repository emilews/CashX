/*
 * Copyright 2011 Thilo Planz
 * Copyright 2014 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.core;

import com.google.common.io.BaseEncoding;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.bitcoinj.core.Utils.toByteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void bigIntegerToBytesTest() {
        assertEquals(null, Utils.bigIntegerToBytes(null, 0));

        assertArrayEquals(new byte[0], Utils.bigIntegerToBytes(new BigInteger("0"), 0));
        assertArrayEquals(new byte[0], Utils.bigIntegerToBytes(new BigInteger("10"), 0));
        assertArrayEquals(new byte[0], Utils.bigIntegerToBytes(new BigInteger("1212110"), 0));

        assertArrayEquals(toByteArray( 0, 1,  -117, 111), Utils.bigIntegerToBytes(new BigInteger("101231"), 4));
        assertArrayEquals(toByteArray( 1, -117, 111), Utils.bigIntegerToBytes(new BigInteger("101231"), 3));
        assertArrayEquals(toByteArray( -117, 111), Utils.bigIntegerToBytes(new BigInteger("101231"), 2));
        assertArrayEquals(toByteArray( 1), Utils.bigIntegerToBytes(new BigInteger("101231"), 1));
    }

    @Test
    public void uint32_64ToByteArrayBE_LeTest() {
        assertArrayEquals(toByteArray(0, 0, 0, -1), Utils.uint32ToByteArrayBE(255));
        assertArrayEquals(toByteArray(-1, -1, -1, 1), Utils.uint32ToByteArrayBE(-255));
        assertArrayEquals(toByteArray(-1, 0, 0, 0), Utils.uint32ToByteArrayLE(255));
        assertArrayEquals(toByteArray(1, -1, -1, -1), Utils.uint32ToByteArrayLE(-255));

        assertArrayEquals(toByteArray(-1, 0, 0, 0, 0, 0, 0, 0), Utils.uint64ToByteArrayLE(255));
        assertArrayEquals(toByteArray(1, -1, -1, -1, -1, -1, -1, -1), Utils.uint64ToByteArrayLE(-255));

        assertArrayEquals(toByteArray(0, 3, -27, 23), Utils.uint32ToByteArrayBE(255255));
        assertArrayEquals(toByteArray(-1, -4, 26, -23), Utils.uint32ToByteArrayBE(-255255));
        assertArrayEquals(toByteArray(23, -27, 3, 0), Utils.uint32ToByteArrayLE(255255));
        assertArrayEquals(toByteArray(-23, 26, -4, -1), Utils.uint32ToByteArrayLE(-255255));

        assertArrayEquals(toByteArray(23, -27, 3, 0, 0, 0, 0, 0), Utils.uint64ToByteArrayLE(255255));
        assertArrayEquals(toByteArray(-23, 26, -4, -1, -1, -1, -1, -1), Utils.uint64ToByteArrayLE(-255255));
        assertArrayEquals(toByteArray(-1, -1, -1, -1, -1, -1, -1, 127), Utils.uint64ToByteArrayLE(Long.MAX_VALUE));
        assertArrayEquals(toByteArray(0, 0, 0, 0, 0, 0, 0, -128), Utils.uint64ToByteArrayLE(Long.MIN_VALUE));
    }

    @Test
    public void uint32_64ToByteArrayOutputStreamLeTest() throws IOException {
        assertArrayEquals(toByteArray(-1, 0, 0, 0), outputStreamUint32LEBytesToArray(255));
        assertArrayEquals(toByteArray(-1, 0, 0, 0, 0, 0, 0, 0), outputStreamInt64LEBytesToArray(255));
        assertArrayEquals(toByteArray(-1, 0, 0, 0, 0, 0, 0, 0), outputStreamUint64LEBytesToArray(new BigInteger("255")));

        assertArrayEquals(toByteArray(23, -27, 3, 0), outputStreamUint32LEBytesToArray(255255));
        assertArrayEquals(toByteArray(23, -27, 3, 0, 0, 0, 0, 0), outputStreamInt64LEBytesToArray(255255));
        assertArrayEquals(toByteArray(23, -27, 3, 0, 0, 0, 0, 0), outputStreamUint64LEBytesToArray(new BigInteger("255255")));

        assertArrayEquals(toByteArray(-1, -1, -1, -1, -1, -1, -1, 127), outputStreamInt64LEBytesToArray(Long.MAX_VALUE));
        assertArrayEquals(toByteArray(-1, -1, -1, -1, -1, -1, -1, 127), outputStreamUint64LEBytesToArray(new BigInteger(""+Long.MAX_VALUE)));
        assertArrayEquals(toByteArray(0, 0, 0, 0, 0, 0, 0, -128), outputStreamInt64LEBytesToArray(Long.MIN_VALUE));
        assertArrayEquals(toByteArray(0, 0, 0, 0, 0, 0, 0, -128), outputStreamUint64LEBytesToArray(new BigInteger(""+Long.MIN_VALUE)));
    }

    private byte[] outputStreamUint32LEBytesToArray(long val) throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Utils.uint32ToByteStreamLE(val, stream);
        return stream.toByteArray();
    }

    private byte[] outputStreamInt64LEBytesToArray(long val) throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Utils.int64ToByteStreamLE(val, stream);
        return stream.toByteArray();
    }

    private byte[] outputStreamUint64LEBytesToArray(BigInteger val) throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Utils.uint64ToByteStreamLE(val, stream);
        return stream.toByteArray();
    }

    @Test
    public void reverseBytesTest() {
        assertArrayEquals(new byte[]{}, Utils.reverseBytes(new byte[]{}));
        assertArrayEquals(toByteArray(1), Utils.reverseBytes(toByteArray(1)));
        assertArrayEquals(toByteArray(200, 10), Utils.reverseBytes(toByteArray(10, 200)));
        assertArrayEquals(toByteArray(10, 200), Utils.reverseBytes(toByteArray(200, 10)));
        assertArrayEquals(toByteArray(0, 10, 200), Utils.reverseBytes(toByteArray(200, 10, 0)));
    }
    
    @Test
    public void reverseDwordBytesTest() {
        assertArrayEquals(toByteArray(40, 30, 20, 10), Utils.reverseDwordBytes(toByteArray(10, 20, 30, 40), 4));
        assertArrayEquals(toByteArray(40, 30, 20, 10), Utils.reverseDwordBytes(toByteArray(10, 20, 30, 40, 50, 60, 70, 80), 4));
        assertArrayEquals(toByteArray(40, 30, 20, 10, 80, 70, 60, 50), Utils.reverseDwordBytes(toByteArray(10, 20, 30, 40, 50, 60, 70, 80), 8));
    }

    @Test
    public void MPITests() {
        List<Long> candidate = new ArrayList<Long>() {
            {
                add(0L);
                add(-0L);
                add(Long.MAX_VALUE);
                add(Long.MIN_VALUE);
                add(-9001000100000100001L);
                add(-9001000100000100001L);
                add( 9001000100000100001L);
                add(-900100010000010000L);
                add( 900100010000010000L);
                add(-900100010000010L);
                add( 900100010000010L);
                add(-9001000100000L);
                add( 9001000100000L);
                add(-9001000100L);
                add( 9001000100L);
                add(-90010001L);
                add( 90010001L);
                add(-900100L);
                add( 900100L);
                add(-900L);
                add( 900L);
                add(-9L);
                add( 9L);
                add(-1L);
                add( 1L);
            }
        };
        for (Long v : candidate) {
            final BigInteger value = BigInteger.valueOf(v);
            assertEquals(value, Utils.decodeMPI(Utils.encodeMPI(value, false), false));
        }
    }

    @Test
    public void compactBitsTest() {
        List<Long> candidate = new ArrayList<Long>() {
            {
                add(8388608L); // Maximum! One more an it will fail
                add(4000000L);
                add(1000000L);
                add(900000L);
                add(90000L);
                add(9000L);
                add(900L);
                add(90L);
                add(9L);
                add(1L);
                add(0L);
                add(-0L);
            }
        };
        for (Long v : candidate) {
            final BigInteger value = BigInteger.valueOf(v);
            System.out.println(value);
            assertEquals(value, Utils.decodeCompactBits(Utils.encodeCompactBits(value)));
        }
    }

    @Test
    public void testReverseDwordBytes() {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, Utils.reverseDwordBytes(new byte[]{4, 3, 2, 1, 8, 7, 6, 5}, -1));
        assertArrayEquals(new byte[]{1, 2, 3, 4}, Utils.reverseDwordBytes(new byte[]{4, 3, 2, 1, 8, 7, 6, 5}, 4));
        assertArrayEquals(new byte[0], Utils.reverseDwordBytes(new byte[]{4, 3, 2, 1, 8, 7, 6, 5}, 0));
        assertArrayEquals(new byte[0], Utils.reverseDwordBytes(new byte[0], 0));
    }

    @Test
    public void testMaxOfMostFreq() throws Exception {
        assertEquals(0, Utils.maxOfMostFreq());
        assertEquals(0, Utils.maxOfMostFreq(0, 0, 1));
        assertEquals(2, Utils.maxOfMostFreq(1, 1, 2, 2));
        assertEquals(1, Utils.maxOfMostFreq(1, 1, 2, 2, 1));
        assertEquals(-1, Utils.maxOfMostFreq(-1, -1, 2, 2, -1));
    }

    @Test
    public void compactEncoding() throws Exception {
        assertEquals(new BigInteger("1234560000", 16), Utils.decodeCompactBits(0x05123456L));
        assertEquals(new BigInteger("c0de000000", 16), Utils.decodeCompactBits(0x0600c0de));
        assertEquals(0x05123456L, Utils.encodeCompactBits(new BigInteger("1234560000", 16)));
        assertEquals(0x0600c0deL, Utils.encodeCompactBits(new BigInteger("c0de000000", 16)));
    }

    @Test
    public void dateTimeFormat() {
        assertEquals("2014-11-16T10:54:33Z", Utils.dateTimeFormat(1416135273781L));
        assertEquals("2014-11-16T10:54:33Z", Utils.dateTimeFormat(new Date(1416135273781L)));
    }

    @Test
    public void testReadUint16BE() {
        assertEquals(Utils.readUint16BE(BaseEncoding.base16().decode("0000"), 0), 0L);
        assertEquals(Utils.readUint16BE(BaseEncoding.base16().decode("00FF"), 0), (long) Math.pow(2, 8) - 1);
        assertEquals(Utils.readUint16BE(BaseEncoding.base16().decode("FFFF"), 0), (long) Math.pow(2, 16) - 1);
    }

    @Test
    public void testReadUint32BE() {
        assertEquals(Utils.readUint32BE(BaseEncoding.base16().decode("00000000"), 0), 0L);
        assertEquals(Utils.readUint32BE(BaseEncoding.base16().decode("000000FF"), 0), (long) Math.pow(2, 8) - 1);
        assertEquals(Utils.readUint32BE(BaseEncoding.base16().decode("0000FFFF"), 0), (long) Math.pow(2, 16) - 1);
        assertEquals(Utils.readUint32BE(BaseEncoding.base16().decode("00FFFFFF"), 0), (long) Math.pow(2, 24) - 1);
        assertEquals(Utils.readUint32BE(BaseEncoding.base16().decode("FFFFFFFF"), 0), (long) Math.pow(2, 32) - 1);
    }
    @Test
    public void testReadUint32() {
        assertEquals(Utils.readUint32(BaseEncoding.base16().decode("00000000"),0), 0L);
        assertEquals(Utils.readUint32(BaseEncoding.base16().decode("FF000000"),0), (long)Math.pow(2,8)-1);
        assertEquals(Utils.readUint32(BaseEncoding.base16().decode("FFFF0000"),0), (long)Math.pow(2,16)-1);
        assertEquals(Utils.readUint32(BaseEncoding.base16().decode("FFFFFF00"),0), (long)Math.pow(2,24)-1);
        assertEquals(Utils.readUint32(BaseEncoding.base16().decode("FFFFFFFF"),0), (long)Math.pow(2,32)-1);
    }

    @Test
    public void testReadInt64() {
        assertEquals(Utils.readInt64(BaseEncoding.base16().decode("0000000000000000"),0), 0L);
        assertEquals(Utils.readInt64(BaseEncoding.base16().decode("FF00000000000000"),0), (long)Math.pow(2,8)-1);
        assertEquals(Utils.readInt64(BaseEncoding.base16().decode("FFFF000000000000"),0), (long)Math.pow(2,16)-1);
        assertEquals(Utils.readInt64(BaseEncoding.base16().decode("FFFFFF0000000000"),0), (long)Math.pow(2,24)-1);
        assertEquals(Utils.readInt64(BaseEncoding.base16().decode("FFFFFFFF00000000"),0), (long)Math.pow(2,32)-1);
        assertEquals(Utils.readInt64(BaseEncoding.base16().decode("FFFFFFFFFF000000"),0), (long)Math.pow(2,40)-1);
        assertEquals(Utils.readInt64(BaseEncoding.base16().decode("FFFFFFFFFFFF0000"),0), (long)Math.pow(2,48)-1);
        assertEquals(Utils.readInt64(BaseEncoding.base16().decode("FFFFFFFFFFFFFF00"),0), (long)Math.pow(2,56)-1);
        assertEquals(Utils.readInt64(BaseEncoding.base16().decode("FFFFFFFFFFFFFFFF"),0), -1L);
    }
}
