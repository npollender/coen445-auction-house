/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.io.Console;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    static int PORT, CODE;
    static InetAddress SERVER_ADDRESS, IP_ADDRESS;
    static DatagramSocket SOCKET;
    static String SERVER, NAME, REQUEST, IP, DESC, MIN, ITEM, ITEM_NAME, BID;
    static ArrayList ITEMS = new ArrayList<Items>();
    static boolean IS_REGISTERED;
    static boolean EXIT = false;

    public Client() {}

    private void userRegisteredCheck()
    {
        if (IS_REGISTERED)
            System.out.println("You are already registered you door knob!");
    }

    private void userRegister()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter desired username: ");
        NAME = input.nextLine();
        REQUEST = NAME;
        new Thread(new Action(0)).start();
    }

    private void userDeregisteredCheck()
    {
        if (!IS_REGISTERED)
            System.out.println("You need to be registered to de-register...");
        else
            new Thread(new Action(1)).start();
    }

    private void userOfferedCheck()
    {
        if (!IS_REGISTERED)
            System.out.println("You need to be registered to offer an item...");
    }

    private void userOffer()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter item name: ");
        ITEM_NAME = input.nextLine();
        System.out.println("Enter item description: ");
        DESC = input.nextLine();
        System.out.println("Enter minimum bid: ");
        MIN = input.nextLine();

        if (!onlyContainsNumbers(MIN) || MIN.length() <= 0)
            System.out.println("Invalid starting bid entered.");
        else
            new Thread(new Action(2)).start();
    }

    private void userBidCheck()
    {
        if (!IS_REGISTERED)
            System.out.println("You need to be registered to bid on an item...");
    }

    private void userBid()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter item name to bid on: ");
        ITEM = input.nextLine();
        System.out.println("Enter bid: ");
        BID = input.nextLine();

        if(!onlyContainsNumbers(BID) || BID.length() <= 0)
            System.out.println("Invalid bid entered.");
        else
            new Thread(new Action(3)).start();
    }

    private boolean onlyContainsNumbers(String s)
    {
        try
        {
            Long.parseLong(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static void main(String args[]) throws IOException
    {
        Configuration CONFIG = new Configuration();
        CONFIG.write_file();
        PORT = DefaultHelper.PORT;
        SERVER = DefaultHelper.SERVER;
        SOCKET = new DatagramSocket(PORT);
        SERVER_ADDRESS = InetAddress.getByName(SERVER);
        IP_ADDRESS = InetAddress.getLocalHost();
        IP = IP_ADDRESS.getHostAddress();
        Scanner input = new Scanner(System.in);
        String command = "";
        int counter = 0;

        Thread listen = new Thread(() -> {
            while (true)
            {
                System.out.println("Listening Thread Working");
                try{
                    Thread.sleep(10000);
                } catch (InterruptedException e) {Thread.currentThread().interrupt();}
            }
        });

        listen.start();

        while (!EXIT)
        {
            System.out.print("Hello " + command);
            command = input.nextLine();
            if (command.equals("stop"))
                break;
        }

        listen.stop();

        /**
         * We need to add a way to call methods.
         *
         * These should be added inside the loop, so we are always looking for an
         * input from the user.
         */
    }
}
