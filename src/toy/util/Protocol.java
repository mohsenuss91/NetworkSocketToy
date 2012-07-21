package toy.util;

import com.sun.deploy.net.proxy.StaticProxyManager;

import java.io.StringReader;
import java.security.PublicKey;
import java.util.Scanner;

/**
 * @author Jonas
 *         Date: 7/20/12
 *         Time: 11:33 PM
 */
public class Protocol {
    public static final String EOM = "[EOM]"; // End of Message Indicator
    private static final String regexEOM = "\\[EOM\\]";

    public static final String LINE_SEP = "[SEP]"; // Line Separator Indicator
    private static final String regexLINE_SEP = "\\[SEP\\]";
    private static final String localSEP =  System.getProperty("line.separator");

    public static final String EOT = "[EOT]"; // End of Transmission Indicator
    private static final String regexEOT = "\\[EOT\\]";

    public static final String INVALID_MSG =
            "The message was received improperly formatted." + LINE_SEP +
            "All discrete messages should end with the character sequence '[','E','O','M',']'." + LINE_SEP +
            "For full compatibility all line separation character sequences can be replaced with '[','S','E','P',']'." + LINE_SEP +
            "When all messages are done being sent, then send the character sequence '[','E','O','T',']'," + LINE_SEP +
            "on a line by itself to indicate that the connection should be closed.";

    public static String unescape(String input) {

        StringBuilder output = new StringBuilder("");

        // Split input first by the EOM indicator (which should ALWAYS be present)
        // Anything after the EOM indicator will NOT be processed here
        // Then split on the LINE_SEP indicator, storing values in an array
        String[] lines = (input.split(regexEOM)[0]).split(regexLINE_SEP);

        // Iterate through lines, and add in the local line separator string between lines
        for (int i = 0; i < lines.length; i++) {
            if (i != 0) { output.append(localSEP); };
            output.append(lines[i]);
        }

        return output.toString();
    }

    public static String escape(String input) {
        if (input == null) { return EOM; }
        if (input.trim().equals("")) { return EOM; }

        StringBuilder output = new StringBuilder("");
        Scanner reader = new Scanner(input);
        do {
            String line = reader.nextLine();
            if (line != null) {
                if (!output.toString().equals("")) { output.append(LINE_SEP); }
                output.append(line);
            }
        } while (reader.hasNextLine());

        output.append(EOM);

        return output.toString();
    }

    public static MessageType analyze(String input) {
        if (input.matches(".*" + Protocol.regexEOT + ".*")) { return MessageType.EOT; }
        else if (input.matches(".*" + Protocol.regexEOM + ".*")) { return MessageType.EOM; }
        else { return MessageType.INVALID; }
    }

    public enum MessageType { EOT, EOM, INVALID }

}
