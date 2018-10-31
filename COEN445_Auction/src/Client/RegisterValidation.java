/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class RegisterValidation extends Thread {

    static String[] INFO;
    static int CODE;

    public RegisterValidation(int code, String[] info)
    {
        INFO = info;
        CODE = code;
    }

    public void userClear()
    {
        Client.REQUEST = null;
        Client.NAME = null;
        Client.ITEMS.clear();
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
             * Case 0 is when we have a successful registration. For this to occur, we require
             * valid data.
             */
            case 0:
            {
                //if all info is correct...
                TimerHelper.time_stop();
                System.out.println("You have been registered as " + Client.NAME);
                Client.IS_REGISTERED = true;
                break;
            }
            /**
             * Case 1 is when we have an unsuccessful registration. There are a number of reasons
             * why this can occur: wrong request, wrong port, or user exists already...
             */
            case 1:
            {
                //if confirm unsuccessful reg
                System.out.println("Registration failed.");
                break;
            }
            /**
             * Case 2 is when when have a successful de-registration.
             */
            case 2:
            {
                //if confirm de-reg
                System.out.println("You have been de-registered.");
                userClear();
                Client.IS_REGISTERED = false;
                break;
            }
            /**
             * Case 3 is when when have an unsuccessful de-registration.
             */
            case 3:
            {
                //if confirm unsuccessful de-reg
                System.out.println("De-registration failed.");
            }
        }
    }
}
