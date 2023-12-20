package DeliveryContract;

import java.io.*;
import java.util.Vector;

/**
 * Class to add a new entry to the list of all previous handovers of this package.
 */
public class TransitRecord implements Serializable {

    private Vector<TransitEntry> entryList = null;

    /**
     * The TransitRecord object is a dynamic vector where TransitEnry objects are added.
     * The advantage of the Java Vecor object is its threadsafty.
     */
    public TransitRecord() {
        this.entryList = new Vector<TransitEntry>();
    }

    /**
     * Adds Vector entries to a newly instatiated TransitRecord object.
     * Only called by transferees.
     *
     * @param entries    All previous handover entries of the package.
     */
    public TransitRecord(Vector<TransitEntry> entries) {
        this.entryList = entries;
    }
    /**
     * Adds a new entry to the transit record list.
     * @param entry    TransitEntry object
     */
    public void addEntry(TransitEntry entry) {
        this.entryList.add(entry);
    }

    /**
     * Returns all entries.
     *
     * @return    All entries fo the Vector.
     */
    public Vector<TransitEntry> getAllEntries() {
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
     * Writes all entries in the transit record list to a local stored file.
     */
    public void writeRecordToFile() {
        try (FileWriter fileWriter = new FileWriter("./TransitRecordList.txt")) {
            for (TransitEntry entry : this.entryList) {
                fileWriter.append(entry.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}