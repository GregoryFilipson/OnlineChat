package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String host = "localhost";
        File setPortSocket = new File("settings.txt");
        Logger logger = null;
        try {
            logger = Logger.getInstance();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try (Socket clientSocket = new Socket(host, setPortFromFile(setPortSocket));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Введите Ваш никнейм");
            String name = reader.readLine();
            Thread.currentThread().setName(name);
            while (true) {
                System.out.println("Ожидание сообшения.......");
                String msg = reader.readLine();
                if ("/exit".equals(msg)) {
                    break;
                }
                out.println(Thread.currentThread().getName() + ": " + msg);
                String resp = in.readLine();
                logger.log(resp);
                System.out.println(resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
