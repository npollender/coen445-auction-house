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
             * However, the item won't be added if it already exists.
             */
            case 0:
            {
                //if check correct info
                System.out.println("Item offered successfully.");

                //create temporary item list to push
                //check for duplicate
                //add new item
                break;
            }
            /**
             * Case 1 is when we have an unsuccessful offer. This should only happen if you
             * are not registered, or if invalid info is provided.
             */
            case 1:
            {
                //if check correct info
                System.out.println("Item offer failed.");
                break;
            }
        }
    }
}
