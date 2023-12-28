package task;

import java.util.HashSet;
import java.util.LinkedList;

public class Subgroup {
    int index;
    HashSet<Subgroup> children = new HashSet<>();

    public Subgroup getUpperParent() {
        Subgroup prevpar = this.parent;
        if (parent == null) {
            return null;
        } else {
            Subgroup par = this.parent;
            while (par != null) {
                prevpar = par;
                par = prevpar.parent;
            }
        }
        this.parent = prevpar;
        return prevpar;
    }

    Subgroup parent = null;

    public Subgroup(int index) {
        this.index = index;
    }

    public void addChildren(Subgroup group) {
        if (group != this) {
            group.parent = this;
            this.children.add(group);
        }
    }

    public int size() {
        LinkedList<Subgroup> stack = new LinkedList<>();
        int count = 1;
        stack.addAll(this.children);
        while (stack.size() > 0) {
            Subgroup child = stack.pollFirst();
            stack.addAll(child.children);
            count++;
        }
        return count;
    }
    public HashSet<Integer> getLineIndexes() {
        HashSet<Integer> lineIndexes = new HashSet<>();
        lineIndexes.add(this.index);
        LinkedList<Subgroup> stack = new LinkedList<>();
        stack.addAll(this.children);
        while (stack.size() > 0) {
            Subgroup child = stack.pollFirst();
            stack.addAll(child.children);
            lineIndexes.add(child.index);
        }
        return lineIndexes;
    }
}
