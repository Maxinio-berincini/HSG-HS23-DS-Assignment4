package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class LTClientThread implements Runnable {

    private final DatagramSocket clientSocket;
    LamportTimestamp lc;

    byte[] receiveData = new byte[1024];

    public LTClientThread(DatagramSocket clientSocket, LamportTimestamp lc) {
        this.clientSocket = clientSocket;
        this.lc = lc;
    }

    @Override
    public void run() {
        String response = null;

        while (true) {
            try {

                // receive the response from the server
                DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(packet);

                // split response into message and timestamp
                response = new String(packet.getData(),0,packet.getLength());
                String[] fullResponse = response.split(":");
                String message = fullResponse[0];
                int responseTimestamp = Integer.parseInt(fullResponse[1]);

                // update the clock
                lc.updateClock(responseTimestamp);

                // print message and timestamp
                System.out.println("Server:" + message + ":" + lc.getCurrentTimestamp());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
