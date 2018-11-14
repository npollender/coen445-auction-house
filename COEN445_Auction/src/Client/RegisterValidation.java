/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class RegisterValidation extends Thread {

    static String REQUEST = Client.REQUEST;
    static String NAME = Client.NAME;
    static String IP = Client.IP;
    static String PORT = Integer.toString(Client.PORT);
    static String[] DATA;
    static int CODE;

    public RegisterValidation(int code, String[] data)
    {
        DATA = data;
        CODE = code;
    }

    private void userClear()
    {
        Client.REQUEST = null;
        Client.NAME = null;
        Client.IS_REGISTERED = false;
    }

    /**
     * Here we will have the different scenarios when attempting to register or
     * de-register. 4 cases exist, successful/unsuccessful registration/de-registration.
     */
    @Override
    public void run()
    {
        switch(CODE)
        {
            /**
             * Case 0 is to indicate a successful registration.
             * For this to occur, the user data sent from the server must match the user data saved on the client.
             */
            case 0:
            {
                /**
                 * Side note, DATA[0] is occupied by a unique number sent by the server used to determine the message type.
                 * In this instance, DATA[0] told the client that this is a registration type.
                 */
                if (DATA[1].equals(REQUEST))
                {
                    if (DATA[2].equals(NAME))
                    {
                        if (DATA[3].equals(IP))
                        {
                            if (DATA[4].equals(PORT))
                            {
                                Client.textArea.append("You have been registered as " + NAME + Client.nextLine);
                                Client.IS_REGISTERED = true;
                            }
                        }
                    }
                }
                break;
            }
            /**
             * Case 1 is to indicate an error with registration.
             * Along with the CODE, the server also sends an error id that indicates why the registration was unsuccessful.
             * Before indicating the error, the client verifies that the correct request is being acknowledged.
             */
            case 1:
            {
                if (DATA[1].equals(REQUEST))
                {
                    /**
                     * We have 3 errors, wrong user info provided, wrong server info provided, or user already exists.
                     */
                    if (DATA[2].equals(DefaultHelper.REG_FAILED_0))
                    {
                        Client.textArea.append("Registration failed, wrong user information provided." + Client.nextLine);
                    }
                    else if (DATA[2].equals(DefaultHelper.REG_FAILED_1))
                    {
                        Client.textArea.append("Registration failed, wrong server information provided." + Client.nextLine);
                    }
                    else if (DATA[2].equals(DefaultHelper.REG_FAILED_2))
                    {
                        Client.textArea.append("Registration failed, user already exists." + Client.nextLine);
                    }
                }
                break;
            }
            /**
             * Case 2 is to indicate a successful de-registration.
             * For this to occur, the request sent from the server must match that of the client.
             */
            case 2:
            {
                if (DATA[1].equals(REQUEST))
                {
                    Client.textArea.append("You have been de-registered." + Client.nextLine);
                    userClear();
                }
                break;
            }
            /**
             * Case 3 is to indicate an error with de-registration.
             * Just like case 1, there are reasons why this will fail. They are outlined by their error id.
             * We first verify the request sent by the server...
             */
            case 3:
            {
                if (DATA[1].equals(REQUEST))
                {
                    /**
                     * There are a number of reasons why a user won't be able to de-register.
                     * They aren't registered, wrong info is provided, they are still selling items or
                     * they have the highest bid on an item.
                     */
                    if (DATA[2].equals(DefaultHelper.DEREG_FAILED_0))
                    {
                        Client.textArea.append("De-registration failed, there was a problem with the socket information." + Client.nextLine);
                    }
                    else if (DATA[2].equals(DefaultHelper.DEREG_FAILED_1))
                    {
                        Client.textArea.append("De-registration failed, you still have item(s) for sale." + Client.nextLine);
                    }
                    else if (DATA[2].equals(DefaultHelper.DEREG_FAILED_2))
                    {
                        Client.textArea.append("De-registration failed, you still have the highest bid on item(s)." + Client.nextLine);
                    }
                    else if (DATA[2].equals(DefaultHelper.DEREG_FAILED_3))
                    {
                        Client.textArea.append("De-registration failed, your username does not exist in the auction server." + Client.nextLine);
                    }
                    else if (DATA[2].equals(DefaultHelper.DEREG_FAILED_4))
                    {
                        Client.textArea.append("De-registration failed, there was a problem with the provided information." + Client.nextLine);
                    }
                }
                break;
            }
        }
    }
}
