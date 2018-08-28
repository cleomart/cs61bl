import java.util.Objects;

/**
 * Vertex Class: represents the location, places or intersection
 * in our map
 */

public class Vertex {
    /* corresponding longtiude of this node */
    private double longitude;
    /* corresponding longtiude of this node */
    private double latitude;

    private double x;

    private double y;
    /* unique reference of this node*/
    private long iD;
    /* Name of the node if it is a location */
    String name;

    /* Constructor */
    public Vertex(double lon, double lat, long iD, double x, double y) {
        longitude = lon;
        latitude = lat;
        this.iD = iD;
        name = null;
        this.x = x;
        this.y = y;
    }

    /* Returns the longitude of this node */
    public double getLon() {
        return longitude;
    }

    /* Returns the latitude of this node */
    public double getLat() {
        return latitude;
    }

    /* Returns the id of this node */
    public long getiD() {
        return iD;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /* Returns the name of this node */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude, x, y, iD, name);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vertex n = (Vertex) o;
        return (longitude == n.getLon() && latitude == n.getLat() && iD == n.getiD());
    }
}

