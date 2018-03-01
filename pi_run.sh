# Start the python image server script
python image-server.ph &

# Start the interface web server
cd webInterface
python main.py &

# Run the actual program
java -jar /home/pi/audible-1.0-SNAPSHOT-jar-with-dependencies.jar &
