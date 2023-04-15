package ru.stqa.pft.sandbox;


public class ClassMain {

    public static void main(String[] args) {

        Point point = new Point(2, 8);
        Point point2 = new Point(5, 10);
        double distance = distance(point, point2);
        System.out.println(distance);

        Point point3 = new Point(6,3);
        Point point4 = new Point(4,9);
        double distance2 = point3.distance(point4);
        System.out.println(distance2);
    }

    public static double distance(Point point1, Point point2) {

        return Math.sqrt(Math.pow((point2.y - point1.y), 2) + Math.pow((point2.x - point1.x), 2));
    }
}
