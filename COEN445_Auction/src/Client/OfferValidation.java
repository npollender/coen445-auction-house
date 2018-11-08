/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class OfferValidation extends Thread {

    static String REQUEST = Client.REQUEST;
    static String[] DATA;
    static int CODE;

    public OfferValidation(int code, String[] data)
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
             * Case 0 is when we have an item offered successfully.
             * The item is added to the client using the data.
             * Like always, we check the request before proceeding.
             */
            case 0:
            {
                if (DATA[1].equals(REQUEST))
                {
                    System.out.println("Item has been offered with an ID of " + DATA[2] + ".");
                }
                break;
            }
            /**
             * Case 1 is when we have an unsuccessful offer.
             * The client will be given the reason why.
             */
            case 1:
            {
                if (DATA[1].equals(REQUEST))
                {
                    /**
                     * Two reasons why offers fail.
                     * Wrong info provided, or user is not registered.
                     */
                    if (DATA[2].equals(DefaultHelper.OFFER_FAILED_0))
                    {
                        System.out.println("Offer failed, wrong information provided.");
                    }
                    if (DATA[2].equals(DefaultHelper.OFFER_FAILED_1))
                    {
                        System.out.println("Offer failed, you are not registered.");
                    }
                    if (DATA[2].equals(DefaultHelper.OFFER_FAILED_2))
                    {
                        System.out.println("Offer failed, you've exceeded your item limit.");
                    }
                }
                break;
            }
        }
    }
}
