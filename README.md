<html>

<head>
<meta http-equiv=Content-Type content="text/html; charset=utf-8">
<meta name=Generator content="Microsoft Word 15 (filtered)">
<style>
<!--
 /* Font Definitions */
 @font-face
	{font-family:"Cambria Math";
	panose-1:2 4 5 3 5 4 6 3 2 4;}
 /* Style Definitions */
 p.MsoNormal, li.MsoNormal, div.MsoNormal
	{margin:0in;
	line-height:115%;
	font-size:11.0pt;
	font-family:"Arial",sans-serif;}
h2
	{margin-top:.25in;
	margin-right:0in;
	margin-bottom:6.0pt;
	margin-left:0in;
	line-height:115%;
	page-break-after:avoid;
	font-size:16.0pt;
	font-family:"Arial",sans-serif;
	font-weight:normal;}
p.MsoTitle, li.MsoTitle, div.MsoTitle
	{margin-top:0in;
	margin-right:0in;
	margin-bottom:3.0pt;
	margin-left:0in;
	line-height:115%;
	page-break-after:avoid;
	font-size:26.0pt;
	font-family:"Arial",sans-serif;}
.MsoChpDefault
	{font-family:"Arial",sans-serif;}
.MsoPapDefault
	{line-height:115%;}
 /* Page Definitions */
 @page WordSection1
	{size:8.5in 11.0in;
	margin:1.0in 1.0in 1.0in 1.0in;}
div.WordSection1
	{page:WordSection1;}
 /* List Definitions */
 ol
	{margin-bottom:0in;}
ul
	{margin-bottom:0in;}
-->
</style>

</head>

<body lang=EN-US style='word-wrap:break-word'>

<div class=WordSection1>

<p class=MsoTitle align=center style='text-align:center'><a name="_hy1e1p8es9r0"></a><span
lang=EN>Mackenzie Chown Maze Solver</span></p>

<h2><a name="_s0ozdzgd3plc"></a><span lang=EN>Authors </span></h2>

<p class=MsoNormal style='margin-left:.5in;text-indent:-.25in'><span lang=EN>●<span
style='font:7.0pt "Times New Roman"'>&nbsp;&nbsp;&nbsp;&nbsp; </span></span><span
lang=EN>Geoffrey Jensen (7148710)</span></p>

<p class=MsoNormal style='margin-left:.5in;text-indent:-.25in'><span lang=EN>●<span
style='font:7.0pt "Times New Roman"'>&nbsp;&nbsp;&nbsp;&nbsp; </span></span><span
lang=EN>Nicholas Parise (7242530)</span></p>

<h2><a name="_vmseg7rvnlbj"></a><span lang=EN>App Description</span></h2>

<p class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>            The
Mackenzie Chown Maze Solver is a GPS navigation app for the Mackenzie Chown
Complex. The building can be very confusing to navigate, and it is very easy to
get lost, especially for visitors and new students. This app aims to solve this
issue.</span></p>

<p class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>&nbsp;</span></p>

<p class=MsoNormal align=center style='text-align:center'><span lang=EN
style='font-size:12.0pt;line-height:115%'><img width=278 height=538
id=image5.png src="MC%20Maze%20Solver%20Documentation_files/image001.png"></span></p>

<p class=MsoNormal align=center style='text-align:center'><span lang=EN
style='font-size:12.0pt;line-height:115%'>&nbsp;</span></p>

<h2><a name="_2swgm3pz05ku"></a><span lang=EN>Advanced API</span></h2>

<p class=MsoNormal><span lang=EN>            </span><span lang=EN
style='font-size:12.0pt;line-height:115%'>This app utilizes the ArcGIS Runtime
API for Android developed by Esri. It’s an advanced mapping API that includes
integration with Esri’s other software products and has many advanced features.
(API Documentation: <u>https://developers.arcgis.com/android/</u>)</span></p>

<p class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>            This
app uses the API’s MapView to display our information to the user. We created a
Mobile Map Package with ArcGIS Pro that contains the map layers (basemap and
Mackenzie Chown floors). This file is packaged with the API, which makes this
app completely offline. The API fetches the device location for us and provides
a listener which we use for our own map updates. We draw the route on the map
using the GraphicsOverlay of the MapView.</span></p>

<h2><a name="_lrtzr5krnrds"></a><span lang=EN>Data</span></h2>

<p class=MsoNormal><span lang=EN>            </span><span lang=EN
style='font-size:12.0pt;line-height:115%'>Our APK comes with three asset files.</span></p>

<ol style='margin-top:0in' start=1 type=1>
 <li class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>Map.mmpk
     which contains the geographic mapping data.</span></li>
 <ol style='margin-top:0in' start=1 type=a>
  <li class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>The
      floor maps of Mackenzie Chown were originally given to us as PDFs by
      Brock’s Facilities Management, which we then converted to TIFF files, and
      finally georeferenced with ArcGIS Pro to create raster layers.</span></li>
 </ol>
 <li class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>nodes.csv
     which contains all of the nodes that make up our graph of the building.</span></li>
 <ol style='margin-top:0in' start=1 type=a>
  <li class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>There
      are over 800 nodes.</span></li>
 </ol>
 <li class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>edges.csv
     which contains all of the edges in our graph of the building.</span></li>
 <ol style='margin-top:0in' start=1 type=a>
  <li class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>There
      are over 1300 edges.</span></li>
 </ol>
</ol>

<p class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>            We
tediously collected the graph data ourselves with the help of ArcGIS Pro.</span></p>

<h2><a name="_rw894jag6p8k"></a><span lang=EN>Routing</span></h2>

<p class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>            We
chose to use a simple Dijkstra’s algorithm for finding the shortest path
between nodes. It runs fast enough that the task can be run on the UI thread
directly without needing to be put on a separate thread.</span></p>

<p class=MsoNormal><span lang=EN>&nbsp;</span></p>

<span lang=EN style='font-size:16.0pt;line-height:115%;font-family:"Arial",sans-serif'><br
clear=all style='page-break-before:always'>
</span>

<h2><a name="_mffiemvr50s4"></a><span lang=EN>&nbsp;</span></h2>

<h2><a name="_2cp5jwsyp6r0"></a><span lang=EN>First Time Setup</span></h2>

<table class=3 border=1 cellspacing=0 cellpadding=0 width=624 style='border-collapse:
 collapse;border:none'>
 <tr>
  <td width=328 valign=top style='width:246.0pt;border:none;padding:5.0pt 5.0pt 5.0pt 5.0pt'>
  <p class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>The
  data required for our app to function is included within the APK, and needs
  to be installed to the phone’s external storage. The map files are a decent
  size, so it will take some time, and there is a progress bar which accurately
  reflects the current progress. <i>Figure 1</i> shows what the screen looks
  like during this process.</span></p>
  </td>
  <td width=296 valign=top style='width:222.0pt;border:none;padding:5.0pt 5.0pt 5.0pt 5.0pt'>
  <p class=MsoNormal align=center style='text-align:center'><span lang=EN><img
  width=238 height=451 id=image2.png
  src="MC%20Maze%20Solver%20Documentation_files/image002.png"></span></p>
  <p class=MsoNormal align=center style='text-align:center'><i><span lang=EN
  style='font-size:12.0pt;line-height:115%'>Figure 1</span></i><span lang=EN
  style='font-size:12.0pt;line-height:115%'>. First launch</span></p>
  </td>
 </tr>
</table>

<h2><a name="_9i09u6gx2jm1"></a><span lang=EN>Changing Floors</span></h2>

<table class=2 border=1 cellspacing=0 cellpadding=0 width=624 style='border-collapse:
 collapse;border:none'>
 <tr>
  <td width=328 valign=top style='width:246.0pt;border:none;padding:5.0pt 5.0pt 5.0pt 5.0pt'>
  <p class=MsoNormal><span lang=EN>            </span><span lang=EN
  style='font-size:12.0pt;line-height:115%'>While traversing Mackenzie Chown,
  you will have to often change floors. When you’re following a route with the
  app, you will be notified to change floors when you approach a staircase in
  your route. <i>Figure 2</i> shows this message, and the map will
  automatically switch to show the next floor.</span></p>
  </td>
  <td width=296 valign=top style='width:222.0pt;border:none;padding:5.0pt 5.0pt 5.0pt 5.0pt'>
  <p class=MsoNormal align=center style='text-align:center'><span lang=EN
  style='font-size:12.0pt;line-height:115%'><img width=282 height=189
  id=image3.png src="MC%20Maze%20Solver%20Documentation_files/image003.png"></span></p>
  <p class=MsoNormal align=center style='text-align:center'><i><span lang=EN
  style='font-size:12.0pt;line-height:115%'>Figure 2</span></i><span lang=EN
  style='font-size:12.0pt;line-height:115%'>. Floor change</span></p>
  </td>
 </tr>
</table>

<h2><a name="_asrtihea2y6x"></a><span lang=EN>Map Screen</span></h2>

<table class=1 border=1 cellspacing=0 cellpadding=0 width=716 style='margin-left:
 -24.0pt;border-collapse:collapse;border:none'>
 <tr>
  <td width=334 valign=top style='width:250.5pt;border:none;padding:5.0pt 5.0pt 5.0pt 5.0pt'>
  <p class=MsoNormal align=center style='text-align:center'><span lang=EN
  style='font-size:12.0pt;line-height:115%'><img width=298 height=467
  id=image4.png src="MC%20Maze%20Solver%20Documentation_files/image004.png"></span></p>
  <p class=MsoNormal align=center style='text-align:center'><i><span lang=EN
  style='font-size:12.0pt;line-height:115%'>Figure 3</span></i><span lang=EN
  style='font-size:12.0pt;line-height:115%'>. Map screen</span></p>
  <p class=MsoNormal align=center style='text-align:center'><span lang=EN
  style='font-size:12.0pt;line-height:115%'>&nbsp;</span></p>
  <p class=MsoNormal align=center style='text-align:center'><span lang=EN><img
  width=284 height=206 id=image1.png
  src="MC%20Maze%20Solver%20Documentation_files/image005.png"></span></p>
  <p class=MsoNormal align=center style='text-align:center'><i><span lang=EN
  style='font-size:12.0pt;line-height:115%'>Figure 4.</span></i><span lang=EN
  style='font-size:12.0pt;line-height:115%'> Floor select</span></p>
  </td>
  <td width=382 valign=top style='width:286.5pt;border:none;padding:5.0pt 5.0pt 5.0pt 5.0pt'>
  <p class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>The
  numbered sections in <i>Figure 3</i> correspond with the numbered
  descriptions below.</span></p>
  <p class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>&nbsp;</span></p>
  <ol style='margin-top:0in' start=1 type=1>
   <li class=MsoNormal><b><span lang=EN style='font-size:12.0pt;line-height:
       115%'>Search Bar</span></b><span lang=EN style='font-size:12.0pt;
       line-height:115%'> - Used to find the room you wish to travel to. Tap to
       select and open an auto completing search field. Start typing the room
       number you want (e.g. D413) and the options will appear below. Tap the
       room number to select it. You will then be prompted for your current
       floor, so the route can be generated accordingly.</span></li>
   <li class=MsoNormal><b><span lang=EN style='font-size:12.0pt;line-height:
       115%'>Floor Select</span></b><span lang=EN style='font-size:12.0pt;
       line-height:115%'> - Tap the square to open a menu where you can select
       which level you wish to view on the map. <i>Figure 4</i> shows this
       expanded menu.</span></li>
   <li class=MsoNormal><b><span lang=EN style='font-size:12.0pt;line-height:
       115%'>Compass Mode</span></b><span lang=EN style='font-size:12.0pt;
       line-height:115%'> - This button will zoom the map to your current
       location and will automatically pan the map for you as you move. The map
       will also rotate based on the direction you are facing. To get out of
       this mode, simply move the map with your finger and it will deactivate.</span></li>
   <li class=MsoNormal><b><span lang=EN style='font-size:12.0pt;line-height:
       115%'>Bathroom Finder</span></b><span lang=EN style='font-size:12.0pt;
       line-height:115%'> - Press the icon for the desired bathroom type to
       find the closest bathroom. You will then be prompted for your current
       floor.</span></li>
   <li class=MsoNormal><b><span lang=EN style='font-size:12.0pt;line-height:
       115%'>Map </span></b><span lang=EN style='font-size:12.0pt;line-height:
       115%'>- This is the main part of the app which shows the inside of
       Mackenize Chown and draws the route path on your screen (will be a red
       line with a dot at your destination). Your current location is displayed
       as a blue circle (the circle will turn gray if there is no GPS
       connectivity). </span></li>
  </ol>
  </td>
 </tr>
</table>

<p class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>&nbsp;</span></p>

<p class=MsoNormal><span lang=EN style='font-size:12.0pt;line-height:115%'>&nbsp;</span></p>

</div>

</body>

</html>
