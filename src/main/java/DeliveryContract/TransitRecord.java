package DeliveryContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to add a new entry to the list of all previous handovers of this package.
 */
public class TransitRecord implements IDeliveryContract, Serializable {

    private TransitEntry transitEntry;
    private List<TransitEntry> entryList = null;
    private boolean isCreated = false;
    private int serialNumber = 0;

    /**
     * The TransitRecord object is a dynamic vector where TransitEntry objects are added.
     * The advantage of the Java Vector object is its threadsaty.
     */
    public TransitRecord() {
        this.entryList = Collections.synchronizedList(new ArrayList<TransitEntry>());
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

//    @Override
//    public boolean getIsCreated() {
//        return this.isCreated;
//    }
//
//    @Override
//    public void setIsCreated(boolean isCreated) {
//        this.isCreated = isCreated;
//    }

    /**
     * Adds a new entry to the transit record list.
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
     * The size of the current transit record list.
     *
     * @return    integer which represents the entries in the list.
     */
    public int getTransitRecordSize() {
        return this.entryList.size();
    }

    public String getString() {
        return this.entryList.toString().replace("[","").replace("]","").replace(", ", "");
    }
}
