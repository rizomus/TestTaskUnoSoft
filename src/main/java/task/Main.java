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
        HashMap<String, Subgroup> seenTokens = new HashMap<>();
        HashSet<Subgroup> groups = new HashSet<>();                             // lines, that need to be transferred to a new group
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
                    Subgroup currentGroup = new Subgroup(newGroupIndex);
                    groups.add(currentGroup);

                    for (int i = 0; i < rowElements.length; i++) {
                        if (!(rowElements[i].equals("\"\"") || rowElements[i].equals(""))) {
                            token = i + rowElements[i];

                            if (seenTokens.containsKey(token)) {
                                Subgroup oldGroup = seenTokens.get(token);
                                if (oldGroup.getParent() != null) {
                                    currentGroup.addChildren(oldGroup.getUpperParent());
                                } else {
                                    currentGroup.addChildren(oldGroup);
                                }
                            } else {
                                seenTokens.put(token, currentGroup);
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


        TreeMap<Integer, HashSet<Subgroup>> groupSort = new TreeMap<>();
        int size = 0;
        int groupCount = 0;

        for (Subgroup group: groups) {
            if (group.getParent() == null) {
                size = group.size();
                if (!groupSort.containsKey(size)) {
                    groupSort.put(size, new HashSet<>());
                }
                groupSort.get(size).add(group);
                groupCount++;
            }
        }
        System.out.println(groupSort.descendingKeySet());

        System.out.println("Количество групп с более чем одним элементом: " + (groupCount - groupSort.get(1).size()));
        System.out.println(Math.round((System.currentTimeMillis() - start) / 1000) + " sec");

        groupCount = 0;
        for (int n : groupSort.descendingKeySet()) {
            HashSet<Subgroup> groupCluster = groupSort.get(n);
            for (Subgroup group: groupCluster) {
                System.out.println("Группа " + groupCount);
                for (int index: group.getLineIndexes()) {
                    System.out.println("    line: " + table.get(index));
                }
                groupCount++;
                System.out.println();
            }
        }
    }
}
