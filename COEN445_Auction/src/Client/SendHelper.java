/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * We're using a helper class here for sending information regarding the server.
 * It's easier to have a single helper class rather than adding the same method to every class using it.
 * Ultimately this reduces clutter in the classes.
 */

public class SendHelper {

    static public final int MSG_SIZE = 300;

    static final private ReentrantReadWriteLock RW_ITEM_LOCK = new ReentrantReadWriteLock();
    static public Lock READ_LIST = RW_ITEM_LOCK.readLock();
    static public Lock WRITE_LIST = RW_ITEM_LOCK.writeLock();

    static int PORT;
    static String SERVER;

    static char P = '_';

    public SendHelper() {}

    synchronized static String create_send(char c, String request, String name, String ip, int port)
    {
        String msg = c + P + request + P + name + P + ip + P + port;
        return msg;
    }

    synchronized static void send(String data)
    {
        try {
            byte[] msg = new byte[MSG_SIZE];
            msg = data.getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, Client.SERVER_ADDRESS, Client.PORT);
            Client.SOCKET.send(packet);
        }
        catch (IOException e) {}
    }
}
