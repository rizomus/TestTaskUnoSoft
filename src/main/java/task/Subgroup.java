package task;

import java.util.HashSet;
import java.util.LinkedList;

public class Subgroup {
    final int index;
    private Subgroup parent = null;
    HashSet<Subgroup> children = new HashSet<>();
    HashSet<Integer> lineIndexes = new HashSet<>();
    LinkedList<Subgroup> stack = new LinkedList<>();

    public Subgroup(int index) {
        this.index = index;
    }

    public Subgroup getParent() {
        return parent;
    }

    public Subgroup getUpperParent() {
        Subgroup upperParent = this.parent;
        if (parent == null) {
            return null;
        } else {
            Subgroup par = this.parent;
            while (par != null) {
                upperParent = par;
                par = upperParent.parent;
            }
        }
        this.parent = upperParent;
        return upperParent;
    }

    public void addChildren(Subgroup group) {
        if (group != this) {
            group.parent = this;
            this.children.add(group);
        }
    }

    public int size() {
        int count = 1;
        stack.clear();
        stack.addAll(this.children);
        while (stack.size() > 0) {
            Subgroup child = stack.pollFirst();
            stack.addAll(child.children);
            count++;
        }
        return count;
    }
    public HashSet<Integer> getLineIndexes() {
        lineIndexes.add(this.index);
        LinkedList<Subgroup> stack = new LinkedList<>();
        stack.clear();
        stack.addAll(this.children);
        while (stack.size() > 0) {
            Subgroup child = stack.pollFirst();
            stack.addAll(child.children);
            lineIndexes.add(child.index);
        }
        return lineIndexes;
    }
}
