/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.*;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.TimerTask;

public class Expire extends TimerTask {

    static String MESSAGE;
    static int ID;
    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;
    static DatagramSocket SOCKET;

    static File ITEM_DATA, USER_DATA;
    static FileInputStream IN_STREAM;
    static DataInputStream DATA_STREAM;
    static BufferedReader BUFFERED_READER;
    static PrintWriter OUT;

    static String P = "/";

    public Expire(int id, DatagramSocket socket, ArrayList<Users> users, ArrayList<Items> items)
    {
        ID = id;
        SOCKET = socket;
        USERS = users;
        ITEMS = items;
    }

    private static void bid_over(Items item)
    {
        for (int i = 0; i < Server.USERS.size(); i++)
        {
            Users tmp_user = USERS.get(i);
            MESSAGE = SendHelper.create_send_bid_over(0, item.get_item_id(), item.get_highest_bid());
            SendHelper.send(MESSAGE, tmp_user.get_IP(), tmp_user.get_port(), SOCKET);

            if (tmp_user.equals(item.get_highest_bidder()))
            {
                MESSAGE = SendHelper.create_send_win(0, item.get_item_id(), item.get_owner().get_IP().getHostAddress(), item.get_owner().get_port(), item.get_highest_bid());
                SendHelper.send(MESSAGE, tmp_user.get_IP(), tmp_user.get_port(), SOCKET);
            }
            else if (tmp_user.equals(item.get_owner()))
            {
                MESSAGE = SendHelper.create_send_sold_to(0, item.get_item_id(), item.get_highest_bidder().get_name(), item.get_highest_bidder().get_IP().getHostAddress(), item.get_highest_bidder().get_port(), item.get_highest_bid());
                SendHelper.send(MESSAGE, tmp_user.get_IP(), tmp_user.get_port(), SOCKET);
            }
        }
        Server.ITEMS.remove(item);
    }

    private static void not_sold(Items item)
    {
        MESSAGE = SendHelper.create_send_not_sold(0, ID, 0);
        SendHelper.send(MESSAGE, item.get_owner().get_IP(), item.get_owner().get_port(), SOCKET);
    }

    private static String gen_user(Users user)
    {
        String msg = user.get_name() + P + user.get_IP().getHostName() + P + user.get_port() + P + user.get_num_for_sale() + P;
        return msg;
    }

    private synchronized void remove_from_file(Items item) throws IOException
    {
        ITEM_DATA = new File("item_data.txt");
        File tmp = new File("itmp.txt");
        IN_STREAM = new FileInputStream(ITEM_DATA);
        DATA_STREAM = new DataInputStream(IN_STREAM);
        BUFFERED_READER = new BufferedReader(new InputStreamReader(DATA_STREAM));
        OUT = new PrintWriter(new FileWriter(tmp, true));
        String read, trim, s_id;

        s_id = Integer.toString(item.get_item_id());

        while ((read = BUFFERED_READER.readLine()) != null)
        {
            trim = read.trim();
            if (!trim.contains(s_id))
            {
                OUT.println(read);
            }
        }
        DATA_STREAM.close();
        OUT.close();
        ITEM_DATA.delete();
        tmp.renameTo(ITEM_DATA);
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
        for (int i = 0; i < Server.ITEMS.size(); i++)
        {
            Items tmp_item = Server.ITEMS.get(i);

            if (tmp_item.get_item_id() == ID)
            {
                if (tmp_item.get_highest_bidder().equals(tmp_item.get_owner()))
                {
                    tmp_item.get_owner().decrement_items();
                    not_sold(tmp_item);
                    try
                    {
                        update_user_file(tmp_item.get_owner());
                        remove_from_file(tmp_item);
                    }
                    catch (IOException e) {}
                }
                else
                {
                    tmp_item.get_owner().decrement_items();
                    bid_over(tmp_item);
                    try
                    {
                        update_user_file(tmp_item.get_owner());
                        remove_from_file(tmp_item);
                    }
                    catch (IOException e) {}
                }
                break;
            }
        }
    }
}
