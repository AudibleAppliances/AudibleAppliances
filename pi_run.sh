# Run GPIO script
python gpio/gpio.py &

# Move to directory
cd AudibleAppliances-master

# Start the python image server script
python image-server.py &

# Start the interface web server
cd webInterface
python main.py &

# Run the actual program
cd ..
java -jar audible-1.0-SNAPSHOT-jar-with-dependencies.jar &
