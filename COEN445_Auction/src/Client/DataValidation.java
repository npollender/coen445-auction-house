/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DataValidation implements Runnable {

    static String REC_DATA;
    static String[] DATA;
    static int CODE;

    static DatagramPacket PACKET;
    static DatagramSocket SOCKET;

    static String P = "/";

    public DataValidation(DatagramPacket packet, DatagramSocket socket)
    {
        PACKET = packet;
        SOCKET = socket;
        REC_DATA = new String(PACKET.getData());
    }

    public void run()
    {
        DATA = REC_DATA.split(P);

        if (DATA.length > 0)
        {
            try
            {
                CODE = Integer.parseInt(DATA[0]);
            }
            catch (Exception e) {}
            switch (CODE)
            {
                case 0:
                {
                    new RegisterValidation(0, DATA).start();
                    break;
                }
                case 1:
                {
                    new RegisterValidation(1, DATA).start();
                    break;
                }
                case 2:
                {
                    new RegisterValidation(2, DATA).start();
                    break;
                }
                case 3:
                {
                    new RegisterValidation(3, DATA).start();
                    break;
                }
                case 4:
                {
                    new OfferValidation(0, DATA).start();
                    break;
                }
                case 5:
                {
                    new OfferValidation(1, DATA).start();
                    break;
                }
                case 6:
                {
                    new ItemsValidation(0, DATA).start();
                    break;
                }
                default:
                {
                    break;
                }
            }
        }
    }
}
