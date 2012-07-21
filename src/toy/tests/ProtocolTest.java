package toy.tests;

import junit.framework.Assert;
import org.junit.Test;
import toy.util.Protocol;

/**
 * @author Jonas
 *         Date: 7/21/12
 *         Time: 2:29 AM
 */
public class ProtocolTest {
    private static final String sep = System.getProperty("line.separator");

    @Test
    public void testBasicDecode() throws Exception {
        String test = "First Line" + Protocol.LINE_SEP + "Second Line" + Protocol.LINE_SEP + "Third Line" + Protocol.EOM;
        String result = "First Line" + sep + "Second Line" + sep + "Third Line";

        Assert.assertEquals(result, Protocol.unescape(test));
    }

    @Test
    public void testNoEOMDecode() throws Exception {
        String test = "First Line" + Protocol.LINE_SEP + "Second Line" + Protocol.LINE_SEP + "Third Line";
        String result = "First Line" + sep + "Second Line" + sep + "Third Line";

        Assert.assertEquals(result, Protocol.unescape(test));
    }
    
    @Test
    public void testExtraDataDecode() throws  Exception {
        String test = "First Line" + Protocol.LINE_SEP + "Second Line" + Protocol.LINE_SEP + "Third Line" + Protocol.EOM +
                "This is extra" + Protocol.LINE_SEP + "And should not appear in result";
        String result = "First Line" + sep + "Second Line" + sep + "Third Line";

        Assert.assertEquals(result, Protocol.unescape(test));
    }

    @Test
    public void testActualLineBreaks() throws Exception {
        String test = "First Line" + '\n' + "Second Line" + '\n' + "Third Line" + Protocol.EOM;
        String result = "First Line" + sep + "Second Line" + sep + "Third Line";

        Assert.assertEquals(result, Protocol.unescape(test));
    }
}
