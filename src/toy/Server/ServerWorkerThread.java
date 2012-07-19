package toy.Server;

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

            // We need to be able to read input as bytes and convert them into usable characters data. The BufferedReader
            // wraps that translator (the InputStreamReader) so that it can be read far more efficiently
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            String lnInput, lnOutput;
            while ((lnInput = in.readLine()) != null) {

                // attempt to log the message, continue to reattempt after short interval, record success
                int count = 0;
                boolean logSuccessful = false;
                while ((!logSuccessful) && count < 200) {
                    logSuccessful = MessageLogger.log(lnInput);
                    count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) { e.printStackTrace(); }
                }

                // setup output header
                lnOutput = "SERVER:" + socket.getRemoteSocketAddress().toString() + "\n" +
                           "CLIENT:" + socket.getLocalSocketAddress().toString() + "\n\t";

                // check success condition of logging
                if (logSuccessful) {
                    // Setup return message
                    lnOutput += messageHead + " [" + lnInput + "]\n";

                    // print and flush
                    out.print(lnOutput);
                    out.flush();
                } else {
                    // Setup return message
                    lnOutput += "unable to log message... :(\n";

                    // print and flush
                    out.print(lnOutput);
                    out.flush();
                }
            }

            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            // TODO: Catch clause implementation
            e.printStackTrace(System.err);
        }
    }
}
