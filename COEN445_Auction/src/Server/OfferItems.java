/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class OfferItems implements Runnable {

    static String REC_DATA, REQUEST, NAME, IP, S_PORT;
    static String[] DATA;
    static int PORT;

    static InetAddress CLIENT_ADDRESS;
    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;

    static Users USER;
    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;

    public OfferItems(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        REC_DATA = rd;
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
        //DATA ???
        //NAME ???
        CLIENT_ADDRESS = PACKET.getAddress();
        PORT = PACKET.getPort();
    }

    /**
     * send offer accepted or denied msg to client...
     * add the item to items backup file
     */

    private boolean is_registered()
    {
        for (int i = 0; i < USERS.size(); i++)
        {
            Users tmp_user = USERS.get(i);
            String tmp_name = tmp_user.get_name();
            if (tmp_name == NAME)
                return true;
        }
        return false;
    }

    @Override
    public void run()
    {

    }
}
