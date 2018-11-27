/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {

    static int PORT, ITEM_PORT, ID;
    static InetAddress SERVER_ADDRESS, IP_ADDRESS;
    static DatagramSocket SOCKET;
    static DatagramPacket PACKET;
    static String SERVER, NAME, REQUEST, IP, ITEM, DESC, MIN, ITEM_NAME, BID, S_PORT;
    static boolean IS_REGISTERED, IS_TCP;
    static boolean EXIT = false;
    static String nextLine = "\n";

    static JFrame jFrame;
    static JTextArea textArea;
    static JScrollPane scrollPane;
    static DefaultCaret caret;

    public Client() {}

    private static void userRegister()
    {
        if (!IS_REGISTERED)
        {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter desired username: ");
            NAME = input.nextLine();
            REQUEST = NAME;
            new Thread(new SendAction(0)).start();
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
            new Thread(new SendAction(1)).start();
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
                new Thread(new SendAction(2)).start();
        }
        else
        {
            System.out.println("you must be registered to offer");
        }
    }

    private static void TCP_connect()
    {
        if (IS_REGISTERED)
        {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter port to connect to: ");
            S_PORT = input.nextLine();
            ITEM_PORT = Integer.parseInt(S_PORT);

            if (!onlyContainsNumbers(S_PORT) || S_PORT.length() <= 0)
                System.out.println("Invalid port entered.");
            else
                new Thread(new SendAction(4)).start();
        }
        else
        {
            System.out.println("you must be registered to make a tcp connection.");
        }
    }

    private static void userBid()
    {
        if (IS_REGISTERED && IS_TCP)
        {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter item id to bid on: ");
            ITEM = input.nextLine();
            ID = Integer.parseInt(ITEM);
            System.out.print("Enter bid: ");
            BID = input.nextLine();

            if (!onlyContainsNumbers(BID) || BID.length() <= 0)
                System.out.println("Invalid bid entered.");
            else
                new Thread(new SendAction(3)).start();
        }
        else if (!IS_REGISTERED)
        {
            System.out.println("you must be registered to offer.");
        }
        else
        {
            System.out.println("you must establish a tcp connection before bidding.");
        }
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

    public static void joke()
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

    public static void initOutput()
    {
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scrollPane = new JScrollPane(textArea);

        jFrame.add(new JLabel("Auction House"), BorderLayout.NORTH);
        jFrame.add(scrollPane);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setSize(800,400);
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

        initOutput();

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
        System.out.println("Welcome to TOTALLY NOT FAKE AUCTION HOUSE - TEST BUILD");
        System.out.println("We're just going to take care of a few things for you...");
        System.out.println("********************************************************");
        System.out.println("Adding funds to your session...");
        System.out.println("ERROR: missing funds!");
        System.out.println("Search for source of funds...");
        HOLDUP(5);
        System.out.println("Selling all your belongings...");
        HOLDUP(5);
        System.out.println("Emptying your savings account...");
        HOLDUP(5);1
        System.out.println("Stealing your credit card information...");
        HOLDUP(5);
        System.out.println("Alright, we're all set! Good luck!");
        System.out.println("Use command HELP for a list of commands.");
        System.out.println("********************************************************");
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
            else if (command.equals("bid"))
            {
                userBid();
            }
            else if (command.equals("connect"))
            {
                TCP_connect();
            }
            else if (command.equals("joke"))
            {
                joke();
            }
            else if (command.equals("HELP") || command.equals("help"))
            {
                System.out.println("List of commands (case sensitive):");
                System.out.println("register - register to the server");
                System.out.println("deregister - deregister from the server");
                System.out.println("offer - offer an item");
                System.out.println("connect - create tcp connection to an item");
                System.out.println("bid - bid on an item");
                System.out.println("joke - probably just a bad joke, I wouldn't bother trying this one.");
                System.out.println("exit - kills the client, may cause errors when trying to reconnect to the server.");
            }
            else
            {
                System.out.println("Please enter a valid command! Use HELP for a list of commands.");
            }
        }
        System.exit(0);
    }
}