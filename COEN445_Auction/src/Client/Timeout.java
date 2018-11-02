/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.util.TimerTask;

public class Timeout extends TimerTask {

    static String MSG;

    public Timeout(String s)
    {
        MSG = s;
    }

    public void run()
    {
        System.out.println("Oops, looks like there was a timeout... Retrying...");
        SendHelper.send(MSG);
    }
}
