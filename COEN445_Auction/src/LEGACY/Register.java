/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018

package LEGACY;

import Client.Client;
import Client.SendHelper;

public class Register extends Thread {

    static int CODE = 0;

    Register() {}

    @Override
    public void run()
    {
        String msg = SendHelper.create_send_reg(CODE, Client.REQUEST, Client.NAME, Client.IP, Client.PORT);
        SendHelper.send(msg);
        TimerHelper.time_start(msg);
    }
} */