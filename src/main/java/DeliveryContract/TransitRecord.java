package DeliveryContract;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to add a new entry to the list of all previous handovers of this package.
 */
public class TransitRecord implements IContractComponent, Serializable {

    private TransitEntry transitEntry;
    private List<TransitEntry> entryList = null;

    /**
     * The TransitRecord object is a dynamic vector where TransitEntry objects are added.
     * The advantage of the Java Vector object is its threadsaty.
     */
    public TransitRecord() {
        this.entryList = Collections.synchronizedList(new ArrayList<TransitEntry>());
    }

    /**
     * Contructor expects a TransitEntry object.
     *
     * @param transitEntry    TransitEntry object.
     */
    public TransitRecord(TransitEntry transitEntry) {
        this.transitEntry = transitEntry;
    }

    /**
     * Adds Vector entries to a newly instantiated TransitRecord object.
     * Only called by transferees.
     *
     * @param entries    All previous handover entries of the package.
     */
    public TransitRecord(List<TransitEntry> entries) {
        this.entryList = entries;
    }

    public Object create(Object object) {
        TransitEntry entry = (TransitEntry) object;
        return new TransitRecord(entry);
    }

    public TransitRecord get() {
        return this;
    }

    @Override
    public boolean isCreated() {
        return false;
    }

    /**
     * Adds a new entry to the transit record list.
     * @param entry    TransitEntry object
     */
    public void addEntry(TransitEntry entry) {
        this.entryList.add(entry);
    }

    /**
     *
     * @return
     */
    public TransitEntry getLastElement() {
        return this.entryList.get(this.entryList.size() -1);
    }
    /**
     * Returns all entries.
     *
     * @return All entries fo the Vector.
     */
    public List<TransitEntry> getAllEntries() {
        return this.entryList;
    }
    /**
     * The size of the current transit record list.
     *
     * @return    integer which represents the entries in the vecotr.
     */
    public int getTransitRecordSize() {
        return this.entryList.size();
    }

    /**
     * Writes all entries in the transit record list to a locally stored file.
     * @param file    "./TransitRecordList.txt"
     */
    public void writeRecordToFile(String file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (TransitEntry entry : this.entryList) {
                fileWriter.append(entry.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}