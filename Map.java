package BrockNavigation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Map {

    static final int EARTH = 6371000;

    ArrayList<Node> graph;

    ArrayList<Node> rooms;
    ArrayList<Node> traversal;
    ArrayList<Node> stairs;

    public Map(){

        graph = new ArrayList<Node>();
        rooms = new ArrayList<Node>();
        traversal = new ArrayList<Node>();
        stairs = new ArrayList<Node>();

        readInNodes("points.csv");
        readInEdges("edges.csv");

        for (Node n: getClosestNode(-79.247719,43.119404,5)) {
            System.out.println(n);
        }



    }

    public ArrayList<Node> getGraph() {
        return graph;
    }

    public void readInNodes(String filename){

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

                System.out.println(id+"+"+longitude+"+"+latitude+"+"+label+"+"+description+"+"+type+"+"+floor);

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
                }
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readInEdges(String filename){

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            myReader.useDelimiter(",|\r\n");

            // skips first line
            myReader.nextLine();

            while (myReader.hasNext()) {

                int origin = Integer.parseInt(myReader.next());
                int destination = Integer.parseInt(myReader.next());

                 System.out.println(origin+"|"+destination);

               addEdge(origin,destination);
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * returns absolute closest node to provided coordinates
     *
     * @param longitude
     * @param latitude
     * @return
     */
    public Node getClosestNode(double longitude, double latitude){

        double distance = Double.MAX_VALUE;
        double tempDist;
        Node currentBest = null;

        for (Node node: rooms) {

            tempDist = calculateDistance(node, longitude, latitude);

            if(tempDist < distance){
                distance = tempDist;
                currentBest = node;
            }
        }
        return currentBest;
    }

    /**
     *
     * @param longitude
     * @param latitude
     * @param number
     * @return
     */
    public ArrayList<Node> getClosestNode(double longitude, double latitude, int number){

        double distance = Double.MAX_VALUE;
        double dist;
        Node currentBest;

        // here we use the Edge class since it contains distance information and a Node
        PriorityQueue<Edge> pq = new PriorityQueue<>(10);
        ArrayList<Node> closest = new ArrayList<Node>();

        for (Node node: rooms) {

            dist = calculateDistance(node, longitude, latitude);

            pq.add(new Edge(node,dist));
        }


        for (int i = 0; i < number; i++) {
            closest.add(pq.poll().getDestination());
        }
       // System.out.println(closest);

        return closest;
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


    /**
     * Calculate distance between two Nodes
     *
     * @param start start node
     * @param end end node
     * @return
     */
    public double calculateDistance(Node start, Node end){

        return calculateDistance(start.getLongitude(), start.getLatitude(), end.getLongitude(), end.getLatitude());
    }


    public double calculateDistance(Node start, double lon2, double lat2){
        return calculateDistance(start.getLongitude(), start.getLatitude(), lon2, lat2);
    }



    public double findDistance(Node a, Node b){

        for (Edge e: a.getEdge()) {

            if(b == e.getDestination()){
                return e.getDistance();
            }
        }
        return -1;
    }


    public Node getNode(String label){

        for (Node node: rooms) {
            if(node.getLabel().equals(label)){
                return node;
            }
        }
    return null;
    }




    private NodeType convertType(char nt){
        switch (nt){
            case 'S':
            case 's':
                return NodeType.STAIRS;
            case 'T':
            case 't':
                return NodeType.HALLWAY;
            case 'R':
            case 'r':
                return NodeType.ROOM;
            case 'B':
            case 'b':
                return NodeType.BATHROOM;
            default:
                return NodeType.HALLWAY;
        }
    }





}
