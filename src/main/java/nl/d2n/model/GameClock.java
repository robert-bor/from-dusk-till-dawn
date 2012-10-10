package nl.d2n.model;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class GameClock {

    long MS_IN_HOUR = 1000 * 60 * 60;

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String JS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private String dateTime;
    private Date dateClocked;

    private boolean initialized = false;

    public GameClock() {}
    public GameClock(String dateTime) {
        setDateTime(dateTime);
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
        this.dateClocked = new Date();
        initialized = true;
    }
    public String getJavaScriptDate() {
        return getStringFromDate(getDateTime(), JS_DATE_FORMAT);
    }
    public String getDateTimeAsString() {
        return getStringFromDate(getDateTime());
    }
    public Date getHoursAgo(int hoursAgo) {
        Date currentDate = getDateTime();
        return new Date(currentDate.getTime() - hoursAgo * MS_IN_HOUR);
    }
    public Date getDateTime() {
        Date date = new Date();
        long millisecondsToAdd = date.getTime() - dateClocked.getTime();
        // 2011-08-11 21:54:05
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date formattedDate = getDateFromString(this.dateTime);
        return new Date(formattedDate.getTime() + millisecondsToAdd);
    }
    public static String getStringFromDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
    public static String getStringFromDate(Date date) {
        return getStringFromDate(date, DATE_FORMAT);
    }
    public static Date getDateFromString(String dateText) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        try {
            return formatter.parse(dateText);
        } catch (ParseException err) {
            return null;
        }
    }
    public boolean isInitialized() { return this.initialized; }
}
