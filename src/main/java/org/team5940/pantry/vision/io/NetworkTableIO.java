package org.team5940.pantry.vision.io;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;

public class NetworkTableIO {

    NetworkTable table;
    NetworkTableEntry entry;
    double[] data;

    public NetworkTableIO(String tableID) {
        this.table = NetworkTableInstance.getDefault().getTable(tableID);
    }

    /** 
     * Return the raw value of a network table entry. You'll likely
     * want to convert this to something useful by using getDouble
     * or something instead
     * @param entryID string
     * @return NetworkTableEntry
     */
    public NetworkTableEntry getEntry(String entryID){
        return table.getEntry(entryID);
    }

    /**
     * Return all known NT data fed by the limelight/pi 
     * as a double[]
     * @return data as a double[]
     */
    public double[] getEverything() {
        NetworkTableEntry tv = table.getEntry("tv"); // Whether the limelight has any valid targets (0 or 1)
        NetworkTableEntry tx = table.getEntry("tx"); // tx	Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
        NetworkTableEntry ty = table.getEntry("ty"); // ty	Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
        NetworkTableEntry ta = table.getEntry("ta"); // ta	Target Area (0% of image to 100% of image)
        NetworkTableEntry ts = table.getEntry("ts"); // ts	Skew or rotation (-90 degrees to 0 degrees)
        NetworkTableEntry tl = table.getEntry("tl"); // tl	The pipeline’s latency contribution (ms) Add at least 11ms for image capture latency.
        data[0] = tv.getDouble(0);
        data[1] = tx.getDouble(0);
        data[2] = ty.getDouble(0);
        data[3] = ta.getDouble(0);
        data[4] = ts.getDouble(0);
        data[5] = tl.getDouble(0);
        return data;
      }

    /** 
     * Return a double from the network table instance.
     * Defaults to 0 if there is no value
     * @param entryID string
     * @return value as a double
     */
    public double getDouble(String entryID) {
        return getEntry(entryID).getDouble(0);
    }

    // TODO extend to strings, etc?

}