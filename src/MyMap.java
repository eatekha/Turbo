import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Stack;

public class MyMap {
    private Graph roadMap;    //A graph representing the the roadMap
    private int privateRoads, constructionRoads;    //The number of bombs available for use
    private Node startNode, endNode;    //The start and endpoint of the roadmap

    /**
     * @param inputFile file we used to build graph
     * @throws MapException if file does not exist throw Map Exception
     */
    public MyMap(String inputFile) throws MapException {
        variableAssign(inputFile);
    }

    /**
     * @return roadmap
     */
    public Graph getGraph() {
        return roadMap;
    }

    /**
     * @return id of the starting node
     */
    public int getStartingNode() {
        return startNode.getId();
    }

    /**
     * @return id of destination node
     */
    public int getDestinationNode() {
        return endNode.getId();
    }

    /**
     * @return maximum number of private roads
     */
    public int maxPrivateRoads() {
        return privateRoads;
    }

    /**
     * @return maximum number of construction roads
     */
    public int maxConstructionRoads() {
        return constructionRoads;
    }

    /**
     * @param start           start destination
     * @param destination     destination node
     * @param maxPrivate      max # of private roads
     * @param maxConstruction max # of public roads
     * @return iterator showing path
     * @throws GraphException call-safe for map
     */
    public Iterator<Node> findPath(int start, int destination, int maxPrivate, int maxConstruction) throws GraphException {
        Stack<Node> path = new Stack<>();
        path.push(roadMap.getNode(start));
        pathFinder(start, destination, maxPrivate, maxConstruction, path);
        return path.iterator();
    }

    /**
     * @param start
     * @param destination
     * @param maxPrivate:      max # of private roads
     * @param maxConstruction: max # of construction roads
     * @param path             stack representing available path, if possible
     * @return null/path
     * @throws GraphException: call-safe for map
     */
    private Stack<Node> pathFinder(int start, int destination, int maxPrivate, int maxConstruction, Stack<Node> path) throws GraphException {
        //Checks if start is equal to the destination and if the start node is equal to the destination
        if (start == destination) return path;
        Iterator<Edge> incidentEdges = roadMap.incidentEdges(roadMap.getNode(start));
        //Iterates through all the available edges
        if (incidentEdges.hasNext()) {
            Edge current;
            Node secondNode;
            //Public Roads
            //current edge you are iterating through
            while (incidentEdges.hasNext()) {
                current = incidentEdges.next();
                secondNode = current.secondNode();
                if (searchRoad(start, path, "Public", current)) {
                    if (pathFinder(secondNode.getId(), destination, maxPrivate, maxConstruction, path) == null) {
                        secondNode.markNode(false);
                        path.remove(secondNode);
                    } else
                        return path;
                }
            }
            //Private Roads
            incidentEdges = roadMap.incidentEdges(roadMap.getNode(start));
            while (incidentEdges.hasNext()) {
                current = incidentEdges.next();
                secondNode = current.secondNode();
                if (maxPrivate > 0) {
                    if (!secondNode.getMark() && current.getType().equals("Private")) {
                        secondNode.markNode(true);
                        roadMap.getNode(start).markNode(true);
                        path.push(secondNode);
                        if (pathFinder(secondNode.getId(), destination, maxPrivate - 1, maxConstruction, path) == null) {
                            secondNode.markNode(false);
                            path.remove(secondNode);
                        } else
                            return path;
                    }
                }
            }
            //Construction Roads
            incidentEdges = roadMap.incidentEdges(roadMap.getNode(start));
            while (incidentEdges.hasNext()) {
                current = incidentEdges.next();
                secondNode = current.secondNode();
                if (maxConstruction > 0) {
                    if (!secondNode.getMark() && current.getType().equals("Construction")) {
                        roadMap.getNode(start).markNode(true);
                        secondNode.markNode(true);
                        path.push(secondNode);
                        if (pathFinder(secondNode.getId(), destination, maxPrivate, maxConstruction - 1, path) == null) {
                            secondNode.markNode(false);
                            path.remove(secondNode);
                        } else
                            return path;
                    }
                }
            }
        }
        return null;
    }

    //Helper method that searches any type of road
    private boolean searchRoad(int start, Stack<Node> path, String type, Edge currentEdge) throws GraphException {
        if (!currentEdge.secondNode().getMark() && currentEdge.getType().equals(type)) {
            currentEdge.secondNode().markNode(true);
            roadMap.getNode(start).markNode(true);
            path.push(currentEdge.secondNode());
            return true;
        }
        return false;
    }

    /**
     * @param inputFile the inputFile we are using for our pathfinder
     * @throws MapException: if issue with input file
     *                       Assigns important variables such as width,length, endNode, startNode;
     */
    private void variableAssign(String inputFile) throws MapException {
        int current = 0, first = 0;
        try {
            //reading in important information of line
            BufferedReader input = new BufferedReader(new FileReader(inputFile));
            input.readLine(); //reads first line, nothing is done with it
            //Start Node
            String line = input.readLine();
            int start_id = Integer.parseInt(line);
            //End Node
            line = input.readLine();
            int end_id = Integer.parseInt(line);

            //Graph is created of size width*length
            line = input.readLine();
            int width = Integer.parseInt(line);
            //Read the length from the input file
            line = input.readLine();
            int length = Integer.parseInt(line);
            //initializes roadmap
            roadMap = new Graph(width * length);
            startNode = roadMap.getNode(start_id);
            endNode = roadMap.getNode(end_id);
            //Private Road
            line = input.readLine();
            privateRoads = Integer.parseInt(line);
            //Construction Road
            line = input.readLine();
            constructionRoads = Integer.parseInt(line);
            //Reading in the elements of Graph
            for (int i = 0; i < 2 * length - 1; i++) {
                line = input.readLine();
                //Horizontal road
                if (i % 2 == 0) {
                    current = first;
                    horizontalRead(line, current);
                    first += width;
                }
                //vertical line read
                else verticalRead(line, current, width);
            }
            //For File
        } catch (Exception e) {
            e.printStackTrace();
            throw new MapException();
        }
    }

    /**
     * @param line    the line of input file we're reading
     * @param current the current node we are using
     * @throws GraphException if something wrong with line
     */
    private void horizontalRead(String line, int current) throws GraphException {
        for (int i = 1; i < line.length(); i += 2) {
            if (line.charAt(i) != 'B') {
                roadMap.addEdge(roadMap.getNode(current), roadMap.getNode(current + 1), strValue(line.charAt(i)));
            }
            current++;
        }
    }

    /**
     * @param line    line of input file we're reading
     * @param current current node we ar editing
     * @param width   width of the roadMap
     * @throws GraphException if error with line
     */
    private void verticalRead(String line, int current, int width) throws GraphException {
        for (int i = 0; i < line.length(); i += 2) {
            if (line.charAt(i) != 'B') {
                roadMap.addEdge(roadMap.getNode(current), roadMap.getNode(current + width), strValue(line.charAt(i)));
            }
            current++;
        }
    }

    /**
     * Only 3 possibilities are 'V', 'C' and 'P'
     *
     * @param typeRoad character we're converting
     * @return the str vale of the roadChar
     */
    private String strValue(char typeRoad) {
        if (typeRoad == 'V') return "Private";
        else if (typeRoad == 'C') return "Construction";
        else return "Public";
    }
}