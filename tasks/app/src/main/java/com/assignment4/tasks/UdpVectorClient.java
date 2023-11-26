package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;


public class UdpVectorClient {

    public static void main(String[] args) throws Exception
    {
        System.out.println("Enter your id (1 to 3): ");
        Scanner id_input = new Scanner(System.in);
        int id = id_input.nextInt();

        // prepare the client socket
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");

        // initialize the buffers
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        int port = 4040;
        List<String> logs;

        int startTime = 0;
        VectorClock vcl = new VectorClock(4);
        vcl.setVectorClock(id, startTime);

        //ask for user input aka message to the server
        System.out.println(id+": Enter any message:");
        Scanner input = new Scanner(System.in);

        while(true) {
            String messageBody = input.nextLine();
            // increment clock
            if (!messageBody.isEmpty()){
                vcl.tick(id);
            }
            HashMap<Integer, Integer> messageTime = new HashMap<>();
            messageTime.put(id,vcl.getCurrentTimestamp(id));
            Message msg = new Message(messageBody, messageTime);
            String responseMessage = msg.content + ':' + msg.messageTime;

            // check if the user wants to quit
            if(messageBody.equals("quit")){
                clientSocket.close();
                System.exit(1);
            }

            // send the message to the server
            sendData = responseMessage.getBytes();


            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(packet);


            // check if the user wants to see the history
            if(messageBody.equals("history")) {
                System.out.println("Receiving the chat history...");
                logs = new ArrayList<>();


                while (true) {
                    DatagramPacket historyPacket = new DatagramPacket(receiveData, receiveData.length);
                    try {
                        clientSocket.receive(historyPacket);
                        clientSocket.setSoTimeout(1000);
                        String message = new String(historyPacket.getData(),0,historyPacket.getLength()).trim();
                        logs.add(message);
                    } catch (IOException e) {
                        break;
                    }
                }

                UdpVectorClient uc = new UdpVectorClient();
                uc.showHistory(logs); // gives out all the unsorted logs stored at the server
                uc.showSortedHistory(logs); // shows sorted logs
            }
            else
            {
                VectorClientThread client;
                client = new VectorClientThread(clientSocket, vcl, id);
                Thread receiverThread = new Thread(client);
                receiverThread.start();
            }
        }
    }
    public void showHistory(List<String> logs){

        // prints the unsorted logs (history) coming form the server
        for (String message : logs) {

            System.out.println(message);
        }
    }
    public void showSortedHistory(List<String> logs){

        // prints sorted logs (history) received
        System.out.println("Print sorted conversation using attached vector clocks");
        Map<int[], String> logMap = new HashMap<>();

        for (String log : logs) {
            String[] responseMessageArray = log.split(":");
            String[] receivedClockArray = responseMessageArray[1].replaceAll("\\p{Punct}", "").trim().split("\\s+");
            int[] receivedClock = new int[receivedClockArray.length];
            for (int i = 0; i < receivedClockArray.length; i++) {
                receivedClock[i] = Integer.parseInt(receivedClockArray[i]);
            }
            logMap.put(receivedClock, responseMessageArray[0]);
        }



        Comparator<Map.Entry<int[], String>> clockComparator = new Comparator<Map.Entry<int[], String>>() {
            @Override
            public int compare(Map.Entry<int[], String> entry1, Map.Entry<int[], String> entry2) {
                int[] clock1 = entry1.getKey();
                int[] clock2 = entry2.getKey();
                for (int i = 0; i < Math.min(clock1.length, clock2.length); i++) {
                    if (clock1[i] != clock2[i]) {
                        return clock1[i] - clock2[i];
                    }
                }
                return clock1.length - clock2.length;
            }
        };

        // Extract entries and sort them
        List<Map.Entry<int[], String>> entries = new ArrayList<>(logMap.entrySet());
        entries.sort(clockComparator);

        // Populate the LinkedHashMap with sorted entries
        Map<int[], String> sortedLogMap = new LinkedHashMap<>();
        for (Map.Entry<int[], String> entry : entries) {
            sortedLogMap.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<int[], String> entry : sortedLogMap.entrySet()) {
            System.out.println(entry.getValue() + " " + Arrays.toString(entry.getKey()));
        }


    }

}
