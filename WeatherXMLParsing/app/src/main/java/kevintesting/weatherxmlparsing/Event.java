package kevintesting.weatherxmlparsing;

import java.io.Serializable;

/**
 * @author Kevin Wang kwang21093@gmail.com
 * @desc this class holds the private instance fields, constructors,
 * and methods for each event that is parsed from the XML
 */
public class Event implements Serializable {
    private String title;
    private String date;
    private String time;
    private String info;
    private boolean multipleDates;
    private boolean timePresent;

    /**
     * creates an empty event with a default format:
     * one date and the time is present
     */
    public Event() {
        title = "";
        date = "";
        time = "";
        info = "";
        multipleDates = false;
        timePresent = true;
    }

    public void checkInfo() // called to check whether there are multiple dates given or time is absent
    {
        if (date.length() > 15)
            multipleDates = true;

        if (time.equals(""))
            timePresent = false;
    }

    /**
     * below are the accessor and mutator methods for each private instance field that each event will hold:
     * title, date, time, info
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * below are the methods used to grab the start and end time for the calendar
     *
     * @return month, year, hour, day, min as integers
     */
    public int getStartMonth() {
        int num;
        String month = date.substring(0, 3);
        if (month.equals("Jan"))
            num = 1;
        else if (month.equals("Feb"))
            num = 2;
        else if (month.equals("Mar"))
            num = 3;
        else if (month.equals("Apr"))
            num = 4;
        else if (month.equals("May"))
            num = 5;
        else if (month.equals("Jun"))
            num = 6;
        else if (month.equals("Jul"))
            num = 7;
        else if (month.equals("Aug"))
            num = 8;
        else if (month.equals("Sep"))
            num = 9;
        else if (month.equals("Oct"))
            num = 10;
        else if (month.equals("Nov"))
            num = 11;
        else if (month.equals("Dec"))
            num = 12;
        else
            num = 0;
        return num;
    }

    public int getEndMonth() {
        int num;
        if (multipleDates) // if there are multiple dates, the second substring containing the month needs to be pulled
        {
            String month = date.substring(13, 16);
            if (month.equals("Jan"))
                num = 1;
            else if (month.equals("Feb"))
                num = 2;
            else if (month.equals("Mar"))
                num = 3;
            else if (month.equals("Apr"))
                num = 4;
            else if (month.equals("May"))
                num = 5;
            else if (month.equals("Jun"))
                num = 6;
            else if (month.equals("Jul"))
                num = 7;
            else if (month.equals("Aug"))
                num = 8;
            else if (month.equals("Sep"))
                num = 9;
            else if (month.equals("Oct"))
                num = 10;
            else if (month.equals("Nov"))
                num = 11;
            else if (month.equals("Dec"))
                num = 12;
            else
                num = 0;
        } else
            num = getStartMonth();
        return num;
    }

    public int getStartDay() {
        String temp = date.substring(4, 6);
        return Integer.parseInt(temp);
    }

    public int getEndDay() {
        int day;
        if (multipleDates)// if there are multiple dates, the second substring containing the day needs to be pulled
            day = Integer.parseInt(date.substring(17, 19));
        else
            day = getStartDay();
        return day;
    }

    public int getStartYear() {
        String temp = date.substring(8, 12);
        return Integer.parseInt(temp);
    }

    public int getEndYear() {
        int num;
        if (multipleDates)// if there are multiple dates, the second substring containing the year needs to be pulled
            num = Integer.parseInt(date.substring(21, 25));
        else
            num = getStartYear();
        return num;
    }

    public int getStartHour() {
        int hour;
        if (!timePresent)// the default start hour is midnight
            hour = 0;
        else {
            hour = Integer.parseInt(time.substring(0, 1));
            if (time.substring(4, 6).equals("PM"))// since military time is used for the calendar, 12 hours needs to be added if the time is in the PM
                hour += 12;
        }
        return hour;
    }

    public int getEndHour() {
        int hour;
        if (!timePresent)// the default end hour is 11:00 PM
            hour = 23;
        else {
            hour = Integer.parseInt(time.substring(8, 9));
            if (time.substring(12, 14).equals("PM"))// since military time is used for the calendar, 12 hours needs to be added if the time is in the PM
                hour += 12;
        }
        return hour;
    }

    public int getStartMin() {
        String temp;
        if (!timePresent)// the default start minute is 0
            temp = "0";
        else
            temp = time.substring(2, 4);
        return Integer.parseInt(temp);
    }

    public int getEndMin() {
        String temp;
        if (!timePresent)// the default end minute is 59
            temp = "59";
        else
            temp = time.substring(10, 12);
        return Integer.parseInt(temp);
    }

    public String getAll()// return all information as one string, but split up into multiple lines
    {
        String text = "";
        text += title + "\n" + "\n";
        text += "Date: " + date + "\n" + "\n";
        if (timePresent)
            text += "Time: " + time + "\n" + "\n";
        text += info + "\n" + "\n" + "Contact: (410) 887-7633";
        return text;
    }

    /**
     * @param search - the user input query
     * @return bool true or false - whether the user's attempted search finds an event or not
     * @desc method used to locate string similarities between an event's title and a user-inputed query
     */
    public boolean hasString(String search) {
        for (int i = 0; i <= title.length() - search.length(); i++) {
            if (title.substring(i, i + search.length()).equalsIgnoreCase(search)) {
                return true;
            }
        }
        return false;
    }
}