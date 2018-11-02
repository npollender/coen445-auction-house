/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class UserAction extends Thread {

    static int CODE;
    static String DESC, MIN, NAME, BID;
    static String REQUEST = Client.REQUEST;
    static String C_NAME = Client.NAME;
    static String IP = Client.IP;
    static int ID = Client.ID;
    static int PORT = Client.PORT;

    /**
     * Moved all 4 actions into a single class. Less clutter in the code, easier to access everything.
     */

    public UserAction(int code)
    {
        CODE = code;
    }

    public UserAction(int code, String desc, String min, String n)
    {
        CODE = code;
        DESC = desc;
        MIN = min;
        NAME = n;
    }

    public UserAction(int code, String n, String bid)
    {
        CODE = code;
        NAME = n;
        BID = bid;
    }

    @Override
    public void run()
    {
        String msg;

        switch(CODE)
        {
            case 0:
            {
                msg = SendHelper.create_send_reg(CODE, REQUEST, C_NAME, IP, PORT);
                SendHelper.send(msg);
                TimerHelper.time_start(msg);
                break;
            }
            case 1:
            {
                msg = SendHelper.create_send_dereg(CODE, REQUEST, C_NAME, IP);
                SendHelper.send(msg);
                break;
            }
            case 2:
            {
                msg = SendHelper.create_send_offer(CODE, REQUEST, C_NAME, NAME, DESC, MIN);
                SendHelper.send(msg);
                break;
            }
            case 3:
            {
                msg = SendHelper.create_send_bid(CODE, REQUEST, ID, BID);
                SendHelper.send(msg);
                break;
            }
        }
    }
}
