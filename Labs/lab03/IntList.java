/**
 * A data structure to represent a Linked List of Integers.
 * Each IntList represents one node in the overall Linked List.
 */
public class IntList {
    public int first;
    public IntList rest;

    public IntList(int f, IntList r) {
        first = f;
        rest = r;
    }

    /** Returns an IntList consisting of the given values. */
    public static IntList of(int... values) {
        if (values.length == 0) {
            return null;
        }
        IntList p = new IntList(values[0], null);
        IntList front = p;
        for (int i = 1; i < values.length; i++) {
            p.rest = new IntList(values[i], null);
            p = p.rest;
        }
        return front;
    }

    /** Returns the size of the list. */
    public int size() {
        if (rest == null) {
            return 1;
        }
        return 1 + rest.size();
    }

    /** Returns [position]th value in this list. */
    public int get(int position) {
        int k = 0;
        IntList traverse = this;
        while(k != position && traverse != null){
            traverse = traverse.rest;
            k++;
        }
        return traverse.first;
    }

    /** Returns the string representation of the list. */
    public String toString() {
        String rep = Integer.toString(this.first);
        IntList traverse = this;
        traverse = traverse.rest;
        while(traverse != null){
            rep = rep + " " + Integer.toString(traverse.first);
            traverse = traverse.rest;
        }
        return rep;
    }

    /** Returns whether this and the given list or object are equal. */
    public boolean equals(Object o) {
        IntList other = (IntList) o;
        IntList traverse1 = this;
        IntList traverse2 = other;

        while(traverse1 != null && traverse2 != null){
            if (traverse1.first != traverse2.first) {
                return false;
            }
            traverse1 = traverse1.rest;
            traverse2 = traverse2.rest;
        }

        return traverse1 == null && traverse2 == null;
    }

    public static void main(String[] args){
        IntList a = new IntList(1, null);
        IntList b = new IntList(4,a);
        //System.out.print(a.get(4));
        IntList c = new IntList(12,b);
        System.out.println();
        System.out.print(c.toString());
        System.out.println();
        System.out.print(a.equals(c));
        System.out.println();
        System.out.print(c.equals(c));

    }






}
