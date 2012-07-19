package toy.Server;

import java.io.IOException;

/**
 * @author Jonas
 *         Date: 7/19/12
 *         Time: 12:16 AM
 */
public class RunServer {
    public static void main(String[] args) throws IOException {
        (new SimpleServer()).start();
    }
}
