/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class ItemsValidation extends Thread {

    String[] INFO;
    int CODE;

    public ItemsValidation(String[] info, int code)
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
             * Case 0 is when an item is added to the item list.
             */
            case 0:
            {}
            /**
             * Case 1 is when an existing item has its price updated.
             */
            case 1:
            {}
            /**
             * Case 2 is when an item is won.
             */
            case 2:
            {}
            /**
             * Case 3 is when an item is sold.
             */
            case 3:
            {}
            /**
             * Case 4 is when the bidding period is over.
             */
            case 4:
            {}
            /**
             * Case 5 empties the item list.
             */
            case 5:
            {}
        }
    }
}
