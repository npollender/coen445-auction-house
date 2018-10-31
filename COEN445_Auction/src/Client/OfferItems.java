/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class OfferItems extends Thread {

    static String DESC, MIN, NAME;
    static char CODE = 2;
    static char P = '_';

    public OfferItems(String desc, String min, String n)
    {
        DESC = desc;
        MIN = min;
        NAME = n;
    }

    @Override
    public void run()
    {
        String msg = CODE + P + Client.REQUEST + P + Client.NAME + P + NAME + P + DESC + P + MIN;

        SendHelper.send(msg);
    }
}
