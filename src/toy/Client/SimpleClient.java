package toy.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Jonas
 *         Date: 7/18/12
 *         Time: 11:44 PM
 */
public class SimpleClient {
    final private static String defaultAddress = "192.168.2.109";
    final private static int defaultPort = 2626;

    public void start(String[] args) throws IOException {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        InetAddress address = null;

        // Setup IP Address either from arguments or from defaults
        String workingIP = defaultAddress;
        int workingPort = defaultPort;
        try {
            switch (args.length) {
                case 1:
                    workingIP = args[0];
                    break;

                case 2:
                    workingIP = args[0];
                    try { workingPort = Integer.valueOf(args[1]); }
                    catch (NumberFormatException e) {
                        System.err.println("Invalid Port Number: " + args[1]);
                        System.exit(-1);
                    }

                default:
            }

            address = InetAddress.getByName(workingIP);
        } catch (UnknownHostException e) {
            System.err.println("Invalid IP address: " + workingIP);
            System.exit(-1);
        }

        try {
            socket = new Socket(address, workingPort);
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            System.err.println("Couldn't get I/O connection for: " + address.getHostAddress() + ":" + workingPort);
            System.exit(-1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String userInput;

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
            System.out.println("Finally Block...");

            out.close();
            in.close();
            stdIn.close();
            socket.close();
        }

    }

}