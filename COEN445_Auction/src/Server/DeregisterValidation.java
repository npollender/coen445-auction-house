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

public class DeregisterValidation extends Thread {

    static String REC_DATA, REQUEST, NAME, IP, MESSAGE;
    static String[] DATA;

    static InetAddress CLIENT_ADDRESS;
    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;

    static Users USER;
    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;

    static File USER_DATA;
    static PrintWriter OUT;
    static BufferedReader BUFFERED_READER;

    static boolean EXISTS;

    static String P = "/";

    public DeregisterValidation(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        REC_DATA = rd;
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
    }

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

    private void dereg_success()
    {
        MESSAGE = SendHelper.create_send_dereg(DefaultHelper.DEREGISTER, REQUEST);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    private void dereg_error(int code)
    {
        MESSAGE = SendHelper.create_send_notReg(DefaultHelper.DEREGISTER_ERROR, REQUEST, code);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

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
        tmp.renameTo(USER_DATA);
        USER_DATA.delete();
    }

    @Override
    public void run()
    {
        DATA = REC_DATA.split(P);

        if (DATA.length == DefaultHelper.USER_INFO_SIZE_B)
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
            }

            for (int i = 0; i < USERS.size(); i++)
            {
                if (USERS.get(i).get_name().equals(NAME) && USERS.get(i).get_IP() == CLIENT_ADDRESS) {
                    USER = USERS.get(i);
                    EXISTS = true;
                    if (has_items())
                    {
                        dereg_error(DefaultHelper.DEREG_ERROR_0);
                        System.out.println("USER: " + NAME + " / Error: cannot deregister, user still has items for sale.");
                        return;
                    }
                    else if (has_highest_bid())
                    {
                        dereg_error(DefaultHelper.DEREG_ERROR_0);
                        System.out.println("USER: " + NAME + " / Error: cannot deregister, user still has a highest bid.");
                        return;
                    }
                }
            }
            if (!EXISTS)
            {
                dereg_error(DefaultHelper.DEREG_ERROR_0);
                System.out.println("USER: " + NAME + " / Error: cannot deregister, user does not exist.");
            }
            else
            {
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
            dereg_error(DefaultHelper.DEREG_ERROR_0);
            System.out.println("USER: " + NAME + " / Error: cannot deregister, wrong info provided.");
        }
    }
}

