package toy.Server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Jonas
 *         Date: 7/18/12
 *         Time: 9:40 PM
 */
public class Server {
    final int portNumber = 2626;

    public void start() throws IOException {

        // Setup server side socket, initialized to port: 'portNumber'
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.err.println("Could not listen on Port: " + portNumber);
            System.exit(-1);
        }

        int id = 0;
        while (true) {
            Runnable job = new ServerMessageLoggingJob(serverSocket.accept());
            Thread t = new Thread(job, "ProcessConnectionJob" + Integer.toString(++id));
            t.start();
        }
    }
}
