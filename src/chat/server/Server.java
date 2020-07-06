package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class Server {
    private List<ClientHandler> clients;
    private AuthService authService;
    Socket socket;

    public AuthService getAuthService() {
        return authService;
    }


    public Server() {
        clients = new Vector<>();
        authService = new SimpleAuthService();

        ServerSocket server = null;


        final int PORT = 8189;

        try {
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");
                System.out.println("socket.getRemoteSocketAddress(): " + socket.getRemoteSocketAddress());
                System.out.println("socket.getLocalSocketAddress() " + socket.getLocalSocketAddress());
                new ClientHandler( this, socket);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

    }
// метод проверяет есть ли клиент с таким ником
    Socket narrowlyTargetedMsg(String nick){

            for (ClientHandler client : clients) {

                if (client.getNick().equals(nick)){
                    return client.getSocket();
                }
        }

        return null;
        }




    void broadcastMsg(String msg) {
        for (ClientHandler client : clients) {
            client.sendMsg(msg);
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}

