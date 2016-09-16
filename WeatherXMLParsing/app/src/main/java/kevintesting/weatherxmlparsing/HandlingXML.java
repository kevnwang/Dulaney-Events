package kevintesting.weatherxmlparsing;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * @author Kevin Wang kwang21093@gmail.com
 * @desc this class extends the provided DefaultHandler class by SAX Parser
 * it overrides methods such as startElement(), endElement(), and characters() to successfully parse XML and pass it to an Event
 */
public class HandlingXML extends DefaultHandler {
    private Boolean currentElement;
    private String currentValue;
    private Event item;
    private ArrayList<Event> itemsList = new ArrayList<Event>(); //used to hold all of the Events that the XML will parse to

    public ArrayList<Event> getItemsList() {
        return itemsList;
    }

    /**
     * @param uri        - the Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed
     * @param localName  - the local name (without prefix), or the empty string if Namespace processing is not being performed
     * @param qName      - the qualified name (with prefix), or the empty string if qualified names are not available
     * @param attributes - the attributes attached to the element. If there are no attributes, it shall be an empty Attributes object
     * @throws SAXException - any SAX exception, possibly wrapping another exception
     * @desc receives notification of the start of an element,
     * if the element is an event, a new event is created
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = true;
        currentValue = "";
        if (localName.equals("item"))
            item = new Event();
    }

    /**
     * @param uri       - the Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed
     * @param localName - the local name (without prefix), or the empty string if Namespace processing is not being performed
     * @param qName     - the qualified name (with prefix), or the empty string if qualified names are not available
     * @throws SAXException - any SAX exception, possibly wrapping another exception
     * @desc receives notification of the end of an element
     * applies the parsed XML to the Event as a title, date, time, or info
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        currentElement = false;

        if (item != null) // makes sure an event is created, used to prevent a NullPointerException
        {
            /** set value */
            if (localName.equalsIgnoreCase("title")) //sets title
                item.setTitle(currentValue);
            else if (localName.equalsIgnoreCase("description")) //sets date, time, and info
            {
                String date;
                String time = "";
                String info = "";
                boolean multipleDates = false;
                boolean multipleDatesTimes = false;

                if (currentValue.substring(27, 28).equals("-")) //testing if the event holds multiple dates
                {
                    multipleDates = true;
                    date = currentValue.substring(15, 40);
                } else if (getItemLength(currentValue) > 55) //testing if the event holds multiple times and dates
                {
                    multipleDatesTimes = true;
                    date = currentValue.substring(15, 27) + currentValue.substring(35, 48);
                    time = currentValue.substring(29, 36) + currentValue.substring(49, 62);
                } else
                    date = currentValue.substring(15, 27);

                if (currentValue.length() > 31) //testing if the event has other information besides a title and date
                {
                    int start;
                    if (multipleDates)
                        start = 40;
                    else if (multipleDatesTimes)
                        start = 66;
                    else
                        start = 29;

                    for (int i = start; i < currentValue.length() - 1; i++) {
                        String letter = currentValue.substring(i, i + 1);

                        if (!(letter.equals("<")) && !(letter.equals("&")) && !(letter.equals("\""))) //detects end of time
                            if (i < 49 && !multipleDatesTimes)
                                time += letter;
                            else
                                info += letter;
                        else
                            for (int j = i; !(currentValue.substring(j, j + 1).equals(">")) && !(currentValue.substring(j, j + 1).equals(";")); j++) //if time ends, navigate to where the info starts
                                i++;
                    }
                }

                item.setDate(date);
                item.setTime(time);
                item.setInfo(info);
            } else if (localName.equalsIgnoreCase("item"))// adds the item to an ArrayList
                itemsList.add(item);
        }
    }

    /**
     * @param ch     - the characters
     * @param start  - the start position in the character array
     * @param length - the number of characters to used from the character array
     * @throws SAXException
     * @desc receives notification of character data inside an element,
     * called to get tag characters
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentElement)
            currentValue += new String(ch, start, length);
    }

    /**
     * @param x - given description
     * @return length of the description
     * @desc used to get the length of the event's description
     */
    public int getItemLength(String x) {
        boolean end = false;
        int length = 0;
        for (int i = 0; end == false; i++) {
            String letter = currentValue.substring(i, i + 1);

            if (!(letter.equals("<")) && !(letter.equals("&")) && !(letter.equals("\""))) // denotes the end of the description
                length++;
            else
                end = true;
        }
        return length;
    }
}