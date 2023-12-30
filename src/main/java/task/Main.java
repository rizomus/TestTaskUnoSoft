/*
Test task https://github.com/PeacockTeam/new-job/blob/master/lng%26java
 */

package task;
import java.io.*;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {

        System.out.println("start... \n");
        long start = System.currentTimeMillis();

        String inputPath = args[0];
        String outputPath = "result.txt";

        ArrayList<String> table = new ArrayList<>();                          // lines from file
        String token;                                                           // [column number + item] (like 3"7894561230")
        HashMap<String, ChainedLine> tokenLines = new HashMap<>();
        HashSet<ChainedLine> groups = new HashSet<>();
        HashSet<String> lineSet = new HashSet<>();                              // check for duplicates

//        try (BufferedReader reader = new BufferedReader(new FileReader("c:\\lng.txt"))) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
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
            writer.write("Groups with more then 1 element: " + (groupCount - groupSort.get(1).size()) + "\n");

            groupCount = 0;
            for (int n : groupSort.descendingKeySet()) {
                HashSet<ChainedLine> groupCluster = groupSort.get(n);
                for (ChainedLine group: groupCluster) {
                    writer.append("Group " + groupCount + "\n");
                    for (int index: group.getLineIndexes()) {
                        writer.append("    line: " + table.get(index) + "\n");
                    }
                    groupCount++;
                    writer.append("\n");
                }
            }
            System.out.println("DONE. (See result.txt)");
            System.out.println(Math.round((System.currentTimeMillis() - start) / 1000) + " sec");
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
