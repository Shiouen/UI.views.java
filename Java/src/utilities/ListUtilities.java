package utilities;

import java.util.AbstractList;
import java.util.ArrayList;
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

    public static String join(List<String> strings, char delimiter) {
        StringBuilder sb = new StringBuilder();

        for (String s: strings) { sb.append(s).append(delimiter); }

        sb.deleteCharAt(sb.length()-1); //delete last delimiter

        return sb.toString();
    }

    public static List<String> replace(List<String> strings, String toReplace, String replacement) {
        List<String> replaced = new ArrayList<>();

        for (String s : strings) {
            replaced.add(s.replaceAll(toReplace, replacement));
        }

        return replaced;
    }

}
