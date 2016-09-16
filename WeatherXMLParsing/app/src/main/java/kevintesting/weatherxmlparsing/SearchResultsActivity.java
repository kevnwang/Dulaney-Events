package kevintesting.weatherxmlparsing;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.NavUtils;
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
import android.widget.TextView;

import java.util.ArrayList;

//Only Activity that can handle search queries
//all incoming intents are search queries
public class SearchResultsActivity extends ActionBarActivity {

    private ListView listView;
    private TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//sets up navigation back button on actionbar
        listView = (ListView) findViewById(R.id.listView2);//initializes listView
        textView = (TextView) findViewById(R.id.TextView);//initializes textView
        handleIntent(getIntent());
    }

    //handles new intent if activity is already open (user searches in SearchResultsActivity)
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    //handles incoming intent
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction()))//checks if incoming intent is search query
        {
            String query = intent.getStringExtra(SearchManager.QUERY);//grabs search query of user

            showResults(query);//displays results
        }
    }

    //displays results
    private void showResults(String query) {
        dataWrapper data = (dataWrapper) getIntent().getSerializableExtra("events");//accesses dataWrapper bundled to search intent
        ArrayList<Event> eventList = data.getEvents();//accesses list of Events from data
        final ArrayList<Event> newEvents = new ArrayList<Event>();//initializes new ArrayList of Events, to be filled with search results
        for (int i = 0; i < eventList.size() - 1; i++)//goes through entire list of Events
        {
            if (eventList.get(i).hasString(query))//checks if individual Event contains the search query
            {
                //if yes, Event is added to newEvents
                newEvents.add(eventList.get(i));
            }
        }
        ArrayList<String> textFields = getNameDate(newEvents);//initializes new ArrayList of titles and dates of all Events in newEvents
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, textFields);//initializes ArrayAdapter, uses textFields as data for output
        listView.setAdapter(adapter);//sets adapter to listView
        String bannerText = "";//initializes String to output to textView
        if (textFields.size() == 0)//if no results
        {
            bannerText = "No results for \"" + query + "\"";
        } else//if there are 1+ results
        {
            bannerText = "Showing results for \"" + query + "\"";
        }
        textView.setText(bannerText + "                                       " + bannerText);//sets text output to textView
        textView.setSelected(true);//sets textView attributes as true
        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Event temp = newEvents.get(position);
                Intent i = new Intent(SearchResultsActivity.this, EventPage.class);

                i.putExtra("event", temp);
                startActivity(i);
            }

        });
    }

    //retrieves title and date for each Event in an arrayList
    public ArrayList<String> getNameDate(ArrayList<Event> arr) {
        ArrayList<String> display = new ArrayList<>();//ArrayList of Strings to return
        //goes through each Event of arr, adds title and date to a temporary String
        for (int i = 0; i < arr.size(); i++) {
            String text = "";
            Event obj = arr.get(i);
            text += obj.getTitle() + "\n";
            text += obj.getDate() + "\n";
            display.add(text);//adds temporary String to Arraylist<String> that is returned
        }
        return display;
    }

    //inflates option menu and action bar buttons
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

    //defines what happens when action bar buttons are clicked.
    public boolean onOptionsItemSelected(MenuItem item) {
        //handles multiple actionbar buttons: back button vs search bar
        switch (item.getItemId()) {
            case android.R.id.home://back button
                NavUtils.navigateUpFromSameTask(this);//returns to parent activity (mainActivity)
                break;
            case R.id.search://search bar
                onSearchRequested();//begins new search query
                break;

        }
        return true;
    }
}
