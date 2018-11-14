/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * For initializing from a backup after the server is closed.
 */
public class InitializeUsers {

    private int PORT, NUMBER_OF_ITEMS;
    private String NAME, IP, S_PORT;
    private String[] DATA;
    private Users USER;
    private ArrayList<Users> USERS;
    private InetAddress CLIENT_ADDRESS;

    private FileInputStream IN_STREAM;
    private DataInputStream DATA_STREAM;
    private BufferedReader BUFFERED_READER;
    private File USER_DATA;

    private String P = "/";

    public InitializeUsers(ArrayList<Users> users)
    {
        USERS = users;
    }

    /**
     * This method initializes the users from the backup.
     */
    public void init()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Restore from backup? (Y/N)");
        String sel = input.nextLine();

        //we ask if we are starting from scratch or from the backup files
        if (sel.equals("Y") || sel.equals("y"))
        {
            Server.RESTORE = true;
            try
            {
                USER_DATA = new File("user_data.txt");
                if (!USER_DATA.exists())
                    USER_DATA.createNewFile();

                IN_STREAM = new FileInputStream(USER_DATA);
                DATA_STREAM = new DataInputStream(IN_STREAM);
                BUFFERED_READER = new BufferedReader(new InputStreamReader(DATA_STREAM));

                String read;

                while ((read = BUFFERED_READER.readLine()) != null) {
                    DATA = read.split(P);

                    if (DATA.length == 4) {
                        NAME = DATA[0];
                        IP = DATA[1];
                        CLIENT_ADDRESS = InetAddress.getByName(IP);
                        S_PORT = DATA[2];
                        PORT = Integer.parseInt(S_PORT);
                        NUMBER_OF_ITEMS = Integer.parseInt(DATA[3]);

                        USER = new Users(NAME, CLIENT_ADDRESS, PORT, NUMBER_OF_ITEMS);
                        USERS.add(new Users(USER));
                    }
                }
                DATA_STREAM.close();
            }
            catch (Exception e) {}
        }
        else
        {
            Server.RESTORE = false;
            try
            {
                USER_DATA = new File("user_data.txt");
                if (!USER_DATA.exists())
                    USER_DATA.createNewFile();
                PrintWriter empty = new PrintWriter(USER_DATA);
                empty.close();
            }
            catch (IOException e) {}
        }
    }
}
