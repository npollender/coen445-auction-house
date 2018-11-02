/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018

package LEGACY;

import Client.Client;
import Client.Items;

public class BidOnItems extends Thread {

    static String NAME, BID;
    static char CODE = '3';
    static char P = '_';

    public BidOnItems(String n, String bid)
    {
        NAME = n;
        BID = bid;
    }

    @Override
    public void run()
    {
        Items tmp = new Items();
        tmp = (Items) Client.ITEMS.get(Integer.parseInt(NAME));

        String msg = CODE + P + Client.REQUEST + P + Client.NAME + P + tmp.get_id() + P + BID;

        SendHelper.send(msg);
    }
} */
