<html>

<body>

<p align=center style='text-align:center'>Mackenzie Chown Maze Solver</p>

<h2>Authors</h2>

<p  style='margin-left:.5in;text-indent:-.25in'><span >●<span
style='font:7.0pt "Times New Roman"'>&nbsp;&nbsp;&nbsp;&nbsp; </span></span><span
>Geoffrey Jensen</span></p>

<p  style='margin-left:.5in;text-indent:-.25in'><span >●<span
style='font:7.0pt "Times New Roman"'>&nbsp;&nbsp;&nbsp;&nbsp; </span></span><span
>Nicholas Parise</span></p>

<h2>Download</h2>
The APK can be found at this google drive <a href="https://drive.google.com/file/d/1u4VdEwharwNgPS4EjNXo-659mdtf95HK/view?usp=drive_link">Download Link</a>.

<h2>App Description</h2>

<p >&nbsp;&nbsp;&nbsp;&nbsp;The Mackenzie Chown Maze Solver is a GPS navigation app for the Mackenzie Chown
Complex. The building can be very confusing to navigate, and it is very easy to
get lost, especially for visitors and new students. This app aims to solve this
issue.</p>


<p  align=center style='text-align:center'><span 
><img width=278 height=538
id=image5.png src="Images/image001.png"></span></p>

<p  align=center style='text-align:center'><span 
>&nbsp;</span></p>

<h2>Advanced API</h2>

<p>&nbsp;&nbsp;&nbsp;&nbsp; This app utilizes the ArcGIS Runtime
API for Android developed by Esri. It’s an advanced mapping API that includes
integration with Esri’s other software products and has many advanced features.
(API Documentation: <u>https://developers.arcgis.com/android/</u>)</span></p>

<p>&nbsp;&nbsp;&nbsp;&nbsp;This app uses the API’s MapView to display our information to the user. We created a
Mobile Map Package with ArcGIS Pro that contains the map layers (basemap and
Mackenzie Chown floors). This file is packaged with the API, which makes this
app completely offline. The API fetches the device location for us and provides
a listener which we use for our own map updates. We draw the route on the map
using the GraphicsOverlay of the MapView.</p>

<h2>Data</h2>

<p>Our APK comes with three asset files.</p>

<ol>
 <li>Map.mmpk which contains the geographic mapping data.</li>
 <ol>
  <li>The floor maps of Mackenzie Chown were originally given to us as PDFs by
      Brock’s Facilities Management, which we then converted to TIFF files, and
      finally georeferenced with ArcGIS Pro to create raster layers.</li>
 </ol>
 <li>nodes.csv which contains all of the nodes that make up our graph of the building.</li>
 <ol>
  <li>There are over 800 nodes.</li>
 </ol>
 <li>edges.csv which contains all of the edges in our graph of the building.</li>
 <ol>
  <li>There are over 1300 edges.</li>
 </ol>
</ol>

<p >We tediously collected the graph data ourselves with the help of ArcGIS Pro.</p>

<h2>Routing</h2>

<p >&nbsp;&nbsp;&nbsp;&nbsp;We chose to use a simple Dijkstra’s algorithm for finding the shortest path
between nodes. It runs fast enough that the task can be run on the UI thread
directly without needing to be put on a separate thread.</p>


<h2>First Time Setup</h2>

  <p>&nbsp;&nbsp;&nbsp;&nbsp;The data required for our app to function is included within the APK, and needs
  to be installed to the phone’s external storage. The map files are a decent
  size, so it will take some time, and there is a progress bar which accurately
  reflects the current progress. <i>Figure 1</i> shows what the screen looks
  like during this process.</p>

  <p align=center style='text-align:center'><span ><img
  width=238 height=451 id=image2.png
  src="Images/image002.png"></span></p>
  <p align=center style='text-align:center'><i>Figure 1</i>. First launch</p>


<h2>Changing Floors</h2>

<p> &nbsp;&nbsp;&nbsp;&nbsp;While traversing Mackenzie Chown,
  you will have to often change floors. When you’re following a route with the
  app, you will be notified to change floors when you approach a staircase in
  your route. <i>Figure 2</i> shows this message, and the map will
  automatically switch to show the next floor.</p>
 
  <p  align=center style='text-align:center'><span 
  ><img width=282 height=189
  id=image3.png src="Images//image003.png"></span></p>
  <p  align=center style='text-align:center'><i>Figure 2</i>. Floor change</p>
 

<h2>Map Screen</h2>

<p >The numbered sections in <i>Figure 3</i> correspond with the numbered descriptions below.</p>
    <ol>
     <li ><b>Search Bar</b> - Used to find the room you wish to travel to. Tap to
         select and open an auto completing search field. Start typing the room
         number you want (e.g. D413) and the options will appear below. Tap the
         room number to select it. You will then be prompted for your current
         floor, so the route can be generated accordingly.</li>
     <li ><b>Floor Select</b> - Tap the square to open a menu where you can select
         which level you wish to view on the map. <i>Figure 4</i> shows this
         expanded menu.</li>
     <li ><b>Compass Mode</b> - This button will zoom the map to your current
         location and will automatically pan the map for you as you move. The map
         will also rotate based on the direction you are facing. To get out of
         this mode, simply move the map with your finger and it will deactivate.</li>
     <li ><b>Bathroom Finder</b> - Press the icon for the desired bathroom type to
         find the closest bathroom. You will then be prompted for your current
         floor.</li>
     <li ><b>Map</b> - This is the main part of the app which shows the inside of
         Mackenize Chown and draws the route path on your screen (will be a red
         line with a dot at your destination). Your current location is displayed
         as a blue circle (the circle will turn gray if there is no GPS
         connectivity).</li>
    </ol>
    </td>

  <p  align=center style='text-align:center'><span 
  ><img width=298 height=467
  id=image4.png src="Images//image004.png"></span></p>
  <p  align=center style='text-align:center'><i>Figure 3</i>. Map screen</p>
  <p  align=center style='text-align:center'><span 
  >&nbsp;</span></p>

  <p  align=center style='text-align:center'><span ><img
  width=284 height=206 id=image1.png
  src="Images//image005.png"></span></p>
  <p  align=center style='text-align:center'><i>Figure 4.</i>Floor select</p>
 
</body>

</html>
