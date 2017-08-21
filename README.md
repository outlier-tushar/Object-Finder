# Object-Finder

Object Finder is an iBeacon Distance Estimation Android application.

In this project, HM-10 Bluetooth Low Energy module is configured in iBeacon mode.

<img src="https://www.dropbox.com/s/5cjsa29tl0t6vtv/oie_7ULYnXMOSolI.jpg?raw=true" height="300" width="300">

Here HM-10 BLE module is coupled with a rechargeable Li-Po battery and a micro USB charging board.
The device dimensions are roughly about 4 cm x 3.5 cm. 

<H2>
<B> ABOUT THE APP </B>
</H2>
The app allows Android devices to search nearby Beacons and estimate distances.

Since there is no default library provided by Google to detect iBeacons, I've used 
<a href="https://github.com/AltBeacon/android-beacon-library">AltBeacon</a> library developed by <a href="https://github.com/davidgyoung"> davidyoung</a> to detect and interact with nearby Beacons.

<h4>Screenshots</h4>
<table style="width:100%">
  <tr>
    <td><img src = "https://www.dropbox.com/s/r6bqpmj7cecb0yj/searching.png?raw=true" height="640" width="350"></td>
    <td><img src = "https://www.dropbox.com/s/ytxiqndd8vdmq3c/found-keys.png?raw=true" height="640" width="350"></td>
  </tr>
  <tr>
    <td><img src = "https://www.dropbox.com/s/2is106g8j414rxf/distance-est.png?raw=true" height="640" width="350"></td>
    <td><img src = "https://www.dropbox.com/s/urn5ycvda9pfxs9/nav-drawer.png?raw=true" height="640" width="350"></td>
  </tr>  
</table>

<h3>IMPORTANT:</h3>
<h4>THE APP CAN ONLY DETECT iBeacons. NO OTHER CONFIGURATION WILL BE DETECTED OR DISPLAYED IN THE APP</h4>

