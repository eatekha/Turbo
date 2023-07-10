import java.util.Iterator;
import java.util.Stack;

public class Graph implements GraphADT {
    /**
     * nodeSet contains the nodes in the graph
     * edgeSet contains all the edges of graph
     */
    private Node[] nodeSet;
    //to change
    private Edge[][] edgeSet;
    /**
     * @param n: number of nodes in the graph
     */
    public Graph(int n) {
        //Size of the adjacency matrix (square matrix)
        nodeSet = new Node[n];
        edgeSet = new Edge[n][n];
        //Initializes all the nodes to the nodeSet
        for (int i = 0; i < n; i++) {
            nodeSet[i] = new Node(i);
        }
    }
    /**
     * @param nodeu first endpoint
     * @param nodev second endpoint
     * @param edgeType type of edge
     * @throws GraphException if node or edge ALREADY exists
     */
    public void addEdge(Node nodeu, Node nodev, String edgeType) throws GraphException {
        //Checks if nodes exist
        nodeCheck(nodeu, nodev);

        //When nodes exists, check if edge already exists
        //only need to check one
        if (edgeSet[nodeu.getId()][nodev.getId()] != null){
            throw new GraphException("Edge Already Exists");
        }
        //Node exist and edge does not, adds edge
        else {
            edgeSet[nodeu.getId()][nodev.getId()] = new Edge(nodeu, nodev, edgeType);
            edgeSet[nodev.getId()][nodeu.getId()] = new Edge(nodev, nodeu, edgeType);

        }
    }
    /**
     * @param id: the id the user is looking for
     * @return node with specified id in nodeSet
     * @throws GraphException if there is no node with specified id
     */
    public Node getNode(int id) throws GraphException {
        for (Node node : nodeSet) {
            if (node.getId() == id) return node;
        }
        throw new GraphException("Node does not exist");
    }
    /**
     *
     * @param u the node we are using
     * @return iterator containing incident edges
     * @throws GraphException if node is invalid
     */
    public Iterator<Edge> incidentEdges(Node u) throws GraphException {
        getNode(u.getId()); //Checks if node is valid
        Stack<Edge> node_edges = new Stack<Edge>();

        //Searches the row containing node u
        for (int i=0; i<nodeSet.length; i++){
            //If Edge exist put in stack
            if (edgeSet[u.getId()][i] != null){
                node_edges.push(edgeSet[u.getId()][i]);
            }
        }
        //Return null if no edges were found
        if (node_edges.isEmpty()) return null;
        //Returns iteration if not empty
        return node_edges.iterator();
    }
    /**
     * @param u first endpoint
     * @param v second endpoint
     * @return Edge between two endpoints
     * @throws GraphException if node invalid or edge does not exist
     */
    public Edge getEdge(Node u, Node v) throws GraphException {
        //Makes sure Nodes are Valid
        nodeCheck(u, v);
        //Returns based on if edge exists or not
        if (edgeSet[u.getId()][v.getId()] != null)
            return edgeSet[u.getId()][v.getId()];
        else throw new GraphException();
    }
    /**
     *
     * @param u first endpoint
     * @param v second endpoint
     * @return if nodes are adjacent(Have an edge between them)
     * @throws GraphException if either node is invalid (doesn't exist)
     */
    public boolean areAdjacent(Node u, Node v) throws GraphException {
        nodeCheck(u, v);
        return edgeSet[u.getId()][v.getId()] != null;
    }
    /**
     *
     * @param u first endpoint
     * @param v second endpoint
     * @throws GraphException if either Node Doesn't Exist
     */
    private void nodeCheck(Node u, Node v) throws GraphException{
        getNode(u.getId());
        getNode(v.getId());
    }
}

