package marks.kyo.altwidget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpDialog extends Dialog implements OnClickListener
{
	Context ctx;
	Button ok;

    public HelpDialog(Context ctx)
    {
    	super(ctx);
    	setContentView(R.layout.helplayout);
    	setTitle(R.string.help);
    	ok = (Button) findViewById(R.id.quit);
    	ok.setOnClickListener(this);
    }

    public void onClick(View v)
    {
    	dismiss();
    }

}