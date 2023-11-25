package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class VectorClientThread implements Runnable {

    private final DatagramSocket clientSocket;
    VectorClock vcl;
    byte[] receiveData = new byte[1024];

    int id;

    public VectorClientThread(DatagramSocket clientSocket, VectorClock vcl, int id) {

        this.clientSocket = clientSocket;
        this.vcl = vcl;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(packet);
                String response = new String(packet.getData(), 0, packet.getLength()).trim();

                String[] responseMessageArray = response.split(":");
                String responseMessage = responseMessageArray[0];


                String[] receivedClockArray = responseMessageArray[1].replaceAll("\\p{Punct}", " ").trim().split("\\s+");


                int processId = Integer.parseInt(receivedClockArray[0]);
                int time = Integer.parseInt(receivedClockArray[1]);

                VectorClock tempClock = new VectorClock(4);
                tempClock.setVectorClock(processId, time);


                // Update the client's vector clock with the temporary clock
                vcl.updateClock(tempClock);
                vcl.tick(id);

                System.out.println("Server:" + responseMessageArray[0] + " " + vcl.showClock());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
