# Run GPIO script
python /home/pi/AudibleAppliances/gpio/gpio.py &

# Start the python image server script
./home/pi/runserver &

# Start the interface web server
./home/pi/runwebserver &

# Run the actual program
./home/pi/runjar &
