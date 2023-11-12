 #Constants
SOURCE_DIR="./src/"
TARGET_DIR="./bin/"
LIBRARY_DIR="./lib/"
MODULES="javafx.media,javafx.swing,javafx.fxml,javafx.controls,javafx.base,javafx.graphics"
CLASSES=";slf4j-api-1.7.36;sqlite-jdbc-3.43.2.2;"

# Run command
LAUNCH_COMMAND=java -cp $(TARGET_DIR) --module-path $(LIBRARY_DIR) --add-modules $(MODULES)

all: clean build

build:
	@javac $(SOURCE_DIR)*.java -d $(TARGET_DIR) -cp $(LIBRARY_DIR)$(CLASSES) --module-path $(LIBRARY_DIR) --add-modules $(MODULES)
	@echo "Build Successful!"

clean:
	@rm -rf $(TARGET_DIR)*
	@echo "Clean Successful!"

# Start the Online Bank app
online_bank: 
	@$(LAUNCH_COMMAND) OnlineBank
	
# Start the bank server
bank_server:
	@$(LAUNCH_COMMAND) BankServer
	
# Start the bank database interface
bank_manager:
	@$(LAUNCH_COMMAND) BankManager
