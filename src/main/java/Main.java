import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException {
        ImageManipulation imageManipulation = new ImageManipulation("/home/savc18/Documents/Repo/AStar_PathFinding/src/main/java/mappa3.jpg", 1);

        boolean[][] grid = imageManipulation.getGrid();

        Node start = new Node(50, 50);
        Node end = new Node(120, 50);

        Dijkstra algorithm = new Dijkstra(grid, start, end);

    }
}
