package fund.splitting;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class CheckerTest {
    
    @Test
    public void testSimpleValidInput() throws XMLStreamException {
        
        String xml = "<funds>"
                + "<fund isin=\"1\">"
                + "<position id=\"2\">"
                + "<percentage>0.6</percentage>"
                + "<percentage>0.4</percentage>"
                + "</position></fund></funds>";
        
        assertTrue(Checker.isFundSplittingCorrect(new ByteArrayInputStream(xml.getBytes())));

    }
    
    @Test
    public void testSimpleInValidInput() throws XMLStreamException {
        
        String xml = "<funds>"
                + "<fund isin=\"1\">"
                + "<position id=\"2\">"
                + "<percentage>10</percentage>"
                + "</position></fund></funds>";
        
        assertFalse(Checker.isFundSplittingCorrect(new ByteArrayInputStream(xml.getBytes())));

    }
    
    @Test(expected = XMLStreamException.class)
    public void testMalformedInput() throws XMLStreamException {
        
        String xml = "<funds>"
                + "<thisIsNotXML>"
                + "</funds>";
        
        assertFalse(Checker.isFundSplittingCorrect(new ByteArrayInputStream(xml.getBytes())));
    }

    @Test
    public void testValidFile() throws XMLStreamException {
        ClassLoader classLoader = Checker.class.getClassLoader();
        InputStream in = classLoader.getResourceAsStream("input_valid.xml");
        assertTrue(Checker.isFundSplittingCorrect(in));
    }
    
    @Test
    public void testInValidFile() throws XMLStreamException {
        ClassLoader classLoader = Checker.class.getClassLoader();
        InputStream in = classLoader.getResourceAsStream("input_invalid.xml");
        assertFalse(Checker.isFundSplittingCorrect(in));
    }
    
}
