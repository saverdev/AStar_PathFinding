import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String args[]) throws IOException {
        ImageManipulation imageManipulation = new ImageManipulation("/Users/applica-mac/Documents/PathFinding/AStar/AStar_PathFinding/src/main/java/mappa3.jpg", 1);

        boolean[][] grid = imageManipulation.getGrid();

        Node start = new Node(50, 30);
        Node end = new Node(200, 150);

        Dijkstra algorithm = new Dijkstra(grid, start, end);

        ArrayList<Node> path = algorithm.getPath();

        BufferedImage img = imageManipulation.getImg();

        int alpha = 127;
        int red = 0;
        int green = 255;
        int blue = 0;

        int p = alpha << 24 + red << 8 + green << 16 + blue;
        File f = new File("/Users/applica-mac/Documents/PathFinding/AStar/AStar_PathFinding/src/main/java/path.jpg");
        if (path != null) {

            for (Node node : path) {
                img.setRGB(node.y, node.x, p);
            }
            ImageIO.write(img, "jpg", f);

        }
    }
}
