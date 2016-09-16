package kevintesting.weatherxmlparsing;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Yiyi on 2/18/2015.
 * This class is a wrapper for an ArrayList of Events
 */
public class dataWrapper implements Serializable {

    private ArrayList<Event> events;

    /**
     * @param event = ArrayList of Events
     */
    public dataWrapper(ArrayList<Event> event) {
        this.events = event;
    }

    /**
     * @return the ArrayList of Events the DataWrapper object contains
     */
    public ArrayList<Event> getEvents() {
        return this.events;
    }
}
