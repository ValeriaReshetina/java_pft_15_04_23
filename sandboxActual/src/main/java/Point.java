public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Point point2) {

        return Math.sqrt(Math.pow((point2.y - this.y), 2) + Math.pow((point2.x - this.x), 2));
    }
}
