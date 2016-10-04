/**
 * @(#)ActualAltDialog.java
 *
 *
 * @author
 * @version 1.00 2012/1/5
 */
package marks.kyo.altwidget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ActualAltDialog extends Dialog implements OnClickListener
{
	AltimeterActivity parent;
	Button ok;
	Button cancel;
	EditText alt;
	
    public ActualAltDialog(AltimeterActivity parent)
    {
    	super(parent);
    	this.parent=parent;
    	setContentView(R.layout.actualaltlayout);
    	setTitle(R.string.calibrate);
    	ok = (Button) findViewById(R.id.ok_button);
    	ok.setOnClickListener(this);
    	cancel = (Button) findViewById(R.id.canc_button);
    	cancel.setOnClickListener(this);
    	alt = (EditText) findViewById(R.id.actual_alt);
    	String calt = parent.alt.getText().toString();
    	alt.setText(calt);
    }

    public void onClick(View v)
    {
    	if ( v==ok)
    	{
    		try
    		{
    			float dalt = new Float(alt.getText().toString()).floatValue();
    			parent.calibrate(dalt);
    		}
    		catch(Exception e){System.out.println(e.toString());}
    	}
		dismiss();
    }
}