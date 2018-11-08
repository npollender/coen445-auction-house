/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class SendBid extends Thread {

    String MESSAGE;

    public SendBid() {}

    @Override
    public void run()
    {
        MESSAGE = SendHelper.create_send_bid(3, Client.REQUEST, Client.ID, Client.BID);
        SendHelper.send(MESSAGE);
    }
}
