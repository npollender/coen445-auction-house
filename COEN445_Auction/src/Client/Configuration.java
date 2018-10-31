/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;


import java.io.*;

public class Configuration {

    public Configuration() {}

    public void write_file()
    {
        File config = new File("config.ini");

        try
        {
            if (!config.exists())
                config.createNewFile();
            else
            {
                FileInputStream inStream = new FileInputStream(config);
                DataInputStream dataStream = new DataInputStream(inStream);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataStream));
                String s = bufferedReader.readLine();
                String port_split[] = s.split(" ");
                if (port_split.length > 1)
                {
                    try
                    {
                        System.out.println("Port " + port_split[1]);
                        DefaultHelper.PORT = new Integer(port_split[1]);
                    }
                    catch (Exception e) {
                        DefaultHelper.PORT = 42069;}
                }
                else
                    DefaultHelper.PORT = 42069;

                s = bufferedReader.readLine();
                String server_split[] = s.split(" ");
                if (server_split.length >= 1) {
                    try {
                        DefaultHelper.SERVER = server_split[1];
                    } catch (Exception e) {
                        DefaultHelper.SERVER = "";
                    }
                }
                else
                    DefaultHelper.SERVER = "";

                dataStream.close();
            }
        }
        catch (Exception e) {}
    }
}
