package task;

import java.util.HashSet;
import java.util.LinkedList;

public class ChainedLine {
    private final int index;
    private ChainedLine parent = null;
    private final HashSet<ChainedLine> children = new HashSet<>();
    private final HashSet<Integer> lineIndexes = new HashSet<>();
    private final LinkedList<ChainedLine> stack = new LinkedList<>();

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

    public void setParent(ChainedLine parent) {
        this.parent = parent;
    }

    public void addChild(ChainedLine line) {
        if (line != this) {
            line.setParent(this);
            this.children.add(line);
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
