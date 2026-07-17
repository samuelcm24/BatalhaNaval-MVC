package batalhanaval;

import java.util.Comparator;

public class CompareInfo implements Comparator <Info>{

    @Override
    public int compare(Info o1, Info o2) {
        if(o1.getTtot() > o2.getTtot())
        {
            return 1;
        }
        else if(o1.getTtot() < o2.getTtot())
        {
            return -1;
        }
        else
        {
            if(o1.getData().compareTo(o2.getData())<=0)
                return 1;
            else
                return -1;
            
        }
    }
}
