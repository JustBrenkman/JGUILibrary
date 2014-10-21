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

import org.jgui.eventbus.EventBusService;
import org.jgui.eventbus.EventHandler;
import org.jgui.events.ShutDownEvent;
import org.jgui.events.listeners.IRSeekerEvent;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.obex.*;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by ben on 13/10/14.
 */
public class BlueTooth implements DiscoveryListener {

    LocalDevice localDevice = null;
    DiscoveryAgent discoveryAgent = null;
    String btUrl = "btspp://localhost:001653190C7E;name=8912";

    // Needed for communication
    StreamConnectionNotifier notifier = null;
    StreamConnection con = null;
    InputStream input;
    OutputStream output;
    Thread serialReader;

    private static Object lock = new Object();
    public ArrayList<RemoteDevice> devices;

    public BlueTooth() {

    }

    public void findDevices() {

        devices = new ArrayList<RemoteDevice>();


        try {
            localDevice = LocalDevice.getLocalDevice();

            discoveryAgent = localDevice.getDiscoveryAgent();

            discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);

            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }


            System.out.println("Device Inquiry Completed. ");


            UUID[] uuidSet = new UUID[1];
            uuidSet[0] = new UUID(0x1105); //OBEX Object Push service

            int[] attrIDs = new int[]{
                    0x0100 // Service name
            };

            for (RemoteDevice device : this.devices) {
                discoveryAgent.searchServices(attrIDs, uuidSet, device, this);


                try {
                    synchronized (lock) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }


                System.out.println("Service search finished.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class RequestHandler extends ServerRequestHandler {

        public int onPut(Operation op) {
            try {
                HeaderSet hs = op.getReceivedHeaders();
                String name = (String) hs.getHeader(HeaderSet.NAME);
                if (name != null) {
                    System.out.println("put name:" + name);
                }

                InputStream is = op.openInputStream();

                StringBuffer buf = new StringBuffer();
                int data;
                while ((data = is.read()) != -1) {
                    buf.append((char) data);
                }

                System.out.println("got:" + buf.toString());

                op.close();
                return ResponseCodes.OBEX_HTTP_OK;
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
            }
        }
    }

    @Override
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        String name;

        try {
            name = remoteDevice.getFriendlyName(false);
        } catch (IOException e) {
//            e.printStackTrace();
            name = remoteDevice.getBluetoothAddress();
        }

        System.out.println("Device Found: " + name + ", " + remoteDevice.getBluetoothAddress());
        devices.add(remoteDevice);

    }

    @Override
    public void servicesDiscovered(int transIndex, ServiceRecord[] serviceRecords) {
        for (int i = 0; i < serviceRecords.length; i++) {
            String url = serviceRecords[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            if (url == null) {
                continue;
            }
            DataElement serviceName = serviceRecords[i].getAttributeValue(0x0100);
            if (serviceName != null) {
                System.out.println("service " + serviceName.getValue() + " found " + url);

                if(serviceName.getValue().equals("OBEX Object Push")){
//                    sendMessageToDevice(url);
                }
            } else {
                System.out.println("service found " + url);
            }
        }
    }

    @Override
    public void serviceSearchCompleted(int i, int i2) {
        if (i2 == DiscoveryListener.SERVICE_SEARCH_COMPLETED) {
            System.out.println("Service search was successful");
        } else {
            System.out.println("Service search has failed");
        }
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void inquiryCompleted(int i) {
        synchronized (lock) {
            lock.notify();
        }
    }

    public void openConnection() {
        try {
            notifier = (StreamConnectionNotifier)Connector.open(btUrl);

            // block the current thread until a client responds
            con = notifier.acceptAndOpen();

            // the client has responded, so open some streams
            input = con.openInputStream();
            output = con.openOutputStream();

            serialReader = new Thread(new SerialReader(input, output));
            serialReader.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SerialReader implements Runnable {

        private InputStream inputStream;
        private OutputStream outputStream;

        InputStreamReader isr;
        BufferedReader ir;
        BufferedInputStream bufferedInputStream;
        int line;

        public SerialReader(InputStream input, OutputStream output) {
            inputStream = input;
            outputStream = output;

            isr = new InputStreamReader(inputStream);
            ir = new BufferedReader(isr);
            bufferedInputStream = new BufferedInputStream(inputStream);
        }

        @Override
        public void run() {
            while (true) {

                try {
                    line = ir.read();

                    EventBusService.publish("AC Direction: " + (char)(line));
                    if (line != 0 && line != 65533 && line >= 100) {
                        EventBusService.publish(new IRSeekerEvent(line - 100));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                EventBusService.publish(true);
            }
        }
    }

    @EventHandler
    public void handle(ShutDownEvent event) {
        if (event.shutingDown) {
            serialReader.stop();
        }
    }
}
