/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * We're using a helper class here for sending information regarding the server.
 * It's easier to have a single helper class rather than adding the same method to every class using it.
 * Ultimately this reduces clutter in the classes.
 */

public class SendHelper {

    static byte[] MESSAGE = new byte[DefaultHelper.MSG_SIZE];

    static final private ReentrantReadWriteLock RW_USER_LOCK = new ReentrantReadWriteLock();
    static public Lock READ_LIST_U = RW_USER_LOCK.readLock();
    static public Lock WRITE_LIST_U = RW_USER_LOCK.writeLock();

    static final private ReentrantReadWriteLock RW_ITEM_LOCK = new ReentrantReadWriteLock();
    static public Lock READ_LIST_I = RW_ITEM_LOCK.readLock();
    static public Lock WRITE_LIST_I = RW_ITEM_LOCK.writeLock();

    static int PORT;
    static String SERVER;

    static String P = "/";

    /**
     * Registered message back to client.
     */
    synchronized static String create_send_reg(int c, String request, String name, String ip, int port)
    {
        String msg = c + P + request + P + name + P + ip + P + port + P;
        return msg;
    }

    /**
     * De-registered message back to client.
     */
    synchronized static String create_send_dereg(int c, String request)
    {
        String msg = c + P + request + P;
        return msg;
    }

    /**
     * Registration or de-registration fail message back to client.
     */
    synchronized static String create_send_notReg(int c, String request, int id)
    {
        String msg = c + P + request + P + id + P;
        return msg;
    }

    /**
     * Confirms offer back to the offering client.
     */
    synchronized static String create_send_offer_conf(int c, String request, int id, String desc, int min)
    {
        String msg = c + P + request + P + id + P + desc + P + min + P;
        return msg;
    }

    /**
     * Offer failed message to client.
     * id is error id, not item id.
     */
    synchronized static String create_send_offer_fail(int c, String request, int id)
    {
        String msg = c + P + request + P + id + P;
        return msg;
    }

    /**
     * Notifies all clients that a new item is up for sale.
     */
    synchronized static String create_send_new_item(int c, int id, String desc, int min, int port)
    {
        String msg = c + P + id + P + desc + P + min + P + port + P;
        return msg;
    }

    /**
     * Sends client message that bid failed.
     */
    synchronized static String create_send_bid_fail(int c, String request, int id)
    {
        String msg = c + P + request + P + id + P;
        return msg;
    }

    /**
     * Send new highest bid on an item to all clients.
     */
    synchronized static String create_send_new_hi(int c, int id, int bid)
    {
        String tmp = Integer.toString(bid);
        String msg = c + P + id + P + tmp + P;
        return msg;
    }

    /**
     * Send win message to winning client.
     */
    synchronized static String create_send_win(int c, int id, String ip, int port, int win)
    {
        String msg = c + P + id + P + ip + P + port + P + win + P;
        return msg;
    }

    /**
     * Send to all clients that had at least one bid (but did not win) on item that bid is over.
     */
    synchronized static String create_send_bid_over(int c, int id, int bid)
    {
        String tmp = Integer.toString(bid);
        String msg = c + P + id + P + tmp + P;
        return msg;
    }


    /**
     * Send the owner who the item was sold to.
     */
    synchronized static String create_send_sold_to(int c, int id, String winner, String ip, int port, int bid)
    {
        String msg = c + P + id + P + winner + P + ip + P + port + P + bid + P;
        return msg;
    }

    /**
     * Send to owner item is not sold.
     */
    synchronized static String create_send_not_sold(int c, int id, int r)
    {
        String tmp = Integer.toString(r);
        String msg = c + P + id + P + tmp + P;
        return msg;
    }

    /**
     * Sends a new user a list of all available items.
     */
    synchronized static String create_send_existing_items(int c, int id, String desc, int min)
    {
        String msg = c + P + id + P + desc + P + min + P;
        return msg;
    }

    synchronized static void send(String data, InetAddress addr, int port, DatagramSocket socket)
    {
        try {
            MESSAGE = data.getBytes();
            DatagramPacket packet = new DatagramPacket(MESSAGE, MESSAGE.length, addr, port);
            System.out.println("Sending: " + data);
            socket.send(packet);
        }
        catch (IOException e) {}
    }
}
