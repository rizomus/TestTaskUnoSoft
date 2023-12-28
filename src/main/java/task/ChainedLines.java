package task;

import java.util.HashSet;
import java.util.LinkedList;

public class ChainedLines {
    final int index;
    private ChainedLines parent = null;
    HashSet<ChainedLines> children = new HashSet<>();
    HashSet<Integer> lineIndexes = new HashSet<>();
    LinkedList<ChainedLines> stack = new LinkedList<>();

    public ChainedLines(int index) {
        this.index = index;
    }

    public ChainedLines getParent() {
        return parent;
    }

    public ChainedLines getUpperParent() {
        ChainedLines upperParent = this.parent;
        if (parent == null) {
            return null;
        } else {
            ChainedLines par = this.parent;
            while (par != null) {
                upperParent = par;
                par = upperParent.parent;
            }
        }
        this.parent = upperParent;
        return upperParent;
    }

    public void addChild(ChainedLines group) {
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
            ChainedLines child = stack.pollFirst();
            stack.addAll(child.children);
            count++;
        }
        return count;
    }
    public HashSet<Integer> getLineIndexes() {
        lineIndexes.add(this.index);
        LinkedList<ChainedLines> stack = new LinkedList<>();
        stack.clear();
        stack.addAll(this.children);
        while (stack.size() > 0) {
            ChainedLines child = stack.pollFirst();
            stack.addAll(child.children);
            lineIndexes.add(child.index);
        }
        return lineIndexes;
    }
}
