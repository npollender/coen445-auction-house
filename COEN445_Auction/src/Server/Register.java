/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Register extends Thread {

    static String REC_DATA, REQUEST, NAME, DESC, IP, S_PORT, MESSAGE;
    static String[] DATA;
    static int PORT, ID;

    static InetAddress CLIENT_ADDRESS;
    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;

    static Users USER;
    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;

    static File USER_DATA;
    static PrintWriter OUT;

    static String P = "/";

    public Register(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        REC_DATA = rd;
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
    }

    private boolean user_exists(String name)
    {
        for (int i = 0; i < USERS.size(); i++)
        {
            Users tmp_user = USERS.get(i);
            String tmp_name = tmp_user.get_name();
            if (tmp_name == name)
                return true;
        }
        return false;
    }

    private synchronized void write_to_file()
    {
        try
        {
            USER_DATA = new File("user_data.txt");
            OUT = new PrintWriter(new FileWriter(USER_DATA), true);
            OUT.println(NAME + P + IP + P + PORT);
            OUT.close();
        }
        catch (Exception e) {}
    }

    private void already_exists()
    {
        //MESSAGE = SendHelper.create_send_notReg(???, REQUEST, ???);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    private void wrong_info(int code)
    {
        //MESSAGE = SendHelper.create_send_notReg(???, REQUEST, code);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    private void success()
    {
        //MESSAGE = SendHelper.create_send_reg(???, REQUEST, user's name, users ip, PORT);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    private void send_all_items()
    {
        Items tmp_item;
        for (int i = 0; i < ITEMS.size(); i++)
        {
            tmp_item = ITEMS.get(i);
            //MESSAGE = SendHelper.create_send_sales(???, ID, NAME, DESC, tmp_item.get_highest_bid());
            SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
        }
    }

    /**
     * Need to add messages that client will receive...
     * add user to backup file for users
     */

    @Override
    public void run()
    {
        //receive the data

        if (DATA == DATA) //this isn't the right check
        {
            //set all the data values to register class values
            try
            {
                //set the port here
                //set the client address here
            }
            catch (Exception e) {} //won't work if not registered

            //set the user
        }

    }
}
