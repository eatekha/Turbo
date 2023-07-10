/** represents node of a graph
 * a node is created with this id
 * mark represents whether node has been visited
 */
public class Node {
    private int id;
    private boolean mark;
    //The id of a node is an integer value between 0 and n − 1, where n is the number of nodes in
    //the graph.
    public Node(int id){
        this.id = id;
    }
    //marks node
    public void markNode(boolean mark){
        this.mark = mark;
    }
    //returns id
    public int getId(){
        return this.id;
    }
    //returns if whether node has been marked
    public boolean getMark(){
        return this.mark;
    }

}
