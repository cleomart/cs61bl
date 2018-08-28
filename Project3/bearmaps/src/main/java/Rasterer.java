/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    /**
     * The max image depth level.
     */
    public static final int MAX_DEPTH = 7;
    public static final int DEGREE_TO_FEET = 288200;
    double base, height;
    int xFirst, xLast, yFirst, yLast, depth, maxTile;

    private void calculateBH(int input) {
        base = baseOfTile(input);
        height = heightOfTile(input);
        depth = input;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These images
     * will be combined into one big image (rastered) by the front end. The grid of images must obey
     * the following properties, where image in the grid is referred to as a "tile".
     * <ul>
     * <li>The tiles collected must cover the most longitudinal distance per pixel (LonDPP)
     * possible, while still covering less than or equal to the amount of longitudinal distance
     * per pixel in the query box for the user viewport size.</li>
     * <li>Contains all tiles that intersect the query bounding box that fulfill the above
     * condition.</li>
     * <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params The RasterRequestParams containing coordinates of the query box and the browser
     *               viewport width and height.
     * @return A valid RasterResultParams containing the computed results.
     */
    public RasterResultParams getMapRaster(RasterRequestParams params) {
        if (params.ullat < params.lrlat || params.ullon > params.lrlon
                || (params.ullat > MapServer.ROOT_ULLAT && params.lrlat > MapServer.ROOT_ULLAT)
                || (params.ullat < MapServer.ROOT_LRLAT && params.lrlat < MapServer.ROOT_LRLAT)
                || (params.ullon > MapServer.ROOT_LRLON && params.lrlon > MapServer.ROOT_LRLON)
                || (params.ullon < MapServer.ROOT_ULLON && params.lrlon < MapServer.ROOT_ULLON)) {
            return RasterResultParams.queryFailed();
        }


        double lonDPP = lonDPP(params.lrlon, params.ullon, params.w) * DEGREE_TO_FEET;

        if (lonDPP >= resolution(0)) {
            calculateBH(0);
        } else if (lonDPP >= resolution(1)) {
            calculateBH(1);
        } else if (lonDPP >= resolution(2)) {
            calculateBH(2);
        } else if (lonDPP >= resolution(3)) {
            calculateBH(3);
        } else if (lonDPP >= resolution(4)) {
            calculateBH(4);
        } else if (lonDPP >= resolution(5)) {
            calculateBH(5);
        } else if (lonDPP >= resolution(6)) {
            calculateBH(6);
        } else {
            calculateBH(7);
        }

        yFirst = (int) Math.floor((MapServer.ROOT_ULLAT - params.ullat) / height);
        yLast = (int) Math.floor((MapServer.ROOT_ULLAT - params.lrlat) / height);
        xFirst = (int) Math.floor((params.ullon - MapServer.ROOT_ULLON) / base);
        xLast = (int) Math.floor((params.lrlon - MapServer.ROOT_ULLON) / base);
        maxTile = (int) Math.pow(2, depth) - 1;

        if (yLast > maxTile) {
            yLast = maxTile;
        }
        if (xLast > maxTile) {
            xLast = maxTile;
        }
        if (yFirst < 0) {
            yFirst = 0;
        }
        if (xFirst < 0) {
            xFirst = 0;
        }

        String[][] images = new String[yLast - yFirst + 1][xLast - xFirst + 1];
        for (int i = 0; i <= (yLast - yFirst); i++) {
            for (int j = 0; j <= (xLast - xFirst); j++) {
                images[i][j] = "d" + Integer.toString(depth) + "_x" + Integer.toString(xFirst + j)
                        + "_y" + Integer.toString(yFirst + i) + ".png";
            }
        }

        RasterResultParams.Builder build = new RasterResultParams.Builder();
        build.setRenderGrid(images);
        double ullon = MapServer.ROOT_ULLON + (xFirst * base);
        double ullat = MapServer.ROOT_ULLAT - (yFirst * height);
        double lrlat = MapServer.ROOT_ULLAT - ((yLast + 1) * height);
        double lrlon = MapServer.ROOT_ULLON + ((xLast + 1) * base);
        build.setRasterUlLon(ullon);
        build.setRasterUlLat(ullat);
        build.setRasterLrLat(lrlat);
        build.setRasterLrLon(lrlon);
        build.setDepth(depth);
        build.setQuerySuccess(true);
        return build.create();
    }

    /**
     * Calculates the lonDPP of an image or query box
     * @param lrlon Lower right longitudinal value of the image or query box
     * @param ullon Upper left longitudinal value of the image or query box
     * @param width Width of the query box or image
     * @return lonDPP
     */
    private double lonDPP(double lrlon, double ullon, double width) {
        return (lrlon - ullon) / width;
    }

    /**
     * Calculates the resolution of a given depth
     *
     */
    private double resolution(int d) {
        return 49.472808837890625 / Math.pow(2, d - 1);
    }

    /**
     * Calculates the height of each tile of a given depth of an image
     *
     */
    private double heightOfTile(int d) {
        return MapServer.ROOT_LAT_DELTA / Math.pow(2, d);
    }

    /**
     * Calculates the base of each tile of a given depth of an image
     */
    private double baseOfTile(int d) {
        return MapServer.ROOT_LON_DELTA / Math.pow(2, d);
    }
}
