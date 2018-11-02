/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class ItemsValidation extends Thread {

    static String[] DATA;
    static int CODE;

    public ItemsValidation(int code, String[] data)
    {
        DATA = data;
        CODE = code;
    }

    @Override
    public void run()
    {
        switch(CODE)
        {
            /**
             * Case 0 is when an item is added to the auction.
             * The client is notified of the new item.
             */
            case 0:
            {
                System.out.println("New item added to the auction!");
                System.out.println("ID: " + DATA[1] + " - Description: " + DATA[2]);
                break;
            }
            /**
             * Case 1 is when an existing item has its price updated.
             * The client is notified of the new high bid.
             */
            case 1:
            {
                System.out.println("New highest bid of " + DATA[2] + "$ on item #" + DATA[1]);
            }
            /**
             * Case 2 is when an item is won.
             * The client that won the item receives this message.
             */
            case 2:
            {
                System.out.println("Congratulations, you won item #" + DATA[1] + " for " + DATA[5] + "$.");
            }
            /**
             * Case 3 is when the bidding period for an item you bid on is over (and you didn't win).
             * All clients that bid, but did not win, will be notified that the bidding period is over for
             * that item.
             */
            case 3:
            {
                System.out.println("The bidding period for item #" + DATA[1] + " is over!");
            }
            /**
             * Case 4 is when the server notifies the original item owner that their item has been sold.
             * Only the owner of the item will be notified.
             */
            case 4:
            {
                System.out.println("Your item #" + DATA[1] + " has been sold to " + DATA[2] + " for " + DATA[5] + "$.");
            }
            /**
             * Case 5 is when the server notifies the original owner that their item has not been sold.
             * Only the owner of the item will be notified.
             */
            case 5:
            {
                System.out.println("You item #" + DATA[1] + " has not been sold. Maybe don't overprice it next time.");
            }
        }
    }
}
