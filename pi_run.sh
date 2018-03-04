#Increase volume to 100%
amixer set PCM -- 100%

# Run GPIO script
python gpio/gpio.py &

# Move to directory
cd AudibleAppliances-master

# Start the python image server script
./AudibleAppliances-master/daemon/daemon &

# Start the interface web server
cd webInterface
python main.py &

# Run the actual program
cd ..
java -jar audible-1.0-SNAPSHOT-jar-with-dependencies.jar &
