package ca.brocku.MazeSolver;

/** COSC 3P97 Course Project
 * This Mackenzie Chown Maze Solver app is a navigation app for Brock's most confusing building
 *
 * It makes use of the ArcGIS Runtime API for Android
 *
 * @author  Geoffrey Jensen (7148710)
 * @author  Nicholas Parise (7242530)
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.location.AndroidLocationDataSource;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.geometry.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.Manifest;

import ca.brocku.BrockNavigation.Navigator;
import ca.brocku.BrockNavigation.Node;
import ca.brocku.BrockNavigation.NodeType;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_LOCATION = 100;
    LayerList mapLayers;
    Point userPosition;

    ExecutorService locationUpdateExecutor;
    LocationDisplay locationDisplay;
    ca.brocku.BrockNavigation.Navigator navigator;
    MapView mapView;
    SharedVariable<Integer> currentFloor;
    int displayFloor;
    List<PointCollection> floor200Paths;
    List<PointCollection> floor300Paths;
    List<PointCollection> floor400Paths;
    ArrayList<ca.brocku.BrockNavigation.Node> path;
    GraphicsOverlay graphicsOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            loadApp(savedInstanceState);
        }

        else requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { //Permission granted
                loadApp(null);
            } else {
                Toast.makeText(this, "This app requires location services to run", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if(locationUpdateExecutor!=null){
            locationUpdateExecutor.shutdownNow();
            try {
                locationUpdateExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Save state information
        savedInstanceState.putSerializable("path", path);
        savedInstanceState.putSerializable("currentFloor", currentFloor);
        savedInstanceState.putInt("displayFloor", displayFloor);
    }

    private void loadApp(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);// Copy mmpk from assets to storage if it does not exist
        String mapFilePath = getExternalFilesDir(null) + "/" + SplashScreen.MAP_MMPK;
        String pointsFilePath = getExternalFilesDir(null) + "/" + SplashScreen.POINTS;
        String edgesFilePath = getExternalFilesDir(null) + "/" + SplashScreen.EDGES;

        // For storing path graphics for each floor
        floor200Paths = new ArrayList<>();
        floor300Paths = new ArrayList<>();
        floor400Paths = new ArrayList<>();

        currentFloor = new SharedVariable<>(2);
        displayFloor = 2;

        locationUpdateExecutor = Executors.newSingleThreadExecutor();

        navigator = new Navigator(pointsFilePath, edgesFilePath);

        mapView = findViewById(R.id.mapview);

        MobileMapPackage mp = new MobileMapPackage(mapFilePath);

        graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        AndroidLocationDataSource location = new AndroidLocationDataSource(this,"gps", 300, 0);
        locationDisplay = mapView.getLocationDisplay();

        locationDisplay.setLocationDataSource(location);
        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
        locationDisplay.setInitialZoomScale(500);

        mp.loadAsync();
        mp.addDoneLoadingListener(() -> {
            if (mp.getLoadStatus() == LoadStatus.LOADED && !mp.getMaps().isEmpty()) {
                System.out.println("Map is done loading!");
                mapView.setMap(mp.getMaps().get(0));
                mapLayers = mp.getMaps().get(0).getOperationalLayers();
                mapLayers.get(3).setVisible(true);
                try{
                    locationDisplay.startAsync();
                }
                catch (Exception e){
                    System.out.println(e);
                }

            } else {
                String error = "Error loading mobile map package: " + mp.getLoadError().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }

        });

        locationDisplay.addDataSourceStatusChangedListener(status -> {
            userPosition = locationDisplay.getLocation().getPosition();

            locationDisplay.addLocationChangedListener(l -> {

                userPosition = locationDisplay.getLocation().getPosition();
                if(!locationUpdateExecutor.isShutdown()){
                    locationUpdateExecutor.execute(new PathGraphicUpdate(
                            this,
                            new Point(userPosition.getX(),
                                    userPosition.getY()),
                            floor200Paths,
                            floor300Paths,
                            floor400Paths,
                            graphicsOverlay,
                            currentFloor,
                            displayFloor,
                            path));
                }
            });

            setupFloorSelect();
            setupSearchBar();

            // Restore state if orientation changed
            if(savedInstanceState != null){
                path = (ArrayList<Node>) savedInstanceState.getSerializable("path");
                currentFloor = (SharedVariable<Integer>) savedInstanceState.getSerializable("currentFloor");
                displayFloor = savedInstanceState.getInt("displayFloor", 2);
                if(path != null && !path.isEmpty()){
                    setupPathGraphics(path);
                }
                setFloor(displayFloor);
            }
        });

        // Display compass button when compass nav mode is turned off
        locationDisplay.addAutoPanModeChangedListener(autoPanMode -> {
            switch(autoPanMode.getAutoPanMode()){
                case OFF:
                    findViewById(R.id.compassNavMode).setVisibility(View.VISIBLE);
                    break;
                case COMPASS_NAVIGATION:
                    findViewById(R.id.compassNavMode).setVisibility(View.INVISIBLE);
                    break;
            }
        });
    }

    /**
     * Sets up the floor select Spinner menu
     */
    private void setupFloorSelect() {
        Spinner spinner = (Spinner) findViewById(R.id.floorSelect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.floor_array,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int i = Integer.parseInt(((TextView)view).getText().toString());
                setFloor(i/100);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // required
            }

        });

    }

    /** Set map floor.
     *  Use 2, 3 or 4;
     *
     * @param floor
     */
    public void setFloor(int floor){
        Spinner spinner = (Spinner) findViewById(R.id.floorSelect);
        spinner.setSelection(floor-2);
        displayFloor = floor;
        mapLayers.get(1).setVisible(false);
        mapLayers.get(2).setVisible(false);
        mapLayers.get(3).setVisible(false);
        if(floor>=2 && floor<=4) mapLayers.get(floor-1).setVisible(true);

        locationUpdateExecutor.shutdownNow();
        try {
            locationUpdateExecutor.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        /* //This should make the visuals smoother when switching between displayed floors but it keeps breaking literally everything
        new PathGraphicUpdate(
                this,
                new Point(userPosition.getX(),
                        userPosition.getY()),
                floor200Paths,
                floor300Paths,
                floor400Paths,
                graphicsOverlay,
                currentFloor,
                displayFloor,
                path).run();
        */

        locationUpdateExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Sets up the AutoCompleteText search bar
     */
    private void setupSearchBar() {
        AutoCompleteTextView searchBar = (AutoCompleteTextView)findViewById(R.id.searchBar);
        searchBar.setThreshold(1);
        ArrayAdapter<ca.brocku.BrockNavigation.Node> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                navigator.getRooms());
        searchBar.setAdapter(adapter);
        searchBar.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            ca.brocku.BrockNavigation.Node nodeTo  = navigator.getNode(((TextView)view).getText().toString());
            closeKeyboard();

            AlertDialog.Builder roomInfoBuilder = new AlertDialog.Builder(this);

            roomInfoBuilder.setTitle(nodeTo.getLabel());
            roomInfoBuilder.setMessage(nodeTo.getDescription());

            roomInfoBuilder.setNegativeButton("Close", null);
            roomInfoBuilder.setNeutralButton("Navigate To", (DialogInterface dialog, int which) -> {
                promptForClosestRoom(nodeTo);
            });
            roomInfoBuilder.show();
        });
    }

    /** Sets up app state for showing a path
     *
     * @param nodeFrom
     * @param nodeTo
     */
    private void setupPathFinding(ca.brocku.BrockNavigation.Node nodeFrom, ca.brocku.BrockNavigation.Node nodeTo) {
        // restart to get rid of old tasks
        locationUpdateExecutor.shutdownNow();
        try {
            locationUpdateExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        path = navigator.shortestPath(nodeFrom, nodeTo);
        currentFloor.setData(path.get(path.size()-1).getFloor());

        setupPathGraphics(path);

        locationUpdateExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Sets up the path graphics lists with the given path
     *
     * @param path
     */
    private void setupPathGraphics(ArrayList<Node> path) {
        floor200Paths = Collections.synchronizedList(new ArrayList<PointCollection>());
        floor300Paths = Collections.synchronizedList(new ArrayList<PointCollection>());
        floor400Paths = Collections.synchronizedList(new ArrayList<PointCollection>());

        getFloorList(path.get(0).getFloor()).add(new PointCollection(SpatialReferences.getWgs84()));

        int lastFloor = path.get(0).getFloor();
        // index of floor lists
        int floor200 = floor200Paths.size()-1;
        int floor300 = floor300Paths.size()-1;
        int floor400 = floor400Paths.size()-1;

        synchronized (path){
            for(int i = 0; i < path.size(); i++){
                ca.brocku.BrockNavigation.Node n = path.get(i);

                if(n.getFloor() != 0){
                    switch (n.getFloor()){
                        case 2:
                            floor200Paths.get(floor200).add(0, new Point(n.getLongitude(), n.getLatitude()));
                            break;
                        case 3:
                            floor300Paths.get(floor300).add(0, new Point(n.getLongitude(), n.getLatitude()));
                            break;
                        case 4:
                            floor400Paths.get(floor400).add(0, new Point(n.getLongitude(), n.getLatitude()));
                            break;
                    }
                }
                else { // Staircase, so add the Point to both floors
                    ca.brocku.BrockNavigation.Node next = path.get(i+1);

                    getFloorList(next.getFloor()).add(new PointCollection(SpatialReferences.getWgs84()));
                    floor200 = floor200Paths.size()-1;
                    floor300 = floor300Paths.size()-1;
                    floor400 = floor400Paths.size()-1;

                    switch(lastFloor){
                        case 2:
                            floor200Paths.get(floor200).add(0, new Point(n.getLongitude(), n.getLatitude()));
                            break;
                        case 3:
                            floor300Paths.get(floor300).add(0, new Point(n.getLongitude(), n.getLatitude()));
                            break;
                        case 4:
                            floor400Paths.get(floor400).add(0, new Point(n.getLongitude(), n.getLatitude()));
                            break;
                    }

                    switch(next.getFloor()){
                        case 2:
                            floor200Paths.get(floor200).add(0, new Point(n.getLongitude(), n.getLatitude()));
                            break;
                        case 3:
                            floor300Paths.get(floor300).add(0, new Point(n.getLongitude(), n.getLatitude()));
                            break;
                        case 4:
                            floor400Paths.get(floor400).add(0, new Point(n.getLongitude(), n.getLatitude()));
                            break;
                    }

                    lastFloor = next.getFloor();
                }
            }
            setFloor(path.get(path.size()-1).getFloor());
        }

        userPosition = locationDisplay.getLocation().getPosition();
        List<PointCollection> list = getFloorList(currentFloor.getData());
        list.get(list.size()-1).add(0, new Point(userPosition.getX(), userPosition.getY())); //Add user position to correct list
    }

    /** Returns the path graphics list for the given floor
     *
     * @param floor
     * @return
     */
    private List<PointCollection> getFloorList(Integer floor){
        switch (floor){
            case 2:
                return floor200Paths;
            case 3:
                return floor300Paths;
            case 4:
                return floor400Paths;
        }
        return null;
    }

    /**
     * Closes on screen keyboard
     */
    private void closeKeyboard() {
        // Close keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = this.getCurrentFocus();
        if (v == null) {
            v = new View(this);
        }
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    /** Compass button event
     *
     * @param view
     */
    public void setCompassNavigation(View view) {
        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
    }

    /** Makes a simple dialog saying which floor to go to
     *
     * @param floor
     */
    public void showFloorChangeDialog(int floor) {
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle("Floor Change");
        d.setMessage("Take the stairs to the "+floor+"00 level");
        d.show();
    }

    /** Bathroom button event. Find closest bathroom to travel to of certain type.
     *
     * @param view
     */
    public void findClosestBathroom(View view) {
        ArrayList<Node> nodes = navigator.getClosestNode(userPosition.getX(), userPosition.getY(), 8, NodeType.BATHROOM);

        Node destination = null;
        String type = "";
        if(view.getId() == R.id.male){
            type = "M";
        }
        else if(view.getId() == R.id.female){
            type = "F";
        }

        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).getLabel().equals(type)){
                destination = nodes.get(i);
                break;
            }
        }
        if(destination==null){
            Toast.makeText(this, "Couldn't find nearest bathroom of type " + type, Toast.LENGTH_LONG);
            return;
        }
        promptForClosestRoom(destination);
    }

    /** Open a dialog to select the closest room, then call setupPathFinding to create the route.
     *
     * @param nodeTo the destination Node
     */
    private void promptForClosestRoom(Node nodeTo) {
        // Open dialog for closest room
        Dialog findClosestRoomDialog = new Dialog(this);
        findClosestRoomDialog.setContentView(R.layout.nearest_node_dialog);
        findClosestRoomDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cancel = findClosestRoomDialog.findViewById(R.id.cancel_closest_room_dialog);
        cancel.setOnClickListener((d) -> {
            findClosestRoomDialog.dismiss();
        });

        ListView findClosestRoomListView = findClosestRoomDialog.findViewById(R.id.listview_closest_room);
        Point coordiantes = locationDisplay.getLocation().getPosition();
        ArrayAdapter<ca.brocku.BrockNavigation.Node> closestAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                navigator.getClosestNode(coordiantes.getX(), coordiantes.getY(), 10));
        findClosestRoomListView.setAdapter(closestAdapter);


        // Closest Room selected
        findClosestRoomListView.setOnItemClickListener((AdapterView<?> parent2, View view2, int position2, long id2) -> {
            ca.brocku.BrockNavigation.Node nodeFrom = navigator.getNode(((TextView)view2).getText().toString());
            setupPathFinding(nodeTo, nodeFrom);
            findClosestRoomDialog.dismiss();
        });
        findClosestRoomDialog.show();
    }
}