/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.util.ArrayList;

public class ServerData implements Runnable {

    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;

    public ServerData(ArrayList<Users> users, ArrayList<Items> items)
    {
        USERS = users;
        ITEMS = items;
    }

    @Override
    public void run()
    {
        while (true)
        {
            //for viewing users/items currently on the server.
        }
    }
}
