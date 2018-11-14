/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 * This class concerns everything to do with correct bid information.
 * This class will send an error back to the client or validate a bid.
 * An actual bid on an item is transferred over to Items.java
 */
public class BidValidation implements Runnable {

    private String REC_DATA, REQUEST, MESSAGE;
    private String[] DATA;
    private int ID, BID;

    private DatagramSocket SOCKET;
    private DatagramPacket PACKET;

    private Users USER;
    private Items ITEM;
    private ArrayList<Users> USERS;
    private ArrayList<Items> ITEMS;

    private String P = "/";

    public BidValidation(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        REC_DATA = rd;
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
    }

    /**
     * This method finds the user in question on the server. If found it will update the local USER variable.
     */
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

    /**
     * This method finds the item in question on the server. If found it will update the local ITEM variable.
     */
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

    /**
     * This method sends an error message to the client.
     * The variable code is defined in DefaultHelper.java
     */
    private void bid_error(int code)
    {
        MESSAGE = SendHelper.create_send_bid_fail(DefaultHelper.BID_ERROR, REQUEST, code);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    /**
     * The thread runs from here.
     * Nothing will happen if the data is empty.
     * Errors are outlined in the system message lines.
     */
    @Override
    public void run()
    {
        //Split the data into an array of strings
        DATA = REC_DATA.split(P);

        if (DATA.length > 0)
        {
            //assign data to corresponding variables
            REQUEST = DATA[1];
            ID = Integer.parseInt(DATA[2]);
            BID = Integer.parseInt(DATA[3]);

            //we check if the user is registered before proceeding
            if (is_registered())
            {
                //then we check if the item exists before proceeding
                if (item_exists(ID))
                {
                    //once the user and item are found, we send the bid request
                    ITEM.bid(USER, BID, REQUEST, ID);
                }
                else
                {
                    bid_error(DefaultHelper.BID_ERROR_0);
                    System.out.println("Bid attempt made on item #" + ID + " / Error: item does not exist.");
                }
            }
            else
            {
                bid_error(DefaultHelper.BID_ERROR_1);
                System.out.println("Error: bid attempt made by a user that does not exist.");
            }
        }
        else
        {
            bid_error(DefaultHelper.BID_ERROR_2);
            System.out.println("Bid attempt made on item #" + ID + " / Error: invalid information provided.");
        }
    }
}
