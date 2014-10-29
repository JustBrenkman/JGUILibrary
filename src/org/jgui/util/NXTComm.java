/*
 *
 *  * Copyright (c) 2014.
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  *  1. Redistributions of source code must retain the above copyright notice, this
 *  *     list of conditions and the following disclaimer.
 *  *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  *     this list of conditions and the following disclaimer in the documentation
 *  *     and/or other materials provided with the distribution.
 *  *
 *  *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  *
 *  *  The views and conclusions contained in the software and documentation are those
 *  *  of the authors and should not be interpreted as representing official policies,
 *  *  either expressed or implied, of the FreeBSD Project.
 *
 */

package org.jgui.util;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import org.jgui.eventbus.EventBusService;
import org.jgui.events.*;
import sun.nio.ByteBuffered;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by ben on 25/10/14.
 */
public class NXTComm implements Runnable {

    //The maximum message size. 5 bytes for header, 95 for body
// The XBee protocol limits the message buffer to 100 bytes, so
// anything bigger than that gets fragmented. Avoid that.
    final int MAX_MSG_SIZE = 95;
    final int HEADER_SIZE = 5;
    final int BUFFER_SIZE = MAX_MSG_SIZE + HEADER_SIZE;
    final int PACKED_HDR_SIZE = 2;

    // field definitions for the header, each field is one byte
    final int MSG_TYPE = 0;
    final int BYTE_COUNT = 1;
    final int CKSUM = 2;
    final int DEST_ADDRESS = 3;
    final int SRC_ADDRESS = 4;
    final int START_OF_MSG = HEADER_SIZE;
    final byte BROADCAST = 0;

    // Type field entries
    final byte ASCII_STRING_TYPE = 0;
    final byte BYTE_STRING_TYPE = 1;
    final byte INT_TYPE = 2;
    final byte LONG_TYPE = 3;
    final byte BYTE_TYPE = 4;
    final byte BOOL_TYPE = 5;
    final byte PACKED_TYPE = 6;
    final byte IRSEEKER_INFO_PACKED = 7;

    final byte[] CRC8_TABLE =
            new byte[] {
                    (byte) 0x00,
                    (byte) 0x07,
                    (byte) 0x0E,
                    (byte) 0x09,
                    (byte) 0x1C,
                    (byte) 0x1B,
                    (byte) 0x12,
                    (byte) 0x15,
                    (byte) 0x38,
                    (byte) 0x3F,
                    (byte) 0x36,
                    (byte) 0x31,
                    (byte) 0x24,
                    (byte) 0x23,
                    (byte) 0x2A,
                    (byte) 0x2D,
                    (byte) 0x70,
                    (byte) 0x77,
                    (byte) 0x7E,
                    (byte) 0x79,
                    (byte) 0x6C,
                    (byte) 0x6B,
                    (byte) 0x62,
                    (byte) 0x65,
                    (byte) 0x48,
                    (byte) 0x4F,
                    (byte) 0x46,
                    (byte) 0x41,
                    (byte) 0x54,
                    (byte) 0x53,
                    (byte) 0x5A,
                    (byte) 0x5D,
                    (byte) 0xE0,
                    (byte) 0xE7,
                    (byte) 0xEE,
                    (byte) 0xE9,
                    (byte) 0xFC,
                    (byte) 0xFB,
                    (byte) 0xF2,
                    (byte) 0xF5,
                    (byte) 0xD8,
                    (byte) 0xDF,
                    (byte) 0xD6,
                    (byte) 0xD1,
                    (byte) 0xC4,
                    (byte) 0xC3,
                    (byte) 0xCA,
                    (byte) 0xCD,
                    (byte) 0x90,
                    (byte) 0x97,
                    (byte) 0x9E,
                    (byte) 0x99,
                    (byte) 0x8C,
                    (byte) 0x8B,
                    (byte) 0x82,
                    (byte) 0x85,
                    (byte) 0xA8,
                    (byte) 0xAF,
                    (byte) 0xA6,
                    (byte) 0xA1,
                    (byte) 0xB4,
                    (byte) 0xB3,
                    (byte) 0xBA,
                    (byte) 0xBD,
                    (byte) 0xC7,
                    (byte) 0xC0,
                    (byte) 0xC9,
                    (byte) 0xCE,
                    (byte) 0xDB,
                    (byte) 0xDC,
                    (byte) 0xD5,
                    (byte) 0xD2,
                    (byte) 0xFF,
                    (byte) 0xF8,
                    (byte) 0xF1,
                    (byte) 0xF6,
                    (byte) 0xE3,
                    (byte) 0xE4,
                    (byte) 0xED,
                    (byte) 0xEA,
                    (byte) 0xB7,
                    (byte) 0xB0,
                    (byte) 0xB9,
                    (byte) 0xBE,
                    (byte) 0xAB,
                    (byte) 0xAC,
                    (byte) 0xA5,
                    (byte) 0xA2,
                    (byte) 0x8F,
                    (byte) 0x88,
                    (byte) 0x81,
                    (byte) 0x86,
                    (byte) 0x93,
                    (byte) 0x94,
                    (byte) 0x9D,
                    (byte) 0x9A,
                    (byte) 0x27,
                    (byte) 0x20,
                    (byte) 0x29,
                    (byte) 0x2E,
                    (byte) 0x3B,
                    (byte) 0x3C,
                    (byte) 0x35,
                    (byte) 0x32,
                    (byte) 0x1F,
                    (byte) 0x18,
                    (byte) 0x11,
                    (byte) 0x16,
                    (byte) 0x03,
                    (byte) 0x04,
                    (byte) 0x0D,
                    (byte) 0x0A,
                    (byte) 0x57,
                    (byte) 0x50,
                    (byte) 0x59,
                    (byte) 0x5E,
                    (byte) 0x4B,
                    (byte) 0x4C,
                    (byte) 0x45,
                    (byte) 0x42,
                    (byte) 0x6F,
                    (byte) 0x68,
                    (byte) 0x61,
                    (byte) 0x66,
                    (byte) 0x73,
                    (byte) 0x74,
                    (byte) 0x7D,
                    (byte) 0x7A,
                    (byte) 0x89,
                    (byte) 0x8E,
                    (byte) 0x87,
                    (byte) 0x80,
                    (byte) 0x95,
                    (byte) 0x92,
                    (byte) 0x9B,
                    (byte) 0x9C,
                    (byte) 0xB1,
                    (byte) 0xB6,
                    (byte) 0xBF,
                    (byte) 0xB8,
                    (byte) 0xAD,
                    (byte) 0xAA,
                    (byte) 0xA3,
                    (byte) 0xA4,
                    (byte) 0xF9,
                    (byte) 0xFE,
                    (byte) 0xF7,
                    (byte) 0xF0,
                    (byte) 0xE5,
                    (byte) 0xE2,
                    (byte) 0xEB,
                    (byte) 0xEC,
                    (byte) 0xC1,
                    (byte) 0xC6,
                    (byte) 0xCF,
                    (byte) 0xC8,
                    (byte) 0xDD,
                    (byte) 0xDA,
                    (byte) 0xD3,
                    (byte) 0xD4,
                    (byte) 0x69,
                    (byte) 0x6E,
                    (byte) 0x67,
                    (byte) 0x60,
                    (byte) 0x75,
                    (byte) 0x72,
                    (byte) 0x7B,
                    (byte) 0x7C,
                    (byte) 0x51,
                    (byte) 0x56,
                    (byte) 0x5F,
                    (byte) 0x58,
                    (byte) 0x4D,
                    (byte) 0x4A,
                    (byte) 0x43,
                    (byte) 0x44,
                    (byte) 0x19,
                    (byte) 0x1E,
                    (byte) 0x17,
                    (byte) 0x10,
                    (byte) 0x05,
                    (byte) 0x02,
                    (byte) 0x0B,
                    (byte) 0x0C,
                    (byte) 0x21,
                    (byte) 0x26,
                    (byte) 0x2F,
                    (byte) 0x28,
                    (byte) 0x3D,
                    (byte) 0x3A,
                    (byte) 0x33,
                    (byte) 0x34,
                    (byte) 0x4E,
                    (byte) 0x49,
                    (byte) 0x40,
                    (byte) 0x47,
                    (byte) 0x52,
                    (byte) 0x55,
                    (byte) 0x5C,
                    (byte) 0x5B,
                    (byte) 0x76,
                    (byte) 0x71,
                    (byte) 0x78,
                    (byte) 0x7F,
                    (byte) 0x6A,
                    (byte) 0x6D,
                    (byte) 0x64,
                    (byte) 0x63,
                    (byte) 0x3E,
                    (byte) 0x39,
                    (byte) 0x30,
                    (byte) 0x37,
                    (byte) 0x22,
                    (byte) 0x25,
                    (byte) 0x2C,
                    (byte) 0x2B,
                    (byte) 0x06,
                    (byte) 0x01,
                    (byte) 0x08,
                    (byte) 0x0F,
                    (byte) 0x1A,
                    (byte) 0x1D,
                    (byte) 0x14,
                    (byte) 0x13,
                    (byte) 0xAE,
                    (byte) 0xA9,
                    (byte) 0xA0,
                    (byte) 0xA7,
                    (byte) 0xB2,
                    (byte) 0xB5,
                    (byte) 0xBC,
                    (byte) 0xBB,
                    (byte) 0x96,
                    (byte) 0x91,
                    (byte) 0x98,
                    (byte) 0x9F,
                    (byte) 0x8A,
                    (byte) 0x8D,
                    (byte) 0x84,
                    (byte) 0x83,
                    (byte) 0xDE,
                    (byte) 0xD9,
                    (byte) 0xD0,
                    (byte) 0xD7,
                    (byte) 0xC2,
                    (byte) 0xC5,
                    (byte) 0xCC,
                    (byte) 0xCB,
                    (byte) 0xE6,
                    (byte) 0xE1,
                    (byte) 0xE8,
                    (byte) 0xEF,
                    (byte) 0xFA,
                    (byte) 0xFD,
                    (byte) 0xF4,
                    (byte) 0xF3 };

    public NXTBTComm nxtbtComm;
    public BufferedInputStream bufferedInputStream;
    public int amountInBufferedStream = 0;

    public NXTComm(BufferedInputStream bis) {
        nxtbtComm = new NXTBTComm(0);
        bufferedInputStream = bis;
    }

    public void read() {
        nxtbtComm.receive(bufferedInputStream);
    }

    @Override
    public void run() {
        while (true) {
            read();

        }
    }

    class NXTBTComm {
        private byte[] transmitBuffer;
        private byte transmitByteCount;

        private ByteBuffer transmitBB;

        // The address that this NXT has chosen to listen for messages on
        // By default this is BROADCAST, meaning this NXT will receive all
        // messages broadcast
        private int myAddress = BROADCAST;

        public ByteBuffer bb = ByteBuffer.allocate(100);

        ////////////////////////////////////////////////////////
        // Initialise the transmit array with the default address
        NXTBTComm() {
            transmitBuffer = new byte[BUFFER_SIZE];
            transmitBB = ByteBuffer.wrap(transmitBuffer);
            transmitBB.clear();
            transmitBB.position(START_OF_MSG);
            transmitByteCount = 0;
        }

        ////////////////////////////////////////////////////////
        // Initialise the transmit array with the given address
        // of myaddr
        NXTBTComm(int myaddr) {
            transmitBuffer = new byte[BUFFER_SIZE];
            myAddress = myaddr;
            transmitBB = ByteBuffer.wrap(transmitBuffer);
            transmitBB.clear();
            transmitBB.position(START_OF_MSG);
            transmitByteCount = 0;
        }

        ////////////////////////////////////////////////////////
        // computeCRC8Checksum
        // compute the checksum across the byte buffer
        // computes checksum but does not checksum on the header
        // Returns the checksum value computed
        //
        // Parameters:
        // transmit: if true checksums the transmit buffer, else the
        // receive buffer contents.
        private byte computeCRC8Checksum(boolean transmit) {
            byte crc = 0;

            if(transmit) {
                for (int i = START_OF_MSG; i < START_OF_MSG+transmitByteCount; i++) {
                    crc = CRC8_TABLE[(crc ^ transmitBuffer[i]) & 0xFF];
                }
            }

            // println("computeCRC8Checksum: crc = " + crc);

            return crc;
        }

        ////////////////////////////////////////////////////////
        // transmit the current buffer contents to the remote side
        //
        // Parameters:
        // msgType: the type of the message to send (e.g. BYTE_TYPE)
        // destAddr: address to send to, or 0 for broadcast
        //
        private void transmit(byte msgType, byte destAddr) {

            transmitBB.position(0); // pack in the header
            transmitBB.put(msgType);
            transmitBB.put(transmitByteCount);
            transmitBB.put(computeCRC8Checksum(true));
            transmitBB.put(destAddr);
            transmitBB.put((byte)myAddress);

            // Verbose debugging
            /**
            println("-----------------");
            println("type = " + transmitBuffer[MSG_TYPE]);
            println("transmitByteCount = " + transmitBuffer[BYTE_COUNT]);
            println("checksum = " + transmitBuffer[CKSUM]);
            println("destAddr = " + transmitBuffer[DEST_ADDRESS]);
            println("srcAddr = " + transmitBuffer[SRC_ADDRESS]);


            for(int i=0; i < transmitBuffer[BYTE_COUNT] + HEADER_SIZE; i++)
            print(transmitBuffer[i] + " ");
            println();
            **/

            // write the byte array to the serial port
//            myPort.write(transmitBuffer);
        }

        // clear the contents of the buffer from a previous packing
        // and thus prepare it for packing new data to transmit
        //
        // Simple reset the write index for the buffer to the start of
        // the message field, and set the byte count to 0
        void clearTransmitBuffer() {

            transmitBB.position(START_OF_MSG);
            transmitByteCount = 0;
        }

        // transmit a packed buffer to the NXT
        //
        void transmitPackedData(byte destAddress) {
            transmit(PACKED_TYPE, destAddress);
        }

        // pack a byte into the buffer
        // Takes a byte value and adds it as a packed value
        // in the transmit buffer. Does not transmit straight away
        //
        // Parameters: b: the byte value to add
        //
        // Throws: BufferOverflowException if no space is available in
        // the buffer
        void packByte(byte b)  {

            transmitBB.put(BYTE_TYPE);
            transmitBB.put((byte)1); // 1 byte message length
            transmitBB.put(b);

            // update the total count of bytes in the buffer by 1 + 2
            transmitByteCount +=  3;
        }

        // pack a 2 byte NXT integer (Java short)  into the buffer
        // Takes  value and adds it as a packed value
        // in the transmit buffer. Does not transmit straight away
        //
        // Parameters: value: the short 2 byte value to add
        //
        // Throws: BufferOverflowException if no space is available in
        // the buffer
        void packInt(short value) {

            // pack the 2 byte value into the transmit buffer
            transmitBB.put(INT_TYPE);
            transmitBB.put((byte)2); // 2 byte message length
            transmitBB.putShort(value);

            // 2 bytes to a Java short/NXT int + 2 bytes header
            transmitByteCount += 4;
        }

        // pack a 4 byte NXT long (Java int)  into the buffer
        // Takes  value and adds it as a packed value
        // in the transmit buffer. Does not transmit straight away
        //
        // Parameters: value: the short 4 byte value to add
        //
        // Throws: BufferOverflowException if no space is available in
        // the buffer
        void packLong(int value)  {

            // pack the 2 byte value into the transmit buffer
            transmitBB.put(LONG_TYPE);
            transmitBB.put((byte)4); // 4 byte message length
            transmitBB.putInt(value);

            // 4 bytes to a Java int/NXT long plus two bytes header
            transmitByteCount += 6;
        }

        // pack a byte array  into the buffer
        // Takes  value and adds it as a packed value
        // in the transmit buffer. Does not transmit straight away
        //
        // Parameters: value: the short 4 byte value to add
        //
        // Throws: BufferOverflowException if no space is available in
        // the buffer
        void packBytes(byte[] data)  {

            int currPos;

            int len = data.length;

            transmitBB.put(BYTE_STRING_TYPE);
            transmitBB.put((byte)len);
            transmitBB.put(data);

            transmitByteCount += 2;
            transmitByteCount += (byte)len;
        }

        // pack a string into the buffer
        // Takes  value and adds it as a packed value
        // in the transmit buffer. Does not transmit straight away
        //
        // Parameters: value: the short 4 byte value to add
        //
        // Throws: BufferOverflowException if no space is available in
        // the buffer
        void packString(String s)  {

            // convert the string into a byte array and copy
            // the first 20 characters into the bytebuffer
            byte[] data = s.getBytes();

            int len = s.length();
            transmitBB.put(ASCII_STRING_TYPE);
            transmitBB.put((byte) len);
            transmitBB.put(data);

            transmitByteCount += (byte)len;
            transmitByteCount += 2;
        }

        //////////////////////////////////////////
        // parsePackedData format
        //

        private void parsePackedData(ByteBuffer bb) {
            byte data[];

            while(bb.hasRemaining()) {

                byte type = bb.get();
                byte byteCount = bb.get();

                switch(type) {

                    case ASCII_STRING_TYPE:
                        data = new byte[byteCount];
                        bb.get(data, 0, byteCount);
                        String s = new String(data);
                        System.out.println("String: " + s);
                        break;

                    case BYTE_STRING_TYPE:
                        data = new byte[byteCount];
                        bb.get(data, 0, byteCount);
                        for(int i=0; i < byteCount; i++) {
                            if( (i>0) && ((i%8) == 0 ))
                                System.out.println();
                            System.out.print(data[i] + " ");
                        }
                        System.out.println();
                        break;

                    case BOOL_TYPE:
                        byte b = bb.get();
                        if(b > 0)
                            System.out.println("val = true");
                        else
                            System.out.println("val = false");
                        break;

                    case LONG_TYPE:
                        System.out.println("Long val = " + bb.getInt());
                        break;

                    case INT_TYPE:
                        System.out.println("Int val = " + bb.getShort());
                        break;

                    case BYTE_TYPE:
                        System.out.println("Byte val = " + bb.get());
                        break;

                    case PACKED_TYPE:
                        System.out.println("Error: cannot pack within packed type");
                        return;

                    default:
                        System.out.println("Unknown data type: " + type);
                        break;
                }
            }
        }

        public void receive(BufferedInputStream myPort) {
            byte[] headerBuffer = new byte[HEADER_SIZE]; // for reading hdr
            int totalSize;

            // Receiver will read the header first assuming that enough bytes are
            // available. Then given a good header, it reads the number of bytes
            // specified in BYTE_COUNT field.


            try {
                if (myPort.available() > 0) {
                    amountInBufferedStream++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (amountInBufferedStream > HEADER_SIZE) {
                    System.out.println("A total of " + amountInBufferedStream + " bytes are available to read");
                    amountInBufferedStream = 0;

                    int len = myPort.read(headerBuffer, 0, HEADER_SIZE);

                    System.out.println("Read header len bytes = " + len);
//                    bb.rewind();
//                    bb.get(headerBuffer);

                    // create a byte buffer to parse the contents of the message
                    ByteBuffer hdrBB =  ByteBuffer.wrap(headerBuffer);

                    // now parse the buffer to determine what type of value has been sent
                    try {
                        byte type = hdrBB.get();
                        System.out.println("type = " + type);

                        byte byteCount = hdrBB.get();
                        System.out.println("byteCount = " + byteCount);

                        byte checksum = hdrBB.get();
                        System.out.println("checksum = " + checksum);

                        byte destAddress = hdrBB.get();
                        System.out.println("Dest address = " + destAddress);

                        byte srcAddress = hdrBB.get();
                        System.out.println("Src address = " + srcAddress);

                        // if this is not for me then ignore the packet
                        if( (destAddress != BROADCAST) && (destAddress != myAddress)) {
                            System.out.println("Packet not for my address");
                            return;
                        }

                        // now read the rest of the message
                        if(myPort.available() < byteCount) {
                            // insufficient data, skip
                            System.out.println("Insufficient data available");
                            return;
                        }

                        byte[] receiveBuffer = new byte[byteCount];
                        len = myPort.read(receiveBuffer);
                        System.out.println("Read " + len + " bytes of message body");

                        // create a byte buffer to parse the contents of the message
                        ByteBuffer bb =  ByteBuffer.wrap(receiveBuffer, 0, len);

                        // Check that the checksum is valid
                        if(verifyReceiveChecksum(bb, byteCount, checksum)) {
                            byte data[];
                            switch(type) {

                                case ASCII_STRING_TYPE:
                                    data = new byte[byteCount];
                                    bb.get(data, 0, byteCount);
                                    String s = new String(data);
                                    System.out.println("String: " + s);
                                    break;

                                case BYTE_STRING_TYPE:
                                    data = new byte[byteCount];
                                    bb.get(data, 0, byteCount);
                                    for(int i=0; i < byteCount; i++) {
                                        if( (i>0) && ((i%8) == 0 ))
                                            System.out.println();
                                        System.out.print(data[i] + " ");
                                    }
                                    System.out.println();
                                    break;

                                case BOOL_TYPE:
                                    byte b = bb.get();
                                    if(b > 0)
                                        System.out.println("val = true");
                                    else
                                        System.out.println("val = false");
                                    break;

                                case LONG_TYPE:
                                    System.out.println("Long val = " + bb.getInt());
                                    break;

                                case INT_TYPE:
                                    System.out.println("Int val = " + bb.getShort());
                                    break;

                                case BYTE_TYPE:
                                    System.out.println("Byte val = " + bb.get());
                                    break;

                                case PACKED_TYPE:
                                    System.out.println("Packed message");
                                    parsePackedData(bb);
                                    break;

                                case IRSEEKER_INFO_PACKED:
                                    System.out.println("IRSeeker info packed data");
                                    parseIRSeekerInfo(bb);
                                    break;

                                default:
                                    System.out.println("Unknown data type: " + type);
                                    break;
                            }
                        } else {
                            System.out.println("Checksum mismatch");
                        }
                    } catch (Exception e) {
                        System.out.println("Format error " + e);
                    }
                    System.out.println("-----------------");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean verifyReceiveChecksum(ByteBuffer bb, byte len, byte checksum) {

            byte crc = 0;
            int b;

            bb.position(0);

            for(int i=0; i < len; i++) {
                b = ((bb.get() ^ crc) & 0xFF);
                if(b < 0)
                    b =  - b;
                crc = CRC8_TABLE[b];
            }

            bb.position(0);

            if(crc == checksum)
                return true;
            else
                return false;
        }
    }

    //////////////////////////////////////////
    // parsePackedData format
    //

    private void parseIRSeekerInfo(ByteBuffer bb) {
        byte data[];
        IRSeekerEvent event = new IRSeekerEvent();
        int count = 0;

        while(bb.hasRemaining()) {

            byte type = bb.get();
            byte byteCount = bb.get();

            switch(type) {

                case ASCII_STRING_TYPE:
                    data = new byte[byteCount];
                    bb.get(data, 0, byteCount);
                    String s = new String(data);
                    System.out.println("String: " + s);
                    break;

                case BYTE_STRING_TYPE:
                    data = new byte[byteCount];
                    bb.get(data, 0, byteCount);
                    for(int i=0; i < byteCount; i++) {
                        if( (i>0) && ((i%8) == 0 ))
                            System.out.println();
                        System.out.print(data[i] + " ");
                    }
                    System.out.println();
                    break;

                case BOOL_TYPE:
                    byte b = bb.get();
                    if(b > 0)
                        System.out.println("val = true");
                    else
                        System.out.println("val = false");
                    break;

                case LONG_TYPE:
                    System.out.println("Long val = " + bb.getInt());
                    break;

                case INT_TYPE:
                    switch (count) {
                        case 0:
                            event.irSeekerID = bb.getShort();
                            System.out.println("IR Seeker ID: " + event.irSeekerID);
                            break;
                        case 1:
                            event.irSeekerDirection = bb.getShort();
                            System.out.println("IR Seeker Direction: " + event.irSeekerDirection);
                            break;
                        case 2:
                            event.getIrSeekerS1 = bb.getShort();
                            System.out.println("IR Seeker Sensor 1: " + event.getIrSeekerS1);
                            break;
                        case 3:
                            event.getIrSeekerS2 = bb.getShort();
                            System.out.println("IR Seeker Sensor 2: " + event.getIrSeekerS2);
                            break;
                        case 4:
                            event.getIrSeekerS3 = bb.getShort();
                            System.out.println("IR Seeker Sensor 3: " + event.getIrSeekerS3);
                            break;
                        case 5:
                            event.getIrSeekerS4 = bb.getShort();
                            System.out.println("IR Seeker Sensor 4: " + event.getIrSeekerS4);
                            break;
                        case 6:
                            event.getIrSeekerS5 = bb.getShort();
                            System.out.println("IR Seeker Sensor 5: " + event.getIrSeekerS5);
                            break;

                    }
                    break;

                case BYTE_TYPE:
                    System.out.println("Byte val = " + bb.get());
                    break;

                case PACKED_TYPE:
                    System.out.println("Error: cannot pack within packed type");
                    return;

                default:
                    System.out.println("Unknown data type: " + type);
                    break;
            }
            count++;
        }
        EventBusService.publish(event);
    }
}
