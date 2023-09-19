import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TibcoUserGroupExtractor {

    private static final Pattern USER_PATTERN = Pattern.compile("(?<=\bU\+)[\w@]+(?=\b)");
    private static final Pattern GROUP_PATTERN = Pattern.compile("(?<=\bG\+)[\w]+(?=\b)");
    private static final Pattern GROUP_MEMBERSHIP_PATTERN = Pattern.compile("(?<=\bM\+)[\w@]+\s+[\w]+(?=\b)");

    public static void main(String[] args) throws IOException {
        // Get the exported file name from the command line arguments.
        String exportedFileName = args[0];

        // Create a map to store the extracted user, group, and membership information.
        Map<String, Map<String, List<String>>> extractedData = new HashMap<>();

        // Create a buffered reader to read the exported file.
        try (BufferedReader reader = new BufferedReader(new FileReader(exportedFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignore comment lines and blank lines.
                if (line.startsWith("#") || line.isBlank()) {
                    continue;
                }

                // Extract the user information.
                Matcher userMatcher = USER_PATTERN.matcher(line);
                if (userMatcher.matches()) {
                    String username = userMatcher.group();
                    extractedData.computeIfAbsent(username, k -> new HashMap<>()).put("attributes", List.of(line.split("\\s+")[1]));
                    continue;
                }

                // Extract the group information.
                Matcher groupMatcher = GROUP_PATTERN.matcher(line);
                if (groupMatcher.matches()) {
                    String groupName = groupMatcher.group();
                    extractedData.computeIfAbsent(groupName, k -> new HashMap<>()).put("attributes", List.of(line.split("\\s+")[1]));
                    continue;
                }

                // Extract the group membership information.
                Matcher groupMembershipMatcher = GROUP_MEMBERSHIP_PATTERN.matcher(line);
                if (groupMembershipMatcher.matches()) {
                    String username = groupMembershipMatcher.group(1);
                    String groupName = groupMembershipMatcher.group(2);

                    extractedData.computeIfAbsent(username, k -> new HashMap<>()).computeIfAbsent("groups", k -> new HashMap<>()).put(groupName, List.of());
                    continue;
                }
            }
        }

        // Print the extracted data.
        for (Map.Entry<String, Map<String, List<String>>> entry : extractedData.entrySet()) {
            String username = entry.getKey();
            Map<String, List<String>> userData = entry.getValue();

            System.out.println("User:");
            System.out.println("  Username: " + username);
            System.out.println("  Attributes:");
            for (String attribute : userData.get("attributes")) {
                System.out.println("    " + attribute);
            }

            System.out.println("  Groups:");
            for (Map.Entry<String, List<String>> groupEntry : userData.get("groups").entrySet()) {
                String groupName = groupEntry.getKey();
                System.out.println("    " + groupName);
            }
        }
    }
}