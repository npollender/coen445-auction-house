/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class Action implements Runnable {

    static int CODE;

    public Action(int ID)
    {
        CODE = ID;
    }

    public void run()
    {
        switch(CODE)
        {
            /**
             * Case 0 starts a thread for registration.
             */
            case 0:
            {
                new UserAction(DefaultHelper.REGISTER).start();
                break;
            }
            /**
             * Case 1 starts a thread for de-registration.
             */
            case 1:
            {
                new UserAction(DefaultHelper.DEREG).start();
                break;
            }
            /**
             * Case 2 starts a thread for a new item offered.
             */
            case 2:
            {
                new UserAction(DefaultHelper.OFFER, Client.DESC, Client.MIN, Client.NAME).start();
                break;
            }
            /**
             * Case 3 starts a thread for bidding on an item.
             */
            case 3:
            {
                new UserAction(DefaultHelper.BID, Client.ITEM, Client.BID).start();
                break;
            }
        }
    }
}
