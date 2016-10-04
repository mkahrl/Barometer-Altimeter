package marks.kyo.altwidget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class AltlWidgetProvider extends AppWidgetProvider
{
		private static String CURRENT_ALT;
		private static String CURRENT_PRESS;
		private static String PRESS_UNITS;
		private static String ALT_UNITS;
		private final static String TENS_FORMAT = "%.1f";
		private final static String ZERO_FORMAT = "%.0f";
		private static String ft;
		private static String mb;
		private String p_lbl;
		String alt_lbl;
        float press=1.0f;
        float alt=1.0f;
       
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
            getStrings(context);
            UnitConvertor convertor = new UnitConvertor(context);
            String alt_units=PreferenceManager.getDefaultSharedPreferences(context).getString(ALT_UNITS, ft);
            String p_units=PreferenceManager.getDefaultSharedPreferences(context).getString(PRESS_UNITS, mb);
         	alt = PreferenceManager.getDefaultSharedPreferences(context).getFloat(CURRENT_ALT, 0.0f);
         	alt = convertor.convertAltitude(alt, alt_units);
         	
         	press = PreferenceManager.getDefaultSharedPreferences(context).getFloat(CURRENT_PRESS, 0.0f);
         	press = convertor.convertPressure(press, p_units);
         	
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.altwidget);
            String p = String.format(TENS_FORMAT, press);
            String a = String.format(ZERO_FORMAT, alt);
        
        	views.setTextViewText(R.id.alt_label, alt_units);
        	views.setTextViewText(R.id.press_label, p_units);
        	
            views.setTextViewText(R.id.alt, a);
            views.setTextViewText(R.id.press, p);
                   
            Intent intent = new Intent(context, AltimeterActivity.class);            
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0); 
            views.setOnClickPendingIntent(R.id.alt_widget_layout, pendingIntent);
          
            for (int i=0;i<appWidgetIds.length; i++)
            {
                appWidgetManager.updateAppWidget(appWidgetIds[i], views);
            }
			super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	private void getStrings(Context ctx)
    {
    	alt_lbl=ctx.getResources().getString(R.string.alt_label);
    	p_lbl=ctx.getResources().getString(R.string.pressure_label);
  		ft = ctx.getResources().getString(R.string.feet);
  		mb = ctx.getResources().getString(R.string.millibar);
  		ALT_UNITS = ctx.getResources().getString(R.string.alt_units);
  		PRESS_UNITS = ctx.getResources().getString(R.string.press_units);
  		CURRENT_ALT = ctx.getResources().getString(R.string.current_alt);
  		CURRENT_PRESS = ctx.getResources().getString(R.string.current_press);
    }
}