/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.util.Timer;

public class TimerHelper {

    static public final int DELAY = 3000;

    static Timer TIMER = new Timer();

    static void time_start(String s)
    {
        TIMER.schedule(new Timeout(s), DELAY);
    }

    static void time_stop()
    {
        TIMER.cancel();
    }
}
