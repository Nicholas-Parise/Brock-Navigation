package ca.brocku.MazeSolver;

/** Runnable task to perform path graphic checks and updates off of the UI thread
 *
 */

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

import java.util.ArrayList;
import java.util.List;

import ca.brocku.BrockNavigation.NodeType;

public class PathGraphicUpdate implements Runnable {

    static final int EARTH = 6371000;
    static final double APPROACH_THRESHOLD = 10;
    List<PointCollection> floor200Points;
    List<PointCollection> floor300Points;
    List<PointCollection> floor400Points;

    SimpleLineSymbol polyLineSymbol;
    SimpleMarkerSymbol markerSymbol;
    GraphicsOverlay graphicsOverlay;
    MainActivity main;
    Point userPosition;
    SharedVariable<Integer> currentFloor;
    int displayFloor;
    ArrayList<ca.brocku.BrockNavigation.Node> path;

    public PathGraphicUpdate(MainActivity main,
                             Point userPosition,
                             List<PointCollection> floor200Points,
                             List<PointCollection> floor300Points,
                             List<PointCollection> floor400Points,
                             GraphicsOverlay graphicsOverlay,
                             SharedVariable<Integer> currentFloor,
                             int displayFloor,
                             ArrayList<ca.brocku.BrockNavigation.Node> path){
        this.main = main;
        this.floor200Points = floor200Points;
        this.floor300Points = floor300Points;
        this.floor400Points = floor400Points;
        this.graphicsOverlay = graphicsOverlay;
        this.userPosition = userPosition;
        this.currentFloor = currentFloor;
        this.displayFloor = displayFloor;
        this.path = path;
        polyLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, main.getColor(R.color.BrockRed), 1);
        markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, main.getColor(R.color.BrockRed), 10);
    }

    @Override
    public void run() {
        //System.out.println("Current: "+currentFloor.getData() + " Displayed: " + displayFloor);
        if(path==null||path.isEmpty()) return;

        ca.brocku.BrockNavigation.Node n = path.get(path.size()-1);
        Point next = new Point(n.getLongitude(), n.getLatitude());
        // Get next point based on floor and refresh user position is path graphic list
        switch (currentFloor.getData()){
            case 2:
                floor200Points.get(floor200Points.size()-1).remove(0);
                floor200Points.get(floor200Points.size()-1).add(0, userPosition);
                break;
            case 3:
                floor300Points.get(floor300Points.size()-1).remove(0);
                floor300Points.get(floor300Points.size()-1).add(0, userPosition);
                break;
            case 4:
                floor400Points.get(floor400Points.size()-1).remove(0);
                floor400Points.get(floor400Points.size()-1).add(0, userPosition);
                break;
        }

        // Check if user is close enough to the next point to delete it
        if (calculateDistance(userPosition.getX(), userPosition.getY(), next.getX(), next.getY()) < APPROACH_THRESHOLD) {
            int lastFloor = currentFloor.getData();

            // If the node being deleted is a staircase
            if(path.get(path.size()-1).getType() == NodeType.STAIRS){
                main.runOnUiThread(() -> {
                    int floor = path.get(path.size()-2).getFloor();
                    main.showFloorChangeDialog(floor);
                    main.setFloor(floor);
                });

                currentFloor.setData(path.get(path.size()-2).getFloor());

                // Delete staircase graphic on both floors
                switch (lastFloor){
                    case 2:
                        floor200Points.remove(floor200Points.size()-1);
                        break;
                    case 3:
                        floor300Points.remove(floor300Points.size()-1);
                        break;
                    case 4:
                        floor400Points.remove(floor400Points.size()-1);
                        break;
                }

                switch (currentFloor.getData()){
                    case 2:
                        floor200Points.get(floor200Points.size()-1).remove(1);
                        floor200Points.get(floor200Points.size()-1).add(0, userPosition);
                        break;
                    case 3:
                        floor300Points.get(floor300Points.size()-1).remove(1);
                        floor300Points.get(floor300Points.size()-1).add(0, userPosition);
                        break;
                    case 4:
                        floor400Points.get(floor400Points.size()-1).remove(1);
                        floor400Points.get(floor400Points.size()-1).add(0, userPosition);
                        break;
                }
            }
            else{ // Not staircase so just delete
                switch (currentFloor.getData()){
                    case 2:
                        floor200Points.get(floor200Points.size()-1).remove(0);
                        break;
                    case 3:
                        floor300Points.get(floor300Points.size()-1).remove(0);
                        break;
                    case 4:
                        floor400Points.get(floor400Points.size()-1).remove(0);
                        break;
                }
            }
            path.remove(path.size()-1);
        }

        graphicsOverlay.getGraphics().clear();

        // Draw correct paths based on displayed floor
        switch (displayFloor){
            case 2:
                for(int i = 0; i < floor200Points.size(); i++){
                    Polyline line = new Polyline(floor200Points.get(i));
                    Graphic g = new Graphic(line, polyLineSymbol);
                    graphicsOverlay.getGraphics().add(g);
                }
                break;
            case 3:
                for(int i = 0; i < floor300Points.size(); i++){
                    Polyline line = new Polyline(floor300Points.get(i));
                    Graphic g = new Graphic(line, polyLineSymbol);
                    graphicsOverlay.getGraphics().add(g);
                }
                break;
            case 4:
                for(int i = 0; i < floor400Points.size(); i++){
                    Polyline line = new Polyline(floor400Points.get(i));
                    Graphic g = new Graphic(line, polyLineSymbol);
                    graphicsOverlay.getGraphics().add(g);
                }
                break;
        }

        // Draw the destination as a Point on the map when the correct floor is displayed
        if(path.size()>0){
            ca.brocku.BrockNavigation.Node last = path.get(0);
            if(displayFloor == last.getFloor()){
                Point destination = new Point(last.getLongitude(), last.getLatitude());
                Geometry point = GeometryEngine.project(destination, SpatialReferences.getWgs84());

                Graphic destinationGraphic = new Graphic(point, markerSymbol);
                graphicsOverlay.getGraphics().add(destinationGraphic);
            }
        }

    }

    /**
     * calculate distance between 2 two points in latitude and longitude
     * @param lon1 longitude 1
     * @param lat1 latitude 1
     * @param lon2 longitude 2
     * @param lat2 latitude 2
     * @return
     */
    public double calculateDistance(double lon1, double lat1, double lon2, double lat2) {

        double latDistance = Math.toRadians(lat2 - lat1);

        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = c * EARTH;

        return distance;
    }
}
