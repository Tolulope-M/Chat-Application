package org.chatApp.Models;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket client;
    private BufferedReader input;
    private BufferedWriter output;
    private String clientUserName;

    public Client(Socket client, String clientUserName) {
        try {
            this.client = client;
            this.output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.clientUserName = clientUserName;
        } catch (IOException e) {
            closeServer(client, input, output);
        }
    }

    public void sendMessage() {
        try {
            output.write(clientUserName);
            output.newLine();
            output.flush();

            Scanner scanner = new Scanner(System.in);
            while (client.isConnected()) {
                String messageToSend = scanner.nextLine();
                output.write(clientUserName + ": " + messageToSend);
                output.newLine();
                output.flush();
            }
        } catch (IOException e) {
            closeServer(client, input, output);
        }
    }

    public void messageFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromChat;

                while (client.isConnected()) {
                    try {
                        messageFromChat = input.readLine();
                        System.out.println(messageFromChat);
                    } catch (IOException e) {
                        closeServer(client, input, output);
                    }
                }
            }
        }).start();
    }

    public void closeServer(Socket client, BufferedReader input, BufferedWriter output) {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String clientUserName = scanner.nextLine();
        Socket client = new Socket("Localhost", 1987);
        Client client1 = new Client(client, clientUserName);
        client1.messageFromServer();
        client1.sendMessage();
    }
}
