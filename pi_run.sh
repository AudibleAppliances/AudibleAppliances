# Run GPIO script
python /home/pi/AudibleAppliances/gpio/gpio.py & &>/dev/null
echo $! >  pids

# Start the python image server script
/home/pi/runserver & &>/dev/null
echo $! >> pids

# Start the interface web server
/home/pi/runwebserver & &>/dev/null
echo $! >> pids

# Run the actual program
/home/pi/runjar & &>/dev/null
echo $! >> pids
