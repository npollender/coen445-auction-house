/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * This class takes the data received from the client and runs threads according to the data.
 */
public class DataValidation implements Runnable {

    private int CODE;
    private String S_CODE, REC_DATA;
    private String[] DATA;
    private ArrayList<Users> USERS;
    private ArrayList<Items> ITEMS;
    private DatagramSocket SOCKET;
    private DatagramPacket PACKET;

    public DataValidation(DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
        REC_DATA = new String(PACKET.getData());
        DATA = REC_DATA.split("/");
    }

    /**
     * The thread runs from here.
     * We have 4 possible outcomes listed below.
     * A fifth outcome exists if the data gets screwed up somehow.
     */
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
                case 0:
                {
                    new RegisterValidation(REC_DATA, SOCKET, PACKET, USERS, ITEMS).start();
                    break;
                }

                case 1:
                {
                    new DeregisterValidation(REC_DATA, SOCKET, PACKET, USERS, ITEMS).start();
                    break;
                }

                case 2:
                {
                    new Thread(new OfferValidation(REC_DATA, SOCKET, PACKET, USERS, ITEMS)).start();
                    break;
                }

                case 3:
                {
                    new Thread(new BidValidation(REC_DATA, SOCKET, PACKET, USERS, ITEMS)).start();
                    break;
                }
                default:
                {
                    System.out.println("Something went wrong with the client that sent this message... Sorry!");
                    break;
                }
            }
        }
    }
}
