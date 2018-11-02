/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.TimerTask;

public class Expire extends TimerTask {

    static int ID;
    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;
    static DatagramSocket SOCKET;

    public Expire(int id, DatagramSocket socket, ArrayList<Users> users, ArrayList<Items> items)
    {
        ID = id;
        SOCKET = socket;
        USERS = users;
        ITEMS = items;
    }

    /**
     * need to send the winning/sold/bid done message to clients
     * need to remove item from backup file
     */

    @Override
    public void run()
    {

    }
}
