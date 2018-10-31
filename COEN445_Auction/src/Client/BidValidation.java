/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class BidValidation extends Thread{

    static String[] INFO;
    static int CODE;

    public BidValidation(String[] info, int code)
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
             * The only case here is for an unsuccessful bid. If any of the following
             * occur, then the bid is unsuccessful.
             * A successful bid is determined in ItemsValidation.java
             */
            case 0:
            {
                System.out.println("Bid failed.");
                break;
            }
        }
    }
}
