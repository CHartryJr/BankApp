## Table of Contents
- [Project Overview](#project-overview)
- [Getting Started](#Getting-Started)
- [Usage](#usage)
- [Dependencies](#dependencies)
- [Contributing](#contributing)

## Project Overview
designed to provide users with a realistic banking experience while showcasing core software
development concepts, including server-client architecture, SQLite database management, and
concurrent operations. The application will be built using Java for the backend, JavaFX for the
frontend GUI, and will feature visual CRUD (Create, Read, Update, Delete) operations.
## Getting Started
This app is only supported by Mac Intel 64x.
There are 3 stand alone files. BankServer.java, OnlineBank.java, and BankManager.java.
The Makefile will be used to run each file. First in the terminal run the "make" command to build the project. secondly, to run any of the files use make and any of the file names. Each word within the file name is separated with a under score. Ex. "make Bank_Manager". For those Using Vscode We have a launch.Json file.
## Usage
here are three main files that this project is comprised of: BankServer.java, OnlineBank.java, and BankManager.java. Each one of these files is a separate application, and the BankServer.java file needs to be run first before any data is to be transferred. However, before any of these files can be run, they need to first be built using the Makefile located within the BankApp folder. In order to initiate the build sequence, use the "make" command to execute the Makefile code in the terminal, which ensures that all the files are compiled and there are now executable files that can be run. Individual files can be run using the "make" command in the terminal followed their respective names in the following format: 		make first_last (e.g. make bank_server). 
For Visual Studio Code users, a dedicated launch.json file has been configured to help optimize the execution process.
For two out of the three main files listed above, both of them have associated folders with the following naming conventions: OnlineBank has the client folder and BankManager has the teller folder. All other folders/files that do not sit in the named conventions are universally used amongst the entire project.
After the server (make bank_server) has started, the client and/or teller may be started to connect and log in. For the client (make online_bank), when the GUI pops up it will ask the user to enter a username and password, and if that user has an account already in the database then all they have to do is click the log in button. However, if the user does not have an account in the database, then the user has to click the create account button. Another GUI will pop up and it will ask the user to enter in some of their information to enter in to the database (first name, last name, physical address, new username, PIN (password)). After the user enters in all required information (and confirm the PIN), they can either click the cancel or submit buttons. The cancel button will take the user back to the login GUI and the submit button will take all the user's information and add it to the database. Clicking the submit button will also take the user back to the log in GUI for them to enter in their new username and password (PIN). After the user has entered in their username and password, the project will pull up their information in another GUI that displays their name, account number in the database, total amount of money in their Savings and Checking accounts, and the date that their account was added. In the bottom half of the GUI there are two buttons: Savings and Checking. Clicking either button will display their respective information for the user's account and also shows the transaction history (transaction id, amount of money (transferred, deposited, or withdrawn), and the date of that transaction). In the upper half of the GUI there are two buttons the user can click: log out and submit. Clicking the log out button will log the user out from the GUI and bring them back to the log in GUI. Clicking the submit button will submit whatever information they have entered into the boxes. The first drop-down box has three choices: Deposit, Withdrawal, and Transfer. After selecting either the deposit or withdrawal options, the user then needs to enter the amount they want put in or take out and the account they want the transaction to go to. If the user selects transfer, then another drop-down box will appear asking the user to select an account they want to transfer from.
For the teller (make bank_manager), when the guy for the login pops up the user is prompt a login for Teller-side information. After logging in the user is then given a search information page. With the Information page you can search though the users of the application using the search criteria: Account, First Name, and Last Name. Once the results show in the information table, the user can click on the account of interest. Once clicked the information based on account of interest will show. The user will have 4 options to decide from. You can delete the entire account of interest by pressing “delete account” under the user meta data on the left side of the window, press “delete” after selecting a transaction to undo, refreshing the page by pressing refresh or exiting the page by pressing exit.
## dependencies
Java Develoment kit 21.0.1 [Site](https://www.oracle.com/java/technologies/downloads/#java21)
JavaFX  21.0.1 [Site](https://gluonhq.com/products/javafx/)
Jdbc 3.43.22 [site](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc/3.43.2.2)
sl4j 1.7.36 [site](https://mvnrepository.com/artifact/org.slf4j/slf4j-api)
## Contributing
Carl Hartry Jr.
[GitHub](https://github.com/CHartryJr)

Brady BlackStone
[GitHub](https://github.com/Brady-Blackstone)