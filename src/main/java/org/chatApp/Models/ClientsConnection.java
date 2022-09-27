package org.chatApp.Models;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

public class ClientsConnection implements Runnable {
    //It is implementing runnable so that instances will be handled by separate threads.
    public static ArrayList<ClientsConnection> clientsConnections = new ArrayList<>();
    private Socket client;
    private BufferedReader input;
    private BufferedWriter output;
    private String clientUserName;

    public ClientsConnection(Socket client) {
        try {
            this.client = client;
            this.output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.clientUserName = input.readLine();
            clientsConnections.add(this);
            connectionMessage("SERVER: " + clientUserName + " has been connected");
        } catch (Exception e) {
            System.err.println("Exception from connectionMessage method");
            closeServer(client, input, output);
        }
    }

        @Override
        public void run(){
            String messageFromClient;

            while(client.isConnected()) {
                try{
                    messageFromClient = input.readLine();
                    connectionMessage(messageFromClient);
                }catch(IOException e){
                    System.err.println("Exception from connectionMessage method");
                    closeServer(client, input, output);
                    break;
                }
            }
            if(!client.isConnected()){
                connectionMessage("Client has been disconnected");
            }
        }

      public void connectionMessage (String messageToSend){
        for (ClientsConnection clientsConnection : clientsConnections){
            try{
                if(!clientsConnection.clientUserName.equals(clientUserName)){
                    clientsConnection.output.write(messageToSend);
                    clientsConnection.output.newLine();
                    clientsConnection.output.flush();
              }
            }catch(Exception e){
                //e.printStackTrace();
                System.err.println("Exception from connectionMessage method");
                closeServer(client, input, output);
            }
        }
        }

        public  void removeClientsConnection(){
        clientsConnections.remove(this);
        connectionMessage("SERVER: " + clientUserName + " has benn disconnected");
    }

    public void closeServer(Socket client, BufferedReader input, BufferedWriter output){
        removeClientsConnection();
        try{
            if(input != null){
                input.close();
            }
            if(output != null){
                output.close();
            }
            if(client != null){
                client.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}