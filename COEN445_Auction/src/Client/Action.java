/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class Action implements Runnable {

    static private String USERNAME, SERVER, REQUEST, IP;
    static private int PORT;
    static int SEL;

    public Action(int ID)
    {
        SEL = ID;
    }

    public void run()
    {
        switch(SEL)
        {
            /**
             * Case 0 starts a thread for registration.
             */
            case 0:
                new Register().start();
            break;
            /**
             * Case 1 starts a thread for de-registration.
             */
            case 1:
                new Deregister().start();
            break;
            /**
             * Case 2 starts a thread for a new item offered.
             */
            case 2:
                new OfferItems(Client.DESC, Client.MIN, Client.NAME).start();
            break;
            /**
             * Case 3 starts a thread for bidding on an item.
             */
            case 3:
                new BidOnItems(Client.ITEM, Client.BID).start();
            break;
        }
    }
}
