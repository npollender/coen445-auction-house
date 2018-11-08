/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class DataValidation implements Runnable {

    static int CODE;
    static String S_CODE, REC_DATA;
    static String[] DATA;
    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;
    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;

    public DataValidation(DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
        REC_DATA = new String(PACKET.getData());
        DATA = REC_DATA.split("/");
    }

    @Override
    public void run()
    {

        System.out.println("Data received from client: " + REC_DATA);

        if (DATA.length > 0)
        {
            try
            {
                S_CODE = DATA[0];
                CODE = Integer.parseInt(S_CODE);
            }
            catch (Exception e) {}
            switch (CODE)
            {
                case 0: {
                    new RegisterValidation(REC_DATA, SOCKET, PACKET, USERS, ITEMS).start();
                    break;
                }

                case 1: {
                    new DeregisterValidation(REC_DATA, SOCKET, PACKET, USERS, ITEMS).start();
                    break;
                }

                case 2: {
                    new Thread(new OfferValidation(REC_DATA, SOCKET, PACKET, USERS, ITEMS)).start();
                    break;
                }

                case 3: {
                    new Thread(new BidValidation(REC_DATA, SOCKET, PACKET, USERS, ITEMS)).start();
                    break;
                }
            }
        }
        else
            new Thread(new CodeValidation(SOCKET, PACKET)).start();
    }
}
