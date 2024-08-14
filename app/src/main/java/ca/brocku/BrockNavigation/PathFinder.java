package ca.brocku.BrockNavigation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;

public class PathFinder implements Serializable {

    Map map;

    public PathFinder(Map map){
        this.map = map;
    }

    protected ArrayList<Node> shortestPath(String start, String end){

        Node s = map.getNode(start);
        Node e = map.getNode(end);

        return shortestPath(s,e);
    }

    /**
     * uses dijkstra algorithm to find the shortest path between two nodes
     * @param start starting node
     * @param end ending node
     * @return list of steps to get from start to finish
     */
    protected ArrayList<Node> shortestPath(Node start, Node end){

        int V = map.getGraph().size();

        double dist[] = new double[V];
        Node prev[] = new Node[V];
        PriorityQueue<Node> pqueue = new PriorityQueue<Node>(V);
        ArrayList<Node> sequence = new ArrayList<>();

        dist[start.getId()] = 0;
        pqueue.add(start.setPriority(0));   // we add the start node to the queue

        for (int i = 0; i < V; i++) {
            if(i!=start.getId()) {
                dist[i] = Double.MAX_VALUE;
                prev[i] = null;
            }
        }

        while (!pqueue.isEmpty()){

            Node current = pqueue.poll();

            if(current == end){
                    // found the end
                while(current != null){
                    sequence.add(0,current);
                    current = prev[current.getId()];
                }
                return sequence;
            }

            for (Edge v:current.getEdge()) { // Go through all v neighbors of "current"
                double altDist = dist[current.getId()] + v.getDistance();

                if(altDist < dist[v.getDestination().getId()]){ // if the new distance is better than the previous

                    dist[v.getDestination().getId()] = altDist; // change distance
                    prev[v.getDestination().getId()] = current; // add to the previous array (for path finding)
                    pqueue.add(v.getDestination().setPriority(altDist)); // add node to the queue and set the priority to the distance
                }
            }
        }


        return null;
    }

}
