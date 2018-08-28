public class UnionFind {

    public int[] set;

    /* Creates a UnionFind data structure holding N vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int N) {
        set = new int[N];
        for (int i = 0; i < N; i++) {
            set[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        if (v > set.length - 1) {
            throw new IllegalArgumentException();
        }
        return -parent(find(v));
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        if (v > set.length - 1) {
            throw new IllegalArgumentException();
        }
        return set[v];
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        if (v1 > set.length - 1 || v2 > set.length - 1) {
            throw new IllegalArgumentException();
        }
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid vertices are passed into this
       function, throw an IllegalArgumentException. */

    public int find(int v) {
        if (v > set.length - 1) {
            throw new IllegalArgumentException();
        }
        int root = v;
        while (parent(root) >= 0) {
            root = parent(root);
        }
        int temp;
        while (parent(v) >= 0) {
            temp = set[v];
            set[v] = root;
            v = temp;
        }
        return root;
    }

    /* Connects two elements V1 and V2 together. V1 and V2 can be any element,
       and a union-by-size heuristic is used. If the sizes of the sets are
       equal, tie break by connecting V1's root to V2's root. Union-ing a vertex
       with itself or vertices that are already connected should not change the
       structure. */
    public void union(int v1, int v2) {
        if (v1 > set.length - 1 || v2 > set.length - 1) {
            throw new IllegalArgumentException();
        }
        if (!connected(v1, v2) && v1 != v2) {
            int sizeV1 = sizeOf(v1);
            int sizeV2 = sizeOf(v2);
            int rootV1 = find(v1);
            int rootV2 = find(v2);

            if (sizeV1 > sizeV2) {
                set[rootV2] = rootV1;
                set[rootV1] -= sizeV2;

            } else if (sizeV1 < sizeV2) {
                set[rootV1] = rootV2;
                set[rootV2] -= sizeV1;

            } else {
                set[rootV1] = rootV2;
                set[rootV2] -= sizeV1;
            }
        }
    }
}
