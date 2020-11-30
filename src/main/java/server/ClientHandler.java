package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler {
    private Socket socket;
    private String name;
    private PrintWriter out;

    public ClientHandler(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
        try {
            this.out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessageToClient(String name, String msg) {
            LocalDateTime date = LocalDateTime.now();
            out.println(date + " " + name + ": " + msg);
            out.flush();
    }

}
