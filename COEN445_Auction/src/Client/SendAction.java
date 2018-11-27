/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class SendAction extends Thread {

    String MESSAGE;
    int CODE;

    public SendAction(int code) {CODE = code;}

    @Override
    public void run()
    {
        switch (CODE)
        {
            case 0:
            {
                MESSAGE = SendHelper.create_send_reg(0, Client.REQUEST, Client.NAME, Client.IP, Client.PORT);
                SendHelper.send(MESSAGE);
                break;
            }
            case 1:
            {
                MESSAGE = SendHelper.create_send_dereg(1, Client.REQUEST, Client.NAME, Client.IP);
                SendHelper.send(MESSAGE);
                break;
            }
            case 2:
            {
                MESSAGE = SendHelper.create_send_offer(2, Client.REQUEST, Client.NAME, Client.ITEM_NAME, Client.DESC, Client.MIN);
                SendHelper.send(MESSAGE);
                break;
            }
            case 3:
            {
                MESSAGE = SendHelper.create_send_bid(3, Client.REQUEST, Client.ID, Client.BID);
                SendHelper.send(MESSAGE);
                break;
            }
            case 4:
            {
                MESSAGE = SendHelper.create_ack(4, Client.NAME, Client.ITEM_PORT);
                SendHelper.send(MESSAGE);
                break;
            }
        }
    }
}
