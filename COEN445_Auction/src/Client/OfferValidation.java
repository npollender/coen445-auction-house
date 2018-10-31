/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class OfferValidation extends Thread {

    static String[] INFO;
    static int CODE;

    public OfferValidation(String[] info, int code)
    {
        INFO = info;
        CODE = code;
    }

    @Override
    public void run()
    {
        switch(CODE)
        {
            /**
             * Case 0 is when we have an item offered successfully.
             */
            case 0:
            {}
            /**
             * Case 1 is when we have an unsuccessful offer. This should only happen if you
             * are not registered, or if invalid info is provided.
             */
            case 1:
            {}
        }
    }
}
