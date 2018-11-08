/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {

    static int PORT, CODE, ID;
    static InetAddress SERVER_ADDRESS, IP_ADDRESS;
    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;
    static String SERVER, NAME, REQUEST, IP, DESC, MIN, ITEM, ITEM_NAME, BID;
    //static ArrayList<Items> ITEMS = new ArrayList<Items>();
    static boolean IS_REGISTERED;
    static boolean EXIT = false;

    public Client() {}

    private static void userRegister()
    {
        if (!IS_REGISTERED)
        {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter desired username: ");
            NAME = input.nextLine();
            REQUEST = NAME;
            new Thread(new SendRegister()).start();
        }
        else
        {
            System.out.println("you are already registered");
        }
    }

    private static void userDeregisteredCheck()
    {
        if (!IS_REGISTERED)
            System.out.println("You need to be registered to de-register...");
        else
            new Thread(new SendDeregister()).start();
    }

    private static void userOffer()
    {
        if (IS_REGISTERED)
        {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter item name: ");
            ITEM_NAME = input.nextLine();
            System.out.print("Enter item description: ");
            DESC = input.nextLine();
            System.out.print("Enter minimum bid: ");
            MIN = input.nextLine();

            if (!onlyContainsNumbers(MIN) || MIN.length() <= 0)
                System.out.println("Invalid starting bid entered.");
            else
                new Thread(new SendOffer()).start();
        }
        else
        {
            System.out.println("you must be registered to offer");
        }
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
            new Thread(new SendBid()).start();
    }

    private static boolean onlyContainsNumbers(String s)
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

    public void joke()
    {
        System.out.println("A programming language walks into a bar and says 'Hello world!'");
    }

    public static void HOLDUP(int time)
    {
        try
        {
            TimeUnit.SECONDS.sleep(time);
        }
        catch (InterruptedException e) {}
    }

    public void addFunds()
    {
        System.out.println("Adding funds...");
        System.out.println("You have successfully mortgaged your home! You now have a bunch of imaginary money!");
    }

    public static void main(String args[]) throws IOException
    {
        PORT = DefaultHelper.PORT;
        SERVER = DefaultHelper.SERVER;
        SOCKET = new DatagramSocket();
        SERVER_ADDRESS = InetAddress.getByName(SERVER);
        IP_ADDRESS = InetAddress.getLocalHost();
        IP = IP_ADDRESS.getHostAddress();
        Scanner input = new Scanner(System.in);
        String command = "";
        int counter = 0;

        Thread listen = new Thread(() -> {
            while (true)
            {
                /**System.out.println("Listening Thread Working");
                try{
                    Thread.sleep(10000);
                } catch (InterruptedException e) {Thread.currentThread().interrupt();}*/
                try
                {
                    byte[] rec_data = new byte[DefaultHelper.MSG_SIZE];
                    PACKET = new DatagramPacket(rec_data, rec_data.length);
                    SOCKET.receive(PACKET);
                    new Thread(new DataValidation(PACKET, SOCKET)).start();
                }
                catch (IOException e) {}
            }
        });
/*
        System.out.println("Welcome to TOTALLY NOT FAKE AUCTION HOUSE");
        System.out.println("We're just going to take care of a few things for you...");
        System.out.println("********************************************************");
        System.out.println("Adding funds to your session...");
        System.out.println("ERROR: missing funds!");
        System.out.println("Search for source of funds...");
        HOLDUP(5);
        System.out.println("Selling all your belongings...");
        HOLDUP(5);
        System.out.println("Emptying your savings account...");
        HOLDUP(5);
        System.out.println("Stealing your credit card information...");
        HOLDUP(5);
        System.out.println("Alright, we're all set! Good luck!");
        HOLDUP(30);
*/

        listen.start();

        while (!EXIT)
        {
            System.out.print("Enter a command: ");
            command = input.nextLine();
            if (command.equals("register"))
            {
                userRegister();
            }
            else if (command.equals("deregister"))
            {
                userDeregisteredCheck();
            }
            else if (command.equals("offer"))
            {
                userOffer();
            }
            else if (command.equals("exit"))
            {
                EXIT = true;
            }

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
