# -*-coding: utf-8 -*- 

import os
import sys
import RPi.GPIO as GPIO
import time
import urllib2
import picamera
import time
import datetime
import socket

GPIO.setmode(GPIO.BCM)
GPIO.setup(6, GPIO.IN)
soc = socket.socket()
host = '192.168.76.60'
port = 2004
soc.bind((host, port))
soc.listen(5)

print "Press the button"
os.system('uv4l --driver raspicam --auto-video_nr --object-detection --min-object-size 80 80 --main-classifier /usr/share/uv4l/raspicam/lbpcascade_frontalface.xml --object-detection-mode accurate_detection --width 340 --height 360 --framerate 15 --encoding h264') 
try:
  while True:
    conn, addr = soc.accept()
    msg = conn.recv(1024)
    print 'Data receive Succeed------'
    if GPIO.input(6)==1:
      print "Button Down"
      time.sleep(1)
      HOST = "192.168.76.49"
      response = urllib2.urlopen('http://192.168.76.49:8080/mvc/mobile/sendFCM')
      html = response.read()
      response.close()
    elif msg is not None:
      msg = None
      print 'press android'
      now = datetime.datetime.now()
      nowDatetime = '/home/pi/gpio-test/piPictures/'+ now.strftime('%Y-%m-%d %H.%M.%S'+'.jpg')
      
      print ('Got connection from',addr)
      
      #if msg is not None:
      os.system('pkill uv4l')
      time.sleep(1)
      with picamera.PiCamera() as camera:
        camera.capture(nowDatetime)
        print nowDatetime
      os.system('uv4l --driver raspicam --auto-video_nr --object-detection --min-object-size 80 80 --main-classifier /usr/share/uv4l/raspicam/lbpcascade_frontalface.xml --object-detection-mode accurate_detection --width 340 --height 360 --framerate 15 --encoding h264')
      print "Press the button (CTRL-C to exit)"
    
except KeyboardInterrupt:
  GPIO.cleanup()
