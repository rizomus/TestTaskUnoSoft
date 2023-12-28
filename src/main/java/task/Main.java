/*
Test task https://github.com/PeacockTeam/new-job/blob/master/lng%26java
 */

package task;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {

        System.out.println("start... \n");

        long start = System.currentTimeMillis();

        String path = args[0];

        LinkedList<String> table = new LinkedList<>();                          // lines from file
        String token;                                                           // [column number + item] (like 3"7894561230")
        HashMap<String, ChainedLine> tokenLines = new HashMap<>();
        HashSet<ChainedLine> groups = new HashSet<>();
        HashSet<String> lineSet = new HashSet<>();                              // check for duplicates

//        try (BufferedReader reader = new BufferedReader(new FileReader("c:\\lng.txt"))) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine();
            Pattern pattern = Pattern.compile("\\d+\"+\\d");

            int newGroupIndex = 0;
            while (line != null) {
                if (lineSet.contains(line)) {
                    line = reader.readLine();
                    continue;
                }
                lineSet.add(line);
                Matcher matcher = pattern.matcher(line);
                if (!matcher.find()) {
                    table.add(line);
                    String[] rowElements = line.split(";");
                    ChainedLine currentLine = new ChainedLine(newGroupIndex);
                    ChainedLine oldLine = null;
                    groups.add(currentLine);

                    for (int i = 0; i < rowElements.length; i++) {
                        if (!(rowElements[i].equals("\"\"") || rowElements[i].equals(""))) {
                            token = i + rowElements[i];

                            if (tokenLines.containsKey(token)) {
                                oldLine = tokenLines.get(token);
                                if (oldLine.getParent() != null) {
                                    currentLine.addChild(oldLine.getUpperParent());
                                } else {
                                    currentLine.addChild(oldLine);
                                }
                            } else {
                                tokenLines.put(token, currentLine);
                            }
                        }
                    }
                    newGroupIndex++;
                } else {
                    System.out.println("STRING NOT VALID");
                    System.out.println(line);
                }
                line = reader.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        TreeMap<Integer, HashSet<ChainedLine>> groupSort = new TreeMap<>();
        int size = 0;
        int groupCount = 0;

        for (ChainedLine group: groups) {
            if (group.getParent() == null) {
                size = group.size();
                if (!groupSort.containsKey(size)) {
                    groupSort.put(size, new HashSet<>());
                }
                groupSort.get(size).add(group);
                groupCount++;
            }
        }
        System.out.println("Groups with more then 1 element: " + (groupCount - groupSort.get(1).size()));
        System.out.println(Math.round((System.currentTimeMillis() - start) / 1000) + " sec");

        groupCount = 0;
        for (int n : groupSort.descendingKeySet()) {
            HashSet<ChainedLine> groupCluster = groupSort.get(n);
            for (ChainedLine group: groupCluster) {
                System.out.println("Group " + groupCount);
                for (int index: group.getLineIndexes()) {
                    System.out.println("    line: " + table.get(index));
                }
                groupCount++;
                System.out.println();
            }
        }
    }
}
