package Session;

import Message.Message;

import java.util.SortedMap;
import java.util.TreeMap;

public class ReceivedMessageList {

    // A TreeMap to store sent and Received Messages with their timestamps as key and the Message as value.
    private final SortedMap<Long, Object> messageList = new TreeMap<>();

    /**
     * Compares to timestamps if the difference is less than the offset of 5 seconds.
     *
     * @param timestamp    timestamp of received message.
     * @return             True if difference is less than offset. False if it is greater than.
     */
    public boolean compareTimestamp(long timestamp, int timeOffset) {
        return timestamp - this.messageList.lastKey() < timeOffset;
    }

    /**
     * Gets the last value from the message TreeMap, which is a (K,V) map.
     *
     * @return               Last object from TreeMap.
     */
    public Object getLastValueFromList() {
        return this.messageList.get(this.messageList.lastKey());
    }

    /**
     * Adds a message object to the TreeMap.
     */
    public void addMessageToList(Message message) {
        this.messageList.put(message.getTimestamp(), message);
    }

    /**
     * Clears the messageList TreeMap object.
     */
    public void clearMessageList() {
        if (!messageList.isEmpty()) {
            this.messageList.clear();
        }
    }
}
