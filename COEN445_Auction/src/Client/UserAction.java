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
    static int PORT = Client.PORT;

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
        Items tmp_items = new Items();
        tmp_items = (Items) Client.ITEMS.get(Integer.parseInt(NAME));
        int id = tmp_items.get_id();

        if (CODE == 0)
        {
            msg = SendHelper.create_send_reg(CODE, REQUEST, C_NAME, IP, PORT);
            SendHelper.send(msg);
            TimerHelper.time_start(msg);
        }
        else if (CODE == 1)
        {
            msg = SendHelper.create_send_reg(CODE, REQUEST, C_NAME, IP, PORT);
            SendHelper.send(msg);
        }
        else if (CODE == 2)
        {
            msg = SendHelper.create_send_offer(CODE, REQUEST, C_NAME, NAME, DESC, MIN);
            SendHelper.send(msg);
        }
        else if (CODE == 3)
        {
            msg = SendHelper.create_send_bid(CODE, REQUEST, C_NAME, id, BID);
            SendHelper.send(msg);
        }
        else
            System.out.println("Something went wrong.");
    }
}
