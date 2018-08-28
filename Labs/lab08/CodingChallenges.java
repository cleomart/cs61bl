import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import  java.util.Set;

public class CodingChallenges {

    /**
     * Return the missing number from an array of length N - 1 containing all
     * the values from 0 to N except for one missing number.
     */
    public static int missingNumber(int[] values) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < values.length; i++) {
            set.add(values[i]);
        }
        for (int i = 0; i < values.length; i++) {
            if (!set.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    /** Returns true if and only if two integers in the array sum up to n. */
    public static boolean sumTo(int[] values, int n) {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                if (i != j) {
                    if (values[i] + values[j] == n) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if and only if s1 is a permutation of s2. s1 is a
     * permutation of s2 if it has the same number of each character as s2.
     */
    public static boolean isPermutation(String s1, String s2) {
        Map<Character, Integer> map1 = new HashMap<>();
        int size = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (!map1.containsKey(s1.charAt(i))) {
                map1.put(s1.charAt(i), 1);

            } else {
                size = map1.get(s1.charAt(i));
                size++;
                //map1.remove(s1.charAt(i));
                map1.replace(s1.charAt(i), size);
            }
        }

        Map<Character, Integer> map2 = new HashMap<>();
        for (int i = 0; i < s2.length(); i++) {
            if (!map2.containsKey(s2.charAt(i))) {
                map2.put(s2.charAt(i), 1);
            } else {
                size = map2.get(s2.charAt(i));
                size++;
                //map1.remove(s2.charAt(i));
                map2.replace(s2.charAt(i), size);
            }
        }
        for (int i = 0; i < s2.length(); i++) {
            if (!map1.containsKey(s2.charAt(i))) {
                return false;
            } else if (map1.get(s2.charAt(i)) != map2.get(s2.charAt(i))) {
                return false;
            }
        }
        return map1.size() == map2.size();
    }
}
