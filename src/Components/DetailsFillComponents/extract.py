import re

# Initialize data containers
groups = {}
users = {}
group_membership = []

# Read the input file
with open('input_file.txt', 'r') as file:
    lines = file.readlines()

current_group = None
current_user = None

# Process each line
for line in lines:
    line = line.strip()

    # Check for the start of a group
    if line.startswith('G+'):
        group_name = line[2:]
        groups[group_name] = {}
        current_group = group_name
        current_user = None  # Reset current user
    
    # Check for the start of a user
    elif line.startswith('U+'):
        user_name = line[2:]
        users[user_name] = {}
        current_user = user_name
        current_group = None

    # Check for group attributes
    elif line.startswith('V+'):
        if current_group:
            parts = line.split(',')
            if len(parts) >= 3:
                attribute_name = parts[1]
                attribute_value = parts[2]
                groups[current_group][attribute_name] = attribute_value

        if current_user:
            parts = line.split(',')
            if len(parts) >= 3:
                attribute_name = parts[1]
                attribute_value = parts[2]
                users[current_user][attribute_name] = attribute_value


    # Check for group membership
    elif line.startswith('M+'):
        parts = line.split(',')
        if len(parts) >= 2:
            user_name = parts[0].split('M+')[1]
            group_name = parts[1]
            group_membership.append((user_name, group_name))

# Print the extracted data
print("Groups:")
for group_name, attributes in groups.items():
    print(f"Group: {group_name}")
    for attribute, value in attributes.items():
        print(f"{attribute}: {value}")
    print()

print("Users:")
for user_name, attributes in users.items():
    print(f"User: {user_name}")
    for attribute, value in attributes.items():
        print(f"{attribute}: {value}")
    print()

print("Group Membership:")
for user_name, group_name in group_membership:
    print(f"User: {user_name}, Group: {group_name}")