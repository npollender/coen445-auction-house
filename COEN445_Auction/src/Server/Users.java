/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.InetAddress;

/**
 * This is our users information...
 * Each user has their own unique name, ip, port.
 * Users also have a value associated to the number of items they currently have for sale.
 */
public class Users {

    private String NAME;
    private InetAddress IP_ADDRESS;
    private int PORT;
    private int NUMBER_OF_ITEMS;

    public Users(String n, InetAddress ip, int port, int number_of_items)
    {
        NAME = n;
        IP_ADDRESS = ip;
        PORT = port;
        NUMBER_OF_ITEMS = number_of_items;
    }

    public Users(Users user)
    {
        this(user.get_name(), user.get_IP(), user.get_port(), user.get_num_for_sale());
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
