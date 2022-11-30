import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    private boolean[][] grid;
    private Node[][] cell;
    private ArrayList<Node> path;
    private ArrayList<Node> closedList;
    private ArrayList<ArrayList<Node>> pathMultiplePoints;

    public Dijkstra(boolean[][] grid, Node start, Node end){
        path = new ArrayList<>();
        closedList = new ArrayList<>();
        this.cell = new Node[grid.length][grid[0].length];
        this.grid = grid;
        EuclidianMethod(this.grid, start.y, start.x, end.y, end.x);
    }

    public Dijkstra(boolean[][] grid, ArrayList<Node> interestPoint){
        path = new ArrayList<>();
        closedList = new ArrayList<>();
        this.grid = grid;
        this.cell = new Node[grid.length][grid[0].length];
        this.getPath(interestPoint, grid);
    }

    private ArrayList<Node> EuclidianMethod(boolean[][] grid, int startY, int startX, int endY, int endX){
        int gCost = 0;
        ArrayList<Node> slicedPath = this.generateHValue(grid, startY, startX, endY, endX, 10, 10, false, 3);

        if (cell[startY][startX].hValue!=-1 && path.contains(cell[endY][endX])) {
            for (int i = 0; i < path.size() - 1; i++) {
                gCost += path.get(i).gValue;
            }
            System.out.println("Euclidean Path Found");
            System.out.println("Total Cost: " + gCost/10.0);
            gCost = 0;

        } else {
            System.out.println("Euclidean Path Not found");
        }
        return slicedPath;
    }
    private ArrayList<Node> generateHValue(boolean matrix[][], int startY, int startX, int endY, int endX, int v, int d, boolean additionalPath, int h) {

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
        return this.generatePath(cell, startY, startX, endY, endX, v, d, additionalPath);
    }

    private ArrayList<Node> generatePath(Node hValue[][], int startY, int startX, int endY, int endX, int v, int d, boolean additionalPath){
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

        Node endNode = closedList.get(closedList.size() - 1);

        while (endNode.parent != null) {
            Node currentNode = endNode;
            this.path.add(currentNode);
            endNode = endNode.parent;
        }

        this.path.add(this.cell[startY][startX]);

        return path;
    }

    private void getPath(ArrayList<Node> interestPoint, boolean[][] grid){
        ArrayList<ArrayList<Node>> completePath = new ArrayList<ArrayList<Node>>();
        for(int i = 0; i < interestPoint.size() - 1; i++){
            completePath.add(EuclidianMethod(grid, interestPoint.get(i).y, interestPoint.get(i).x, interestPoint.get(i + 1).y, interestPoint.get(i + 1).x));
        }
        this.pathMultiplePoints = completePath;
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }

    public Node[][] getCell() {
        return cell;
    }

    public void setCell(Node[][] cell) {
        this.cell = cell;
    }

    public ArrayList<Node> getPath() {
        return path;
    }

    public void setPath(ArrayList<Node> path) {
        this.path = path;
    }

    public ArrayList<Node> getClosedList() {
        return closedList;
    }

    public void setClosedList(ArrayList<Node> closedList) {
        this.closedList = closedList;
    }

    public ArrayList<ArrayList<Node>> getPathMultiplePoints() {
        return pathMultiplePoints;
    }

    public void setPathMultiplePoints(ArrayList<ArrayList<Node>> pathMultiplePoints) {
        this.pathMultiplePoints = pathMultiplePoints;
    }
}
