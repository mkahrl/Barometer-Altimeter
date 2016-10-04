package marks.kyo.altwidget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NoBaromDialog extends Dialog implements OnClickListener
{
	Context ctx;
	Button ok;

    public NoBaromDialog(Context ctx)
    {
    	super(ctx);
    	setContentView(R.layout.nobaromlayout);
    	setTitle(R.string.no_barom);
    	ok = (Button) findViewById(R.id.quit);
    	ok.setOnClickListener(this);
    }

    public void onClick(View v)
    {
    	dismiss();
    	System.exit(0);
    }

}