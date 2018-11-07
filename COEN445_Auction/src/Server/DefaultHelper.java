/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

public class DefaultHelper {

    static int PORT = 42069;
    static String SERVER = "127.0.0.1";

    static int MSG_SIZE = 300;
    static int BID_DURATION = 300000;
    static int MAX_ITEMS_FOR_SALE = 3;

    static int USER_INFO_SIZE_A = 5;
    static int USER_INFO_SIZE_B = 4;
    static int ITEM_INFO_SIZE = 6;
    static int BID_INFO_SIZE = 4;

    static int REGISTER = 0;
    static int REGISTER_ERROR = 1;
    static int DEREGISTER = 2;
    static int DEREGISTER_ERROR = 3;
    static int OFFER = 4;
    static int OFFER_ERROR = 5;
    static int BID;
    static int BID_ERROR;

    static int CODE_ERROR;

    static int ITEM_LIST;
    static int ITEM_ID = 0;

    static int REG_ERROR_0 = 0;
    static int REG_ERROR_1 = 1;
    static int REG_ERROR_2 = 2;

    static int DEREG_ERROR_0 = 0;

    static int OFFER_ERROR_0 = 0;

    public DefaultHelper() {}
}
