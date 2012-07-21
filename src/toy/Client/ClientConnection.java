package toy.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Jonas
 *         Date: 7/21/12
 *         Time: 12:59 AM
 */
public class ClientConnection {
    final private static String defaultAddress = "192.168.2.109";
    final private static int defaultPort = 2626;

    private Socket socket = null;

    public ClientConnection(String[] args) {
        initSocket(args);
    }

    private ClientConnection() {

    }

    public Socket getSocket() {
        return socket;
    }

    private void initSocket(String[] args) {
        InetAddress ipAddress = null;
        int portNumber = -1;
        try {
            switch (args.length) {
                case 1:
                    ipAddress = getIPAddress(args[0]);
                    portNumber = getPort();
                    break;

                case 2:
                    ipAddress = getIPAddress(args[0]);
                    portNumber = getPort(args[1]);
                    break;

                default:
                    ipAddress = getIPAddress();
                    portNumber = getPort();
                    break;
            }
        } catch (IOException e) {
            System.err.println("Couldn't resolve IP address");
            System.exit(-1);
        }

        Socket socket = null;
        try {
            socket = new Socket(ipAddress, portNumber);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O connection for: " + ipAddress.getHostAddress() + ":" + portNumber);
            System.exit(-1);
        }

        this.socket = socket;
    }

    private InetAddress getIPAddress() throws UnknownHostException {
        return InetAddress.getByName(defaultAddress);
    }

    private InetAddress getIPAddress(String ip) throws UnknownHostException {
        return InetAddress.getByName(ip);
    }

    private int getPort () {
        return defaultPort;
    }

    private int getPort(String port) {
        int portNumber = -1;
        try { portNumber = Integer.valueOf(port); }
        catch (NumberFormatException e) {
            System.err.println("Invalid Port Number: " + port);
            System.exit(-1);
        }

        return portNumber;
    }


}
