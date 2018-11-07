/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CodeValidation implements Runnable {

    static String MESSAGE, CODE;
    static String[] DATA;
    static int PORT;
    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;
    static InetAddress CLIENT_ADDRESS;

    static String P = "/";

    public CodeValidation(DatagramSocket socket, DatagramPacket packet)
    {
        SOCKET = socket;
        PACKET = packet;
        DATA = (new String(PACKET.getData())).split(P);
    }

    @Override
    public void run()
    {
        if (!DATA[1].isEmpty())
        {
            CODE = DATA[1];
        }
        else
        {
            CODE = "0";
        }
        MESSAGE = DefaultHelper.CODE_ERROR + P + CODE;
        SendHelper.send(MESSAGE, PACKET.getAddress(), PACKET.getPort(), SOCKET);
        System.out.println("code got fucked somewhere");
    }
}
