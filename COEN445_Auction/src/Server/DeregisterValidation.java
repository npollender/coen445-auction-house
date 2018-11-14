/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * This class concerns everything to do with deregistering.
 */
public class DeregisterValidation extends Thread {

    private String REC_DATA, REQUEST, NAME, IP, MESSAGE;
    private String[] DATA;

    private InetAddress CLIENT_ADDRESS;
    private DatagramSocket SOCKET;
    private DatagramPacket PACKET;

    private Users USER;
    private ArrayList<Users> USERS;
    private ArrayList<Items> ITEMS;

    private File USER_DATA;
    private PrintWriter OUT;
    private BufferedReader BUFFERED_READER;

    private boolean EXISTS;

    private String P = "/";

    public DeregisterValidation(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        REC_DATA = rd;
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
    }

    /**
     * Method for checking if user still has items for sale.
     * If they do, they cannot deregister.
     */
    private boolean has_items()
    {
        for (int i = 0; i < ITEMS.size(); i++)
        {
            Items tmp_items = ITEMS.get(i);
            Users tmp_user = tmp_items.get_owner();
            if (tmp_user == USER)
                return true;
        }
        return false;
    }

    /**
     * Method for checking if user still has the highest bid on an item.
     * If they do, they cannot deregister.
     */
    private boolean has_highest_bid()
    {
        Items tmp_items;
        for (int i = 0; i < ITEMS.size(); i++)
        {
            tmp_items = ITEMS.get(i);
            Users tmp_user = tmp_items.get_highest_bidder();
            if (tmp_user == USER)
                return true;
        }
        return false;
    }

    /**
     * This method sends a confirmation to the client that they have been deregistered.
     */
    private void dereg_success()
    {
        MESSAGE = SendHelper.create_send_dereg(DefaultHelper.DEREGISTER, REQUEST);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    /**
     * This method sends an error message to the client.
     * The error code is defined in DefaultHelper.java
     */
    private void dereg_error(int code)
    {
        MESSAGE = SendHelper.create_send_notReg(DefaultHelper.DEREGISTER_ERROR, REQUEST, code);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    /**
     * This method takes care of removing the deregistered user from the backup file.
     */
    private synchronized void remove_from_file(String name) throws IOException
    {
        USER_DATA = new File("user_data.txt");
        File tmp = new File("utmp.txt");
        BUFFERED_READER = new BufferedReader(new FileReader(USER_DATA));
        OUT = new PrintWriter(new FileWriter(tmp), true);
        String read, trim;

        while((read = BUFFERED_READER.readLine()) != null)
        {
            trim = read.trim();
            if (trim.contains(name))
                continue;
            OUT.println(read);
        }
        BUFFERED_READER.close();
        OUT.close();
        USER_DATA.delete();
        tmp.renameTo(USER_DATA);
    }

    /**
     * The thread runs from here.
     * Nothing will happen if the data is empty.
     * Errors are outlined in the system message lines.
     */
    @Override
    public void run()
    {
        //split the data into an array of strings
        DATA = REC_DATA.split(P);

        if (DATA.length > 0)
        {
            REQUEST = DATA[1];
            NAME = DATA[2];
            IP = DATA[3];
            try
            {
                CLIENT_ADDRESS = InetAddress.getByName(IP);
            }
            catch (Exception e)
            {
                dereg_error(DefaultHelper.DEREG_ERROR_0);
                System.out.println("USER: " + NAME + " / Error: cannot deregister, invalid ip address.");
            }

            //we go through the list of users on the server
            for (int i = 0; i < USERS.size(); i++)
            {
                USER = USERS.get(i);

                //we check if the user is present
                if (USER.get_name().equals(NAME)) {
                    EXISTS = true;

                    //the user if found but we have to check if they have items for sale, this takes priority over highest bids.
                    if (has_items())
                    {
                        dereg_error(DefaultHelper.DEREG_ERROR_1);
                        System.out.println("USER: " + NAME + " / Error: cannot deregister, user still has items for sale.");
                        return;
                    }

                    //and also check if they the highest bid on any items
                    else if (has_highest_bid())
                    {
                        dereg_error(DefaultHelper.DEREG_ERROR_2);
                        System.out.println("USER: " + NAME + " / Error: cannot deregister, user still has a highest bid.");
                        return;
                    }
                }
            }
            //the user doesn't exist
            if (!EXISTS)
            {
                dereg_error(DefaultHelper.DEREG_ERROR_3);
                System.out.println("USER: " + NAME + " / Error: cannot deregister, user does not exist.");
            }
            else
            {
                //user is removed from the server, confirmation sent to client, and user removed from backup file
                USERS.remove(USER);
                dereg_success();
                try
                {
                    remove_from_file(NAME);
                }
                catch (IOException e) {}
                System.out.println("USER: " + NAME + " de-registered.");
            }
        }
        else
        {
            dereg_error(DefaultHelper.DEREG_ERROR_4);
            System.out.println("USER: " + NAME + " / Error: cannot deregister, wrong info provided.");
        }
    }
}

