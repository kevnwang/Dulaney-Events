package kevintesting.weatherxmlparsing;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * @author Kevin Wang kwang21093@gmail.com
 * @desc this class is the activity that represents a particular event
 * when the user opens an event, this activity is started and displays all of its information and utilities
 */
public class EventPage extends ActionBarActivity {
    private String extracted;
    private EditText custom;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//sets up navigation back button on actionbar

        TextView info = (TextView) findViewById(R.id.info);
        Button share = (Button) findViewById(R.id.sharing);
        Button calendar = (Button) findViewById(R.id.calendar);
        custom = (EditText) findViewById((R.id.customMessage));
        custom.setText("Check out this DHS event!");

        event = (Event) getIntent().getSerializableExtra("event"); //retrieving the serializable event from MainActivity
        event.checkInfo(); //preparing the data for extraction
        extracted = event.getAll();

        /**
         * @desc social media feature for each event
         * allows user to share the event via social media using Android's default ACTION_SEND intent
         */
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = custom.getText().toString() + "\n" + "\n" + extracted;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                startActivity(sharingIntent);
            }
        });

        /**
         * @desc google calendar feature for each event
         * allows user to place the event on his/her google calendar using Android's Calendar API
         */
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(event.getStartYear(), event.getStartMonth() - 1, event.getStartDay(), event.getStartHour(), event.getStartMin());
                Calendar endTime = Calendar.getInstance();
                endTime.set(event.getEndYear(), event.getEndMonth() - 1, event.getEndDay(), event.getEndHour(), event.getEndMin());
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, event.getTitle())
                        .putExtra(CalendarContract.Events.DESCRIPTION, "ALL TIMES IN EST")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, event.getInfo())
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

                startActivity(intent);
            }
        });

        info.setText(extracted);
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    //defines what happens when back button (on ActionBar) is pressed
    public boolean onOptionsItemSelected(MenuItem item) {

        this.finish();//ends activity


        return true;
    }
}
