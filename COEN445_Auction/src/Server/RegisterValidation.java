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

public class RegisterValidation extends Thread {

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

    public RegisterValidation(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        REC_DATA = rd;
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
    }

    private boolean user_exists(String name)
    {
        Users tmp_user;
        for (int i = 0; i < USERS.size(); i++)
        {
            tmp_user = USERS.get(i);
            String tmp_name = tmp_user.get_name();
            if (tmp_name.equals(name))
                return true;
        }
        return false;
    }

    private void reg_success()
    {
        MESSAGE = SendHelper.create_send_reg(DefaultHelper.REGISTER, REQUEST, USER.get_name(), USER.get_IP().getHostAddress(), PORT);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    private void reg_error(int code)
    {
        MESSAGE = SendHelper.create_send_notReg(DefaultHelper.REGISTER_ERROR, REQUEST, code);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    private void send_all_items()
    {
        Items tmp_item;
        for (int i = 0; i < ITEMS.size(); i++)
        {
            tmp_item = ITEMS.get(i);
            MESSAGE = SendHelper.create_send_existing_items(DefaultHelper.ITEM_LIST, ID, DESC, tmp_item.get_highest_bid());
            SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
        }
    }

    private synchronized void write_to_file() throws IOException
    {
            USER_DATA = new File("user_data.txt");
            OUT = new PrintWriter(new FileWriter(USER_DATA), true);
            OUT.println(NAME + P + IP + P + PORT + P + USER.get_num_for_sale());
            OUT.close();
    }

    @Override
    public void run()
    {
        DATA = REC_DATA.split(P);

        if (DATA.length == DefaultHelper.USER_INFO_SIZE_A)
        {
            REQUEST = DATA[1];
            NAME = DATA[2];
            IP = DATA[3];
            S_PORT = DATA[4];
            PORT = Integer.parseInt(S_PORT);
            try
            {
                CLIENT_ADDRESS = InetAddress.getByName(IP);
            }
            catch (Exception e)
            {
                reg_error(DefaultHelper.REG_ERROR_2);
                System.out.println("USER: " + NAME + " / Error: cannot register, invalid IP Address.");
                return;
            }

            USER = new Users(NAME, CLIENT_ADDRESS, PORT);

            if (!user_exists(NAME))
            {
                USERS.add(USER);
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
                reg_error(DefaultHelper.REG_ERROR_0);
                System.out.println("USER: " + NAME + " / Error: cannot register, user already exists.");
            }
        }
        else
        {
            reg_error(DefaultHelper.REG_ERROR_1);
            System.out.println("USER: " + NAME + " / Error: cannot register, invalid information provided.");
        }
    }
}
