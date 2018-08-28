import java.util.LinkedList;

public class SimpleNameMap {

    /* Instance variables here? */
    LinkedList<SimpleNameMap.Entry>[] map;
    int size;
    /* TODO: Add a constructor here */
    public SimpleNameMap() {
        map = new LinkedList[10];
        size = 0;
    }

    /* Returns true if the given KEY is a valid name that starts with A - Z. */
    private static boolean isValidName(String key) {
        return 'A' <= key.charAt(0) && key.charAt(0) <= 'Z';
    }

    /* Returns true if the map contains the KEY. */
    boolean containsKey(String key) {
        if (map[hashCodeHelper(key)].contains(key)) {
            return true;
        }
        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    String get(String key) {
        if (containsKey(key)) {
            int index = hashCodeHelper(key);
            for (Entry entry: map[index]) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    void put(String key, String value) {
        int index = hashCodeHelper(key);
        double loadFactor = size / (double) map.length;
        if (loadFactor >= 0.75) {
            resize();
        }
        if (containsKey(key)) {
            for (Entry entry: map[index]) {
                if (entry.key.equals(key)) {
                    entry.value = value;
                }

            }
        } else {
            map[index].addLast(new Entry(key, value));
            size++;
        }

    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    String remove(String key) {
        if (containsKey(key)) {
            for (Entry entry: map[hashCodeHelper(key)]) {
                if (entry.key.equals(key)) {
                    String val = entry.value;
                    map[hashCodeHelper(key)].remove(key);
                    size--;
                    return val;
                }
            }

        }
        return null;
    }

    public int hashCodeHelper(String index) {
        return Math.floorMod(index.hashCode(), map.length);
    }

    public void resize() {
        int newLength = map.length * 2;
        LinkedList<Entry>[] newMap =  new LinkedList[newLength];



    }
    private static class Entry {

        private String key;
        private String value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
