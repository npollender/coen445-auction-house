/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class SendOffer extends Thread {

    String MESSAGE;

    public SendOffer() {}

    @Override
    public void run()
    {
        MESSAGE = SendHelper.create_send_offer(2, Client.REQUEST, Client.NAME, Client.ITEM_NAME, Client.DESC, Client.MIN);
        SendHelper.send(MESSAGE);
    }
}
