public class Edge {
    private Node first, second;
    private String type;
    //constructor
    public Edge(Node u, Node v, String type){
        first = u;
        second = v;
        this.type = type;
    }
    //returns first endpoint of edge
    public Node firstNode(){
        return first;
    }
    //returns second endpoint of edge
    public Node secondNode(){
        return second;
    }
    //returns type of edge
    public String getType(){
        return this.type;
    }
}
