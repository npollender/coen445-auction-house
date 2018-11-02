/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;

public class InitializeUsers {

    static int PORT;
    static String NAME, IP, S_PORT;
    static String[] DATA;
    static Users USER;
    static ArrayList<Users> USERS;
    static InetAddress CLIENT_ADDRESS;

    static FileInputStream IN_STREAM;
    static DataInputStream DATA_STREAM;
    static BufferedReader BUFFERED_READER;
    static File USER_DATA;

    public InitializeUsers(ArrayList<Users> users)
    {
        USERS = users;
    }

    public void init()
    {
        try
        {
            USER_DATA = new File("user_data.txt");
            if (!USER_DATA.exists())
                USER_DATA.createNewFile();

            IN_STREAM = new FileInputStream(USER_DATA);
            DATA_STREAM = new DataInputStream(IN_STREAM);
            BUFFERED_READER = new BufferedReader(new InputStreamReader(DATA_STREAM));

            String read;

            while ((read = BUFFERED_READER.readLine()) != null)
            {
                DATA = read.split("/");

                if (DATA.length == 3)
                {
                    NAME = DATA[0];
                    IP = DATA[1];
                    CLIENT_ADDRESS = InetAddress.getByName(IP);
                    S_PORT = DATA[2];
                    PORT = Integer.parseInt(S_PORT);

                    USER = new Users(NAME, CLIENT_ADDRESS, PORT);
                    USERS.add(USER);
                }
            }
            DATA_STREAM.close();
        }
        catch (Exception e) {}
    }
}
