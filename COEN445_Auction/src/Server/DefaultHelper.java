/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Server;

/**
 * DefaultHelper is a helper class that contains all our default values, action codes, and error codes.
 */
public class DefaultHelper {

    /**
     * Server information.
     * The port is the server default UDP port.
     * The server is the address the client will reach the server at.
     */
    static int PORT = 42069;
    static String SERVER = "192.168.1.116";

    /**
     * This is the max message size the client/server can exchange.
     * It's unlikely we'll ever exceed this value.
     */
    static int MSG_SIZE = 500;

    /**
     * This is the default duration that an item will be available to bid on.
     */
    static long BID_DURATION = 30000; //1000 ms in 1 sec, 60 sec in 1 min, 5 min in ms = 300000

    /**
     * A user can't have more than 3 items offered at a time, so we limit them with this value.
     */
    static final int MAX_ITEMS_FOR_SALE = 3;

    /**
     * The id of all the items is tracked in here. It always defaults at 0, but is incremented whenever an item is offered.
     * Once an id is used, it will never be used again (unless server is rebooted).
     * The default value might differ if the server is being restored from the backup .txt files.
     */
    public static int ITEM_ID = 0;

    /**
     * These codes are for the client to analyze.
     * Client.DataValidation.java takes care of this and starts a new thread according to the code.
     */
    static int REGISTER = 0;
    static int REGISTER_ERROR = 1;
    static int DEREGISTER = 2;
    static int DEREGISTER_ERROR = 3;
    static int OFFER = 4;
    static int OFFER_ERROR = 5;
    static int OFFER_BROADCAST = 6;
    static int BID = 7;
    static int BID_ERROR = 8;
    static int BID_BROADCAST = 9;
    static int ITEM_LIST = 10;
    static int BID_OVER = 11;
    static int BID_SOLD_TO = 12;
    static int BID_WINNER = 13;
    static int BID_NOT_SOLD = 14;

    /**
     * These codes are for errors related to registering.
     */
    static int REG_ERROR_0 = 0;
    static int REG_ERROR_1 = 1;
    static int REG_ERROR_2 = 2;

    /**
     * These codes are for errors related to deregistering.
     */
    static int DEREG_ERROR_0 = 0;
    static int DEREG_ERROR_1 = 1;
    static int DEREG_ERROR_2 = 2;
    static int DEREG_ERROR_3 = 3;
    static int DEREG_ERROR_4 = 4;

    /**
     * These codes are for errors related to offering items.
     */
    static int OFFER_ERROR_0 = 0;
    static int OFFER_ERROR_1 = 1;
    static int OFFER_ERROR_2 = 2;

    /**
     * These codes are for errors related to bidding.
     * Some of these errors occur in BidValidation.java - these are related to invalid information.
     * The rest of the errors occur in Items.java - these are related to bid amount and bidder.
     */
    static int BID_ERROR_0 = 0;
    static int BID_ERROR_1 = 1;
    static int BID_ERROR_2 = 2;
    static int BID_ERROR_3 = 3;
    static int BID_ERROR_4 = 4;

    public DefaultHelper() {}
}
