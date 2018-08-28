import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Collections;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Kevin Lowe, Antares Chen, Kevin Lin
 */
public class GraphDB {
    private HashMap<Long, Vertex> vertices = new HashMap<>();
    private HashMap<Long, HashSet<Edge>> edges = new HashMap<>();
    /* Maps id of vertices to a list of its neighboring vertices. */
    private HashMap<Long, HashSet<Long>> neighbors = new HashMap<>();
    private TreeVertex root;

    /**
     * This constructor creates and starts an XML parser, cleans the nodes, and prepares the
     * data structures for processing. Modify this constructor to initialize your data structures.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        File inputFile = new File(dbPath);
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputStream, new GraphBuildingHandler(this));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
        ArrayList<Long> vert = new ArrayList<>();
        vert.addAll(vertices.keySet());
        root = twoDTree(vert, 0);

    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        ArrayList<Long> willRemove = new ArrayList<>();
        for (long id : vertices.keySet()) {
            if (neighbors.get(id).isEmpty()) {
                willRemove.add(id);
            }
        }
        for (long id: willRemove) {
            removeVertex(id);
        }
    }

    /**
     * Returns the longitude of vertex <code>v</code>.
     * @param v The ID of a vertex in the graph.
     * @return The longitude of that vertex, or 0.0 if the vertex is not in the graph.
     */
    double lon(long v) {
        if (!vertices.containsKey(v)) {
            return 0.0;
        } else {
            return getVertex(v).getLon();
        }
    }

    /**
     * Returns the latitude of vertex <code>v</code>.
     * @param v The ID of a vertex in the graph.
     * @return The latitude of that vertex, or 0.0 if the vertex is not in the graph.
     */
    double lat(long v) {
        if (!vertices.containsKey(v)) {
            return 0.0;
        } else {
            return getVertex(v).getLat();
        }
    }

    /* Adds a vertex in this graph using a Vertex instance */
    public void addVertex(Vertex v) {
        if (!vertices.containsKey(v.getiD())) {
            vertices.put(v.getiD(), v);
            neighbors.put(v.getiD(), new HashSet<>());
            edges.put(v.getiD(), new HashSet<>());
        }
    }

    /* Removes a vertex in this graph */
    public void removeVertex(long iD) {
        vertices.remove(iD);
        neighbors.remove(iD);
    }

    /* Adds an edge in this graph */
    public void addEdge(long id1, long id2, String name) {
        Edge e1 = new Edge(id1, id2, name, distance(id1, id2));
        Edge e2 = new Edge(id2, id1, name, distance(id1, id2));
        neighbors.get(id1).add(id2);
        neighbors.get(id2).add(id1);
        edges.get(id1).add(e1);
        edges.get(id2).add(e2);
    }

    /* Returns the vertex with the corresponding id*/
    public Vertex getVertex(long id) {
        if (vertices.keySet().contains(id)) {
            return vertices.get(id);
        }
        return null;
    }

    /* Returns the ID of the vertex with the same lan and lot*/
    public long getVertexId(double lon, double lat) {
        long id = (long) -1;
        for (Vertex  v: vertices.values()) {
            if (v.getLat() == lat && v.getLon() == lon) {
                id = v.getiD();
            }
        }
        return id;
    }

    /* Returns the number of vertices in this graph */
    public int numVertices() {
        return vertices.keySet().size();
    }

    public HashSet<Edge> getEdges(long id) {
        return edges.get(id);
    }

    public HashSet<Long> getNeighbors(long v) {
        return neighbors.get(v);
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of all vertex IDs in the graph.
     */
    Iterable<Long> vertices() {
        return vertices.keySet();
    }

    /**
     * Returns an iterable over the IDs of all vertices adjacent to <code>v</code>.
     * @param v The ID for any vertex in the graph.
     * @return An iterable over the IDs of all vertices adjacent to <code>v</code>, or an empty
     * iterable if the vertex is not in the graph.
     */
    Iterable<Long> adjacent(long v) {
        if (vertices.containsKey(v)) {
            return neighbors.get(v);
        }
        return new HashSet<>();

    }

    /**
     * Returns the great-circle distance between two vertices, v and w, in miles.
     * Assumes the lon/lat methods are implemented properly.
     * @param v The ID for the first vertex.
     * @param w The ID for the second vertex.
     * @return The great-circle distance between vertices and w.
     * @source https://www.movable-type.co.uk/scripts/latlong.html
     */
    public double distance(long v, long w) {
        double phi1 = Math.toRadians(lat(v));
        double phi2 = Math.toRadians(lat(w));
        double dphi = Math.toRadians(lat(w) - lat(v));
        double dlambda = Math.toRadians(lon(w) - lon(v));

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public long closest(double lon, double lat) {
        double x =  projectToX(lon, lat);
        double y =  projectToY(lon, lat);
        return nearNeighbor(root, root,
                euclidean(root.x, x, root.y, y), x, y, 0).id;

    }

    /**
     *  Creates the two d tree
     * @param vertex lat and lon already converted in x and y
     * @param depth
     */
    public TreeVertex twoDTree(ArrayList<Long> vertex, int depth) {
        if (vertex.isEmpty()) {
            return null;
        }
        int median = ((vertex.size()) / 2);
        TreeVertex rootTree;
        // determines if we cut in x or y axis
        int axis = depth % DIMENSION;
        int leftSize = median;
        int rightSize = vertex.size() - median - 1;
        ArrayList<Long> leftVertices = new ArrayList<>(leftSize);
        ArrayList<Long> rightVertices = new ArrayList<>(rightSize);

        // if axis is 0, cut in x-axis
        if (axis == 0) {
            vertex.sort(compareX);

        } else {
            vertex.sort(compareY);
        }


        for (int i = 0; i < leftSize; i++) {
            leftVertices.add(i, vertex.get(i));
        }

        for (int i = 0; i < rightSize; i++) {
            rightVertices.add(i, vertex.get(median + 1 + i));
        }

        long medID = vertex.get(median);
        rootTree = new TreeVertex(medID, getVertex(medID).getX(), getVertex(medID).getY());
        rootTree.left = twoDTree(leftVertices, depth + 1);
        rootTree.right = twoDTree(rightVertices, depth + 1);
        return rootTree;
    }

    public TreeVertex nearNeighbor(TreeVertex curr, TreeVertex best,
                                   double dist, double x, double y, int d) {
        if (curr == null) {
            return best;
        }
        double currDist, rDist, lDist;
        if (curr.id != best.id) {
            currDist = euclidean(curr.x, x, curr.y, y);
            if (dist > currDist) {
                best = curr;
                dist = currDist;
            }
        }
        TreeVertex r, l;
        if (d % DIMENSION == 0) {
            if (curr.x < x) {
                r = nearNeighbor(curr.right, best, dist, x, y, d + 1);
                rDist = euclidean(r.x, x, r.y, y);
                if (rDist < dist) {
                    best = r;
                    dist = rDist;
                }
                if (Math.abs(x - curr.x) <= dist) {
                    r = nearNeighbor(curr.left, best, dist, x, y, d + 1);
                    rDist = euclidean(r.x, x, r.y, y);
                    if (rDist < dist) {
                        best = r;
                    }
                }
            } else {
                l = nearNeighbor(curr.left, best, dist, x, y, d + 1);
                lDist = euclidean(l.x, x, l.y, y);
                if (lDist < dist) {
                    best = l;
                    dist = lDist;
                }
                if (Math.abs(x - curr.x) <= dist) {
                    l = nearNeighbor(curr.right, best, dist, x, y, d + 1);
                    lDist = euclidean(l.x, x, l.y, y);
                    if (lDist < dist) {
                        best = l;
                    }
                }
            }
        } else {
            if (curr.y < y) {
                r = nearNeighbor(curr.right, best, dist, x, y, d + 1);
                rDist = euclidean(r.x, x, r.y, y);
                if (rDist < dist) {
                    best = r;
                    dist = rDist;
                }
                if (Math.abs(y -  curr.y) <= dist) {
                    r = nearNeighbor(curr.left, best, dist, x, y, d + 1);
                    rDist = euclidean(r.x, x, r.y, y);
                    if (rDist < dist) {
                        best = r;
                    }
                }
            } else {
                l = nearNeighbor(curr.left, best, dist, x, y, d + 1);
                lDist = euclidean(l.x, x, l.y, y);
                if (lDist < dist) {
                    best = l;
                    dist = lDist;
                }
                if (Math.abs(y - curr.y) <= dist) {
                    l = nearNeighbor(curr.right, best, dist, x, y, d + 1);
                    lDist = euclidean(l.x, x, l.y, y);
                    if (lDist < dist) {
                        best = l;
                    }
                }
            }
        }
        return best;
    }

    static double euclidean(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }


    //public Vertex findClosest(TreeVertex t, double x)
    /**
     * Return the Euclidean x-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean x-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToX(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double b = Math.sin(dlon) * Math.cos(phi);
        return (K0 / 2) * Math.log((1 + b) / (1 - b));
    }

    /**
     * Return the Euclidean y-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean y-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToY(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double con = Math.atan(Math.tan(phi) / Math.cos(dlon));
        return K0 * (con - Math.toRadians(ROOT_LAT));
    }

    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        HashSet<String> locationsByPrefix = new HashSet<String>();
        return Collections.emptyList();
    }

    /**
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A <code>List</code> of <code>LocationParams</code> whose cleaned name matches the
     * cleaned <code>locationName</code>
     */
    public List<LocationParams> getLocations(String locationName) {
        HashSet<String> locations = new HashSet<String>();
        return Collections.emptyList();
    }

    /**
     * Returns the initial bearing between vertices <code>v</code> and <code>w</code> in degrees.
     * The initial bearing is the angle that, if followed in a straight line along a great-circle
     * arc from the starting point, would take you to the end point.
     * Assumes the lon/lat methods are implemented properly.
     * @param v The ID for the first vertex.
     * @param w The ID for the second vertex.
     * @return The bearing between <code>v</code> and <code>w</code> in degrees.
     * @source https://www.movable-type.co.uk/scripts/latlong.html
     */
    double bearing(long v, long w) {
        double phi1 = Math.toRadians(lat(v));
        double phi2 = Math.toRadians(lat(w));
        double lambda1 = Math.toRadians(lon(v));
        double lambda2 = Math.toRadians(lon(w));

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /** Radius of the Earth in miles. */
    private static final int R = 3963;
    /** Latitude centered on Berkeley. */
    private static final double ROOT_LAT = (MapServer.ROOT_ULLAT + MapServer.ROOT_LRLAT) / 2;
    /** Longitude centered on Berkeley. */
    private static final double ROOT_LON = (MapServer.ROOT_ULLON + MapServer.ROOT_LRLON) / 2;
    /**
     * Scale factor at the natural origin, Berkeley. Prefer to use 1 instead of 0.9996 as in UTM.
     * @source https://gis.stackexchange.com/a/7298
     */
    private static final double K0 = 1.0;

    private Comparator<Long> compareX = Comparator.comparingDouble(o -> getVertex(o).getX());
    private Comparator<Long> compareY = Comparator.comparingDouble(o -> getVertex(o).getY());
    private final int DIMENSION = 2;
}
