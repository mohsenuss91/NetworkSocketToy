package toy.Server;

import toy.util.MessageLogger;
import toy.util.Protocol;
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
public class MessageLoggingJob implements Runnable {
    private Socket socket = null;

    public MessageLoggingJob(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Protocol.MessageType msgType = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            //Print confirmation Message
            System.out.println("Connection established with: " + socket.getRemoteSocketAddress().toString());

            // Since the socket is passing byte data to and from, we need to wrap be able to translate character data
            // that we write into bytes that the output stream understands
            out = new PrintWriter(socket.getOutputStream());

            // The socket, by default returns byte data (InputStream), so we need to convert it into human readable
            // character data. This is done by the InputStreamReader. Finally the BufferedReader more efficiently
            // processes the data (rather than read and process each character as soon as it comes in, the
            // BufferedReader reads the characters into its preset buffer and processes the data from there instead)
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String rawInput;

            // while loop *should* keep running, while connection is open, since a socket's stream would remain open
            // (and hence not return null) while the connection is active
            while ((rawInput = in.readLine()) != null) {
                switch (Protocol.analyze(rawInput)) {
                    // EOT signals that this connection should close
                    // NO other text in line will be processed (EOT indicator should normally be sent by itself)
                    case EOT:
                        msgType = Protocol.MessageType.EOT;
                        throw new IOException();

                    // EOM indicator recognition of a discrete message that can be logged and processed
                    // any text AFTER the EOM indicator will NOT be processed
                    case EOM:
                        msgType = Protocol.MessageType.EOM;

                        String returnMessage;

                        // attempt to log message, returning success or failure status to client
                        if (logMessage(Protocol.unescape(rawInput))) {
                            returnMessage = "The Server logged the following: [" +
                                    Protocol.LINE_SEP + rawInput + Protocol.LINE_SEP + "]" + Protocol.EOM;
                        } else {
                            returnMessage = "The Server was unable to log your message :( ..." + Protocol.EOM;
                        }

                        // print and flush
                        out.println(returnMessage);
                        out.flush();

                        // end the transmission
                        out.println(Protocol.EOT);
                        out.flush();

                        break;

                    // INVALID indicator signal that the message is improperly formatted. Send a usage message
                    case INVALID:
                        msgType = Protocol.MessageType.INVALID;

                        out.println(Protocol.INVALID_MSG);
                        out.flush();

                        break;

                    default:
                }
            }

        } catch (IOException e) {
            if (msgType != Protocol.MessageType.EOT) {
                logMessage("IOException ");
            }
        } finally {
            try {
                if (out != null) { out.close(); }
                if (in != null) { in.close(); }
                socket.close();
            } catch (IOException e) { /* Do Nothing */ }
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
