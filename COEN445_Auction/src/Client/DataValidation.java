/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DataValidation implements Runnable {

    private DatagramPacket PACKET;
    private DatagramSocket SOCKET;

    public DataValidation(DatagramPacket packet, DatagramSocket socket)
    {
        PACKET = packet;
        SOCKET = socket;
    }

    public void run()
    {
        /**
         * Need cases for registration, de-registration, item offer, item bidding,
         * item management...
         *
         * We also need a case to deal with a potential server crash...
         */
    }
}
