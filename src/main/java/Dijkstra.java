import stdlib_supp.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    private boolean[][] grid;
    private Node[][] cell;
    private ArrayList<Node> path;
    private ArrayList<Node> closedList;

    public Dijkstra(boolean[][] grid, Node start, Node end){
        path = new ArrayList<>();
        closedList = new ArrayList<>();
        this.cell = new Node[grid.length][grid[0].length];
        this.grid = grid;
        this.show(this.grid, false, start.y, start.x, end.y, end.x);
        EuclidianMethod(this.grid, start.y, start.x, end.y, end.x);
    }

    private void show(boolean[][] a, boolean which) {
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

    private void show(boolean[][] a, boolean which, int x1, int y1, int x2, int y2) {
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

    private void EuclidianMethod(boolean[][] grid, int startY, int startX, int endY, int endX){
        int gCost = 0;
        this.generateHValue(grid, startY, startX, endY, endX, 10, 10, true, 3);

        if (cell[startY][startX].hValue!=-1 && path.contains(cell[endY][endX])) {
            StdDraw.setPenColor(Color.ORANGE);
            StdDraw.setPenRadius(0.01);

            for (int i = 0; i < path.size() - 1; i++) {
                StdDraw.line(path.get(i).y, grid.length - 1 - path.get(i).x, path.get(i + 1).y, grid.length - 1 - path.get(i + 1).x);
                gCost += path.get(i).gValue;
            }

            System.out.println("Euclidean Path Found");
            System.out.println("Total Cost: " + gCost/10.0);
            gCost = 0;

        } else {
            System.out.println("Euclidean Path Not found");
        }
    }
    private void generateHValue(boolean matrix[][], int startY, int startX, int endY, int endX, int v, int d, boolean enableDiagonal, int h) {

        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                //Creating a new Node object for each and every Cell of the Grid (Matrix)
                cell[y][x] = new Node(y, x);
                //Checks whether a cell is Blocked or Not by checking the boolean value
                if (!matrix[y][x]) {
                    if (h == 1) {
                        //Assigning the Chebyshev Heuristic value
                        if (Math.abs(y - endY) > Math.abs(x - endX)) {
                            cell[y][x].hValue = Math.abs(y - endY);
                        } else {
                            cell[y][x].hValue = Math.abs(x - endX);
                        }
                    } else if (h == 2) {
                        //Assigning the Euclidean Heuristic value
                        cell[y][x].hValue = Math.sqrt(Math.pow(y - endY, 2) + Math.pow(x - endX, 2));
                    } else if (h == 3) {
                        //Assigning the Manhattan Heuristic value by calculating the absolute length (x+y) from the ending point to the starting point
                        cell[y][x].hValue = Math.abs(y - endY) + Math.abs(x - endX);
                    }
                } else {
                    //If the boolean value is false, then assigning -1 instead of the absolute length
                    cell[y][x].hValue = -1;
                }
            }
        }
        this.generatePath(cell, startY, startX, endY, endX, v, d, enableDiagonal);
    }

    private void generatePath(Node hValue[][], int startY, int startX, int endY, int endX, int v, int d, boolean enableDiagonal){
        PriorityQueue<Node> openList = new PriorityQueue<>(11, new Comparator() {
            @Override
            public int compare(Object cell1, Object cell2) {
                return Double.compare(((Node) cell1).fValue, ((Node) cell2).fValue);
            }
        });

        openList.add(this.cell[startY][startX]);

        while(true){
            Node node = openList.poll();
            if (node == null) {
                break;
            }
            if (node == cell[endY][endX]) {
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

        Node endNode = closedList.get(closedList.size() - 1);

        while (endNode.parent != null) {
            Node currentNode = endNode;
            this.path.add(currentNode);
            endNode = endNode.parent;
        }

        this.path.add(this.cell[startY][startX]);
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public Node[][] getCell() {
        return cell;
    }

    public ArrayList<Node> getPath() {
        return path;
    }

    public ArrayList<Node> getClosedList() {
        return closedList;
    }
}
