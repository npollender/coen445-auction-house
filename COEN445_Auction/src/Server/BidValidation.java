/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class BidValidation implements Runnable {

    static String REC_DATA, REQUEST, NAME, IP, S_PORT;
    static String[] DATA;
    static int PORT;

    static InetAddress CLIENT_ADDRESS;
    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;

    static Users USER;
    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;

    public BidValidation(String rd, DatagramSocket socket, DatagramPacket packet, ArrayList<Users> users, ArrayList<Items> items)
    {
        REC_DATA = rd;
        SOCKET = socket;
        PACKET = packet;
        USERS = users;
        ITEMS = items;
    }

    /**
     * send message for client if the bid fails
     */

    @Override
    public void run()
    {

    }
}
