public class TreeVertex {
    long id;
    TreeVertex left;
    TreeVertex right;
    double x;
    double y;
    int size = 0;

    public TreeVertex(long id, double x, double y) {
        this.id = id; left = right = null;
        this.x = x;
        this.y = y;
    }
    public TreeVertex(long id, double x, double y, TreeVertex left, TreeVertex right) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.left = left;
        this.right = right;
    }

}
