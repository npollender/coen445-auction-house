/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class SendRegister extends Thread {

    String MESSAGE;

    public SendRegister() {}

    @Override
    public void run()
    {
        MESSAGE = SendHelper.create_send_reg(0, Client.REQUEST, Client.NAME, Client.IP, Client.PORT);
        SendHelper.send(MESSAGE);
        //TimerHelper.time_start(MESSAGE);
    }
}
