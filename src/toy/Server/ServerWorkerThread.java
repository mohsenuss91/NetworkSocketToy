package toy.Server;

import toy.util.MessageLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Jonas
 *         Date: 7/18/12
 *         Time: 10:41 PM
 */
public class ServerWorkerThread extends Thread {
    private Socket socket = null;
    private static int threadCount = 0;
    private final static String messageHead = "SIMPLE SERVER HAS LOGGED";

    public ServerWorkerThread(Socket socket) {
        super(ServerWorkerThread.class.getName() + (++threadCount));
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //Print confirmation Message
            System.out.println("Connection established with: " + socket.getRemoteSocketAddress().toString());

            // Since the socket is passing byte data to and from, we need to wrap be able to translate output into bytes.
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            /* The socket, by default returns byte data (InputStream), so we need to convert it into human readable
             * character data. This is done by the InputStreamReader. Finally the BufferedReader more efficiently
             * processes the data (rather than read and process each character as soon as it comes in, the
             * BufferedReader reads the characters into its preset buffer and processes the data from there instead)
             */
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String lnInput, lnOutput;
            while ((lnInput = in.readLine()) != null) {
                if (!lnInput.trim().equalsIgnoreCase("")) {
                    // Log the message
                    boolean logSuccessful = logMessage(lnInput);

                    // setup output header
                    lnOutput = "SERVER:" + socket.getRemoteSocketAddress().toString() + "\n" +
                               "CLIENT:" + socket.getLocalSocketAddress().toString() + "\n\t";

                    // check success condition of logging
                    if (logSuccessful) { lnOutput += messageHead + " [" + lnInput + "]\n"; }
                    else { lnOutput += "unable to log message... :("; }

                    // print and flush
                    out.print(lnOutput);
                    out.flush();
                }
            }

            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            logMessage("Unable to Log Message");
        }
    }

    private boolean logMessage(String message) {
        // attempt to log the message, continue to reattempt after short interval, record success
        int count = 0;
        boolean logSuccessful = false;
        while ((!logSuccessful) && count < 50) {

            // attempt to log the message, and record the success and count of the attempts
            logSuccessful = MessageLogger.log(message);
            count++;

            // check the state of the message log operation
            if (logSuccessful) {
                return true;
            } else {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }

        // if we've tried to log 50 times and it still hasn't happened then return false
        return false;
    }
}
