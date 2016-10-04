/**
 * @(#)SensorCallback.java
 *
 *
 * @author
 * @version 1.00 2012/1/3
 */
package marks.kyo.altwidget;

public interface SensorCallback {
	
    public void onPressureChange(double value);
    public void onAltChange(double value);
    public void noSensorFound();
}