/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Items {

    static String NAME, DESC;
    static int ID, HI_BID;
    static Users OWNER, HI_BIDDER;
    static ArrayList<Users> USERS;
    static DatagramSocket SOCKET;

    public Items(int id, DatagramSocket socket, Users owner, String n, String desc, int hi_bid, ArrayList<Users> users)
    {

    }

    public Items(int id, DatagramSocket socket, Users owner, Users hi_bidder, String n, String desc, int hi_bid, ArrayList<Users> users)
    {

    }

    public Users get_owner()
    {
        return OWNER;
    }

    public Users get_highest_bidder()
    {
        return HI_BIDDER;
    }

    public int get_highest_bid()
    {
        return HI_BID;
    }

    public int get_item_id()
    {
        return ID;
    }
}
