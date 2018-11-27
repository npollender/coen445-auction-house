/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.io.IOException;

public class ItemsValidation extends Thread {

    static String[] DATA;
    static int CODE;

    public ItemsValidation(int code, String[] data)
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
             * Case 0 is when an item is added to the auction.
             * The client is notified of the new item.
             */
            case 0:
            {
                Client.textArea.append("New item added to the auction!" + Client.nextLine);
                Client.textArea.append("ID: " + DATA[1] + " - " + DATA[2] + " for " + DATA[3] + "$" + Client.nextLine);
                Client.textArea.append("To bid on the item, first connect to port #" + DATA[4] + Client.nextLine);
                break;
            }
            /**
             * Case 1 is when an existing item has its price updated.
             * The client is notified of the new high bid.
             */
            case 1:
            {
                Client.textArea.append("New highest bid of " + DATA[2] + "$ on item #" + DATA[1] + Client.nextLine);
                break;
            }
            /**
             * Case 2 is when an item is won.
             * The client that won the item receives this message.
             */
            case 2:
            {
                Client.textArea.append("Congratulations, you won item #" + DATA[1] + " for " + DATA[4] + "$." + Client.nextLine);
                break;
            }
            /**
             * Case 3 is when the bidding period for an item you bid on is over (and you didn't win).
             * All clients that bid, but did not win, will be notified that the bidding period is over for
             * that item.
             */
            case 3:
            {
                Client.textArea.append("The bidding period for item #" + DATA[1] + " is over!" + Client.nextLine);
                try
                {
                    TCPClient.SOCKET.close();
                    Client.textArea.append("All TCP connections associated to the item have been closed.");
                    Client.IS_TCP = false;
                }
                catch (IOException e) {}
                break;
            }
            /**
             * Case 4 is when the server notifies the original item owner that their item has been sold.
             * Only the owner of the item will be notified.
             */
            case 4:
            {
                Client.textArea.append("Your item #" + DATA[1] + " has been sold to " + DATA[2] + " for " + DATA[5] + "$." + Client.nextLine);
                break;
            }
            /**
             * Case 5 is when the server notifies the original owner that their item has not been sold.
             * Only the owner of the item will be notified.
             */
            case 5:
            {
                Client.textArea.append("You item #" + DATA[1] + " has not been sold. Maybe don't overprice it next time." + Client.nextLine);
                break;
            }
            case 6:
            {
                Client.textArea.append("Items currently up for auction: " + Client.nextLine);
                Client.textArea.append("ID #" + DATA[1] + " " + DATA[2] + " offered for: " + DATA[3] + "$." + Client.nextLine);
                break;
            }
        }
    }
}
