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
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * This class concerns everything to do with registering on the server.
 */
public class RegisterValidation extends Thread {

    private String REC_DATA, REQUEST, NAME, DESC, IP, S_PORT, MESSAGE;
    private String[] DATA;
    private int PORT, ID;

    private InetAddress CLIENT_ADDRESS;
    private DatagramSocket SOCKET;
    private DatagramPacket PACKET;

    private Users USER;
    private ArrayList<Users> USERS;
    private ArrayList<Items> ITEMS;

    private File USER_DATA;
    private PrintWriter OUT;

    private String P = "/";

    public RegisterValidation(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        REC_DATA = rd;
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
    }

    /**
     * We send a message back to the client when they have successfully registered on the server.
     */
    private void reg_success()
    {
        MESSAGE = SendHelper.create_send_reg(DefaultHelper.REGISTER, REQUEST, USER.get_name(), USER.get_IP().getHostAddress(), PORT);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    /**
     * We send an error back to the client when there is an error with registering.
     */
    private void reg_error(int code)
    {
        MESSAGE = SendHelper.create_send_notReg(DefaultHelper.REGISTER_ERROR, REQUEST, code);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    /**
     * Once a user is registered successfully, they are sent a list of items that a currently for sale on the server.
     */
    private void send_all_items()
    {
        Items tmp_item;
        for (int i = 0; i < ITEMS.size(); i++)
        {
            tmp_item = ITEMS.get(i);
            String new_desc = tmp_item.get_name() + ": " + DESC;
            MESSAGE = SendHelper.create_send_existing_items(DefaultHelper.ITEM_LIST, ID, new_desc, tmp_item.get_highest_bid());
            SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
        }
    }

    /**
     * User backup is updated when a user is registered.
     */
    private synchronized void write_to_file() throws IOException
    {
        USER_DATA = new File("user_data.txt");
        OUT = new PrintWriter(new FileWriter(USER_DATA, true));
        OUT.println(NAME + P + IP + P + PORT + P + USER.get_num_for_sale());
        OUT.close();
    }

    /**
     * The thread runs from here.
     * Errors are outlined by system message lines.
     */
    @Override
    public void run()
    {
        DATA = REC_DATA.split(P);

        if (DATA.length > 0)
        {
            REQUEST = DATA[1];
            NAME = DATA[2];
            IP = DATA[3];
            S_PORT = DATA[4];
            try
            {
                CLIENT_ADDRESS = InetAddress.getByName(IP);
            }
            catch (Exception e)
            {
                reg_error(DefaultHelper.REG_ERROR_1);
                System.out.println("USER: " + NAME + " / Error: cannot register, invalid IP Address.");
                return;
            }
            try
            {
                PORT = Integer.parseInt(S_PORT);
            }
            catch (Exception e)
            {
                reg_error(DefaultHelper.REG_ERROR_1);
                System.out.println("USER: " + NAME + " / Error: cannot register, invalid port number.");
                return;
            }

            USER = new Users(NAME, PACKET.getAddress(), PACKET.getPort(), 0);

            //first we have to check if the user exists already
            boolean exists = false;
            for (int i = 0; i < USERS.size(); i++)
            {
                if (USER == USERS.get(i))
                {
                    exists = true;
                    break;
                }
            }

            //if the user doesnt exist then we can add them to the server
            if (!exists)
            {
                USERS.add(new Users(USER));
                reg_success();
                send_all_items();
                try
                {
                    write_to_file();
                }
                catch (IOException e) {}
                System.out.println("USER: " + NAME + " registered.");
            }
            else
            {
                reg_error(DefaultHelper.REG_ERROR_2);
                System.out.println("USER: " + NAME + " / Error: cannot register, user already exists.");
            }
        }
        else
        {
            reg_error(DefaultHelper.REG_ERROR_0);
            System.out.println("USER: " + NAME + " / Error: cannot register, invalid information provided.");
        }
    }
}
