# script for front panel controls
# the switch is connected between HIGH and pin 14, rotary encoder between HIGH, 9 and 10
# 

import RPi.GPIO, subprocess

# numbering of pins depends on that
RPi.GPIO.setmode(RPi.GPIO.BCM)

# set as input + software pull down resistors
for i in [14,9,10]:
	RPi.GPIO.setup(i, RPi.GPIO.IN, pull_up_down=RPi.GPIO.PUD_DOWN)

#initial volume
last = 9
volume = 100

def set_volume(i):
	subprocess.call(['amixer','set','PCM',str(i)+'%'])

def call(c):
	global last
	global volume
	
	# debouncing 
	
	m = last
	last = c
	if (not (RPi.GPIO.input(c))) or (m == c) or (not (RPi.GPIO.input(m))):
		return

	# check switch
	
	if not RPi.GPIO.input(14):
		print 'VOLUME MUTED'
		set_volume(0)
		return

	# dec/increment	
	
	if (c == 9):
		volume+= 3
		if(volume > 100):
			volume = 100
		print 'VOLUME++:' + str(volume)
	else :
		volume-= 3
		if(volume < 0):
			volume = 0
		print  'VOLUME--:' + str(volume)
	set_volume(volume)

# detect rising edges with debounce time
RPi.GPIO.add_event_detect(10, RPi.GPIO.RISING, bouncetime=100)
RPi.GPIO.add_event_detect(9, RPi.GPIO.RISING, bouncetime=100)

# add event callbacks on rising edges
RPi.GPIO.add_event_callback(9, call)
RPi.GPIO.add_event_callback(10, call)

# switch, waits for any edge
while True:
	RPi.GPIO.wait_for_edge(14, RPi.GPIO.BOTH, bouncetime=500)
	if not RPi.GPIO.input(14):
		print 'SWITCH OFF'
		set_volume(0)
	else:
		set_volume(volume)
		print 'SWITCH ON'