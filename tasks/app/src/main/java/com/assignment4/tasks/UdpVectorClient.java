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

            /*
             * write your code to send message to the server. clientSocket.send(messageTosend);
             */

            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(packet);


            // check if the user wants to see the history
            if(messageBody.equals("history")) {
                System.out.println("Receiving the chat history...");
                logs = new ArrayList<>();

                /*
                 * write your code to receive the logs, clientSocket.receive(getack);
                 * it should keep receiving till all the messages are reached.
                 * You can use the clientSocket.setSoTimeout(timeinmiliseconds); to detect if the all the messages have been received
                 * update the logs list
                 */

                while (true) {
                    DatagramPacket historyPacket = new DatagramPacket(receiveData, receiveData.length);
                    try {
                        clientSocket.receive(historyPacket);
                        String message = new String(receivePacket.getData());
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

        /*
         * write your code to sort the logs (history) in ascending order
         * to sort the logs, use the clock array, for example, [0,0,1,1] as key the to the logMap.
         * Since this is a custom sorting, create a custom comparator to sort logs
         * once sorted print the logs that are following the correct sequence of the message flow
         * to store the sorted logs for printing you could use LinkedHashMap
         */

    }

}
