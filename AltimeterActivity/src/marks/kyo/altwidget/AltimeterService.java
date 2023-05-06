package marks.kyo.altwidget;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AltimeterService extends Service implements SensorCallback
{
	private BarometerManager bManager;
	private final static String CURRENT_ALT = "current_alt";
	private final static String CURRENT_PRESS = "current_press";
	public void onCreate()
	{
		bManager = BarometerManager.getInstance(this.getApplicationContext(), this);
		
	}
	
	public void onDestroy()
	{
	}
	
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	public void onPressureChange(double value)
        {
    		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor ed = sp.edit();
		ed.putFloat(CURRENT_PRESS, (float)value);
		ed.commit();
         }

    	public void onAltChange(double value)
    	{
    		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor ed = sp.edit();
		ed.putFloat(CURRENT_ALT, (float)value);
		ed.commit();
   	 }
    
    	public void noSensorFound()
   	{
    		new NoBaromDialog(this).show();
    	}
}
