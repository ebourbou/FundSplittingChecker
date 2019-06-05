package fund.splitting;

import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * @author admin
 *
 */
public class Checker {

    private static final String PERCENTAGE_ELEMENT_NAME = "percentage";
    private static final String FUND_ELEMENT_NAME = "fund";
    private static final Double TOLERANCE = 0.01;
    private static final Double TARGETED_PERCENTAGE = 1.0;

    
    public static void main(String[] args) throws XMLStreamException {

        ClassLoader classLoader = Checker.class.getClassLoader();
        InputStream in = classLoader.getResourceAsStream("input_invalid.xml");
        if (!isFundSplittingCorrect(in)) {
            throw new RuntimeException("fund positions do not add up to 100%");
        }
    }
    
    public static boolean isFundSplittingCorrect(InputStream in) throws XMLStreamException  {
        boolean isCorrectSoFar = true;
        XMLEventReader eventReader = XMLInputFactory.newInstance().createXMLEventReader(in);
        while (eventReader.hasNext() && isCorrectSoFar) {
            XMLEvent event = eventReader.nextEvent();
            if (isStartElementWithName(event, FUND_ELEMENT_NAME)) {
                isCorrectSoFar = isPercentageWithinTolerance(sumAllPercentagesOfFund(eventReader));
            }
        }
        return isCorrectSoFar;
    }

    
    /**
     * @param eventReader which must be positioned at the startelement of {@link Checker#FUND_ELEMENT_NAME}
     * @return Double containing the sum of all percentage values of this fund
     * @throws XMLStreamException
     */
    private static Double sumAllPercentagesOfFund(XMLEventReader eventReader) throws XMLStreamException {
        Double percentage = 0.0;
        while (eventReader.hasNext() && !isEndElementWithName(eventReader.peek(), FUND_ELEMENT_NAME)) {
            XMLEvent event = eventReader.nextEvent();
            if (isStartElementWithName(event, PERCENTAGE_ELEMENT_NAME)) {
                percentage += Double.valueOf(eventReader.nextEvent().asCharacters().getData());
            }
        }
        return percentage;
    }

    private static boolean isStartElementWithName(XMLEvent event, String name) {
        boolean returnValue = false;
        if (event.isStartElement()) {
            StartElement startElement = event.asStartElement();
            returnValue = startElement.getName().getLocalPart().equals(name);
        }
        return returnValue;
    }
    
    private static boolean isEndElementWithName(XMLEvent event, String name) {
        boolean returnValue = false;
        if (event.isEndElement()) {
            EndElement endElement = event.asEndElement();
            returnValue = endElement.getName().getLocalPart().equals(name);
        }
        return returnValue;
    }

    private static boolean isPercentageWithinTolerance(Double currentPercentage) {
        return !(currentPercentage < TARGETED_PERCENTAGE - TOLERANCE || currentPercentage > TARGETED_PERCENTAGE + TOLERANCE);
    }
}
