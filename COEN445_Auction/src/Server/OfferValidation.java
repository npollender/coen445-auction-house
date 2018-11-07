/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Timer;

public class OfferValidation implements Runnable {

    static String REC_DATA, REQUEST, C_NAME, NAME, DESC, MESSAGE, S_MIN;
    static String[] DATA;
    static int ID, MIN;

    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;

    static Users USER;
    static Items ITEM;
    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;

    static File ITEM_DATA;
    static PrintWriter OUT;

    static String P = "/";

    public OfferValidation(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
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
            if (tmp_name.equals(C_NAME))
            {
                USER = tmp_user;
                return true;
            }
        }
        return false;
    }

    private void start_bid()
    {
        Timer item_bid = new Timer();
        System.out.println("Bid started on " + NAME + ".");
        item_bid.schedule(new Expire(ID, SOCKET, USERS, ITEMS), DefaultHelper.BID_DURATION);
    }

    private void offer_success()
    {
        MESSAGE = SendHelper.create_send_offer_conf(DefaultHelper.OFFER, REQUEST, ID, DESC, MIN);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    private void offer_error(int code)
    {
        MESSAGE = SendHelper.create_send_offer_fail(DefaultHelper.OFFER_ERROR, REQUEST, code);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    private synchronized void write_to_file() throws IOException
    {
        ITEM_DATA = new File("item_data.txt");
        OUT = new PrintWriter(new FileWriter(ITEM_DATA), true);
        OUT.println(ID + P + NAME + P + DESC + P + MIN + P + USER.get_name() + P + ITEM.get_highest_bidder().get_name() + P + "???");
        OUT.close();
    }

    @Override
    public void run()
    {
        DATA = REC_DATA.split(P);

        if (DATA.length == DefaultHelper.ITEM_INFO_SIZE)
        {
            REQUEST = DATA[1];
            C_NAME = DATA[2];
            NAME = DATA[3];
            DESC = DATA[4];
            S_MIN = DATA[5];
            MIN = Integer.parseInt(S_MIN);
            if (is_registered())
            {
                if (USER.get_num_for_sale() < DefaultHelper.MAX_ITEMS_FOR_SALE) {
                    ITEM = new Items(DefaultHelper.ITEM_ID++, SOCKET, USER, NAME, DESC, MIN, USERS);
                    System.out.println("Item: " + NAME + " offered for " + MIN + ".");
                    ITEMS.add(ITEM);
                    USER.increment_items();
                    offer_success();
                    try
                    {
                        write_to_file();
                    }
                    catch (IOException e) {}
                    start_bid();
                }
            }
            else
            {
                offer_error(DefaultHelper.OFFER_ERROR_0);
                System.out.println("Item: " + NAME + " / Error: user not registered.");
            }
        }
        else
        {
            offer_error(DefaultHelper.OFFER_ERROR_0);
            System.out.println("Item: " + NAME + " / Error: invalid info provided.");
        }
    }
}
