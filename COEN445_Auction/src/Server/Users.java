/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.InetAddress;

public class Users {

    static String NAME;
    static InetAddress IP_ADDRESS;
    static int PORT;
    static int NUMBER_OF_ITEMS = 0;

    public Users(String n, InetAddress ip, int port)
    {
        NAME = n;
        IP_ADDRESS = ip;
        PORT = port;
    }

    public String get_name()
    {
        return NAME;
    }

    public InetAddress get_IP()
    {
        return IP_ADDRESS;
    }

    public int get_port()
    {
        return PORT;
    }

    public int get_num_for_sale()
    {
        return NUMBER_OF_ITEMS;
    }

    public void increment_items()
    {
        NUMBER_OF_ITEMS++;
    }

    public void decrement_items()
    {
        NUMBER_OF_ITEMS--;
    }
}
