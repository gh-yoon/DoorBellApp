# -*-coding: utf-8 -*- 

import RPi.GPIO as GPIO
import time
import urllib2
import time

GPIO.setmode(GPIO.BCM)
GPIO.setup(17, GPIO.IN)
GPIO.setup(6, GPIO.IN)
GPIO.setup(13, GPIO.IN)
GPIO.setup(19, GPIO.IN)
GPIO.setup(26, GPIO.IN)

print "Press the button"
temp = '1'
temp = [0,0,0,0]
try:
  while True:
    if GPIO.input(17)==1:
      print "Button Down: "+str(GPIO.input(17))
      time.sleep(1)
      HOST = "192.168.76.49"
      # response = urllib2.urlopen('http://192.168.76.49:8080/mvc/mobile/sendFCM')
      data = '192.168.76.60'
      request = urllib2.Request('http://192.168.76.49:8080/mvc/mobile/sendFCM', data, {'Content-Type': 'text/plane'})
      response = urllib2.urlopen(request)
      html = response.read()
      response.close()

#    temp[0] = GPIO.input(6)
#    temp[1] = GPIO.input(13)
#    temp[2] = GPIO.input(19)
#    temp[3] = GPIO.input(26)
#    if temp[1] == 1:
#      print '번호입력'
#      print str(temp[0])+' '+str(temp[1])+' '+str(temp[2])+' '+str(temp[3])
#      time.sleep(2) 
#    print str(temp[0])+' '+str(temp[1])+' '+str(temp[2])+' '+str(temp[3])
    #time.sleep(1)
    
except KeyboardInterrupt:
  GPIO.cleanup()
