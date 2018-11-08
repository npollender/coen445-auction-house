/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Items {

    static String NAME, DESC, MESSAGE;
    static int ID, HI_BID;
    static Users OWNER, HI_BIDDER;
    static ArrayList<Users> USERS;
    static DatagramSocket SOCKET;

    static File ITEM_DATA;
    static FileInputStream IN_STREAM;
    static DataInputStream DATA_STREAM;
    static BufferedReader BUFFERED_READER;
    static PrintWriter OUT;

    static String P = "/";

    public Items(int id, DatagramSocket socket, Users owner, String n, String desc, int hi_bid, ArrayList<Users> users)
    {
        ID = id;
        SOCKET= socket;
        OWNER = owner;
        NAME = n;
        DESC = desc;
        HI_BID = hi_bid;
        USERS = users;

        //broadcast_item();
    }

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

    public String gen_item(String bidder, int bid)
    {
        String msg = ID + P + NAME + P + DESC + P + bid + P + OWNER.get_name() + P + bidder;
        return msg;
    }

    private void broadcast_bid()
    {
        Users tmp_user;
        for (int i = 0; i < Server.USERS.size(); i++)
        {
            tmp_user = Server.USERS.get(i);
            MESSAGE = SendHelper.create_send_new_hi(0, ID, HI_BID);
            SendHelper.send(MESSAGE, tmp_user.get_IP(), tmp_user.get_port(), SOCKET);
        }
    }

    private void bid_error(int code, String request, Users bidder)
    {
        MESSAGE = SendHelper.create_send_bid_fail(0, request, code);
        SendHelper.send(MESSAGE, bidder.get_IP(), bidder.get_port(), SOCKET);
    }

    public synchronized void bid(Users bidder, int bid, String REQUEST)
    {
        int old_bid = HI_BID;
        String old_bidder = HI_BIDDER.get_name();
        if (OWNER != bidder)
        {
            if (bid > HI_BID)
            {
                HI_BID = bid;
                HI_BIDDER = bidder;
                broadcast_bid();
                try
                {
                    write_to_file(old_bidder, old_bid);
                }
                catch (IOException e) {}
            }
            else
            {
                bid_error(0, REQUEST, bidder);
            }
        }
        else
        {
            bid_error(0, REQUEST, bidder);
        }
    }

    private synchronized void write_to_file(String prev_bidder, int prev_bid) throws IOException
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
}
