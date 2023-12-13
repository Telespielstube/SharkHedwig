package DeliveryContract;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * Class to add a new entry to the list of all previous handovers of this package.
 */
public class TransitRecord {

    private final Vector<TransitEntry> transitRecord;

    /**
     * The TransitRecord object is a dynamic vector where TransitEnry objects are added.
     * The advantage of the Java Vecor object is its threadsafty.
     */
    public TransitRecord() {
        this.transitRecord = new Vector<TransitEntry>();
    }

    /**
     * Adds a new entry to the transit record list.
     * @param entry    TransitEntry object
     */
    public void addEntry(TransitEntry entry) {
        this.transitRecord.add(entry);
    }

    /**
     * Prints all elements in the vector.
     */
    public void showAllElements() {
        if (this.transitRecord.elements().hasMoreElements()) {
            System.out.println(this.transitRecord.elements().nextElement());
        }
    }

    /**
     * The size of the current transit record list.
     *
     * @return    integer which represents the entries in the vecotr.
     */
    public int getTransitRecordSize() {
        return this.transitRecord.size();
    }

    /**
     * Writes all entries in the transit record list to a local stored file.
     */
    public void writeRecordToFile() {
        try (FileWriter fileWriter = new FileWriter("./TransitRecordList.txt")) {
            for (TransitEntry entry : this.transitRecord) {
                fileWriter.append(entry.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}