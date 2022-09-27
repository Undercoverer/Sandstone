## Use Case Model

(All use cases were worked on by the entire team so no one person is responsible for any of them.)

| **Table of Contents**                |
|--------------------------------------|
| 1. Use Case Diagram                  |
| 2. Use Case Descriptions             |

### 1. Use case diagram: ![Use case diagram](https://raw.githubusercontent.com/Undercoverer/Sandstone/master/Use%20Case%20Diagram.png)

### 2. Use Case Descriptions

#### a.) Brief Description

- **Register New Account**: Anybody can register a new account with the system. It will by default be a guest account.
- **Login**: All registered accounts will be able to log in with their credentials. After logging in, they will be able
  to perform actions according to their credentials
- **View Stock Data**: Administrators, Managers, and Users can view the stock data, which includes the current price,
  the price history, current volume, and profit/loss.
- **Modify Stock Data**: Administrators and Managers can make changes to their desired stock data. Which includes the
  current price, the price history, the current value, the current item ID number
- **Insert Stock Data**: Administrators and Managers can insert new stock data.
- **Modify User Data**: Administrators and Managers can change the data of any user. They can only change the data of
  users with a lower permission level than themselves.
- **Modify Permissions**: Administrators and Managers can modify the permissions of certain users. Managers can modify
  the role of users and guests, being able to make them any other role besides administrator. Administrators can modify
  any role to be any other role.
- **Modify Users**: Administrators are able to add and remove users from the system. They have complete control of user
  permission levels.
- All actions taken by the system will be logged in an audit log. The data in the audit log will be viewable by
  Administrators.

#### b.) Use case description scenarios

- **Register New Account**:
    1. ) Initial Assumptions: The user is not already registered with the system.\
    2. ) Normal Flow:
        1. ) The user enters their desired username and password into the system.
        2. ) The system checks if the username is already taken.
        3. ) If the username is not taken, the system creates a new account with the username and password.
        4. ) The system then logs the user in.
    3. ) What can go wrong:
        1. ) The username is already taken. The system will notify the user that the username is already taken.
        2. ) The user enters an invalid username or password. The system will notify the user that the username or
           password is invalid.
    4. ) System state on completion: The user is logged in and has a guest account.
- **Login**
    1. ) Initial Assumption: The user is not already logged in.
    2. ) Normal Flow:
        1. ) The user enters their username and password into the system.
        2. ) The system checks if the username and password match an existing account.
        3. ) If the username and password are valid, the system logs the user in.
    3. )
- **View Stock Data**
    1. ) Initial Assumption: The user is logged in and has a permission level that allows them to view stock data.
    2. ) Normal Flow:
        1. ) The user selects the stock data they want to view.
        2. ) The system displays the stock data to the user.
    3. ) What can go wrong:
        1. ) The user selects invalid data. The system will notify the user that the data they selected is invalid.
    4. ) Other activities:
        1. ) The user can select which information they want to view or export.
    5. ) System state on completion: The user is viewing the stock data they selected.
- **Modify Stock Data**
    1. ) Initial Assumption: The user is logged in and has a permission level that allows them to modify stock data.
    2. ) Normal Flow:
        1. ) The user selects the stock data they want to modify.
        2. ) The system displays the stock data to the user.
        3. ) The user modifies the stock data.
        4. ) The system saves the modified stock data.
    3. ) What can go wrong:
        1. ) The user enters invalid data. The system will notify the user that the data they entered is invalid.
    4. ) Other activities:
        1. ) The user can select which information they want to modify in the stock data.
    5. ) System state on completion: The user is viewing the modified stock data.
- **Insert Stock Data**
    1. ) Initial Assumption: The user is logged in and has a permission level that allows them to insert stock data.
    2. ) Normal Flow:
        1. ) The user enters the stock data they want to insert or selects a file to import the stock data from.
        2. ) The system saves the stock data they entered if it is valid.
    3. ) What can go wrong:
        1. ) The user enters/imports invalid data. The system will notify the user that the data they entered/imported
           is invalid.
    4. ) Other activities:
        1. ) The user can select how the imported file is formatted.
    5. ) System state on completion: The user is viewing the inserted stock data.
- **Modify User Data**
    1. ) Initial Assumption: The user is logged in and has a permission level that allows them to modify user data.
    2. ) Normal Flow:
        1. ) The user goes to the users page.
        2. ) The user selects the user they want to modify.
        3. ) The system displays the user data to the user.
        4. ) The user modifies the user data.
        5. ) The system saves the modified user data.
    3. ) What can go wrong:
        1. ) The user enters invalid data. The system will notify the user that the data they entered is invalid.
        2. ) The user selected has higher permissions. The system will notify the user that they do not have permission
           to modify the user.
    4. ) Other activities:
        1. ) The user can select which information they want to modify in the user data.
    5. ) System state on completion: The user is viewing the modified user data.
- **Modify Permissions**
    1. ) Initial Assumption: The user is logged in and has a permission level that allows them to modify permissions.
    2. ) Normal Flow:
        1. ) The user goes to the users page.
        2. ) The user selects the user they want to modify.
        3. ) The system displays the user data to the user.
        4. ) The user modifies the user's permissions.
        5. ) The system saves the modified user's permissions.
    3. ) What can go wrong:
        1. ) The user selected has higher permissions. The system will notify the user that they do not have permission
           to modify the user.
        2. ) The user selected themselves. The system will notify the user that they cannot modify their own
           permissions without permission of a higher level user.
    4. ) System state on completion: The user is viewing the modified user's permissions.
- **Modify Users**
    1. ) Initial Assumption: The user is logged in and has a permission level that allows them to modify users.
    2. ) Normal Flow:
        1. ) The user goes to the users page.
        2. ) The user selects the user they want to modify.
        3. ) The system displays the user modification options to the user.
        4. ) The user selects the user modification option they want to use.
        5. ) The system performs the user modification option.
    3. ) What can go wrong:
        1. ) The user selected has higher permissions. The system will notify the user that they do not have permission
           to modify the user.
        2. ) The user selected themselves. The system will notify the user that they cannot modify themselves without
           permission of a higher level user.
    4. ) Other activities:
        1. ) The user can select which user modification option they want to use.
    5. ) System state on completion:
        1. ) If the user selected to delete a user, the user is deleted and the user is viewing the users page.
           1. ) If the user selected to delete themselves, the user is logged out and the user is viewing the login page.
        2. ) If the user selected to add a user, the user is added and the user is viewing the users page.
  