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

    static boolean RESTORE;

    public static void main(String args[]) throws Exception
    {
        int PORT = DefaultHelper.PORT;
        String SERVER = DefaultHelper.SERVER;
        DatagramSocket SOCKET = new DatagramSocket(PORT);
        InetAddress CLIENT_ADDRESS = InetAddress.getByName(SERVER);
        InetAddress IP_ADDRESS = InetAddress.getLocalHost();
        String IP = IP_ADDRESS.getHostAddress();

        ArrayList<Users> USERS = new ArrayList<Users>();
        ArrayList<Items> ITEMS = new ArrayList<Items>();

        InitializeUsers init_users = new InitializeUsers(USERS);
        init_users.init();

        InitializeItems init_items = new InitializeItems(SOCKET, USERS, ITEMS);
        init_items.init();

        System.out.println("Server started. Data exchange will be displayed here.");

        while (true)
        {
            byte[] data = new byte[DefaultHelper.MSG_SIZE];
            DatagramPacket PACKET = new DatagramPacket(data, data.length);
            SOCKET.receive(PACKET);
            new Thread(new DataValidation(SOCKET, PACKET, USERS, ITEMS)).start();
        }
    }
}
