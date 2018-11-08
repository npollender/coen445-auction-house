/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class SendDeregister extends Thread {

    String MESSAGE;

    public SendDeregister() {}

    @Override
    public void run()
    {
        MESSAGE = SendHelper.create_send_dereg(1, Client.REQUEST, Client.NAME, Client.IP);
        SendHelper.send(MESSAGE);
    }
}
