/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class AckValidation implements Runnable {

    private String REC_DATA, C_NAME, MESSAGE, S_PORT;
    private String[] DATA;
    private int ID, BID, PORT;

    private DatagramSocket SOCKET;
    private DatagramPacket PACKET;

    private Users USER;
    private Items ITEM;
    private ArrayList<Users> USERS;
    private ArrayList<Items> ITEMS;

    private String P = "/";

    public AckValidation(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
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

    private void ack_error()
    {
        MESSAGE = SendHelper.create_send_ack_fail(DefaultHelper.ACK_ERROR);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    private void ack_success()
    {
        MESSAGE = SendHelper.create_send_ack(DefaultHelper.ACK);
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
    }

    @Override
    public void run()
    {
        //Split the data into an array of strings
        DATA = REC_DATA.split(P);

        if (DATA.length > 0)
        {
            //assign data to corresponding variables
            C_NAME = DATA[1];
            S_PORT = DATA[2];
            PORT = Integer.parseInt(S_PORT);
            //we check if the user is registered before proceeding
            if (is_registered())
            {
                Items tmp_item;
                for (int i = 0; i < ITEMS.size(); i++)
                {
                    tmp_item = ITEMS.get(i);
                    if (tmp_item.get_port() == PORT)
                    {
                        System.out.println("Port confirmed, attempting to connect...");
                        ack_success();
                    }
                    else
                    {
                        System.out.println("Error: port does not exist");
                        ack_error();
                    }
                }
            }
            else
            {
                System.out.println("Error: user does not exist."); //impossible happen since client check is_register before sending, in case of sending error
                ack_error();
            }
        }
        else
        {
            System.out.println("Error: invalid information provided.");
            ack_error();
        }
    }
}