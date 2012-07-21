package toy.Client;

import java.io.IOException;

/**
 * @author Jonas
 *         Date: 7/20/12
 *         Time: 10:31 PM
 */
public class RunClient {
    public static void main(String[] args) throws IOException {
        (new SimpleClient()).start(args);
    }

}
