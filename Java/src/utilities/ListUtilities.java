package utilities;

import java.util.AbstractList;
import java.util.List;

public class ListUtilities {
    /**
     * @param begin inclusive
     * @param end exclusive
     * @return list of integers from begin to end
     */
    public static List<Integer> range(final int begin, final int end) {
        return new AbstractList<Integer>() {
            @Override
            public Integer get(int index) {
                return begin + index;
            }

            @Override
            public int size() {
                return end - begin;
            }
        };
    }
}
