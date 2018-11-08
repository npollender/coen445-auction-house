/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Server {

    static int PORT;
    static String SERVER, IP;
    static InetAddress CLIENT_ADDRESS, IP_ADDRESS;

    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;

    static ArrayList<Users> USERS = new ArrayList<Users>();
    static ArrayList<Items> ITEMS = new ArrayList<Items>();

    public static void HOLDUP(int time)
    {
        try
        {
            TimeUnit.SECONDS.sleep(time);
        }
        catch (InterruptedException e) {}
    }

    public static void main(String args[]) throws Exception
    {
        PORT = DefaultHelper.PORT;
        SERVER = DefaultHelper.SERVER;
        SOCKET = new DatagramSocket(PORT);
        CLIENT_ADDRESS = InetAddress.getByName(SERVER);
        IP_ADDRESS = InetAddress.getLocalHost();
        IP = IP_ADDRESS.getHostAddress();

        InitializeUsers init_users = new InitializeUsers(USERS);
        init_users.init();

        InitializeItems init_items = new InitializeItems(SOCKET, USERS, ITEMS);
        init_items.init();

        while (true)
        {
            byte[] data = new byte[DefaultHelper.MSG_SIZE];
            PACKET = new DatagramPacket(data, data.length);
            SOCKET.receive(PACKET);
            new Thread(new DataValidation(SOCKET, PACKET, USERS, ITEMS)).start();
        }
    }
}
