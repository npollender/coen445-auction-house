/**
 * Created for COEN445 Auction House Project by Nicholas and Liyuan.
 * Fall 2018

package Client;

import java.util.ArrayList;

public class Items {

    static int ITEM_ID;
    static float MIN;
    static String DESC;

    public Items() {}

    public Items(String id, String desc, String min)
    {
        ITEM_ID = Integer.parseInt(id);
        MIN = new Float(min);
        DESC = desc;
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
} */
