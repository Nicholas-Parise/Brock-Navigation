package BrockNavigation;

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


    /**
     * returns the closest room node to provided coordinates
     * @param longitude current long
     * @param latitude current long
     * @return room node
     */
    public Node getClosestNode(double longitude, double latitude){

        return map.getClosestNode(longitude,latitude,2,NodeType.ROOM,-1).get(0);
    }


    /**
     * get the closest node of type: type, on all floors
     * @param longitude current long
     * @param latitude current lat
     * @param type NodeType parameter
     * @return closest node of type: type
     */
    public Node getClosestNode(double longitude, double latitude, NodeType type) {
        return map.getClosestNode(longitude,latitude,2,type,-1).get(0);
    }


    /**
     * get the closest node of type: type on a specific floor
     * @param longitude current long
     * @param latitude current lat
     * @param type NodeType parameter
     * @param floor int of floor (-1 is for all floors)
     * @return closest node of type: type
     */
    public Node getClosestNode(double longitude, double latitude, NodeType type, int floor) {
        return map.getClosestNode(longitude,latitude,2,type,floor).get(0);
    }


    /**
     * This method will return a list of the closest rooms
     * @param longitude current long
     * @param latitude current lat
     * @param number amount of elements in array
     * @return list of the closest rooms
     */
    public ArrayList<Node> getClosestNode(double longitude, double latitude, int number){
        return map.getClosestNode(longitude,latitude,number,NodeType.ROOM,-1);
    }




    /**
     * This method will return a list of the closest room nodes to the provided coordinates
     * @param longitude current long
     * @param latitude current lat
     * @param number amount of elements in array
     * @param floor int of floor (-1 is for all floors)
     * @return list of the closest rooms
     */
    public ArrayList<Node> getClosestNode(double longitude, double latitude, int number, int floor){
        return map.getClosestNode(longitude,latitude,number,NodeType.ROOM,floor);
    }


    /**
     * This method will return a list of the closest type of nodes to the provided coordinates
     * @param longitude current long
     * @param latitude current lat
     * @param number amount of elements in array
     * @param type NodeType parameter
     * @param floor specific floor of node (-1 is for all floors)
     * @return list of the closest nodes of type: type
     */
    protected ArrayList<Node> getClosestNode(double longitude, double latitude, int number, NodeType type, int floor) {
        return map.getClosestNode(longitude,latitude,number,type,floor);
    }



    public static double calculateDistance(double lon1, double lat1, double lon2, double lat2) {
        return Map.calculateDistance(lon1,lat1,lon2,lat2);
    }


    public static double calculateDistance(Node start, Node end){
        return Map.calculateDistance(start,end);
    }


    public static double calculateDistance(Node start, double lon2, double lat2){
        return Map.calculateDistance(start,lon2,lat2);
    }


    public static double findDistance(Node a, Node b){
        return Map.findDistance(a,b);
    }


    public Node getNode(String label){
        return map.getNode(label);
    }


    public ArrayList<Node> getRooms(){return map.getRooms();}

}
