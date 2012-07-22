package toy.Client;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Jonas
 *         Date: 7/21/12
 *         Time: 3:35 AM
 */
public class ClientConsoleUI {
    private static final String DRAW_LINE = "\n----------------------------------------------------------";
    private static Client client = null;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("Welcome to the Client for a basic logging server...");

        char continueLoop;
        do {
            client = getNewClient(args);
            sendReceiveSet(in);
            System.out.println(DRAW_LINE);
            System.out.println("\nDo you wish to continue (y/n)?");
            continueLoop = in.nextLine().toLowerCase().charAt(0);
            client.close();
        } while (continueLoop == 'y');
    }

    private static void sendReceiveSet(Scanner in) {
        String line;
        StringBuilder message = new StringBuilder("");

        System.out.println(DRAW_LINE);
        System.out.println("**TYPE IN YOUR MESSAGE**");

        do {
            line = in.nextLine();
            if (!message.toString().equals("")) { message.append('\n'); }
            message.append(line);
        } while (!line.trim().equals(""));

        client.sendMessage(message.toString());
        System.out.println(DRAW_LINE);
        System.out.println("**SERVER RESPONSE**\n");
        System.out.println(client.readResponse());
    }

    private static Client getNewClient(String[] args) {
        try {
            client = new Client(args);
        } catch (IOException e) {
            System.err.println("Unable to connect client to server");
            System.exit(-1);
        }
        return client;
    }

}
