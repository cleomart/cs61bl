import java.util.Objects;

/**
 * Edge Class:
 * Represents the path or roads in our map
 */
public class Edge implements Comparable<Edge> {
    /* Source of this edge/path */
    Long from;
    /* Destination of this edge/path */
    Long to;
    /* Weight of this edge/path */
    double distance;

    String name;

    /* Constructor */
    public Edge(Long from, Long to, String name, double distance) {
        this.from = from;
        this.to = to;
        this.name = name;
        this.distance = distance;
    }

    /* Returns the source of this edge/path */
    public Long getFrom() {
        return from;
    }

    /* Returns the destination of this edge/path */
    public Long getTo() {
        return to;
    }

    /* Returns the weight of this edge/path */
    public double getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Edge e) {
        return (int)  Math.round(this.distance - e.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, distance, name);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Edge e = (Edge) o;
        return (from.equals(e.from) && to.equals(e.to) && distance == e.distance)
                || (from.equals(e.to) && to.equals(e.from) && distance == e.distance);
    }
}
