package ca.brocku.BrockNavigation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

public class Map implements Serializable {

    private static final int EARTH = 6371000;

    ArrayList<Node> graph;

    ArrayList<Node> rooms;
    ArrayList<Node> traversal;
    ArrayList<Node> stairs;
    ArrayList<Node> bathrooms;
    ArrayList<Node> doors;

    public Map(String points, String edges){

        graph = new ArrayList<>();
        rooms = new ArrayList<>();
        traversal = new ArrayList<>();
        stairs = new ArrayList<>();
        bathrooms = new ArrayList<>();
        doors = new ArrayList<>();

        readInNodes(points);
        readInEdges(edges);
    }

    protected ArrayList<Node> getGraph() {
        return graph;
    }

    protected ArrayList<Node> getRooms() {
        return rooms;
    }

    private void readInNodes(String filename){

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            myReader.useDelimiter(",|\r\n");

            // skips first line
            myReader.nextLine();

            while (myReader.hasNext()) {

                int id = Integer.valueOf(myReader.next());
                double longitude = Double.valueOf(myReader.next());
                double latitude = Double.valueOf(myReader.next());
                String label = myReader.next();
                String description = myReader.next();
                char type = myReader.next().charAt(0);
                int floor = Integer.valueOf(myReader.next());

                //System.out.println(id+"+"+longitude+"+"+latitude+"+"+label+"+"+description+"+"+type+"+"+floor);

                Node temp = new Node(id,longitude,latitude,floor,label,description,convertType(type));

                graph.add(id,temp);

                switch (convertType(type)){
                    case ROOM:
                        rooms.add(temp);
                        break;
                    case HALLWAY:
                        traversal.add(temp);
                        break;
                    case STAIRS:
                        stairs.add(temp);
                        break;
                    case BATHROOM:
                        bathrooms.add(temp);
                        break;
                    case DOOR:
                        doors.add(temp);
                        break;
                }
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readInEdges(String filename){

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            myReader.useDelimiter(",|\r\n");

            // skips first line
            myReader.nextLine();

            while (myReader.hasNext()) {

                int origin = Integer.parseInt(myReader.next());
                int destination = Integer.parseInt(myReader.next());

                 //System.out.println(origin+"|"+destination);

               addEdge(origin,destination);
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param id1 first node
     * @param id2 destination node
     */
    private void addEdge(int id1, int id2){

        Node a = graph.get(id1);
        Node b = graph.get(id2);

        double distance = calculateDistance(a,b);

        a.addEdge(new Edge(b,distance));
        b.addEdge(new Edge(a,distance));
    }


    private NodeType convertType(char nt){
        switch (nt){
            case 'S':
            case 's':
                return NodeType.STAIRS;
            case 'R':
            case 'r':
                return NodeType.ROOM;
            case 'B':
            case 'b':
                return NodeType.BATHROOM;
            case 'D':
            case 'd':
                return NodeType.DOOR;
            case 'T':
            case 't':
            default:
                return NodeType.HALLWAY;
        }
    }



    /**
     * returns the closest room node to provided coordinates
     * @param longitude current long
     * @param latitude current long
     * @return room node
     */
    protected Node getClosestNode(double longitude, double latitude){

        return getClosestNode(longitude,latitude,2,NodeType.ROOM).get(0);
    }


    /**
     * get the closest node of type: type
     * @param longitude current long
     * @param latitude current lat
     * @param type NodeType parameter
     * @return closest node of type: type
     */
   protected Node getClosestNode(double longitude, double latitude, NodeType type) {
       return getClosestNode(longitude,latitude,2,type).get(0);
    }


    /**
     * This method will return a list of the closest room nodes to the provided coordinates
     * @param longitude current long
     * @param latitude current lat
     * @param number amount of elements in array
     * @return list of the closest rooms
     */
    protected ArrayList<Node> getClosestNode(double longitude, double latitude, int number){
        return getClosestNode(longitude,latitude,number,NodeType.ROOM);
    }


    /**
     * This method will return a list of the closest type of nodes to the provided coordinates
     * @param longitude current long
     * @param latitude current lat
     * @param number amount of elements in array
     * @param type NodeType parameter
     * @return list of the closest nodes of type: type
     */
    protected ArrayList<Node> getClosestNode(double longitude, double latitude, int number, NodeType type) {

        double dist;
        ArrayList<Node> temp;

        // here we use the Edge class since it contains distance information and a Node
        PriorityQueue<Edge> pq = new PriorityQueue<>(10);
        ArrayList<Node> closest = new ArrayList<>();

        switch (type){
            case HALLWAY:
                temp = traversal;
                break;
            case STAIRS:
                temp = stairs;
                break;
            case BATHROOM:
                temp = bathrooms;
                break;
            case DOOR:
                temp = doors;
                break;
            case ROOM:
                temp = rooms;
                break;
            default:
                temp = graph;
                break;
        }

        for (Node node: temp) {
            dist = calculateDistance(node, longitude, latitude);
            pq.add(new Edge(node,dist));
        }


        for (int i = 0; i < number; i++) {
            if(!pq.isEmpty()) {
                closest.add(pq.poll().getDestination());
            }
        }

        return closest;
    }








    /**
     * calculate distance between 2 two points in latitude and longitude
     * @param lon1 longitude 1
     * @param lat1 latitude 1
     * @param lon2 longitude 2
     * @param lat2 latitude 2
     * @return
     */
    protected double calculateDistance(double lon1, double lat1, double lon2, double lat2) {

        double latDistance = Math.toRadians(lat2 - lat1);

        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return c * EARTH;
    }


    /**
     * Calculate distance between two Nodes
     *
     * @param start start node
     * @param end end node
     * @return
     */
    protected double calculateDistance(Node start, Node end){

        return calculateDistance(start.getLongitude(), start.getLatitude(), end.getLongitude(), end.getLatitude());
    }


    protected double calculateDistance(Node start, double lon2, double lat2){
        return calculateDistance(start.getLongitude(), start.getLatitude(), lon2, lat2);
    }


    /**
     * if the two nodes share an edge, quickly get the distance between them.
     * @param a
     * @param b
     * @return
     */
    protected double findDistance(Node a, Node b){

        for (Edge e: a.getEdge()) {

            if(b == e.getDestination()){
                return e.getDistance();
            }
        }
        return -1;
    }


    /**
     * converts a label into a node
     * @param label
     * @return
     */
    protected Node getNode(String label){

        for (Node node: rooms) {
            if(node.getLabel().equalsIgnoreCase(label)){
                return node;
            }
        }
    return null;
    }









}
