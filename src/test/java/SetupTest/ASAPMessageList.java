package SetupTest;

import DeliveryContract.TransitEntry;
import net.sharksystem.asap.ASAPChunk;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPMessages;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ASAPMessageList implements ASAPMessages {

    private Vector<byte[]> entryList = null;

    public ASAPMessageList() {
        this.entryList = new Vector<>();
    }

    public void addMessage(byte[] message) {
        entryList.add(message);
    }
    @Override
    public int size() throws IOException {
        return entryList.size();
    }

    @Override
    public CharSequence getURI() {
        return null;
    }

    @Override
    public CharSequence getFormat() {
        return null;
    }

    @Override
    public Iterator<CharSequence> getMessagesAsCharSequence() throws IOException {
        return null;
    }

    @Override
    public Iterator<byte[]> getMessages() throws IOException {
        return entryList.iterator();


    }

    @Override
    public CharSequence getMessageAsCharSequence(int i, boolean b) throws ASAPException, IOException {
        return null;
    }

    @Override
    public byte[] getMessage(int i, boolean b) throws ASAPException, IOException {
        return new byte[0];
    }

    @Override
    public ASAPChunk getChunk(int i, boolean b) throws IOException, ASAPException {
        return null;
    }
}
