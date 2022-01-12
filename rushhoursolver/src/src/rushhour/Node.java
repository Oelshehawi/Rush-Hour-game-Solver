package rushhour;

public class Node{
    char board[][];
    Node prevNode;
    String prevMove;

    Node (char board[][]){
        this.board = board;
        prevNode=null;
        prevMove=null;
    }
    public char[][] getBoard(){
        return this.board;
    }
    public Node getPrevNode(){
        return prevNode;
    }
    public void setPrevMove(String a){
        this.prevMove=a;
    }
    public void setPrevNode(Node t){
        this.prevNode=t;
    }
}
