/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Client {

    static int PORT, SEL;
    static InetAddress SERVER_ADDRESS, IP_ADDRESS;
    static DatagramSocket SOCKET;
    static String SERVER, NAME, REQUEST, IP, DESC, MIN, ITEM, ITEM_NAME, BID;
    static ArrayList ITEMS = new ArrayList<Items>();
    static boolean IS_REGISTERED;

    public Client() {}

    public void empty_items()
    {
        ITEMS.clear();
    }

    public static void main(String args[]) throws IOException
    {
        Configuration CONFIG = new Configuration();
        CONFIG.write_file();
        PORT = SendHelper.PORT;
        SERVER = SendHelper.SERVER;
        SOCKET = new DatagramSocket(PORT);
        SERVER_ADDRESS = InetAddress.getByName(SERVER);
        IP_ADDRESS = InetAddress.getLocalHost();
        IP = IP_ADDRESS.getHostAddress();

        /**
         * We need to add a way to call methods.
         *
         * These should be added inside the loop, so we are always looking for an
         * input from the user.
         */
        while (true)
        {
            try
            {
                byte[] msg = new byte[SendHelper.MSG_SIZE];
                DatagramPacket PACKET = new DatagramPacket(msg, msg.length);
                SOCKET.receive(PACKET);
                new Thread(new DataValidation(PACKET, SOCKET)).start();
            }
            catch (IOException io) {}
        }
    }
}
