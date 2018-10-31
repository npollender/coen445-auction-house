/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018
 */

package Client;

import java.util.ArrayList;

public class Items {

    int ITEM_ID;
    float MIN;
    String DESC, NAME;

    public Items() {}

    public Items(String id, String min, String desc, String n)
    {
        ITEM_ID = Integer.parseInt(id);
        MIN = new Float(min);
        DESC = desc;
        NAME = n;
    }

    public void set_min(String s)
    {
        MIN = new Float(s);
    }

    public int get_id()
    {
        return ITEM_ID;
    }

    public float get_min()
    {
        return MIN;
    }

    public String get_desc()
    {
        return DESC;
    }

    public String get_name()
    {
        return NAME;
    }

    public boolean check_dupes(ArrayList item_list)
    {
        for (int i = 0; i < item_list.size(); i++)
        {
            Items tmp = new Items();
            tmp = (Items) item_list.get(i);
            if (tmp.get_id() == ITEM_ID)
                return true;
        }
        return false;
    }
}
