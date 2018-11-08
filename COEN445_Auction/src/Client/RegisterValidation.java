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
                                TimerHelper.time_stop();
                                System.out.println("You have been registered as " + NAME);
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
                        TimerHelper.time_stop();
                        System.out.println("Registration failed, wrong user information provided.");
                    }
                    else if (DATA[2].equals(DefaultHelper.REG_FAILED_1))
                    {
                        TimerHelper.time_stop();
                        System.out.println("Registration failed, wrong server information provided.");
                    }
                    else if (DATA[2].equals(DefaultHelper.REG_FAILED_2))
                    {
                        TimerHelper.time_stop();
                        System.out.println("Registration failed, user already exists.");
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
                    TimerHelper.time_stop();
                    System.out.println("You have been de-registered.");
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
                        System.out.println("De-registration failed, you aren't registered.");
                    }
                    else if (DATA[2].equals(DefaultHelper.DEREG_FAILED_1))
                    {
                        System.out.println("De-registration failed, wrong user info provided.");
                    }
                    else if (DATA[2].equals(DefaultHelper.DEREG_FAILED_2))
                    {
                        System.out.println("De-registration failed, you still have items for sale.");
                    }
                    else if (DATA[2].equals(DefaultHelper.DEREG_FAILED_3))
                    {
                        System.out.println("De-registration failed, you still have the highest bid on an item(s).");
                    }
                }
                break;
            }
        }
    }
}
