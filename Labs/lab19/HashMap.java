import java.util.LinkedList;
import java.util.Iterator;



public class HashMap<K, V> implements Map61BL<K, V> {

    /* Instance variables here? */
    LinkedList<Entry<K,V>>[] map;
    int size;
    double loadFactor;

    /* TODO: Add a constructor here */
    public HashMap() {
        map = new LinkedList[16];
        size = 0;
    }
    public HashMap(int size) {
        map = new LinkedList[size];
        this.size = size;
    }
    public HashMap(int size, double loadFactor) {
        map = new LinkedList[size];
        this.loadFactor = loadFactor;
    }


    /* Returns true if the given KEY is a valid name that starts with A - Z. */
    private static boolean isValidName(String key) {
        return 'A' <= key.charAt(0) && key.charAt(0) <= 'Z';
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(K key) {
        if (map[hashCodeHelper(key)].contains(key)) {
            return true;
        }
        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public V get(K key) {
        if (containsKey(key)) {
            int index = hashCodeHelper(key);
            for (Entry<K, V> entry: map[index]) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(K key, V value) {
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
            map[index].addLast(new HashMap.Entry(key, value));
            size++;
        }

    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public V remove(K key) {
        if (containsKey(key)) {
            for (Entry entry: map[hashCodeHelper(key)]) {
                if (entry.key.equals(key)) {
                    V val = get(key);
                    map[hashCodeHelper(key)].remove(entry);
                    size--;
                    return val;
                }
            }

        }
        return null;
    }
//if you removed something, return true
    public boolean remove(K key, V value) {
        int index = hashCodeHelper(key);
        if (containsKey(key)) {
            for (Entry entry: map[index]) {
                if (entry.key == key) {
                    map[index].remove(entry);
                    size--;
                }
            }
        }
        return true;
    }

    public int hashCodeHelper(K index) {
        return Math.floorMod(index.hashCode(), map.length);
    }

    public void resize() {
        int newLength = map.length * 2;
        LinkedList<Entry>[] newMap =  new LinkedList[newLength];
    }
    public int size() {
        return size;
    }
    public Iterator<K> iterator() {
        return new HashMapIterator<>();
    }


    private class HashMapIterator<K> implements Iterator<K> {
        int mapIndex;
        int listIndex;

        public HashMapIterator() {
            mapIndex = 0;
            listIndex = 0;
        }

        public K next() {
            K nextKey = (K) map[mapIndex].get(listIndex).key;
            listIndex++;
            if (listIndex >= map[mapIndex].size()) {
                listIndex = 0;
                mapIndex++;
            }
            return nextKey;
        }

        @Override
        public boolean hasNext() {
            return size != 0 && mapIndex < map.length;
        }
    }

    public int capacity() {
        return map.length;
    }

    public void clear() {
        map = new LinkedList[map.length];
    }

    private class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
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
