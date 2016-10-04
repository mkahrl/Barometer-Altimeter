package marks.kyo.altwidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.*;
import android.preference.PreferenceManager;
import java.util.*;

public class BarometerManager
{
	private Sensor barometer;
	private Averager averager = new Averager();
	private float currentPressure;
	private double pressureCorrection;
	private SensorCallback sensorCallback;
    private static BarometerManager bmanager;
    private Context ctx;
    private static String ft;
    private static String m;
    private static String ALT_UNITS;
     
    private final static String PRESSURE_CORRECTION = "press_correction";
        
   public static BarometerManager getInstance(Context ctx, SensorCallback sensorCallback)
   {
       if (bmanager==null) bmanager = new BarometerManager(ctx, sensorCallback);
       return bmanager;
   }

    private BarometerManager(Context ctx, SensorCallback sensorCallback)
    {
    	this.sensorCallback=sensorCallback;
        this.ctx=ctx;
        getStrings(ctx);
        SensorManager smanager = null;
        try
        {
            smanager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        }
        catch(Exception e)
        {
            this.sensorCallback.noSensorFound();
            return;
        }
    	
    	if (smanager==null)
    	{
    		this.sensorCallback.noSensorFound();
    		return;
    	}
    	List<Sensor> sensors = smanager.getSensorList(Sensor.TYPE_PRESSURE);
    	if (sensors==null)
    	{
    		this.sensorCallback.noSensorFound();
    		return;
    	}
        barometer = sensors.get(0);
        if ( barometer!=null )
        {
        	smanager.registerListener(new BaromListener(), barometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            this.sensorCallback.noSensorFound();
            return;
        }
        pressureCorrection=(double)PreferenceManager.getDefaultSharedPreferences(ctx).getFloat(PRESSURE_CORRECTION, 0.0f);
    }
    
    class BaromListener implements SensorEventListener
    {

    	public void onAccuracyChanged (Sensor sensor, int accuracy)
    	{

    	}
    	public void onSensorChanged (SensorEvent event)
    	{
    		 Sensor sensor = event.sensor;
    	  	 if (sensor==barometer)
    	  	 {
    	  	 	float[] press = event.values;
    	  	 	averager.addValue(press[0]);
    	  	 	currentPressure=averager.getAverage();
    	  	 	if (sensorCallback!=null)
    	  	 	{
    	  	 		sensorCallback.onPressureChange(currentPressure);
    	  	 		double alt = getAltitude(currentPressure);
    	  	 		sensorCallback.onAltChange(alt);
    	  	 	}
    	  	 }
    	}
    }

    private double getAltitude(double press)
    {
    	press=press-pressureCorrection;
    	double prs = press*100;// convert millibar to PA;
    	double h=0;
    	double res = Math.pow(prs, .190263);
    	h = 44.3308-4.94654*res;// h in km.
    	h = h*3280.8399;// convert to ft.
    	return h;

    }

    public void calibrate(float actualAlt)
    {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String alt_units=sp.getString(ALT_UNITS, ft);
        if (alt_units.equals(m)) actualAlt = actualAlt*3.2808399f;
    	
    	double ap = getPressure(actualAlt);
    	pressureCorrection = currentPressure - ap;
        SharedPreferences.Editor ed = sp.edit();
		ed.putFloat(PRESSURE_CORRECTION, (float)pressureCorrection);
		ed.commit();
    }

    public double getPressure(double alt)
    {
    	alt = alt/3280.8399;
    	double q = (44.3308-alt)/4.94654;
    	double p = Math.pow(q, 5.2559);
    	return p/100;
    }

    public double getPressureCorrection(double actualAlt)
    {
    	double cp = getPressure(actualAlt);
    	return currentPressure - cp;
    }
    
    private void getStrings(Context ctx)
    {
  		ft = ctx.getResources().getString(R.string.feet);
  		m = ctx.getResources().getString(R.string.meter);
  		ALT_UNITS = ctx.getResources().getString(R.string.alt_units);
    }

    class Averager
    {
    	int size = 3;
    	int it=0;
    	float[] vals = new float[size];

    	public Averager(){}

    	public void addValue(float val)
    	{
    		if (it >= size ) it=0;
    		vals[it] = val;
    		it++;
    	}

    	public float getAverage()
    	{
    		float total=0f;
    		int ii=0;
    		for (int i=0; i<size; i++)
    		{
    			if (vals[i]!=0.0)
    			{
    				total+=vals[i];
    				ii++;
    			}
    		}
    		return total/ii;
    	}
    }
}