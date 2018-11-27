/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class TCPClient extends Thread {

    static int PORT;
    static Socket SOCKET = null;
    static DataOutputStream OUT;

    public TCPClient(int port)
    {
        PORT = port;
    }

    @Override
    public void run()
    {
        try
        {
            SOCKET = new Socket(DefaultHelper.SERVER, PORT);
            OUT = new DataOutputStream(SOCKET.getOutputStream());
        }
        catch (IOException e) {}
    }
}
