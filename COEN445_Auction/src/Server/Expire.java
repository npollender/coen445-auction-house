/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.*;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * This class concerns item expiration.
 * This thread is fired after 5 minutes for each item.
 * The item information will determine what messages the client receives.
 */
public class Expire extends TimerTask {

    private String MESSAGE;
    private int ID;
    private ArrayList<Users> USERS;
    private ArrayList<Items> ITEMS;
    private DatagramSocket SOCKET;

    private File ITEM_DATA, USER_DATA;
    private FileInputStream IN_STREAM;
    private DataInputStream DATA_STREAM;
    private BufferedReader BUFFERED_READER;
    private PrintWriter OUT;

    private static String P = "/";

    public Expire(int id, DatagramSocket socket, ArrayList<Users> users, ArrayList<Items> items)
    {
        ID = id;
        SOCKET = socket;
        USERS = users;
        ITEMS = items;
    }

    /**
     * This method is for announcing to all users that a bid is over.
     * Furthermore, it will also tell the owner that their items has been sold.
     * Also, it will tell the winner that they won their item.
     * This method is only invoked if there is at least 1 bid on the item.
     * The item is removed from the server afterwards.
     */
    private void bid_over(Items item)
    {
        for (int i = 0; i < USERS.size(); i++)
        {
            //we message all users that the bid is over.
            Users tmp_user = USERS.get(i);
            MESSAGE = SendHelper.create_send_bid_over(DefaultHelper.BID_OVER, item.get_item_id(), item.get_highest_bid());
            SendHelper.send(MESSAGE, tmp_user.get_IP(), tmp_user.get_port(), SOCKET);

            //we find the highest bidder to tell them they won
            if (tmp_user.equals(item.get_highest_bidder()))
            {
                HOLDUP(1);
                MESSAGE = SendHelper.create_send_win(DefaultHelper.BID_WINNER, item.get_item_id(), item.get_owner().get_IP().getHostAddress(), item.get_owner().get_port(), item.get_highest_bid());
                SendHelper.send(MESSAGE, tmp_user.get_IP(), tmp_user.get_port(), SOCKET);
            }

            //we find the owner to tell them their item has been sold
            else if (tmp_user.equals(item.get_owner()))
            {
                HOLDUP(1);
                MESSAGE = SendHelper.create_send_sold_to(DefaultHelper.BID_SOLD_TO, item.get_item_id(), item.get_highest_bidder().get_name(), item.get_highest_bidder().get_IP().getHostAddress(), item.get_highest_bidder().get_port(), item.get_highest_bid());
                SendHelper.send(MESSAGE, tmp_user.get_IP(), tmp_user.get_port(), SOCKET);
            }
        }
        ITEMS.remove(item);
    }

    /**
     * This method is for when the bidding period of an item has ended but no one has bid on it.
     * It only tells the owner that the item wasn't sold, and no announcement is made to the other clients.
     */
    private void not_sold(Items item)
    {
        MESSAGE = SendHelper.create_send_not_sold(DefaultHelper.BID_NOT_SOLD, ID, 0);
        SendHelper.send(MESSAGE, item.get_owner().get_IP(), item.get_owner().get_port(), SOCKET);
    }

    /**
     * For finding the user in the backup file and updating their data.
     */
    private static String gen_user(Users user)
    {
        String msg = user.get_name() + P + user.get_IP().getHostName() + P + user.get_port() + P + user.get_num_for_sale() + P;
        return msg;
    }

    /**
     * This method removes the item from the backup file because it has either been sold or not sold after the bid period.
     */
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

    /**
     * This method updates the owner of an item in the backup once their item bid duration has ended.
     * It decrements their item count so they may offer more items.
     */
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

    public static void HOLDUP(int time)
    {
        try
        {
            TimeUnit.SECONDS.sleep(time);
        }
        catch (InterruptedException e) {}
    }

    /**
     * The thread runs from here.
     * After 5 minutes it will run.
     */
    @Override
    public void run()
    {
        System.out.println("Times up for item #" + ID + ".");

        //we search for the item on the server
        for (int i = 0; i < ITEMS.size(); i++)
        {
            Items tmp_item = ITEMS.get(i);

            if (tmp_item.get_item_id() == ID)
            {
                //this is a check to see if the item has not been bid on
                if (tmp_item.get_highest_bidder().get_name().equals(tmp_item.get_owner().get_name()))
                {
                    for (int j = 0; j < USERS.size(); j++)
                    {
                        //if the item isn't bid on, it is still removed and we decrement the owners items.
                        if (tmp_item.get_owner().equals(USERS.get(i)))
                        {
                            USERS.get(i).decrement_items();
                            break;
                        }
                    }
                    //item is removed, and only the owner is notified
                    ITEMS.remove(tmp_item);
                    not_sold(tmp_item);
                    try
                    {
                        update_user_file(tmp_item.get_owner());
                        remove_from_file(tmp_item);
                    }
                    catch (IOException e) {}
                    break;
                }
                //the other scenario where an item is bid on
                else
                {
                    for (int j = 0; j < USERS.size(); j++)
                    {
                        if (tmp_item.get_owner().equals(USERS.get(i)))
                        {
                            USERS.get(i).decrement_items();
                            break;
                        }
                    }
                    //the item is removed and the clients are notified of the bid period being over. The owner is notified the item is sold and the winner is notified they won.
                    ITEMS.remove(tmp_item);
                    bid_over(tmp_item);
                    try
                    {
                        update_user_file(tmp_item.get_owner());
                        remove_from_file(tmp_item);
                    }
                    catch (IOException e) {}
                    break;
                }
            }
        }
    }
}
