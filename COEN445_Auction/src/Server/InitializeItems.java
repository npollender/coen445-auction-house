/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.*;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Timer;

/**
 * This class is for initializing from a backup in case the server is closed.
 */
public class InitializeItems {

    private String ID, NAME, DESC, MIN, S_OWNER, S_HI_BIDDER, S_PORT;
    private String[] DATA;
    private int ITEM_ID, MIN_BID, PORT;

    private Users USER, OWNER, HI_BIDDER;
    private Items ITEM;

    private ArrayList<Users> USERS;
    private ArrayList<Items> ITEMS;
    private DatagramSocket SOCKET;

    private File ITEM_DATA;
    private FileInputStream IN_STREAM;
    private DataInputStream DATA_STREAM;
    private BufferedReader BUFFERED_READER;

    private String P = "/";

    public InitializeItems(DatagramSocket socket, ArrayList<Users> users, ArrayList<Items> items)
    {
        SOCKET = socket;
        USERS = users;
        ITEMS = items;
    }

    /**
     * The method that is called by the server to initialize the items.
     * It won't restore from backup if the option was declined in the user initialization.
     */
    public void init()
    {
        if (Server.RESTORE)
        {
            try
            {
                ITEM_DATA = new File("item_data.txt");
                if (!ITEM_DATA.exists())
                    ITEM_DATA.createNewFile();
                IN_STREAM = new FileInputStream(ITEM_DATA);
                DATA_STREAM = new DataInputStream(IN_STREAM);
                BUFFERED_READER = new BufferedReader(new InputStreamReader(DATA_STREAM));
                String read;

                while ((read = BUFFERED_READER.readLine()) != null) {
                    DATA = read.split(P);

                    if (DATA.length == 7) {
                        ID = DATA[0];
                        NAME = DATA[1];
                        DESC = DATA[2];
                        MIN = DATA[3];
                        S_OWNER = DATA[4];
                        S_HI_BIDDER = DATA[5];
                        S_PORT = DATA[6];
                        ITEM_ID = Integer.parseInt(ID);
                        MIN_BID = Integer.parseInt(MIN);
                        PORT = Integer.parseInt(S_PORT);

                        for (int i = 0; i < USERS.size(); i++) {
                            USER = USERS.get(i);
                            if (USER.get_name().equals(S_OWNER)) {
                                OWNER = USER;
                            }
                            if (USER.get_name().equals(S_HI_BIDDER)) {
                                HI_BIDDER = USER;
                            }
                        }

                        ITEM = new Items(ITEM_ID, SOCKET, OWNER, HI_BIDDER, NAME, DESC, MIN_BID, USERS, PORT);
                        ITEMS.add(ITEM);
                        DefaultHelper.ITEM_ID = ITEM_ID;

                        Timer item_bid = new Timer();
                        item_bid.schedule(new Expire(ITEM_ID, SOCKET, USERS, ITEMS), DefaultHelper.BID_DURATION);
                    }
                }
                DATA_STREAM.close();
            }
            catch (IOException e) {}
        }
        else
        {
            try
            {
                ITEM_DATA = new File("item_data.txt");
                if (!ITEM_DATA.exists())
                    ITEM_DATA.createNewFile();
                PrintWriter empty = new PrintWriter(ITEM_DATA);
                empty.close();
            }
            catch (IOException e) {}
        }
    }
}
