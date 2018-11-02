/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

import java.util.Timer;

public class TimerHelper {

    static public final int DELAY = 3000;

    static Timer TIMER = new Timer();

    static void time_start(String s)
    {

    }

    static void time_stop()
    {
        TIMER.cancel();
    }
}
