package marks.kyo.altwidget;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.view.View.OnClickListener;

public class PrefsActivity extends Activity implements OnClickListener
{
	private static String ALT_UNITS;
	private static String PRESS_UNITS;
	Button save;
	Button cancel;
	Spinner altu;
	Spinner presu;
	private static String ft;
	private static String mb;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getStrings();
        setContentView(R.layout.preferences);
        ArrayAdapter<CharSequence> au = ArrayAdapter.createFromResource(this, R.array.alt_units, R.layout.listitem);
        au.setDropDownViewResource(R.layout.listitem);
        altu = (Spinner)findViewById(R.id.alt_unit);
        altu.setAdapter(au);
        
        String punt = PreferenceManager.getDefaultSharedPreferences(this).getString(PRESS_UNITS, mb);
        ArrayAdapter<CharSequence> pu = ArrayAdapter.createFromResource(this, R.array.press_units, R.layout.listitem);
        pu.setDropDownViewResource(R.layout.listitem);
        presu = (Spinner)findViewById(R.id.pressure_unit);
        presu.setAdapter(pu);
        int i = pu.getPosition(punt);
        presu.setSelection(i);
        
        String unt = PreferenceManager.getDefaultSharedPreferences(this).getString(ALT_UNITS, ft);
        if (unt.equals(ft)) altu.setSelection(0);
        else altu.setSelection(1);
        save = (Button) findViewById(R.id.save_button);
        cancel = (Button) findViewById(R.id.cancel_button);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
    
    public void setAltUnits(String s)
    {
    	 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		 SharedPreferences.Editor ed = sp.edit();
		 ed.putString(ALT_UNITS, s);
		 ed.commit();
    }
    
    public void setPressUnits(String s)
    {
    	 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		 SharedPreferences.Editor ed = sp.edit();
		 ed.putString(PRESS_UNITS, s);
		 ed.commit();
    }
    
    public void onClick(View v)
    {
    	if ( v==save)
    	{
    		setAltUnits(altu.getSelectedItem().toString());
    		setPressUnits(presu.getSelectedItem().toString());
    		finish();
    	}
    	if ( v==cancel)finish();
    }
    
    private void getStrings()
    {
  		ft = getResources().getString(R.string.feet);
  		mb = getResources().getString(R.string.millibar);
  		ALT_UNITS = getResources().getString(R.string.alt_units);
  		PRESS_UNITS = getResources().getString(R.string.press_units);
    }
}