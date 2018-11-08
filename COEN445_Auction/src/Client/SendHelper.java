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

    static public byte[] MESSAGE = new byte[DefaultHelper.MSG_SIZE];

    static final private ReentrantReadWriteLock RW_ITEM_LOCK = new ReentrantReadWriteLock();
    static public Lock READ_LIST = RW_ITEM_LOCK.readLock();
    static public Lock WRITE_LIST = RW_ITEM_LOCK.writeLock();

    static String P = "/";

    public SendHelper() {}

    /**
     * RegisterValidation request to the server
     */
    synchronized static String create_send_reg(int c, String request, String name, String ip, int port)
    {
        String msg = c + P + request + P + name + P + ip + P + port + P;
        return msg;
    }

    /**
     * De-register request to the server
     */
    synchronized static String create_send_dereg(int c, String request, String name, String ip)
    {
        String msg = c + P + request + P + name + P + ip + P;
        return msg;
    }

    /**
     * Offer request to the server
     */
    synchronized static String create_send_offer(int c, String request, String name, String ip, String i_desc, String min)
    {
        String msg = c + P + request + P + name + P + ip + P + i_desc + P + min + P;
        return msg;
    }

    /**
     * Bid request to the server
     * TCP CONNECTION
     */
    synchronized static String create_send_bid(int c, String request, int id, String bid)
    {
        String msg = c + P + request + P + id + P + bid + P;
        return msg;
    }

    synchronized static void send(String data)
    {
        try {
            MESSAGE = data.getBytes();
            DatagramPacket packet = new DatagramPacket(MESSAGE, MESSAGE.length, Client.SERVER_ADDRESS, Client.PORT);
            Client.SOCKET.send(packet);
            return;
        }
        catch (IOException e) {}
    }
}
