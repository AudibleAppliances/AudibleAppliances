# Run GPIO script
python /home/pi/AudibleAppliances/gpio/gpio.py &>/dev/null &

# Start the python image server script
/home/pi/runserver &>/dev/null &

sleep 5

# Start the interface web server
/home/pi/runwebserver &>/dev/null &

# Run the actual program
/home/pi/runjar &>/dev/null &
