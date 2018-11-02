/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Deregister extends Thread {

    static String REC_DATA, REQUEST, NAME, IP;
    static String[] DATA;

    static InetAddress CLIENT_ADDRESS;
    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;

    static Users USER;
    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;

    static String P = "/";

    public Deregister(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        REC_DATA = rd;
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
    }

    private boolean item_exists()
    {
        for (int i = 0; i < ITEMS.size(); i++)
        {
            Items tmp_items = ITEMS.get(i);
            Users tmp_user = tmp_items.get_owner();
            if (tmp_user == USER)
                return true;
        }
        return false;
    }

    private boolean has_highest_bid()
    {
        for (int i = 0; i < ITEMS.size(); i++)
        {
            Items tmp_items = ITEMS.get(i);
            Users tmp_user = tmp_items.get_highest_bidder();
            if (tmp_user == USER)
                return true;
        }
        return false;
    }

    /**
     * Need to add messages to be sent to client in relation to de-reg
     * also need to add a method for removing the user from the backup file.
     */

    @Override
    public void run()
    {

    }
}

