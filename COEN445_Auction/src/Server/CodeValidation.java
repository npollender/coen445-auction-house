/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CodeValidation implements Runnable {

    static String REC_DATA;
    static String[] DATA;
    static int PORT;
    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;
    static InetAddress CLIENT_ADDRESS;

    public CodeValidation(DatagramSocket socket, DatagramPacket packet)
    {
        SOCKET = socket;
        PACKET = packet;
        CLIENT_ADDRESS = PACKET.getAddress();
        PORT = PACKET.getPort();
    }

    @Override
    public void run()
    {
        //if data is valid it will add the info
        //if data is invalid, add error bit
        //send data
    }
}
