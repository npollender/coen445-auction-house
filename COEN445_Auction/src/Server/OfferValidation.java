/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.*;
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

    static File ITEM_DATA, USER_DATA;
    static FileInputStream IN_STREAM;
    static DataInputStream DATA_STREAM;
    static BufferedReader BUFFERED_READER;
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
        for (int i = 0; i < Server.USERS.size(); i++)
        {
            Users tmp_user = Server.USERS.get(i);
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
        item_bid.schedule(new Expire(DefaultHelper.ITEM_ID, SOCKET, USERS, ITEMS), DefaultHelper.BID_DURATION);
    }


    private void broadcast_item()
    {
        Users tmp_user;
        for (int i = 0; i < Server.USERS.size(); i++)
        {
            tmp_user = Server.USERS.get(i);
            MESSAGE = SendHelper.create_send_new_item(DefaultHelper.OFFER_BROADCAST, DefaultHelper.ITEM_ID, DESC, MIN, tmp_user.get_port());
            SendHelper.send(MESSAGE, tmp_user.get_IP(), tmp_user.get_port(), SOCKET);
        }
    }

    private void offer_success()
    {
        MESSAGE = SendHelper.create_send_offer_conf(DefaultHelper.OFFER, REQUEST, DefaultHelper.ITEM_ID, DESC, MIN);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    private void offer_error(int code)
    {
        MESSAGE = SendHelper.create_send_offer_fail(DefaultHelper.OFFER_ERROR, REQUEST, code);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    private static String gen_user(Users user)
    {
        String msg = user.get_name() + P + user.get_IP().getHostAddress() + P + user.get_port() + P + user.get_num_for_sale() + P;
        return msg;
    }

    private synchronized void write_to_file() throws IOException
    {
        ITEM_DATA = new File("item_data.txt");
        OUT = new PrintWriter(new FileWriter(ITEM_DATA, true));
        OUT.println(DefaultHelper.ITEM_ID + P + NAME + P + DESC + P + MIN + P + USER.get_name() + P + USER.get_name() + P + "???");
        OUT.close();
    }

    private synchronized void update_user_file(Users user) throws IOException
    {
        USER_DATA = new File("user_data.txt");
        File tmp = new File("utmp.txt");
        IN_STREAM = new FileInputStream(USER_DATA);
        DATA_STREAM = new DataInputStream(IN_STREAM);
        BUFFERED_READER = new BufferedReader(new InputStreamReader(DATA_STREAM));
        OUT = new PrintWriter(new FileWriter(tmp, true));
        String read, trim, replace;

        replace = gen_user(user);

        while ((read = BUFFERED_READER.readLine()) != null)
        {
            trim = read.trim();
            if (!trim.contains(user.get_name()))
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
        USER_DATA.delete();
        tmp.renameTo(USER_DATA);
    }

    @Override
    public void run()
    {
        DATA = REC_DATA.split(P);

        if (DATA.length > 0)
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
                    ITEM = new Items(DefaultHelper.ITEM_ID++, SOCKET, USER, USER, NAME, DESC, MIN, USERS);
                    System.out.println("Item: " + NAME + " offered for " + MIN + "$.");
                    Server.ITEMS.add(ITEM);
                    USER.increment_items();
                    offer_success();
                    broadcast_item();
                    try
                    {
                        write_to_file();
                        update_user_file(USER);
                    }
                    catch (IOException e) {}
                    start_bid();
                }
                else
                {
                    offer_error(DefaultHelper.OFFER_ERROR_2);
                    System.out.println("Item: " + NAME + " / Error: user item limit exceeded.");
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
            offer_error(DefaultHelper.OFFER_ERROR_1);
            System.out.println("Item: " + NAME + " / Error: invalid info provided.");
        }
    }
}
