import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String args[]) throws IOException {
        ImageManipulation imageManipulation = new ImageManipulation("/home/savc18/Documents/Repo/AStar_PathFinding/src/main/java/mappa3.jpg", 1);

        boolean[][] grid = imageManipulation.getGrid();

        /*Node start = new Node(50, 50);
        Node end = new Node(120, 50);

        Dijkstra algorithm = new Dijkstra(grid, start, end);*/

        ArrayList<Node> interestPoint = new ArrayList<>();

        interestPoint.add(new Node(48, 34));
        interestPoint.add(new Node(50, 143));
        interestPoint.add(new Node(60, 68));
        interestPoint.add(new Node(87, 142));
        interestPoint.add(new Node(135, 52));
        interestPoint.add(new Node(96, 75));

        Dijkstra algorithm = new Dijkstra(grid, interestPoint);

        ArrayList<ArrayList<Node>> path = algorithm.getPathMultiplePoints();

        BufferedImage img = imageManipulation.getImg();

        int p;
        int alpha = 1;
        int red = 255;
        int green = 0;
        int blue = 0;

        //set the pixel value
        p =  (alpha>>24) | (red>>16) | (green>>8) | blue;

        if (path != null) {
            File f = new File("/home/savc18/Documents/Repo/AStar_PathFinding/src/main/java/path.jpg");
            for(ArrayList<Node> slicedPath : path){
                for(Node point: slicedPath){
                    img.setRGB(point.y, point.x, p);
                    ImageIO.write(img, "jpg", f);
                }
            }
        }


    }
}
