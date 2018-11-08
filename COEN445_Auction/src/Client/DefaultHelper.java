/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class DefaultHelper {

    static int PORT = 42069;
    static String SERVER = "127.0.0.1";
    static int MSG_SIZE = 300;

    /**
     * User Action id's
     */
    static int REGISTER = 0;
    static int DEREG = 1;
    static int OFFER = 2;
    static int BID = 3;

    /**
     * Registration error id's
     */
    static String REG_FAILED_0 = "0";
    static String REG_FAILED_1 = "1";
    static String REG_FAILED_2 = "2";

    /**
     * De-registration error id's
     */
    static String DEREG_FAILED_0 = "0";
    static String DEREG_FAILED_1 = "1";
    static String DEREG_FAILED_2 = "2";
    static String DEREG_FAILED_3 = "3";

    /**
     * Offer error id's
     */
    static String OFFER_FAILED_0 = "0";
    static String OFFER_FAILED_1 = "1";
    static String OFFER_FAILED_2 = "2";

    /**
     * Bid error id's
     */
    static String BID_FAILED_0 = "0";
    static String BID_FAILED_1 = "1";
    static String BID_FAILED_2 = "2";
    static String BID_FAILED_3 = "3";
    static String BID_FAILED_4 = "4";

    public DefaultHelper() {}
}
