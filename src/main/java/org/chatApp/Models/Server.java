package org.chatApp.Models;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //Responsible for listening for incoming connections from clients
   private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    //The class responsible for keeping the server running
    public void beginServer(){
        try{
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A new connection has been made.");
                ClientsConnection clientsConnection = new ClientsConnection(socket);

                Thread thread = new Thread(clientsConnection);
                thread.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    public void closeServerSocket(){
//        try{
//            if(serverSocket != null){
//                serverSocket.close();
//            }
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1987);
        Server server = new Server(serverSocket);
        server.beginServer();
//        server.closeServerSocket();
    }
}
