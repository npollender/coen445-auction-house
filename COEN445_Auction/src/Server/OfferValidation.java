/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * This class concerns everything to do with offering a new item.
 * A message will be sent back to the client if the offer was successful or unsuccessful.
 * All users on the server will be notified when a new item is available.
 */
public class OfferValidation implements Runnable {

    private String REC_DATA, REQUEST, C_NAME, NAME, DESC, MESSAGE, S_MIN;
    private String[] DATA;
    private int MIN, PORT;

    private DatagramSocket SOCKET;
    private DatagramPacket PACKET;

    private Users USER;
    private Items ITEM;
    private ArrayList<Users> USERS;
    private ArrayList<Items> ITEMS;

    private File ITEM_DATA, USER_DATA;
    private FileInputStream IN_STREAM;
    private DataInputStream DATA_STREAM;
    private BufferedReader BUFFERED_READER;
    private PrintWriter OUT;

    private static String P = "/";

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

    /**
     * Starts the bidding period on the item. After BID_DURATION ms, the thread will fire and messages will be sent to
     * clients according to the item's information.
     */
    private void start_bid()
    {
        Timer item_bid = new Timer();
        System.out.println("Bid started on " + NAME + ".");
        item_bid.schedule(new Expire(DefaultHelper.ITEM_ID, SOCKET, USERS, ITEMS), DefaultHelper.BID_DURATION);
    }

    /**
     * All registered clients are notified when a new item is offered.
     */
    private void broadcast_item(int port)
    {
        Users tmp_user;
        for (int i = 0; i < USERS.size(); i++)
        {
            tmp_user = USERS.get(i);
            String new_desc = NAME + ": " + DESC;
            MESSAGE = SendHelper.create_send_new_item(DefaultHelper.OFFER_BROADCAST, DefaultHelper.ITEM_ID, new_desc, MIN, port);
            SendHelper.send(MESSAGE, tmp_user.get_IP(), tmp_user.get_port(), SOCKET);
        }
    }

    /**
     * The confirmation message to be sent back to the owner of an offer.
     */
    private void offer_success()
    {
        MESSAGE = SendHelper.create_send_offer_conf(DefaultHelper.OFFER, REQUEST, DefaultHelper.ITEM_ID, DESC, MIN);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    /**
     * The error message sent back to the owner of an offer if something goes wrong.
     */
    private void offer_error(int code)
    {
        MESSAGE = SendHelper.create_send_offer_fail(DefaultHelper.OFFER_ERROR, REQUEST, code);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    /**
     * User generator for updating the backup file.
     */
    private static String gen_user(Users user)
    {
        String msg = user.get_name() + P + user.get_IP().getHostAddress() + P + user.get_port() + P + user.get_num_for_sale() + P;
        return msg;
    }

    /**
     * Writes the new item to the backup file.
     */
    private synchronized void write_to_file() throws IOException
    {
        ITEM_DATA = new File("item_data.txt");
        OUT = new PrintWriter(new FileWriter(ITEM_DATA, true));
        OUT.println(DefaultHelper.ITEM_ID + P + NAME + P + DESC + P + MIN + P + USER.get_name() + P + USER.get_name() + P);
        OUT.close();
    }

    /**
     * Updates the user information backup file, it simply modifies the number of item owned for the user that offered an item.
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

    private int generate_port() throws IOException
    {
        int port;
        ServerSocket item_socket = new ServerSocket(0);
        item_socket.setReuseAddress(true);
        port = item_socket.getLocalPort();
        item_socket.close();
        return port;
    }

    /**
     * Messages have been arriving too quickly at the clients so we must put a delay between sending messages.
     */
    public static void HOLDUP(int time)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(time);
        }
        catch (InterruptedException e) {}
    }

    /**
     * Thread runs from here.
     * Errors are outlined in the system message lines.
     */
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

            //we search for the user in the server
            boolean exists = false;
            for (int i = 0; i < USERS.size(); i++)
            {
                if (USERS.get(i).get_name().contains(REQUEST))
                {
                    exists = true;
                    USER = USERS.get(i);
                    break;
                }
            }

            if (exists)
            {
                //once we find the user, we make sure they are allowed to offer more items
                if (USER.get_num_for_sale() < DefaultHelper.MAX_ITEMS_FOR_SALE)
                {
                    DefaultHelper.ITEM_ID += 1;
                    try
                    {
                        PORT = generate_port();
                    }
                    catch (IOException e)
                    {
                        System.out.println("Something went wrong with setting up the item port...");
                        offer_error(DefaultHelper.OFFER_ERROR_1);
                        System.out.println("Item: " + NAME + " / Error: invalid info provided.");
                        return;
                    }
                    ITEM = new Items(DefaultHelper.ITEM_ID, SOCKET, USER, USER, NAME, DESC, MIN, USERS, PORT);
                    System.out.println("Item: " + NAME + " offered for " + MIN + "$.");
                    ITEMS.add(ITEM);

                    //we increment the users items for sale (max of 3)
                    for (int i = 0; i < USERS.size(); i++)
                    {
                        Users tmp_user = USERS.get(i);
                        if (tmp_user.equals(USER))
                        {
                            USERS.get(i).increment_items();
                            break;
                        }
                    }
                    //send the messages to the clients and update the backups
                    offer_success();
                    HOLDUP(250);
                    broadcast_item(PORT);
                    try
                    {
                        write_to_file();
                        update_user_file(USER);
                    }
                    catch (IOException e) {}
                    start_bid();
                    new Thread(new TCPServer(PORT)).start();
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
