/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

public class RegisterValidation extends Thread {

    static String[] INFO;
    static int CODE;

    public RegisterValidation(String[] info, int code)
    {
        INFO = info;
        CODE = code;
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
            {}
            /**
             * Case 1 is when we have an unsuccessful registration. There are a number of reasons
             * why this can occur: wrong request, wrong port, or user exists already...
             */
            case 1:
            {}
            /**
             * Case 2 is when when have a successful de-registration.
             */
            case 2:
            {}
            /**
             * Case 3 is when when have an unsuccessful de-registration.
             */
            case 3:
            {}

        }
    }
}
