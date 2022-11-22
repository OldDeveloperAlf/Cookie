package us.rettopvp.cookie.rank.comparator;

import java.util.Comparator;

import us.rettopvp.cookie.rank.Rank;

public class RankComparator implements Comparator<Rank> {
	
    @Override
    public int compare(Rank parent, Rank child) {
        return parent.getWeight() - child.getWeight();
    }
}
