package Connection;

import DeliveryContract.ShippingLabel;
import Message.MessageHandler;
import Misc.Utilities;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;


/**
 * This class handles the server endpoint of a two-way communication link between the SharkHedwigComponent and
 * to the SharkHedwig graphical user interface. To prevent the inefficient blocking sockets communication the
 * communication takes place over an asynchronous channel for stream-oriented listening sockets.
 * <p>
 * Java Oracle Doc:<p></p>
 * https://docs.oracle.com/javase/8/docs/api/java/nio/channels/AsynchronousServerSocketChannel.html
 */
public class ServerSocket implements Runnable {

    private AsynchronousServerSocketChannel serverSocket; //An asynchronous channel for stream-oriented listening sockets.
    private ByteBuffer byteBuffer; // Like a builder it creates a byte[] but it offers methods to manipulate the byte[] received from a I/O component like a channel.

    public ServerSocket(int port) {
        try {
            serverSocket = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));

        } catch (IOException e) {
            System.err.println(Utilities.formattedTimestamp()
                    + "An error occurred while setting up the socket port for receiving shipping label data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    // There needs to be a check if the mobile drone is connected to the owners local network.
    // Otherwise it does not make sense to let the thread running.
    @Override
    public void run() {
        while (true) {
            serverSocket.accept(this, new CompletionHandler<AsynchronousSocketChannel, ServerSocket>() {
                @Override
                public void completed(AsynchronousSocketChannel socketChannel, ServerSocket attachment) {
                    serverSocket.accept(null, this);
                    readFromChannel(socketChannel);
                    ShippingLabel labelContent = (ShippingLabel) MessageHandler.byteArrayToObject(byteBuffer.array());

                    new ShippingLabel(
                            labelContent.getUUID(),
                            labelContent.getSender(),
                            labelContent.getOrigin(),
                            labelContent.getPackageOrigin(),
                            labelContent.getRecipient(),
                            labelContent.getDestination(),
                            labelContent.getPackageDestination(),
                            labelContent.getPackageWeight());
                }

                @Override
                public void failed(Throwable exc, ServerSocket attachment) {
                    System.err.println(Utilities.formattedTimestamp() + "Failed to establish a connection: " + exc.getMessage());
                }
            });
        }
    }

    /**
     * Reads the received data from the client.
     *
     * @param socketChannel    Channel the byte stream from the client is sent.
     */
    private void readFromChannel(AsynchronousSocketChannel socketChannel) {
        byteBuffer = ByteBuffer.allocate(512);
        socketChannel.read(byteBuffer);
    }
}
