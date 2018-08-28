import java.lang.reflect.Array;
import java.util.*;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        Edge newEdge = new Edge(v1, v2, weight);
        adjLists[v1].add(newEdge);
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        addEdge(v1, v2, weight);
        addEdge(v2, v1, weight);
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        for (Edge e : adjLists[from]) {
            if (e.to == to && e.from == from) {
                return true;
            }
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        List<Integer> vertex = new LinkedList<>();
        for (LinkedList<Edge> from : adjLists) {
            for (Edge e : from) {
                if (e.to == v) {
                    vertex.add(e.from);
                }
            }
        }
        for (Edge e : adjLists[v]) {
            if (!vertex.contains(e.to)) {
                vertex.add(e.to);
            }
        }
        return vertex;
    }

    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        int count = 0;
        for (LinkedList<Edge> e : adjLists) {
            if (e.size() > 0) {
                for (int i = 0; i < e.size(); i++) {
                    Edge edge = e.get(i);
                    if (edge.to == v) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /* A class that iterates through the vertices of this graph, starting with
       vertex START. If the iteration from START has no path from START to some
       vertex v, then the iteration will not include v. */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        DFSIterator(int start) {
            fringe = new Stack<>();
            fringe.push(start);
            visited = new HashSet<>();
        }

        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        public Integer next() {
            Integer oldInt = fringe.pop();
            Iterator<Edge> dfsIterator = adjLists[oldInt].iterator();
            while (dfsIterator.hasNext()) {
                Edge e = dfsIterator.next();
                if (!visited.contains(e.to)) {
                    fringe.push(e.to);
                    visited.add(e.to);
                }
            }
            visited.add(oldInt);
            return oldInt;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        if (start == stop) {
            return true;
        } else {
            if (dfs(start).contains(stop)) {
                return true;
            }
        }
        return false;
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        ArrayList<Integer> toReturn = new ArrayList<>();
        if (!pathExists(start, stop)) {
            return new ArrayList<>();
        } else if (start == stop) {
            toReturn.add(start);
            return toReturn;
        } else {
            int curr = dfs(start).indexOf(stop);
            int pos = curr - 1;
            toReturn.add(0, stop);

            while (curr > 0) {
                int pathFrom = dfs(start).get(pos);
                int pathTo = dfs(start).get(curr);
                if (isAdjacent(pathFrom, pathTo)) {
                    toReturn.add(0, dfs(start).get(pos));
                    curr = pos;
                }
                pos--;
            }
            return toReturn;
        }
    }


    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;

        TopologicalIterator() {
            fringe = new Stack<Integer>();
        }

        public boolean hasNext() {
            return false;
        }

        public Integer next() {
            return 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }


    public List<Integer> shortestPath(int start, int stop) {
        HashMap<Integer, Integer> distance = new HashMap<>();
        int[] prev = new int[vertexCount];
        PriorityQueue<Integer> fringe = new PriorityQueue<>(vertexCount,
                (a, b) -> distance.get(a) - distance.get(b));
        ArrayList<Integer> visited = new ArrayList<>();
        ArrayList<Integer> path = new ArrayList<>();
        distance.put(start, 0);
        prev[start] = start;
        fringe.add(start);


        while(!fringe.isEmpty()) {
            int v = fringe.poll();
            if (v == stop) {
                while (prev[v] != v) {
                    path.add(0, v);
                    v = prev[v];
                }
                path.add(0, v);
                return path;
            }
            if (!visited.contains(v)) {
                for (Edge e: adjLists[v]) {
                    if (!distance.containsKey(e.to)) {
                        distance.put(e.to, distance.get(v) + e.weight);
                        prev[e.to] = v;
                        fringe.add(e.to);
                    } else {
                        if (distance.get(v) + e.weight < distance.get(e.to)) {
                            distance.put(e.to, distance.get(v) + e.weight);
                            prev[e.to] = v;
                            fringe.add(e.to);
                        }
                    }
                    fringe.add(e.to);

                }
            }
            visited.add(v);
        }
        return path;
    }


    public Edge getEdge(int u, int v) {
        LinkedList<Edge> list = adjLists[u];
        for (Edge edge: list) {
            if (edge.to == v) {
                return edge;
            }
        }
        return null;
    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        private boolean equals(Edge e) {
            return this.to == e.to;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void generateDijktras() {
        addEdge(0, 1,5);
        addEdge(1, 2, 3);
        addEdge(2, 0, 5);
        addEdge(2, 3, 10);
        addEdge(4, 2, 7);
        addEdge(0, 2, 4);
        addEdge(2,1 ,1);
        addEdge(1,0,2);
        addEdge(2,4,2);
        addEdge(4,3,2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {
//        Graph g1 = new Graph(5);
//        g1.addEdge(1, 2, 1);
//        g1.addEdge(2, 3, 1);
//        System.out.println(g1.pathExists(1,  3));
//        g1.generateG1();
//        g1.printDFS(0);
//        g1.printDFS(2);
//        g1.printDFS(3);
//        g1.printDFS(4);
//
//        g1.printPath(0, 3);
//        g1.printPath(0, 4);
//        g1.printPath(1, 3);
//        g1.printPath(1, 4);
//        g1.printPath(4, 0);
//
//        Graph g2 = new Graph(5);
//        g2.generateG2();
//        g2.printTopologicalSort();

        Graph g3 = new Graph(5);
        g3.generateDijktras();
        System.out.println(g3.shortestPath(4,0));
        System.out.println(g3.shortestPath(1,1));
        System.out.println(g3.shortestPath(0,3));
    }
}
