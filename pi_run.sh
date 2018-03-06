#Increase volume to 100%
amixer set PCM -- 100%

# Run GPIO script
python AudibleAppliances/gpio/gpio.py &

# Start the python image server script
./runserver

# Start the interface web server
python AudibleAppliances/webInterface/main.py &

# Run the actual program
~/runjar
