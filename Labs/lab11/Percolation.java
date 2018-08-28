// import edu.princeton.cs.algs4.QuickFindUF;
// import edu.princeton.cs.algs4.QuickUnionUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF open;
    private WeightedQuickUnionUF backwash;
    private int N;
    private int openedSites;

    /* Creates an N-by-N grid with all sites initially blocked. */
    public Percolation(int N) {

        this.N = N;
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = false;
            }
        }
        open = new WeightedQuickUnionUF(N * N + 2);
        backwash = new WeightedQuickUnionUF(N * N + 2);
        for (int col = 0; col < N; col++) {
            int target = xyTo1D(0, col);
            open.union(N * N, target);
            backwash.union(N * N, target);
            target = xyTo1D(N - 1, col);
            open.union(N * N + 1, target);
        }
    }

    /* Opens the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        try {
            if (!grid[row][col]) {
                grid[row][col] = true;
                openedSites += 1;
            }
            if (isOpen(row + 1, col)) {
                open.union(xyTo1D(row, col), xyTo1D(row + 1, col));
                backwash.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            }
            if (isOpen(row - 1, col)) {
                open.union(xyTo1D(row, col), xyTo1D(row - 1, col));
                backwash.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            }
            if (isOpen(row, col + 1)) {
                open.union(xyTo1D(row, col), xyTo1D(row, col + 1));
                backwash.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            }
            if (isOpen(row, col - 1)) {
                open.union(xyTo1D(row, col), xyTo1D(row, col - 1));
                backwash.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.print("IndexOutOfBound Error");
        }
    }

    /* Returns true if the site at (row, col) is open. */
    public boolean isOpen(int row, int col) {
        if (!valid(row, col)) {
            return false;
        }
        return grid[row][col];
    }

    /* Returns true if the site (row, col) is full. */
    public boolean isFull(int row, int col) {
        if (!valid(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return open.connected(N * N, xyTo1D(row, col))
                && isOpen(row, col) && backwash.connected(N * N, xyTo1D(row, col));
    }

    /* Returns the number of open sites. */
    public int numberOfOpenSites() {
        return openedSites;
    }

    /* Returns true if the system percolates. */
    public boolean percolates() {
        if (N <= 1) {
            return isOpen(0, 0);
        }
        return open.connected(N * N, N * N + 1);
    }

    /* Converts row and column coordinates into a number. This will be helpful
       when trying to tie in the disjoint sets into our NxN grid of sites. */
    private int xyTo1D(int row, int col) {
        if (!valid(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return row * N + col;
    }

    /* Returns true if (row, col) site exists in the NxN grid of sites.
       Otherwise, return false. */
    private boolean valid(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            return false;
        }
        return true;
    }
}