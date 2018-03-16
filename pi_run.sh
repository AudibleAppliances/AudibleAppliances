#Increase volume to 100%
amixer set PCM -- 100%

wget -q --spider http://google.com

if [ $? -eq 0 ]; then
    echo "Online"
else
    echo "Offline"
fi


# Run GPIO script
python AudibleAppliances/gpio/gpio.py &

# Start the python image server script
./runserver &

# Start the interface web server
./runwebserver &

# Run the actual program
~/runjar
