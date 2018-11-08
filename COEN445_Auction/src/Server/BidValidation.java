/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.File;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class BidValidation implements Runnable {

    static String REC_DATA, REQUEST, MESSAGE;
    static String[] DATA;
    static int ID, BID;

    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;

    static Users USER;
    static Items ITEM;
    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;

    static String P = "/";

    public BidValidation(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        REC_DATA = rd;
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
    }

    private boolean is_registered()
    {
        for (int i = 0; i < USERS.size(); i++)
        {
            Users tmp_user = USERS.get(i);
            String tmp_name = tmp_user.get_name();
            if (tmp_name.equals(REQUEST))
            {
                USER = tmp_user;
                return true;
            }
        }
        return false;
    }

    private boolean item_exists(int id)
    {
        for (int i = 0; i < ITEMS.size(); i++)
        {
            Items tmp_item = ITEMS.get(i);
            int tmp_id = tmp_item.get_item_id();
            if (tmp_id == id)
            {
                ITEM = tmp_item;
                return true;
            }
        }
        return false;
    }

    private void bid_error(int code)
    {
        MESSAGE = SendHelper.create_send_bid_fail(DefaultHelper.BID_ERROR, REQUEST, code);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    @Override
    public void run()
    {
        DATA = REC_DATA.split(P);

        if (DATA.length == DefaultHelper.BID_INFO_SIZE)
        {
            REQUEST = DATA[1];
            ID = Integer.parseInt(DATA[2]);
            BID = Integer.parseInt(DATA[3]);
            if (is_registered())
            {
                if (item_exists(ID))
                {
                    ITEM.bid(USER, BID, REQUEST);
                }
                else
                {
                    bid_error(0);
                    System.out.println("item doesnt exist");
                }
            }
            else
            {
                bid_error(0);
                System.out.println("user doesnt exist");
            }
        }
        else
        {
            bid_error(0);
            System.out.println("wrong info");
        }
    }
}
