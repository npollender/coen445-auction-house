/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.io.IOException;
import java.net.Socket;

public class AckValidation extends Thread {

    static String[] DATA;
    static int CODE;
    static Socket SOCKET;

    public AckValidation(int code, String[] data)
    {
        DATA = data;
        CODE = code;
    }

    public static void TCP_connect(int port) throws IOException
    {
        SOCKET = new Socket(DefaultHelper.SERVER, port);
    }

    @Override
    public void run()
    {
        switch(CODE)
        {
            /**
             * Case 0 is to indicate a successful ack.
             */
            case 0:
            {
                Client.textArea.append("TCP request successful!" + Client.nextLine);
                Client.IS_TCP = true;
                new Thread(new TCPClient(Client.PORT)).start();
                break;
            }
            /**
             * Case 1 is to indicate an error with ack, resend ack request.
             */
            case 1:
            {
                Client.textArea.append("TCP Connection failed." + Client.nextLine);
                break;
            }
        }
    }
}
