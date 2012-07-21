package toy.Client;

import toy.util.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author Jonas
 *         Date: 7/18/12
 *         Time: 11:44 PM
 */
public class Client {
    private ClientConnection connection = null;

    public Client(String[] args) {
        connection = new ClientConnection(args);
    }

    public void close() {
        PrintWriter out = null;
        try {
            out = new PrintWriter(connection.getSocket().getOutputStream());
            out.println(Protocol.EOT);
            out.flush();
        } catch (IOException e) {
            /* do nothing */
        } finally {
            if (out != null) { out.close(); }
            try {
                connection.getSocket().close();
            } catch (IOException e) { /* do nothing */ }
        }
    }

    public boolean sendMessage(String message) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(connection.getSocket().getOutputStream());
            out.println(Protocol.escape(message));
            out.flush();
        } catch (IOException e) {
            return false;
        } finally {
            if (out != null) { out.close(); }
        }

        return true;
    }

    public String readResponse() {
        BufferedReader in = null;
        String response = "";

        try {
            in = new BufferedReader(new InputStreamReader(connection.getSocket().getInputStream()));
            response = in.readLine();
        } catch (IOException e) {
            response = "Error Reading Response :(";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {/* do nothing */}
            }
        }
        if (response != null) { return Protocol.unescape(response); }
        else { return ""; }
    }


}