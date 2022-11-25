import stdlib_supp.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class PathFindingOnSquaredGrid {

    static Node[][] cell;
    static ArrayList<Node> pathList = new ArrayList<>();
    static ArrayList<Node> closedList = new ArrayList<>();
    static boolean additionalPath = false;

    // draw the N-by-N boolean matrix to standard draw
    public static void show(boolean[][] a, boolean which) {
        int x = a[0].length;
        int y = a.length;
        StdDraw.setXscale(-1, x);
        StdDraw.setYscale(-1, y);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < y; i++)
            for (int j = 0; j < x; j++)
                if (a[i][j] == which)
                    StdDraw.square(j, y - i - 1, 1);
                else StdDraw.filledSquare(j, y - i - 1, 1);
    }

    // draw the N-by-N boolean matrix to standard draw, including the points A (x1, y1) and B (x2,y2) to be marked by a circle

    public static void show(boolean[][] a, boolean which, int x1, int y1, int x2, int y2) {
        int x = a[0].length;
        int y = a.length;
        StdDraw.setXscale(1, x);
        StdDraw.setYscale(1, y);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < y; i++)
            for (int j = 0; j < x; j++)
                if (a[i][j] == which)
                    if (i == x1 && j == y1){
                        StdDraw.setPenColor(StdDraw.GREEN);
                        StdDraw.circle(j, y - i - 1, .5);
                        StdDraw.setPenColor(StdDraw.BLACK);
                    }else if(i == x2 && j == y2){
                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.circle(j, y - i - 1, .5);
                        StdDraw.setPenColor(StdDraw.BLACK);
                    }else StdDraw.square(j, y - i - 1, .5);
                else StdDraw.filledSquare(j, y - i - 1, .5);
    }

    // return a random N-by-N boolean matrix, where each entry is
    // true with probability p
    /**
     * @param matrix         The boolean matrix that the framework generates
     * @param Ai             Starting point's x value
     * @param Aj             Starting point's y value
     * @param Bi             Ending point's x value
     * @param Bj             Ending point's y value
     * @param v              Cost between 2 cells located horizontally or vertically next to each other
     * @param d              Cost between 2 cells located Diagonally next to each other
     * @param additionalPath Boolean to decide whether to calculate the cost of through the diagonal path
     * @param h              int value which decides the correct method to choose to calculate the Heuristic value
     */
    public static void generateHValue(boolean matrix[][], int Ai, int Aj, int Bi, int Bj, int v, int d, boolean additionalPath, int h) {

        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                //Creating a new Node object for each and every Cell of the Grid (Matrix)
                cell[y][x] = new Node(y, x);
                //Checks whether a cell is Blocked or Not by checking the boolean value
                if (!matrix[y][x]) {
                    if (h == 1) {
                        //Assigning the Chebyshev Heuristic value
                        if (Math.abs(y - Bi) > Math.abs(x - Bj)) {
                            cell[y][x].hValue = Math.abs(y - Bi);
                        } else {
                            cell[y][x].hValue = Math.abs(x - Bj);
                        }
                    } else if (h == 2) {
                        //Assigning the Euclidean Heuristic value
                        cell[y][x].hValue = Math.sqrt(Math.pow(y - Bi, 2) + Math.pow(x - Bj, 2));
                    } else if (h == 3) {
                        //Assigning the Manhattan Heuristic value by calculating the absolute length (x+y) from the ending point to the starting point
                        cell[y][x].hValue = Math.abs(y - Bi) + Math.abs(x - Bj);
                    }
                } else {
                    //If the boolean value is false, then assigning -1 instead of the absolute length
                    cell[y][x].hValue = -1;
                }
            }
        }
        generatePath(cell, Ai, Aj, Bi, Bj, v, d, additionalPath);
    }


    public static void menu() throws IOException {
        ImageManipulation imageManipulation = new ImageManipulation("/home/savc18/Documents/Repo/AStar_PathFinding/src/main/java/mappa3.jpg", 1);

        boolean[][] grid = imageManipulation.getGrid();

        int gCost = 0;
        //int fCost = 0;

        //StdArrayIO.print(randomlyGenMatrix);
        //show(grid, false);

        //n = grid size


        //Creation of a Node type 2D array
        cell = new Node[grid.length][grid[0].length];

        /*Scanner in = new Scanner(System.in);


        System.out.println("Enter x1: ");
        int startX = in.nextInt(); //Aj

        System.out.println("Enter y1: ");
        int startY = in.nextInt(); //Ai

        System.out.println("Enter x2: ");
        int endX = in.nextInt(); //Bj

        System.out.println("Enter y2: ");
        int endY = in.nextInt(); //Bi

        */

        int startX = 47;
        int startY = 38;
        int endX = 207;
        int endY = 142;


        show(grid, false, startY, startX, endY, endX);


        Stopwatch timerFlow = new Stopwatch();
        generateHValue(grid, startY, startX, endY, endX, 10, 10, false, 3);

        if (cell[startY][startX].hValue != -1 && pathList.contains(cell[endY][endX])) {
            StdDraw.setPenColor(Color.orange);
            StdDraw.setPenRadius(0.006);

            for (int i = 0; i < pathList.size() - 1; i++) {
            //System.out.println(pathList.get(i).x + " " + pathList.get(i).y);
            //StdDraw.filledCircle(pathList.get(i).y, n - pathList.get(i).x - 1, .2);
                StdDraw.line(pathList.get(i).y, grid.length - 1 - pathList.get(i).x, pathList.get(i + 1).y, grid.length - 1 - pathList.get(i + 1).x);
                gCost += pathList.get(i).gValue;
                //fCost += pathList.get(i).fValue;
            }

            System.out.println("Manhattan Path Found");
            System.out.println("Total Cost: " + gCost/10);
            //System.out.println("Total fCost: " + fCost);
            StdOut.println("Elapsed time = " + timerFlow.elapsedTime());
            gCost = 0;
            //fCost = 0;

        } else {
            System.out.println("Manhattan Path Not found");
            StdOut.println("Elapsed time = " + timerFlow.elapsedTime());
        }

        BufferedImage img = imageManipulation.getImg();
        int p;
        int alpha = 1;
        int red = 255;
        int green = 0;
        int blue = 0;

        //set the pixel value
        p =  (alpha>>24) | (red>>16) | (green>>8) | blue;


        if (pathList != null) {
            File f = new File("/home/savc18/Documents/Repo/AStar_PathFinding/src/main/java/path.jpg");
            for (Node node : pathList) {
                img.setRGB(node.y, node.x, p);
                ImageIO.write(img, "jpg", f);
                System.out.println("(" + node.x + ", " + node.y + ")");
            }
        }
    }

    /**
     * @param hValue         Node type 2D Array (Matrix)
     * @param Ai             Starting point's y value
     * @param Aj             Starting point's x value
     * @param Bi             Ending point's y value
     * @param Bj             Ending point's x value
     * @param v              Cost between 2 cells located horizontally or vertically next to each other
     * @param d              Cost between 2 cells located Diagonally next to each other
     * @param additionalPath Boolean to decide whether to calculate the cost of through the diagonal path
     */
    public static void generatePath(Node hValue[][], int Ai, int Aj, int Bi, int Bj, int v, int d, boolean additionalPath) {

        //Creation of a PriorityQueue and the declaration of the Comparator
        PriorityQueue<Node> openList = new PriorityQueue<>(11, new Comparator() {
            @Override
            //Compares 2 Node objects stored in the PriorityQueue and Reorders the Queue according to the object which has the lowest fValue
            public int compare(Object cell1, Object cell2) {
                return Double.compare(((Node) cell1).fValue, ((Node) cell2).fValue);
            }
        });

        //Adds the Starting cell inside the openList
        openList.add(cell[Ai][Aj]);

        //Executes the rest if there are objects left inside the PriorityQueue
        while (true) {

            //Gets and removes the objects that's stored on the top of the openList and saves it inside node
            Node node = openList.poll();

            //Checks if whether node is empty and f it is then breaks the while loop
            if (node == null) {
                break;
            }

            //Checks if whether the node returned is having the same node object values of the ending point
            //If it des then stores that inside the closedList and breaks the while loop
            if (node == cell[Bi][Bj]) {
                closedList.add(node);
                break;
            }

            closedList.add(node);

            //Left Cell
            try {
                if (cell[node.x][node.y - 1].hValue != -1
                        && !openList.contains(cell[node.x][node.y - 1])
                        && !closedList.contains(cell[node.x][node.y - 1])) {
                    double tCost = node.fValue + v;
                    cell[node.x][node.y - 1].gValue = v;
                    double cost = cell[node.x][node.y - 1].hValue + tCost;
                    if (cell[node.x][node.y - 1].fValue > cost || !openList.contains(cell[node.x][node.y - 1]))
                        cell[node.x][node.y - 1].fValue = cost;

                    openList.add(cell[node.x][node.y - 1]);
                    cell[node.x][node.y - 1].parent = node;
                }
            } catch (IndexOutOfBoundsException e) {
            }

            //Right Cell
            try {
                if (cell[node.x][node.y + 1].hValue != -1
                        && !openList.contains(cell[node.x][node.y + 1])
                        && !closedList.contains(cell[node.x][node.y + 1])) {
                    double tCost = node.fValue + v;
                    cell[node.x][node.y + 1].gValue = v;
                    double cost = cell[node.x][node.y + 1].hValue + tCost;
                    if (cell[node.x][node.y + 1].fValue > cost || !openList.contains(cell[node.x][node.y + 1]))
                        cell[node.x][node.y + 1].fValue = cost;

                    openList.add(cell[node.x][node.y + 1]);
                    cell[node.x][node.y + 1].parent = node;
                }
            } catch (IndexOutOfBoundsException e) {
            }

            //Bottom Cell
            try {
                if (cell[node.x + 1][node.y].hValue != -1
                        && !openList.contains(cell[node.x + 1][node.y])
                        && !closedList.contains(cell[node.x + 1][node.y])) {
                    double tCost = node.fValue + v;
                    cell[node.x + 1][node.y].gValue = v;
                    double cost = cell[node.x + 1][node.y].hValue + tCost;
                    if (cell[node.x + 1][node.y].fValue > cost || !openList.contains(cell[node.x + 1][node.y]))
                        cell[node.x + 1][node.y].fValue = cost;

                    openList.add(cell[node.x + 1][node.y]);
                    cell[node.x + 1][node.y].parent = node;
                }
            } catch (IndexOutOfBoundsException e) {
            }

            //Top Cell
            try {
                if (cell[node.x - 1][node.y].hValue != -1
                        && !openList.contains(cell[node.x - 1][node.y])
                        && !closedList.contains(cell[node.x - 1][node.y])) {
                    double tCost = node.fValue + v;
                    cell[node.x - 1][node.y].gValue = v;
                    double cost = cell[node.x - 1][node.y].hValue + tCost;
                    if (cell[node.x - 1][node.y].fValue > cost || !openList.contains(cell[node.x - 1][node.y]))
                        cell[node.x - 1][node.y].fValue = cost;

                    openList.add(cell[node.x - 1][node.y]);
                    cell[node.x - 1][node.y].parent = node;
                }
            } catch (IndexOutOfBoundsException e) {
            }

            if (additionalPath) {

                //TopLeft Cell
                try {
                    if (cell[node.x - 1][node.y - 1].hValue != -1
                            && !openList.contains(cell[node.x - 1][node.y - 1])
                            && !closedList.contains(cell[node.x - 1][node.y - 1])) {
                        double tCost = node.fValue + d;
                        cell[node.x - 1][node.y - 1].gValue = d;
                        double cost = cell[node.x - 1][node.y - 1].hValue + tCost;
                        if (cell[node.x - 1][node.y - 1].fValue > cost || !openList.contains(cell[node.x - 1][node.y - 1]))
                            cell[node.x - 1][node.y - 1].fValue = cost;

                        openList.add(cell[node.x - 1][node.y - 1]);
                        cell[node.x - 1][node.y - 1].parent = node;
                    }
                } catch (IndexOutOfBoundsException e) {
                }

                //TopRight Cell
                try {
                    if (cell[node.x - 1][node.y + 1].hValue != -1
                            && !openList.contains(cell[node.x - 1][node.y + 1])
                            && !closedList.contains(cell[node.x - 1][node.y + 1])) {
                        double tCost = node.fValue + d;
                        cell[node.x - 1][node.y + 1].gValue = d;
                        double cost = cell[node.x - 1][node.y + 1].hValue + tCost;
                        if (cell[node.x - 1][node.y + 1].fValue > cost || !openList.contains(cell[node.x - 1][node.y + 1]))
                            cell[node.x - 1][node.y + 1].fValue = cost;

                        openList.add(cell[node.x - 1][node.y + 1]);
                        cell[node.x - 1][node.y + 1].parent = node;
                    }
                } catch (IndexOutOfBoundsException e) {
                }

                //BottomLeft Cell
                try {
                    if (cell[node.x + 1][node.y - 1].hValue != -1
                            && !openList.contains(cell[node.x + 1][node.y - 1])
                            && !closedList.contains(cell[node.x + 1][node.y - 1])) {
                        double tCost = node.fValue + d;
                        cell[node.x + 1][node.y - 1].gValue = d;
                        double cost = cell[node.x + 1][node.y - 1].hValue + tCost;
                        if (cell[node.x + 1][node.y - 1].fValue > cost || !openList.contains(cell[node.x + 1][node.y - 1]))
                            cell[node.x + 1][node.y - 1].fValue = cost;

                        openList.add(cell[node.x + 1][node.y - 1]);
                        cell[node.x + 1][node.y - 1].parent = node;
                    }
                } catch (IndexOutOfBoundsException e) {
                }

                //BottomRight Cell
                try {
                    if (cell[node.x + 1][node.y + 1].hValue != -1
                            && !openList.contains(cell[node.x + 1][node.y + 1])
                            && !closedList.contains(cell[node.x + 1][node.y + 1])) {
                        double tCost = node.fValue + d;
                        cell[node.x + 1][node.y + 1].gValue = d;
                        double cost = cell[node.x + 1][node.y + 1].hValue + tCost;
                        if (cell[node.x + 1][node.y + 1].fValue > cost || !openList.contains(cell[node.x + 1][node.y + 1]))
                            cell[node.x + 1][node.y + 1].fValue = cost;

                        openList.add(cell[node.x + 1][node.y + 1]);
                        cell[node.x + 1][node.y + 1].parent = node;
                    }
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }

        /*for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                System.out.print(cell[i][j].fValue + "    ");
            }
            System.out.println();
        }*/

        //Assigns the last Object in the closedList to the endNode variable
        Node endNode = closedList.get(closedList.size() - 1);

        //Checks if whether the endNode variable currently has a parent Node. if it doesn't then stops moving forward.
        //Stores each parent Node to the PathList so it is easier to trace back the final path
        while (endNode.parent != null) {
            Node currentNode = endNode;
            pathList.add(currentNode);
            endNode = endNode.parent;
        }

        pathList.add(cell[Ai][Aj]);
        //Clears the openList
        openList.clear();

        System.out.println();

    }


    public static void main(String[] args) throws IOException {
        menu();
    }
}