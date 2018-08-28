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
        while (k != position && traverse != null) {
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
        while (traverse != null) {
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

        while (traverse1 != null && traverse2 != null) {
            if (traverse1.first != traverse2.first) {
                return false;
            }
            traverse1 = traverse1.rest;
            traverse2 = traverse2.rest;
        }

        return traverse1 == null && traverse2 == null;
    }

    public void add(int value) {
        IntList head = this;
        while (head.rest != null) {
            head = head.rest;
        }
        head.rest = new IntList(value, null);

    }

    public int smallest() {
        int small = this.first;
        IntList head = this.rest;
        while (head != null) {
            if (head.first < small) {
                small = head.first;
            }
            head = head.rest;
        }
        return small;
    }

    public int squaredSum() {
        if (this.rest == null) {
            return (int) Math.pow(this.first, 2);
        }
        return (int) Math.pow(this.first, 2) + this.rest.squaredSum();
    }

    public static void dSquareList(IntList L) {
        while (L != null) {
            L.first = L.first * L.first;
            L = L.rest;
        }
    }

    public static IntList dcatenate(IntList A, IntList B) {
        if (A != null) {
            IntList head = A;
            while (head.rest != null) {
                head = head.rest;
            }
            head.rest = B;
            return A;
        }
        return B;

    }

    public static IntList catenate(IntList A, IntList B) {
        if (A != null) {
            IntList head = A;
            IntList copy = new IntList(head.first, null);
            IntList returnList = copy;


            while (head.rest != null) {
                head = head.rest;
                copy.rest = new IntList(head.first, null);
                copy = copy.rest;
            }
            copy.rest = B;
            return returnList;
        }
        return B;
    }






}
