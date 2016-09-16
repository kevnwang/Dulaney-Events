package kevintesting.weatherxmlparsing;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author Kevin Wang kwang21093@gmail.com
 * @desc this class is the starting default activity that parses all of the XML data from the school website's calendar RSS feed to Events and displays them in a ListView
 * a user can search for a particular event, using the ActionBar
 */
public class MainActivity extends ActionBarActivity {
    private AsyncTask<String, Void, ArrayList<Event>> retrieveTask;
    private ListView calendar;
    private ArrayList<String> display;
    private ArrayAdapter<String> adapter;
    private ArrayList<Event> extractList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        display = new ArrayList<String>();

        retrieveTask = new AsyncTask<String, Void, ArrayList<Event>>() {
            boolean success = true;

            /**
             * @desc background thread used to parse the XML from a URL
             * @param urls - the URL containing the XML to be parsed
             * @return ArrayList of events to be passed to execute(Params...)
             */
            @Override
            protected ArrayList<Event> doInBackground(String... urls) {
                try {
                    URL url = new URL(urls[0]);

                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    HandlingXML inProgress = new HandlingXML();
                    xr.setContentHandler(inProgress);
                    InputSource is = new InputSource(url.openStream());
                    xr.parse(is);

                    ArrayList<Event> itemsList = inProgress.getItemsList();
                    return itemsList;
                } catch (Exception e) {
                    success = false;
                    e.printStackTrace();
                }
                return null;
            }

            /**
             * @desc runs on the thread after doInBackground(Params...)
             * @param events - the ArrayList of events passed by doInBackground(Params...)
             */
            @Override
            protected void onPostExecute(ArrayList<Event> events) {
                if (success)
                    display(events);
            }
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        calendar = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, display);
        retrieveTask.execute("http://dulaneyhs.bcps.org/syndication/rss.aspx?serverid=37" +
                "02047&userid=5&feed=portalcalendarevents&key=705MSIBJB%2bGrvG%2bPIWVUqU" +
                "hMJxpLxaRrBolVShl0Y8i92T9fPXdREAdkWfz1sJHqyF5giXc5yyhAAYj2CVxX1A%3d%3d&" +
                "portal_id=3702131&page_id=3702152&calendar_context_id=3702295&portlet_i" +
                "nstance_id=186641&calendar_id=3702296&v=2.0");
    }

    /**
     * @param arr - ArrayList of events passed from the parsed XML data
     * @desc mutator method that displays the ListView and applies onClick to each item(event) in the ListView
     */
    public void display(ArrayList<Event> arr) {
        extractList = arr;
        setNameDate(arr);
        calendar.setAdapter(adapter);

        calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * @desc callback method to be invoked when an item in the AdapterView has been clicked
             * uses the event at the given index in the ArrayList to pass it to the EventPage activity, and then starts the activity
             * @param parent - the AdapterView where the click happened
             * @param view - the view within the AdapterView that was clicked (this will be a view provided by the adapter)
             * @param position - the position of the view in the adapter
             * @param id - the row id of the item that was clicked
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Event temp = extractList.get(position);
                Intent i = new Intent(MainActivity.this, EventPage.class);

                i.putExtra("event", temp);
                startActivity(i);
            }

        });
    }

    /**
     * @param arr - ArrayList of events passed from parsed XML data
     * @desc mutator method to retrieve each event's title and date and add it to an ArrayList (to be used in the AdapterView)
     */
    public void setNameDate(ArrayList<Event> arr) {
        for (int i = 0; i < arr.size(); i++) {
            String text = "";
            Event obj = arr.get(i);
            text += obj.getTitle() + "\n";
            text += obj.getDate() + "\n";
            display.add(text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);//initializes new SearchManager
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        ;//creates new SearchView from ActionVew of search box
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));//sets SearchableInfo for searchView
        searchView.setIconifiedByDefault(false);//makes searchView always visible

        return true;
    }

    //overrides startActivity to add Event to a search intent
    public void startActivity(Intent intent) {
        // check if given intent is search intent
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra("events", new dataWrapper(extractList));//add dataWrapper containing ArrayList of Events
        }

        super.startActivity(intent);
    }

    //defines what happens on search bar press
    public boolean onOptionsItemSelected(MenuItem item) {
        onSearchRequested();//start search query

        return true;
    }
}


