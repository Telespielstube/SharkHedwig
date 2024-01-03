package SetupTest;

import net.sharksystem.asap.ASAPChunk;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPMessages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ASAPMessageListTest implements ASAPMessages {

    private List<byte[]> messageList;

    public ASAPMessageListTest() {
        this.messageList = new ArrayList<>();
    }

    public void addMessage(byte[] message) {
        this.messageList.add(message);
    }
    @Override
    public int size() throws IOException {
        return 0;
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
        return this.messageList.iterator();
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
