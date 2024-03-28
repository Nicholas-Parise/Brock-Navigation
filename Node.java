package BrockNavigation;

import java.util.ArrayList;

public class Node {

    int id;
    double longitude;
    double latitude;
    int floor;
    String Label;
    String description;
    NodeType type;
    ArrayList<Edge> edges;

    public Node(int id, double longitude, double latitude, int floor, String Label, String description, NodeType type){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.floor = floor;
        this.Label = Label;
        this.description = description;
        this.type = type;

        edges = new ArrayList<Edge>();
    }

    public int getId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public ArrayList<Edge> getEdge() {
        return edges;
    }

    public void addEdge(Edge e){
        edges.add(e);
    }


    public NodeType getType() {
        return type;
    }

    public String getLabel() {
        return Label;
    }

    public int getFloor() {
        return floor;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", floor=" + floor +
                ", Label='" + Label + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                '}';
    }
}
