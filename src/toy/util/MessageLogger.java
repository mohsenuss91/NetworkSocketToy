package toy.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jonas
 *         Date: 7/18/12
 *         Time: 10:56 PM
 */
public class MessageLogger {

    final private static String pathName = "./data_store/loggedMessages.txt";
    final private static DateFormat dateFormat = new SimpleDateFormat("[MM/dd/yyyy HH:mm:ss] ");

    public static synchronized boolean log(String message) {
        try {
            FileWriter fileWriter = new FileWriter(pathName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dateFormat.format(new Date()) + message + "\n");
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
