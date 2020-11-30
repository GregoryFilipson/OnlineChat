package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String host = "localhost";
        File setPortSocket = new File("settings.txt");
        try (Socket clientSocket = new Socket(host, setPortFromFile(setPortSocket))) {
            try {
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Введите Ваш никнейм: ");
                    try {
                        out.println(reader.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ServerSender sender = new ServerSender(in);
                    sender.start();
                    String msg = "";
                    while (!msg.equals("/exit")) {
                        try {
                            System.out.println("Введите сообщение: ");
                            msg = reader.readLine();
                            out.println(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    sender.interrupt();
                    try {
                        sender.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ServerSender extends Thread {
        BufferedReader in;

        public ServerSender(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    String str = in.readLine();
                    System.out.println(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    String str = in.readLine();
                    System.out.println(str);
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
}