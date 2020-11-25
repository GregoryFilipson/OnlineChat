package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        final List<ClientHandler> listActiveClients = new ArrayList<>();
        final Logger logger = Logger.getInstance();
        File setPortSocket = new File("settings.txt");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(setPortFromFile(setPortSocket));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Сервер чата запущен!");
        while (true) {
            try (Socket socket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                new Thread(() -> {
                    ClientHandler clientHandler = new ClientHandler(socket, out);
                    listActiveClients.add(clientHandler);
                    String name = "";
                    try {
                        name = in.readLine();
                        for (ClientHandler client: listActiveClients) {
                            client.getOut().println(name + " зашел в чат!");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String msg = null;
                    while (true) {
                        try {
                            msg = in.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (msg.equals("/exit")) {
                            removeClient(clientHandler, listActiveClients);
                            try {
                                in.close();
                                out.close();
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        sendToAllConnections(name, msg, listActiveClients);
                        logger.log(msg);
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
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

    public static void sendToAllConnections(String name, String msg, List<ClientHandler> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).sendMessageToClient(name, msg);
        }
    }

    public static synchronized void removeClient(ClientHandler clientHandler, List<ClientHandler> list) {
        list.remove(clientHandler);
    }
}
