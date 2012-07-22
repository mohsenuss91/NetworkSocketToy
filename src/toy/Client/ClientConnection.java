package toy.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
    private String[] args;

    public ClientConnection(String[] args) {
        this.args = args;
        this.socket = initSocket(args);
    }

    public Socket getSocket() {
        if (socket.isClosed()) { }

        return socket;
    }


    private Socket initSocket(String[] args) {
        InetAddress ipAddress;
        int portNum;
        switch (args.length) {
            case 1:
                ipAddress = getIPAddress(args[0]);
                portNum = getPort();
                break;

            case 2:
                ipAddress = getIPAddress(args[0]);
                portNum = getPort(args[1]);
                break;

            default:
                ipAddress = getIPAddress();
                portNum = getPort();
                break;
        }
        return initSocket(ipAddress, portNum);
    }

    private Socket initSocket(InetAddress ipAddress, int portNum) {
        Socket socket = null;

        try {
            socket = new Socket(ipAddress, portNum);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O connection for: " + ipAddress.getHostAddress() + ":" + portNum);
            System.exit(-1);
        }

        return socket;
    }

    private InetAddress getIPAddress() {
        InetAddress address = null;
        try {
           address = InetAddress.getByName(defaultAddress);
        } catch (UnknownHostException e) {
            System.err.println("Unknown Host: " + defaultAddress);
            System.exit(-1);
        }

        return address;
    }

    private InetAddress getIPAddress(String ip) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            System.err.println("Unknown Host: " + ip);
            System.exit(-1);
        }

        return address;
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
