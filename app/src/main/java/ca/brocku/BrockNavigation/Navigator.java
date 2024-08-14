package ca.brocku.BrockNavigation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A composition class to make it easier to interact with the code
 */

public class Navigator implements Serializable {

    Map map;
    PathFinder pathFinder;
    User user;

    public Navigator(String points, String edges){
        map = new Map(points,edges);
        pathFinder = new PathFinder(map);
        user = new User();
    }

    public ArrayList<Node> shortestPath(String start, String end){
        return pathFinder.shortestPath(start,end);
    }

    public ArrayList<Node> shortestPath(Node start, Node end) {
        return pathFinder.shortestPath(start,end);
    }

    public Node getClosestNode(double longitude, double latitude){
        return map.getClosestNode(longitude,latitude);
    }

    public ArrayList<Node> getClosestNode(double longitude, double latitude, int number){
        return map.getClosestNode(longitude,latitude,number);
    }

    public Node getClosestNode(double longitude, double latitude, NodeType type) {
        return map.getClosestNode(longitude,latitude,type);
    }

    public ArrayList<Node> getClosestNode(double longitude, double latitude, int number, NodeType type) {
        return map.getClosestNode(longitude,latitude,number,type);
    }

    public double calculateDistance(double lon1, double lat1, double lon2, double lat2) {
        return map.calculateDistance(lon1,lat1,lon2,lat2);
    }

    public double calculateDistance(Node start, Node end){
        return map.calculateDistance(start,end);
    }

    public double calculateDistance(Node start, double lon2, double lat2){
        return map.calculateDistance(start,lon2,lat2);
    }

    public double findDistance(Node a, Node b){
        return map.findDistance(a,b);
    }

    public Node getNode(String label){
        return map.getNode(label);
    }

    public ArrayList<Node> getGraph(){
        return map.getGraph();
    }

    public ArrayList<Node> getRooms(){
        return map.getRooms();
    }


}
