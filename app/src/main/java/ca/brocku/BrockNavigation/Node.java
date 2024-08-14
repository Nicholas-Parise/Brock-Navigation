package ca.brocku.BrockNavigation;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable, Comparable<Node>{

    int id;
    double longitude;
    double latitude;
    int floor;
    String Label;
    String description;
    NodeType type;
    ArrayList<Edge> edges;

    double priority; // used for the pathfinder

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


    public double getPriority() {
        return priority;
    }

    public Node setPriority(double priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public String toString() {
        return this.Label;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(getPriority(), o.getPriority());
    }
}
