package ca.brocku.BrockNavigation;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    double longitude;
    double latitude;
    Node currentNode;

    ArrayList<Node> path;

    public User(){}

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Node getCurrentNode() {return currentNode;}

    public void setCurrentNode(Node currentNode) {this.currentNode = currentNode;}

    public void setPath(ArrayList<Node> path) {this.path = path;}

    public ArrayList<Node> getPath() {return path;}
}