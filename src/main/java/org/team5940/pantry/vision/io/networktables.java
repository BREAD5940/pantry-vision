package org.team5940.pantry.vision.io;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;

public class networktables {

    NetworkTable table;
    NetworkTableEntry entry;

    public networktables(String tableID) {
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