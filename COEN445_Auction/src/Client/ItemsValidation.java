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
            {
                //check for dupes
                //add item to list
                //if dupe, do nothing
                break;
            }
            /**
             * Case 1 is when an existing item has its price updated.
             */
            case 1:
            {
                Items tmp_items = new Items();
                for (int i = 0; i < Client.ITEMS.size(); i++)
                {
                    tmp_items = (Items) Client.ITEMS.get(i);
                    //if info is correct...
                    //remove old item price, add new item price.
                }
                break;
            }
            /**
             * Case 2 is when an item is won.
             */
            case 2:
            {
                System.out.println("You won an item!");
                //should probably get the item name and include in the msg
            }
            /**
             * Case 3 is when an item is sold to another user.
             */
            case 3:
            {
                System.out.println("Someone won an item!");
                //same thing as case 2
            }
            /**
             * Case 4 is when the bidding period is over.
             */
            case 4:
            {
                Items tmp_items = new Items();
                System.out.println("Bidding is over for an item!");
                for (int i = 0; i < Client.ITEMS.size(); i++)
                {
                    tmp_items = (Items) Client.ITEMS.get(i);
                    //gotta find the correct item
                    //remove the item, break out of loop
                }
                break;
            }
            /**
             * Case 5 empties the item list.
             */
            case 5:
            {
                Client.ITEMS.clear();
                break;
            }
        }
    }
}
