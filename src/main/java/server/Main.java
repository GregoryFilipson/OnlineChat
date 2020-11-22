package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        File setPortSocket = new File("settings.txt");
        List<Thread> listActiveThreads = new ArrayList<>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(setPortFromFile(setPortSocket));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try (Socket socket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                new Thread(() -> {
                    String msg = null;
                    listActiveThreads.add(Thread.currentThread());
                    while (true) {
                        try {
                            if ((msg = in.readLine()) == null) break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sendToAllConnections(out, msg, listActiveThreads);
                        if (msg.equals("/exit")) {
                            listActiveThreads.remove(Thread.currentThread());
                            break;
                        }
                    }
                }).start();
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }

    public static int setPortFromFile(File file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String portSocket = scanner.nextLine();
        return Integer.parseInt(portSocket);
    }

    public static void sendToAllConnections(PrintWriter out, String msg, List<Thread> list) {
        for (int i = 0; i < list.size(); i++) {
            Date date = new Date();
            out.println(date.toString() + " " + msg);
        }
    }
}
