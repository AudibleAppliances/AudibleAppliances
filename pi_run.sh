###
# This is run from /etc/rc.local on startup as the user "pi"
# It starts all the scripts and programs that need to run for the
# appliance to be usable
###

cd /home/pi

sleep 5
echo "Audible Appliances: Starting GPIO and image server"

# Run GPIO script
python AudibleAppliances/gpio/gpio.py &>/dev/null &

# Start the python image server script
./runserver &>/dev/null &

sleep 5
echo "Audible Appliances: Starting web server and main program"

# Start the interface web server
./runwebserver &>/dev/null &

# Run the actual program
./runjar &>/dev/null &

pwd
