package marks.kyo.altwidget;

import android.content.Context;

public class UnitConvertor 
{
	public static String defaultElevationUnit;
	public static String defaultPressureUnit;
	private static String m;
	private static String ft;
	private static String mb;
	private static String kpa;
	private static String inhg;
	private static String mmhg;
	private static String psi;
	
    public UnitConvertor(Context ctx) 
    {
    	getStrings(ctx);
    	defaultElevationUnit=ft;
    	defaultPressureUnit=mb;
    }
    
    public static float convertAltitude(float alt, String unit)
    {
    	if (unit.equals(defaultElevationUnit)) return alt;
    	if (unit.equals(m)) return alt/3.2808399f;
    	return alt;
    }
    
    public static float convertPressure(float press, String unit)
    {
    	if (unit.equals(defaultPressureUnit)) return press;
    	if (unit.equals(kpa))   return press/10.0f;
    	if (unit.equals(inhg))   return 0.029533f*press;
    	if (unit.equals(psi))   return 0.0145037738f*press;
    	if (unit.equals(mmhg)) return 0.750061683f*press;
    	return press;
    }
    
    private void getStrings(Context ctx)
    {
  	ft = ctx.getResources().getString(R.string.feet);
  	mb = ctx.getResources().getString(R.string.millibar);
  	m = ctx.getResources().getString(R.string.meter);
  	kpa = ctx.getResources().getString(R.string.kpa);
  	inhg = ctx.getResources().getString(R.string.inhg);
  	mmhg = ctx.getResources().getString(R.string.mmhg);
  	psi = ctx.getResources().getString(R.string.psi);
    }
}
