import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static List<Node> graph;

    private static Node createNode(String node, boolean parent) {
        String[] properties = node.split(" ");
        Node newNode = new Node();
        graph.add(newNode);
        newNode.setName(graph.size());
        if (parent) {
            newNode.setFinish(properties[0]);
            newNode.setColour(properties[1]);
        } else {
            newNode.setFinish(properties[1]);
            newNode.setColour(properties[2]);
        }
        return newNode;
    }

    private static boolean isNodeInList(Node node, ArrayList<Node> list) {
        for (Node nodeInList : list) {
            if (nodeInList.getName() == node.getName()) {
                return true;
            }
        }
        return false;
    }

    private static Node getNodeByString(String node, boolean parent) {
        String[] properties = node.split(" ");
        for (Node existingNode : graph) {
            if ((parent && existingNode.getFinish().equals(properties[0]) && existingNode.getColour().equals(properties[1])) ||
               (!parent && existingNode.getFinish().equals(properties[1]) && existingNode.getColour().equals(properties[2]))) {
                return existingNode;
            }
        }
        return null;
    }

    private static void process_row(String row) {
        String[] parent_and_children = row.split(" contain ");
        String parent = parent_and_children[0];
        String[] children = (parent_and_children[1]).split(", ");

        Node parentNode = getNodeByString(parent, true);
        if (parentNode == null) {
            parentNode = createNode(parent, true);
        }

        for (String child : children) {
            if (child.split(" ")[0].equals("no")) {
                continue;
            }

            Node childNode = getNodeByString(child, false);
            if (childNode == null) {
                childNode = createNode(child, false);
            }

            childNode.addParent(parentNode);

            int number = Integer.parseInt(child.split(" ")[0]);
            parentNode.addChild(childNode, number);
        }
    }

    private static void findTheParentsOf(Node node, ArrayList<Node> result) {
        if (node.getParents().size() == 0) {
        } else {
            for (Node parent : node.getParents()) {
                result.add(parent);
                findTheParentsOf(parent, result);
            }
        }
    }

    private static int countTheChildrenOf(Node node) {
        int result = 0;
        if (node.getChildren().size() == 0) {
            return 0;
        } else {
            for (Map.Entry<Node, Integer> entry : node.getChildren().entrySet()) {
                result = result + entry.getValue() + entry.getValue() * countTheChildrenOf(entry.getKey());
            }
        }
        return result;
    }

    private static ArrayList<Node> withoutDuplicates(ArrayList<Node> original) {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : original) {
            if (!isNodeInList(node, result)) {
                result.add(node);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        graph = new ArrayList<>();
        try {
            File input = new File("inputChallenge7.txt");
            Scanner myReader = new Scanner(input);
            while (myReader.hasNextLine()) {
                String row = myReader.nextLine();
                if (!row.equals("")) {
                    process_row(row);
                }
            }

            Node wanted = getNodeByString("1 shiny gold", false);
            ArrayList<Node> parentsOfWanted = new ArrayList<>();
            int numberOfChildrenOfWanted = 0;

            if (wanted != null) {
                findTheParentsOf(wanted, parentsOfWanted);
                parentsOfWanted = withoutDuplicates(parentsOfWanted);
                numberOfChildrenOfWanted = countTheChildrenOf(wanted);
            }

            System.out.println("Result Part I: " + parentsOfWanted.size());
            System.out.println("Result Part II: " + numberOfChildrenOfWanted);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
