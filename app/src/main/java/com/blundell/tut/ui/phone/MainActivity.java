package com.blundell.tut.ui.phone;

import java.time.LocalDateTime;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.blundell.tut.R;
import com.blundell.tut.service.ScheduleClient;

/**
 * This is the Main Activity of our app.
 * Here we allow the user to select a date,
 * we then set a notification for that date to appear in the status bar
 *  
 * @author paul.blundell
 */
public class MainActivity extends Activity  {
	// This is a handle so that we can call methods on our service
    private ScheduleClient scheduleClient;
    // This is the date picker used to select the date for our notification
	private DatePicker picker;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        // Get a reference to our date picker
        picker = (DatePicker) findViewById(R.id.scheduleTimePicker);
    }
	
    /**
     * This is the onClick called from the XML to set a new notification 
     */
    public void onDateSelectedButtonClick(View v){
    	// Get the date from our datepicker
    	int day = picker.getDayOfMonth();
    	int month = picker.getMonth();
    	int year = picker.getYear();
    	// Create a new calendar set to the date chosen
    	// we set the time to midnight (i.e. the first minute of that day)
    	Calendar c = Calendar.getInstance();
    	c.set(year, month, day);

        LocalDateTime now = LocalDateTime.now();

        c.set(Calendar.HOUR_OF_DAY, now.getHour());
    	c.set(Calendar.MINUTE, now.getMinute() + 1);
    	c.set(Calendar.SECOND, now.getSecond());
    	// Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
    	scheduleClient.setAlarmForNotification(c);
    	// Notify the user what they just did
    	Toast.makeText(this, "Notification set for: "+ day +"/"+ (month+1) +"/"+ year, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onStop() {
    	// When our activity is stopped ensure we also stop the connection to the service
    	// this stops us leaking our activity into the system *bad*
    	if(scheduleClient != null)
    		scheduleClient.doUnbindService();
    	super.onStop();
    }
}
