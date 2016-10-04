package marks.kyo.altwidget;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;
import android.view.*;
import android.view.View.OnClickListener;

public class AltimeterActivity extends Activity implements SensorCallback, OnClickListener
{
	TextView pressure;
	TextView alt;
	TextView altLabel;
	TextView pressLabel;
	//MenuItem calibrate;
	//MenuItem prefs;
    MenuItem help;
	private final static String TENS_FORMAT = "%.1f";
	private final static String ZERO_FORMAT = "%.0f";
	private static String ALT_UNITS;
	private static String PRESS_UNITS;
	private static String CURRENT_ALT;
	private static String CURRENT_PRESS;
	private static String alt_lbl;
	private static String prs_lbl;
	private static String ft;
	private static String mb;
	private Handler handler;
	private long update_interval=2600;
	private static UnitConvertor convertor;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pressure = (TextView) findViewById(R.id.pressure);
        alt = (TextView) findViewById(R.id.altitude);
        altLabel = (TextView) findViewById(R.id.altitude_label);
        pressLabel = (TextView) findViewById(R.id.pressure_label);
        convertor = new UnitConvertor(this);
  		getStrings();
        startService();
        handler = new Handler(getMainLooper());
        handler.postDelayed(new Update(), update_interval);
        findViewById(R.id.preferences).setOnClickListener(this);
        findViewById(R.id.calibrate).setOnClickListener(this);
        setValues();
    }
    
    private void getStrings()
    {
    	alt_lbl=getResources().getString(R.string.alt_label);
    	prs_lbl=getResources().getString(R.string.pressure_label);
  		ft = getResources().getString(R.string.feet);
  		mb = getResources().getString(R.string.millibar);
  		ALT_UNITS = getResources().getString(R.string.alt_units);
  		PRESS_UNITS = getResources().getString(R.string.press_units);
  		CURRENT_ALT = getResources().getString(R.string.current_alt);
  		CURRENT_PRESS = getResources().getString(R.string.current_press);
    }
    
    private void startService()
    {
    	Intent intent1 = new Intent();
        intent1.setClassName(getPackageName(), "marks.kyo.altwidget.AltimeterService");
         // Start the service
        startService(intent1);
    }
    
    class Update extends Thread
    {
    	@Override
    	public void run()
    	{
    		setValues();
    		handler.postDelayed(new Update(), update_interval);
    	}
    }
    
    private void setValues()
    {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    	String alt_units=sp.getString(ALT_UNITS, ft);
    	String p_units=sp.getString(PRESS_UNITS, mb);
    
		float press = sp.getFloat(CURRENT_PRESS, 0.0f);
		press = convertor.convertPressure(press, p_units);
		String s = String.format(TENS_FORMAT, press);
		pressure.setText(s);
		
		String pp=prs_lbl+" ("+p_units+")";
        pressLabel.setText(pp); 
		
		float lt = sp.getFloat(CURRENT_ALT, 0.0f);
		lt = convertor.convertAltitude(lt, alt_units);
		String ss = String.format(ZERO_FORMAT, lt);
		alt.setText(ss);
		
		String aa=alt_lbl+" ("+alt_units+")";
        altLabel.setText(aa);
    }
    
   

    public void onPressureChange(double value)
    {
    }

    public void onAltChange(double value)
    {
    }
    
    public void noSensorFound()
    {
    	new NoBaromDialog(this).show();
    }
    
    public void onClick(View v)
    {
        int id = v.getId();
        if (id == R.id.preferences)
        {
            Intent intent = new Intent(this, PrefsActivity.class);
		    startActivity(intent);
        }
        
        if (id == R.id.calibrate) new ActualAltDialog(this).show();
    }

   // removed menus for now in favor of buttons on main screen.
     @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	super.onCreateOptionsMenu(menu);
	    help = menu.add(R.string.help);
    	return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
    	if (item==help) new HelpDialog(this).show();
    	return true;
    }

    public void calibrate(float actualAlt)
    {
        
		String ss = String.format(ZERO_FORMAT, actualAlt);
        alt.setText(ss);
       
        BarometerManager.getInstance(this.getApplicationContext(), this).calibrate(actualAlt);
        
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String alt_units=sp.getString(ALT_UNITS, ft);
        if (alt_units.equals("m")) actualAlt = actualAlt*3.2808399f;
        
        SharedPreferences.Editor ed = sp.edit();
        ed.putFloat(CURRENT_ALT, actualAlt);
        ed.commit();
        
    }
}
