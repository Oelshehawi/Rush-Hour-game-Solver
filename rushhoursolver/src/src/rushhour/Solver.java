package rushhour;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

public class Solver {
    public final static int VERTICAL = 0;
    public final static int HORIZONTAL = 1;
    public final static int UP = 0;
    public final static int DOWN = 1;
    public final static int LEFT = 2;
    public final static int RIGHT = 3;


    private static char[][] array;
    List<String> prevMoves=new ArrayList<>();
    private static List<Character> name = new ArrayList<Character>();
    private static List<Integer> length = new ArrayList<Integer>();
    private static List<Integer> direction = new ArrayList<Integer>();
    private static List<String> prevMove = new ArrayList<>();

    public static void solveFromFile(String inputPath,String outputPath) throws Exception {
        array = new char[6][6];
        File file = new File(inputPath);
        Scanner reader = new Scanner(file);
        int i = 0;
        //loop to initialize array with elements from file
        while (reader.hasNext()) {
            String next = reader.next();
            for (int j = 0; j < next.length(); j++) {
                array[i][j] = next.charAt(j);
            }
            i++;
            if (next.length() != 6) {
                throw new Exception("bad board");
            }
        }
        setUp();
        solve(array,name,direction);
        File file2=new File(outputPath);
        PrintWriter output=new PrintWriter(file2);
        for(int j=prevMove.size()-2;j>=0;j--){
            String s=prevMove.get(j);
            output.println(s);
            PrintStream screenoutput= System.out;
        }
        output.close();
    }

    public static void setUp() {// test size of lists to make sure right number of elements are being added.

        for (int i = 0; i <= 5; i++)
            for (int j = 0; j <= 5; j++) {
                if (array[i][j] != '.' && !name.contains(array[i][j])) {
                    name.add(array[i][j]);

                    if ((j + 1 <= 5 && array[i][j] == array[i][j + 1]) || (j - 1 >= 0 && array[i][j] == array[i][j - 1])) {
                        direction.add(HORIZONTAL);
                    } else {
                        direction.add(VERTICAL);
                    }
                    char a = array[i][j];
                    int counter = 0;
                    for (int x = 0; x <= 5; x++) {
                        for (int y = 0; y <= 5; y++) {
                            if (array[x][y] == a) {
                                counter++;
                            }
                        }
                    }
                    length.add(counter);
                }


            }

    }
    public static String carToString(Character c, int direction, int length){

        if(direction==0){
            return "" + c +"U"+ length;
            //up
        }
        if(direction==1){
            //down
            return "" + c +"D"+ length;
        }
        if(direction==2){
            //left
            return "" + c +"L"+ length;
        }
        if (direction==3){
            //right
            return "" + c +"R"+ length;
        }
        return null;

    }
    public static List<Node> getNeighbours(char[][] board, List<Character> carNames, List<Integer> direction) {
        int i = 0;
        List<Node> states = new LinkedList<>();

        while (i < carNames.size()) {
            Character c = carNames.get(i);
            if (direction.get(i) == 0) {
                for (int k = 1; k <= 4; k++) {
                    if (!checkLegalMove(board, c, 0, k)) {
                        continue;
                    }
                    Node newnode = new Node(makeMove(board, 0, k, c));
                    String s=carToString(c,0,k);
                    newnode.setPrevMove(s);
                    states.add(newnode);

                }
                for (int k = 1; k <= 4; k++) {
                    if (!checkLegalMove(board, c, 1, k)) {
                        continue;
                    }
                    Node newnode = new Node(makeMove(board, 1, k, c));
                    String s=carToString(c,1,k);
                    newnode.setPrevMove(s);
                    states.add(newnode);

                }
            }
            if (direction.get(i) == 1) {
                for (int k = 1; k <= 4; k++) {
                    if (!checkLegalMove(board, c, 2, k)) {
                        continue;
                    }
                    Node newnode = new Node(makeMove(board, 2, k, c));
                    String s=carToString(c,2,k);
                    newnode.setPrevMove(s);
                    states.add(newnode);


                }
                for (int k = 1; k <= 4; k++) {
                    if (!checkLegalMove(board, c, 3, k)) {
                        continue;
                    }
                    Node newnode;
                    newnode = new Node(makeMove(board, 3, k, c));
                    String s=carToString(c,3,k);
                    newnode.setPrevMove(s);
                    states.add(newnode);
                }

            }
            i++;
        }
        return states;
    }
    public static char[][] makeMove(char board[][], int dir, int length, char carName) {

        if (!checkLegalMove(board, carName, dir, length)) {
            return null;
        }
        char[][] intboard = new char[6][6];
        for (int i = 0; i <= 5; i++)
            for (int j = 0; j <= 5; j++) {
                intboard[i][j] = board[i][j];
            }
        if (dir == UP || dir == LEFT) {

            for (int i = 0; i <= 5; i++)
                for (int j = 0; j <= 5; j++) {
                    if (intboard[i][j] == carName) {

                        if (dir == UP) {
                            char temp = intboard[i][j];
                            intboard[i][j] = intboard[i - length][j];
                            intboard[i - length][j] = temp;


                        }
                        if (dir == LEFT) {
                            char temp = intboard[i][j];
                            intboard[i][j] = intboard[i][j - length];
                            intboard[i][j - length] = temp;
                        }
                    }
                }
        }
        if (dir == DOWN || dir == RIGHT) {
            for (int i = 5; i >= 0; i--)
                for (int j = 5; j >= 0; j--) {
                    if (intboard[i][j] == carName) {

                        if (dir == DOWN) {
                            char temp = intboard[i][j];
                            intboard[i][j] = intboard[i + length][j];
                            intboard[i + length][j] = temp;
                        }
                        if (dir == RIGHT) {
                            char temp = intboard[i][j];
                            intboard[i][j] = intboard[i][j + length];
                            intboard[i][j + length] = temp;
                        }

                    }

                }
        }
        return intboard;
    }
    public static boolean checkLegalMove(char[][] board, char carName, int dir, int length) {
        int x = 0, y = 0;
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 5; j++) {
                if (board[i][j] == carName) {
                    x = i;
                    y = j;
                    i = 6;
                    j = 6;
                    break;
                }
            }
        }
        if (dir == UP) {
            if (x - length < 0)
                return false;
            for (int i = x; i >= x - length; i--)
                if (board[i][y] != '.' && board[i][y] != carName)
                    return false;

        }
        if (dir == LEFT) {
            if (y - length < 0)
                return false;
            for (int j = y; j >= y - length; j--) {
                if (board[x][j] != '.' && board[x][j] != carName)
                    return false;
            }
        }
        x = 0;
        y = 0;
        for (int i = 5; i >= 0; i--)
            for (int j = 5; j >= 0; j--)
                if (board[i][j] == carName) {
                    x = i;
                    y = j;
                    i = -1;
                    j = -1;
                    break;
                }
        if (dir == DOWN) {
            if (x + length > 5)
                return false;
            for (int i = x; i <= x + length; i++)
                if (board[i][y] != '.' && board[i][y] != carName)
                    return false;
        }
        if (dir == RIGHT) {
            if (y + length > 5)
                return false;
            for (int j = y; j <= y + length; j++) {
                if (board[x][j] != '.' && board[x][j] != carName)
                    return false;
            }
        }

        return true;
    }
    public static List<String> prevBoards(Node end){
        List<char[][]> prevBoards=new ArrayList<>();
        List<String> prevMoves = new ArrayList<>();
        Node newnode = end;
        while(newnode!=null){
            prevMoves.add(newnode.prevMove);
            prevBoards.add(newnode.getBoard());
            newnode=newnode.getPrevNode();
        }
        prevMove=prevMoves;
        return prevMoves;
    }

    public static Node solve(char[][] board, List<Character> carName, List<Integer> direction) {
        LinkedList<Node> q = new LinkedList<>();
        List<Node> states;
        Map<String, Boolean> Bmap = new HashMap<>();
        Node intboard = new Node(array);
        q.add(intboard);
        int counter = 0;
        Bmap.put(ArraytoList(intboard.getBoard()), true);

        while (!q.isEmpty()) {
            Node newnode = q.pop();

            if (isSolved(newnode)) {
                prevBoards(newnode);
                return newnode;
            }
            states = getNeighbours(newnode.getBoard(), carName, direction);

            while (!states.isEmpty()) {
                Node node=states.remove(0);
                node.setPrevNode(newnode);
                char[][] firststate=node.getBoard();
                if (!Bmap.containsKey(ArraytoList(firststate))) {
                    Bmap.put(ArraytoList(firststate), true);
                    q.add(node);
                }
            }
        }
        return null;
    }
    public static String ArraytoList(char[][] board) {
        String s="";
        for(int i=0;i<=5;i++)
            for(int j=0;j<=5;j++){
                s+=Character.toString(board[i][j]);
            }

        return s;
    }
    public static boolean isSolved(Node t) {
        char board[][] = t.getBoard();
        if (board[2][5] == 'X'){
            return true;
        }
        return false;
    }
}

