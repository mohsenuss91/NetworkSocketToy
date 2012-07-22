package toy.Client;

import toy.util.Protocol;

import java.io.*;

/**
 * @author Jonas
 *         Date: 7/18/12
 *         Time: 11:44 PM
 */
public class Client {
    private ClientConnection connection = null;
    private PrintWriter writeOut = null;
    private BufferedReader readIn = null;

    public Client(String[] args) throws IOException {
        connection = new ClientConnection(args);
        writeOut = new PrintWriter(connection.getSocket().getOutputStream());
        readIn = new BufferedReader(new InputStreamReader(connection.getSocket().getInputStream()));
    }

    public void close() {
        writeOut.println(Protocol.EOT);
        writeOut.flush();
        try {
            if (writeOut != null) { writeOut.close(); }
            if (readIn != null) { readIn.close(); }
            connection.getSocket().close();
        } catch (IOException e) { /* do nothing */ }
    }

    public void sendMessage(String message) {
        writeOut.println(Protocol.escape(message));
        writeOut.flush();
    }

    public String readResponse() {
        String response = "";
        try {
            if (readIn != null) { response = readIn.readLine(); }
        } catch (IOException e) {
            response = "Error Reading Response :(";
        }

        return Protocol.unescape(response);
    }


}