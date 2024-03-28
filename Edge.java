package BrockNavigation;

public class Edge implements Comparable<Edge>{

    Node destination;
    double distance;

    public Edge(Node destination, double distance){
        this.destination = destination;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public Node getDestination() {
        return destination;
    }


    @Override
    public int compareTo(Edge otherEdge) {
        return Double.compare(getDistance(), otherEdge.getDistance());
    }

    @Override
    public String toString() {
        return "Edge{" +
                "destination=" + destination +
                ", distance=" + distance +
                '}';
    }
}
