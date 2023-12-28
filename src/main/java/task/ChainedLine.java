package task;

import java.util.HashSet;
import java.util.LinkedList;

public class ChainedLine {
    final int index;
    private ChainedLine parent = null;
    HashSet<ChainedLine> children = new HashSet<>();
    HashSet<Integer> lineIndexes = new HashSet<>();
    LinkedList<ChainedLine> stack = new LinkedList<>();

    public ChainedLine(int index) {
        this.index = index;
    }

    public ChainedLine getParent() {
        return parent;
    }

    public ChainedLine getUpperParent() {
        ChainedLine upperParent = this.parent;
        if (parent == null) {
            return null;
        } else {
            ChainedLine par = this.parent;
            while (par != null) {
                upperParent = par;
                par = upperParent.parent;
            }
        }
        this.parent = upperParent;
        return upperParent;
    }

    public void addChild(ChainedLine group) {
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
            ChainedLine child = stack.pollFirst();
            stack.addAll(child.children);
            count++;
        }
        return count;
    }
    public HashSet<Integer> getLineIndexes() {
        lineIndexes.add(this.index);
        LinkedList<ChainedLine> stack = new LinkedList<>();
        stack.clear();
        stack.addAll(this.children);
        while (stack.size() > 0) {
            ChainedLine child = stack.pollFirst();
            stack.addAll(child.children);
            lineIndexes.add(child.index);
        }
        return lineIndexes;
    }
}
