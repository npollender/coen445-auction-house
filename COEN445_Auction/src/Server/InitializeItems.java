/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.*;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class InitializeItems {

    static String ID, NAME, DESC, MIN, S_OWNER, S_HI_BIDDER, TIME_REM;
    static String[] DATA;
    static int ITEM_ID, MIN_BID;

    static Users USER, OWNER, HI_BIDDER;
    static Items ITEM;

    static ArrayList<Users> USERS;
    static ArrayList<Items> ITEMS;
    static DatagramSocket SOCKET;

    static File ITEM_DATA;
    static FileInputStream IN_STREAM;
    static DataInputStream DATA_STREAM;
    static BufferedReader BUFFERED_READER;

    static String P = "/";

    public InitializeItems(DatagramSocket socket, ArrayList<Users> users, ArrayList<Items> items)
    {
        SOCKET = socket;
        USERS = users;
        ITEMS = items;
    }

    public void init()
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

            while ((read = BUFFERED_READER.readLine()) != null)
            {
                DATA = read.split(P);

                if (DATA.length == 7)
                {
                    ID = DATA[0];
                    NAME = DATA[1];
                    DESC = DATA[2];
                    MIN = DATA[3];
                    S_OWNER = DATA[4];
                    S_HI_BIDDER = DATA[5];
                    TIME_REM = DATA[6];
                    ITEM_ID = Integer.parseInt(ID);
                    MIN_BID = Integer.parseInt(MIN);

                    for (int i = 0; i < USERS.size(); i++)
                    {
                        USER = USERS.get(i);
                        if (USER.get_name().equals(S_OWNER))
                        {
                            OWNER = USER;
                        }
                        if (USER.get_name().equals(S_HI_BIDDER))
                        {
                            HI_BIDDER = USER;
                        }
                    }

                    ITEM = new Items(ITEM_ID, SOCKET, OWNER, HI_BIDDER, NAME, DESC, MIN_BID, USERS);
                    ITEMS.add(ITEM);
                    DefaultHelper.ITEM_ID = ITEM_ID;
                }
            }
            DATA_STREAM.close();
        }
        catch (IOException e) {}
    }
}
