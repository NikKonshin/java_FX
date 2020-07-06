package chat.server;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.SocketHandler;

public class ClientHandler {
    Server server;
    Socket socket = null;
    DataInputStream in;
    DataOutputStream out;

    private String nick;
    private String login;


    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/auth")) {
                            String[] token = str.split("\\s");
                            if (token.length < 3) {
                                continue;
                            }
                            String newNick = server
                                    .getAuthService()
                                    .getNicknameByLoginAndPassword(token[1], token[2]);
                            if (newNick != null) {
                                sendMsg("/authok " + newNick);
                                nick = newNick;
                                login = token[1];
                                server.subscribe(this);
                                System.out.printf("Клиент %s подключился \n", nick);
                                break;
                            } else {
                                sendMsg("Неверный логин / пароль");
                            }
                        }

                        server.broadcastMsg(str);
                    }


                    //цикл работы
                    while (true) {
                        out = new DataOutputStream(socket.getOutputStream());
                        String str = in.readUTF();
                        if (str.startsWith("/w ")) {

                            sendMsgNick(str);


                        } else if (str.equals("/end")) {
                            out.writeUTF("/end");
                            break;
                        } else {
                            server.broadcastMsg(str);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Клиент отключился");
                    server.unsubscribe(this);
                    try {
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// метод отправляет сообщение определленному клиенту
   void sendMsgNick(String msg) {
        try {
            String msgNick = msg.split("\\s")[1];
            msg = msg.split("\\s")[2];
            Socket socketNick = server.narrowlyTargetedMsg(msgNick);
            if (socketNick != null) {

                out = new DataOutputStream(socketNick.getOutputStream());
                out.writeUTF(getNick() + ": "+ msg);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMsg(String str) {
        try {
            out.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }

    public Socket getSocket() {
        return socket;
    }

}
