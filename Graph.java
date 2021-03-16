import java.util.*;
import java.io.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.File;

public class Graph {

    // We will store the nodes in a 2-dimensional Arraylist
    private ArrayList<ArrayList<Integer>> nodes;

    // Constructor
    public Graph(ArrayList<ArrayList<Integer>> nodes) {
        this.nodes = nodes;
    }

    // A getter for the nodes attribute
    public ArrayList<ArrayList<Integer>> get_nodes() {
        return this.nodes;
    }

    // Method for reading a text file into a 2D ArrayList
    public static ArrayList<ArrayList<Integer>> read_txt(String filename) throws IOException, FileNotFoundException {
        Scanner s = new Scanner(new BufferedReader(new FileReader(filename)));
        ArrayList<ArrayList<Integer>> nodes = new ArrayList<ArrayList<Integer>>();
        while (s.hasNextLine()) {
            nodes.add(createRow(s.nextLine()));
        }
        return nodes;
    }

    // A method for transforming a String into an ArrayList of Integers - used in
    // the previous method
    public static ArrayList<Integer> createRow(String line) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        String[] help = line.split(" "); // construct an array of string objects
        for (int i = 0; i < help.length; i++) {
            arr.add(Integer.parseInt(help[i])); // add them to the ArrayList
        }
        return arr;
    }

    // This method compares the ranking of two nodes - returns true if the rank of
    // the first one is greater, thus should be considered first when colouring
    public static boolean compare(ArrayList<Integer> arr1, ArrayList<Integer> arr2) {

        if (arr1.size() > arr2.size()) { // the first node has got more neighbours
            return true;
        } // equal # of neighbours but lower id
        else if (arr1.size() == arr2.size() && arr1.get(0) < arr2.get(0)) {
            return true;
        }
        return false;
    }

    // Method for swapping the elements in two ArrayLists
    public static void swapArrays(ArrayList<Integer> arr1, ArrayList<Integer> arr2) {

        // We use temporary ArrayLists
        ArrayList<Integer> temp1 = new ArrayList<Integer>();

        // Copy the elements of arr1 into temp1
        for (int i = 0; i < arr1.size(); i++) {
            temp1.add(arr1.get(i));
        }

        ArrayList<Integer> temp2 = new ArrayList<Integer>();

        // Copy the elements of arr2 into temp2
        for (int i = 0; i < arr2.size(); i++) {
            temp2.add(arr2.get(i));
        }

        // We clear the content from arr1 and arr2
        arr1.clear();
        arr2.clear();

        // We fill arr1 with the elements from temp2
        for (int i = 0; i < temp2.size(); i++) {
            arr1.add(temp2.get(i));
        }

        // We fill arr2 with the elements from temp1
        for (int i = 0; i < temp1.size(); i++) {
            arr2.add(temp1.get(i));
        }

        temp1.clear();
        temp2.clear();
    }

    // A method for ranking the nodes of a graph
    public static ArrayList<ArrayList<Integer>> sortNodes(Graph g) {
        ArrayList<ArrayList<Integer>> arr = g.get_nodes();
        int size = arr.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1; j++) {
                if (!compare(arr.get(j), arr.get(j + 1))) {
                    swapArrays(arr.get(j), arr.get(j + 1)); // use the swapArrays method here
                }
            }
        }
        return arr;
    }

    // A method for creating an output file, colouring the nodes of the graph, and
    // writing to this output file
    public static void colour_and_write(String filename, Graph g) throws IOException {
        File file = new File(filename);
        if (file.exists()) { // delete the file if it exists
            file.delete();
        }
        FileWriter myWriter = new FileWriter(filename); // create the file

        String[] result = new String[51]; // to store the colours for every node

        // an array of all the possible colours
        String[] col = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
                "T", "U", "V", "W", "X", "Y", "Z" };

        ArrayList<ArrayList<Integer>> sorted = sortNodes(g); // ranking the nodes in the graph

        for (int i = 0; i < sorted.size(); i++) {
            boolean check = true;
            int index = 0;
            int number = sorted.get(i).get(0);
            for (int k = 0; k < 26; k++) {
                check = true;
                for (int j = 1; j < sorted.get(i).size(); j++) {
                    if (col[k].equals(result[sorted.get(i).get(j)])) {
                        check = false;
                        index++;
                        break;
                    }
                }
                if (check) { // if none of the neighbours has this colour
                    result[number] = col[index];
                    break;
                }
            }
            result[number] = col[index];
        }

        for (int i = 1; i < 51; i++) { // write to the output file
            if (result[i] == null) {
                continue;
            }
            myWriter.write(String.valueOf(i) + result[i] + "\n");
        }
        myWriter.close();
    }

    public static void main(String[] args) throws IOException, FileNotFoundException {

        String input = args[0]; // the input file
        String output = args[1]; // the output file
        Graph g = new Graph(read_txt(input)); // create a graph from the input file
        colour_and_write(output, g); // colour its nodes
        System.out.println("Successfully coloured - check the file " + output);
    }

}
