import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class User {
    String name;
    Map<String, Map<String, String>> attributes = new HashMap<>();
    List<String> groups = new ArrayList<>();

    User(String name) {
        this.name = name;
    }
}

class Group {
    String name;
    Map<String, Map<String, String>> attributes = new HashMap<>();

    Group(String name) {
        this.name = name;
    }
}

public class UserGroupMigration {
    private static Map<String, User> users = new HashMap<>();
    private static Map<String, Group> groups = new HashMap<>();
    private static User currentUser = null;

    public static void main(String[] args) {
        String controlFilePath = "control_file.txt"; // Provide the correct file path

        try (BufferedReader br = new BufferedReader(new FileReader(controlFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith(";")) {
                    continue; // Skip comments and empty lines
                }

                String[] parts = line.split(",");
                char command = parts[0].charAt(0);
                String[] commandArgs = new String[parts.length - 1];
                System.arraycopy(parts, 1, commandArgs, 0, commandArgs.length);

                processInstruction(command, commandArgs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Now you have the users and groups data in the "users" and "groups" maps.
        // You can process this data further or perform any necessary actions.
    }

    private static void processInstruction(char command, String[] args) {
        switch (command) {
            case 'R':
                resetData();
                break;
            case 'U':
                if (args[0].charAt(0) == '+') {
                    addUser(args[0].substring(1));
                } else if (args[0].charAt(0) == '-') {
                    deleteUser(args[0].substring(1));
                }
                break;
            case 'G':
                if (args[0].charAt(0) == '+') {
                    addGroup(args[0].substring(1));
                } else if (args[0].charAt(0) == '-') {
                    deleteGroup(args[0].substring(1));
                }
                break;
            case 'A':
                addAttribute(args);
                break;
            case 'V':
                changeAttributeValue(args);
                break;
            case 'M':
                boolean add = args[0].charAt(0) == '+';
                changeMembership(args[0].substring(1), args[1], add);
                break;
        }
    }

    private static void resetData() {
        users.clear();
        groups.clear();
        currentUser = null;
    }

    private static void addUser(String name) {
        currentUser = new User(name);
        users.put(name, currentUser);
    }

    private static void deleteUser(String name) {
        users.remove(name);
        currentUser = null;
    }

    private static void addGroup(String name) {
        groups.put(name, new Group(name));
    }

    private static void deleteGroup(String name) {
        groups.remove(name);
    }

    private static void addAttribute(String[] args) {
        if (currentUser != null) {
            String attribute = args[0];
            String type = args[1];
            String length = args[2];
            String changer = args[3];
            String defaultValue = args.length > 4 ? args[4] : "";

            Map<String, String> attributeInfo = new HashMap<>();
            attributeInfo.put("type", type);
            attributeInfo.put("length", length);
            attributeInfo.put("changer", changer);
            attributeInfo.put("default", defaultValue);

            currentUser.attributes.put(attribute, attributeInfo);
        }
    }

    private static void changeAttributeValue(String[] args) {
        if (currentUser != null) {
            String attribute = args[0];
            String value = args.length > 1 ? args[1] : "";
            Map<String, String> attributeInfo = currentUser.attributes.get(attribute);

            if (attributeInfo != null) {
                attributeInfo.put("value", value);
            }
        }
    }

    private static void changeMembership(String userName, String groupName, boolean add) {
        if (users.containsKey(userName) && groups.containsKey(groupName)) {
            User user = users.get(userName);
            if (add) {
                user.groups.add(groupName);
            } else {
                user.groups.remove(groupName);
            }
        }
    }
}
