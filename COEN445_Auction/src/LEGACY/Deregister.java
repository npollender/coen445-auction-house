/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018

package LEGACY;

import Client.Client;
import Client.SendHelper;

public class Deregister extends Thread {

    static int CODE = 1;

    Deregister() {}

    @Override
    public void run()
    {
        String msg = SendHelper.create_send_reg(CODE, Client.REQUEST, Client.NAME, Client.IP, Client.PORT);
        SendHelper.send(msg);
    }
} */