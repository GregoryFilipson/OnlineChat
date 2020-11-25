package server;

import java.io.*;

public class Logger {
    final File file = new File("log.txt");
    private static Logger logger;

    private Logger() {
    }

    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public void log(String msg) {
        BufferedWriter bw = null;
        FileWriter fr = null;
        try {
            fr = new FileWriter(file, true);
            bw = new BufferedWriter(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.newLine();
            bw.append(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}