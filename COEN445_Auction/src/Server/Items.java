/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.*;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class concerns everything to do with items.
 * It manages the item data, updates item data, allows bidding on items.
 */
public class Items {

    private String NAME, DESC, MESSAGE;
    private int ID, HI_BID;
    private Users OWNER, HI_BIDDER;
    private ArrayList<Users> USERS;
    private DatagramSocket SOCKET;

    private File ITEM_DATA;
    private FileInputStream IN_STREAM;
    private DataInputStream DATA_STREAM;
    private BufferedReader BUFFERED_READER;
    private PrintWriter OUT;

    private String P = "/";

    public Items(int id, DatagramSocket socket, Users owner, Users hi_bidder, String n, String desc, int hi_bid, ArrayList<Users> users)
    {
        ID = id;
        SOCKET= socket;
        OWNER = owner;
        HI_BIDDER = hi_bidder;
        NAME = n;
        DESC = desc;
        HI_BID = hi_bid;
        USERS = users;
    }

    public String get_name()
    {
        return NAME;
    }

    public String get_desc()
    {
        return DESC;
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

    public void set_new_hi_bidder(Users bidder)
    {
        HI_BIDDER = bidder;
    }

    public void set_new_hi_bid(int bid)
    {
        HI_BID = bid;
    }

    /**
     * For updating the item in the backup file.
     */
    public String gen_item(String bidder, int bid)
    {
        String msg = ID + P + NAME + P + DESC + P + bid + P + OWNER.get_name() + P + bidder;
        return msg;
    }

    /**
     * Notifies all clients that an item has had their bid updated.
     */
    private void broadcast_bid()
    {
        Users tmp_user;
        for (int i = 0; i < USERS.size(); i++)
        {
            tmp_user = USERS.get(i);
            MESSAGE = SendHelper.create_send_new_hi(DefaultHelper.BID_BROADCAST, ID, HI_BID);
            SendHelper.send(MESSAGE, tmp_user.get_IP(), tmp_user.get_port(), SOCKET);
        }
    }

    /**
     * Notifies the bidder that their bid was successful.
     */
    private void bid_success(String request, Users bidder)
    {
        MESSAGE = SendHelper.create_send_bid_conf(DefaultHelper.BID, request);
        SendHelper.send(MESSAGE, bidder.get_IP(), bidder.get_port(), SOCKET);
    }

    /**
     * Notifies the bidder that there was an error with their bid.
     * Errors are outlined in DefaultHelper.java
     */
    private void bid_error(int code, String request, Users bidder)
    {
        MESSAGE = SendHelper.create_send_bid_fail(DefaultHelper.BID_ERROR, request, code);
        SendHelper.send(MESSAGE, bidder.get_IP(), bidder.get_port(), SOCKET);
    }

    /**
     * Updates the backup file with the new highest bid and highest bidder.
     */
    private synchronized void write_to_file() throws IOException
    {
        ITEM_DATA = new File("item_data.txt");
        File tmp = new File("itmp.txt");
        IN_STREAM = new FileInputStream(ITEM_DATA);
        DATA_STREAM = new DataInputStream(IN_STREAM);
        BUFFERED_READER = new BufferedReader(new InputStreamReader(DATA_STREAM));
        OUT = new PrintWriter(new FileWriter(tmp, true));
        String read, trim, replace, s_id;

        replace = gen_item(HI_BIDDER.get_name(), HI_BID);
        s_id = Integer.toString(ID);

        while ((read = BUFFERED_READER.readLine()) != null)
        {
            trim = read.trim();
            if (!trim.contains(s_id))
            {
                OUT.println(read);
            }
            else
            {
                OUT.println(replace);
            }
        }
        DATA_STREAM.close();
        OUT.close();
        ITEM_DATA.delete();
        tmp.renameTo(ITEM_DATA);
    }

    public static void HOLDUP(int time)
    {
        try
        {
            TimeUnit.SECONDS.sleep(time);
        }
        catch (InterruptedException e) {}
    }

    /**
     * This method is called from BidValidation.java
     * It checks if the user's bid is valid and if they are not the owner of the item that is bid on.
     * A successful bid will update the item in question's information as well as the backup file.
     * Errors are outlined in the system message lines.
     */
    public synchronized void bid(Users bidder, int bid, String request, int id)
    {
        //first we check if the owner is the bidder...
        if (OWNER != bidder)
        {
            //next we check if the bid exceeds the previous highest bid
            if (bid > HI_BID)
            {
                HI_BID = bid;
                HI_BIDDER = bidder;
                /*for (int i = 0; i < ITEMS.size(); i++)
                {
                    if (ITEMS.get(i).get_item_id() == id)
                    {
                        //we update the values of the item that was bid on
                        Server.ITEMS.get(i).set_new_hi_bid(HI_BID);
                        Server.ITEMS.get(i).set_new_hi_bidder(HI_BIDDER);
                        break;
                    }
                }*/
                //bid was successful so we notify the user and all clients.
                bid_success(request, bidder);
                HOLDUP(1);
                broadcast_bid();
                try
                {
                    write_to_file();
                }
                catch (IOException e) {}
            }
            else
            {
                bid_error(DefaultHelper.BID_ERROR_3, request, bidder);
                System.out.println("Bid attempt made on item #" + ID + " / Error: bid does not exceed current minimum bid.");
            }
        }
        else
        {
            bid_error(DefaultHelper.BID_ERROR_4, request, bidder);
            System.out.println("Bid attempt made on item #" + ID + " / Error: owner attempted to bid on own item.");
        }
    }
}
