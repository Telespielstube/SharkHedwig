package DeliveryContract;

import Misc.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to keep a record of all previous successfully completed physical package handovers.
 */
public class TransitRecord implements Contractable {

    private final List<TransitEntry> entryList;
    private boolean isCreated = false;
    private int serialNumber = 1;

    /**
     * The TransitRecord object is a dynamic vector where TransitEntry objects are added.
     * The advantage of the Java Vector object is its threadsaty.
     */

    public TransitRecord() {
        this.entryList = new ArrayList<>();
        this.isCreated = true;
    }

    /**
     * Adds entries to a newly instantiated TransitRecord object.
     * Only called by transferees.
     *
     * @param entries    All previous handover entries of the package.
     */
    public TransitRecord(List<TransitEntry> entries) {
        this.entryList = entries;
        this.isCreated = true;
    }

    /**
     * Just adds 1 tho the serialNumber.
     *
     * @return    serial number plus 1.
     */
    public int countUp() {
        return this.serialNumber++;
    }

    @Override
    public TransitRecord get() {
        return this;
    }

    public boolean getIsCreated() {
        return this.isCreated;
    }

    public void setIsCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    /**
     * Adds a new entry to the end of the transit record list.
     * @param entry    TransitEntry object
     */
    public void addEntry(TransitEntry entry) {
        this.entryList.add(entry);
    }

    /**
     * Fetches the last entry in the list.
     *
     * @return  Last added TransitEntry object in list.
     */
    public TransitEntry getLastElement() {
        return this.entryList.get(this.entryList.size() -1);
    }
    /**
     * Returns all entries.
     *
     * @return All entries in list
     */
    public List<TransitEntry> getAllEntries() {
        return this.entryList;
    }

    /**
     * Returns the size of the list.
     *
     * @return    Size of the list.
     */
    public int getListSize() {
        return this.entryList.size();
    }

    /**
     * Formats the list entries to a nice readable String format.
     *
     * @return    Formatted list entries.
     */
    @Override
    public String toString() {
        return this.entryList.toString().replace("[","").replace("]","").replace(", ", "");
    }

}
