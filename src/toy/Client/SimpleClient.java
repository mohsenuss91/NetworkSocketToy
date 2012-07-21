package toy.Client;

import toy.util.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author Jonas
 *         Date: 7/18/12
 *         Time: 11:44 PM
 */
public class SimpleClient {


    public void start(String[] args) throws IOException {
        ClientConnection connection = new ClientConnection(args);

        PrintWriter out = null;
        BufferedReader in = null;
        Scanner stdIn = null;

        out = new PrintWriter(connection.getSocket().getOutputStream());
        in = new BufferedReader(new InputStreamReader(connection.getSocket().getInputStream()));
        stdIn = new Scanner(new InputStreamReader(System.in));

        StringBuilder userInput = new StringBuilder("");

        String line;
        while ((line = stdIn.nextLine()) != null) {
            if (line.trim().equals("")) {
                userInput.append(Protocol.EOM);
                break;
            }

            if (!userInput.toString().equals("")) { userInput.append(Protocol.LINE_SEP); }
            userInput.append(line);

        }

        System.out.println(userInput.toString());
/*
        try {
            while (!(userInput = stdIn.readLine()).equalsIgnoreCase("exit")) {
                out.println(userInput);
                out.flush();
                System.out.println(in.readLine());
            }
        } catch (IOException e) {
            // TODO: Catch clause implementation
            e.printStackTrace();
        } finally {
            out.close();
            in.close();
            stdIn.close();
            socket.close();
        }
*/

    }

}