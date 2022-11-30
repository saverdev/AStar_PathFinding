import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String args[]) throws IOException {
        ImageManipulation imageManipulation = new ImageManipulation("/home/savc18/Documents/Repo/AStar_PathFinding/src/main/java/mappa4.jpg", 1);

        boolean[][] grid = imageManipulation.getGrid();

        /*Node start = new Node(50, 50);
        Node end = new Node(120, 50);

        Dijkstra algorithm = new Dijkstra(grid, start, end);*/

        ArrayList<Node> interestPoint = new ArrayList<>();

        interestPoint.add(new Node(100, 57));
        interestPoint.add(new Node(187, 151));
        interestPoint.add(new Node(289, 151));
        interestPoint.add(new Node(187, 237));
        interestPoint.add(new Node(382, 107));
        interestPoint.add(new Node(352, 243));

        Dijkstra algorithm = new Dijkstra(grid, interestPoint);

        ArrayList<ArrayList<Node>> path = algorithm.getPathMultiplePoints();

        BufferedImage img = imageManipulation.getImg();

        Color colorToUse = new Color(0, 0, 255);

        imageManipulation.drawPath(path, "/home/savc18/Documents/Repo/AStar_PathFinding/src/main/java/path.jpg", colorToUse, 8888);


    }
}
