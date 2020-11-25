package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler {
    private Socket socket;
    private PrintWriter out;
    private String name;


    public ClientHandler(Socket socket, PrintWriter out) {
        this.socket = socket;
        this.out = out;
    }

    public PrintWriter getOut() {
        return out;
    }

    public synchronized void sendMessageToClient(String name, String msg) {
            LocalDateTime date = LocalDateTime.now();
            out.println(date + " " + name + ": " + msg);
            out.flush();
    }
}
