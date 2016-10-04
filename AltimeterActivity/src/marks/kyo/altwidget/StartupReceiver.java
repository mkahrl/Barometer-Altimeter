/**
 *
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/5/13
 */
package marks.kyo.altwidget;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver // listens for device boot, starts reminder service.
{
   @Override
   public void onReceive(Context context, Intent intent)
   {
      String action = intent.getAction();
      if (action==null) return;
      
      if ("android.intent.action.BOOT_COMPLETED".equals(action))// start on bootup
      {
         Intent intent1 = new Intent();
         intent1.setClassName(context.getPackageName(), context.getPackageName()+".AltimeterService");
         // Start the service
         context.startService(intent1);
      }
   }
}
