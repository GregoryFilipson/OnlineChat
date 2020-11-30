package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        final List<ClientHandler> listActiveClients = new ArrayList<>();
        final Logger logger = Logger.getInstance();
        File setPortSocket = new File("settings.txt");
        ServerSocket serverSocket = new ServerSocket(setPortFromFile(setPortSocket));
        System.out.println("Сервер чата запущен!");
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String name = "";
                    ClientHandler clientHandler = null;
                    try {
                        name = in.readLine();
                        clientHandler = new ClientHandler(socket, name);
                        addClient(clientHandler, listActiveClients);
                        sendToAllConnections(name, "вошел в чат!", listActiveClients);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String msg = "";
                    while (true) {
                        try {
                            msg = in.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if ("/exit".equals(msg)) {
                            assert clientHandler != null;
                            sendToAllConnections(name, "вышел из чата!", listActiveClients);
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
                        if (!msg.equals("")) {
                            sendToAllConnections(name, msg, listActiveClients);
                            LocalDateTime date = LocalDateTime.now();
                            logger.log(date + " " + name + ": " + msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
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

    public static synchronized void sendToAllConnections(String name, String msg,
                                                         List<ClientHandler> list) {
        for (ClientHandler clientHandler : list) {
            clientHandler.sendMessageToClient(name, msg);
        }
    }

    public static synchronized void removeClient(ClientHandler clientHandler, List<ClientHandler> list) {
        list.remove(clientHandler);
    }

    public static synchronized void addClient(ClientHandler clientHandler, List<ClientHandler> list) {
        list.add(clientHandler);
    }
}
