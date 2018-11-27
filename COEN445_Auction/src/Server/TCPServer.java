/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class TCPServer extends Thread {

    private int PORT;
    private static ServerSocket SOCKET = null;
    private DataOutputStream OUT;

    public TCPServer(int port)
    {
        PORT = port;
    }

    public static void Close(int port)
    {
        try
        {
            SOCKET.close();
        }
        catch (IOException e) {}
        System.out.println("TCP connection closed on port #" + port + ".");
    }

    @Override
    public void run()
    {
        try
        {
            SOCKET = new ServerSocket(PORT);
        }
        catch (IOException e) {}
        System.out.println("TCP socket opened on port #" + PORT + ".");
        while (true)
        {
            try
            {
                Socket item_socket = SOCKET.accept();
                OUT = new DataOutputStream(item_socket.getOutputStream());
            }
            catch (IOException e) {}
        }
    }
}
