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
                    String msg = null;
                    while (!msg.equals("exit")) {
                        try {
                            msg = in.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        out.println(msg);
                    }
                    sender.setStop();
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
        private boolean stop;
        BufferedReader in;

        public void setStop() {
            stop = true;
        }

        public ServerSender(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                while (!stop) {
                    String str = in.readLine();
                    System.out.println(str);
                }
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
}


//public class Main {
//    public static void main(String[] args) {
//        String host = "localhost";
//        File setPortSocket = new File("settings.txt");
//        try (Socket clientSocket = new Socket(host, setPortFromFile(setPortSocket));
//             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            System.out.println("Введите Ваш никнейм");
//            String name = reader.readLine();
//            Thread.currentThread().setName(name);
//            out.println("Пользователь " + Thread.currentThread().getName() + " в чате!");
//            while (true) {
//                String msg = "";
//                try {
//                    if (in.ready()) {
//                        System.out.println(in.readLine());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (reader.ready()) {
//                    msg = reader.readLine();
//                }
//                if ("/exit".equals(msg)) {
//                    break;
//                }
//                out.println(Thread.currentThread().getName() + ": " + msg);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static int setPortFromFile(File file) {
//        Scanner scanner = null;
//        try {
//            scanner = new Scanner(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        String portSocket = scanner.nextLine();
//        return Integer.parseInt(portSocket);
//    }
//}