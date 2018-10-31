/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class Register extends Thread {

    static char CODE = '0';

    Register() {}

    @Override
    public void run()
    {
        String msg = SendHelper.create_send(CODE, Client.REQUEST, Client.NAME, Client.IP, Client.PORT);
        SendHelper.send(msg);
        TimerHelper.time_start(msg);
    }
}