/** A class that represents a path via pursuit curves. */
public class Path {
	public Point curr;
	public Point next;

	public Path(double x, double y) {
		next = new Point(x,y);
		curr = new Point();
	}

	void iterate(double dx, double dy) {
		curr.x = next.x;
		curr.y = next.y;
		next.x += dx;
		next.y += dy;
	}


}
