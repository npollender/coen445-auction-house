/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class BidValidation extends Thread{

    static String REQUEST = Client.REQUEST;
    static String[] DATA;
    static int CODE;

    public BidValidation(int code, String[] data)
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
             * Case 0 is when there is an unsuccessful bid.
             * (A successful bid is determined in items validation class.)
             * There are a number of reasons why a bid can fail. First we ensure that the request sent by the server
             * is correct...
             */
            case 0:
            {
                if (DATA[1].equals(REQUEST))
                {
                    /**
                     * We have five types of errors.
                     * Wrong info provided, user not registered, item doesn't exist, bid too low, bidding on own item.
                     */
                    if (DATA[2].equals(DefaultHelper.BID_FAILED_0))
                    {
                        System.out.println("Bid failed, wrong information provided.");
                    }
                    if (DATA[2].equals(DefaultHelper.BID_FAILED_1))
                    {
                        System.out.println("Bid failed, you are not registered.");
                    }
                    if (DATA[2].equals(DefaultHelper.BID_FAILED_2))
                    {
                        System.out.println("Bid failed, that item is no longer available or does not exist.");
                    }
                    if (DATA[2].equals(DefaultHelper.BID_FAILED_3))
                    {
                        System.out.println("Bid failed, your bid is too low. Consider selling your car for more funds.");
                    }
                    if (DATA[2].equals(DefaultHelper.BID_FAILED_4))
                    {
                        System.out.println("Bid failed, you can't bid on your own item. You're not fooling anybody.");
                    }
                }
                break;
            }
        }
    }
}
