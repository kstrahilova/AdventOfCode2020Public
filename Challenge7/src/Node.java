import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Node {
    private int name;
    private String finish;
    private String colour;
    private List<Node> parents;
    private HashMap<Node, Integer> children;

    Node() {
        parents = new ArrayList<>();
        children = new HashMap<>();
    }

    public int getName() {
        return name;
    }

    void setName(int name) {
        this.name = name;
    }

    public String getFinish() {
        return finish;
    }

    void setFinish(String finish) {
        this.finish = finish;
    }

    public String getColour() {
        return colour;
    }

    void setColour(String colour) {
        this.colour = colour;
    }

    public List<Node> getParents() {
        return parents;
    }

    public void addParent(Node parent) {
        this.parents.add(parent);
    }

    public HashMap<Node, Integer> getChildren() {
        return children;
    }

    public void addChild(Node child, Integer number) {
        children.put(child, number);
    }
}
