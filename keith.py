import time
import picamera
import io

with picamera.PiCamera() as camera:
    camera.resolution = (1280, 720)
    camera.rotation = 180 # Pi is flipped from what we want
    camera.framerate = 30
    camera.exposure_mode = 'backlight' # Give us a washed-out image
    time.sleep(1) # Allow camera to adapt to new settings

    lastCap = time.time()
    i = 0
    # use_video_port tells the pi to use video encoding rather than still image encoding
    # This produces much faster captures but lower quality images
    # We can afford to make this tradeoff
    for filename in camera.capture_continuous('/mnt/rd/test.jpg', use_video_port = True):
        thisCap = time.time()
        dur = thisCap - lastCap
        lastCap = thisCap
        print('{0}'.format(dur))
        i = i + 1
        if i > 3:
            break
