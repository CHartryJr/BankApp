# Constants
SOURCE_DIR="./src/"
TARGET_DIR="./bin/"
LIBRARY_DIR="./lib/"
DEPENDENCIES="javafx.media,javafx.swing,javafx.fxml,javafx.controls,javafx.base,javafx.graphics"

# Run command
LAUNCH_COMMAND=java -cp $(TARGET_DIR) --module-path $(LIBRARY_DIR) --add-modules $(DEPENDENCIES)

all: clean build
	@echo "Build is complete"

build:
	javac $(SOURCE_DIR)*.java -d $(TARGET_DIR) --module-path $(LIBRARY_DIR) --add-modules $(DEPENDENCIES)
	@echo "Build Successful!"

clean:
	@rm -rf $(TARGET_DIR)*
	@echo "Clean Successful" 
# Start the Online Bank app
online_bank: 
	$(LAUNCH_COMMAND) OnlineBank
	
# Start the bank server
bank_server:
	$(LAUNCH_COMMAND) BankServer
	
# Start the bank database interface
bank_manager:
	$(LAUNCH_COMMAND) BankManager