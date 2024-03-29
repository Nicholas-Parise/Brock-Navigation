package BrockNavigation;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;

public class PathFinder {

    Map map;

    public PathFinder(Map map){
        this.map = map;
    }

    public ArrayList<Node> shortestPath(String start, String end){

        Node s = map.getNode(start);
        Node e = map.getNode(end);

        return shortestPath(s,e);
    }


    public ArrayList<Node> shortestPath(Node start, Node end){

        int V = map.getGraph().size();

        double dist[] = new double[V];
        Node prev[] = new Node[V];
        PriorityQueue<Node> pqueue = new PriorityQueue<Node>(V);

        ArrayList<Node> sequence = new ArrayList<>();

        dist[start.getId()] = 0;
        pqueue.add(start.setPriority(0));

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

            for (Edge v:current.getEdge()) {
                double altDist = dist[current.getId()] + v.getDistance();

                if(altDist < dist[v.getDestination().getId()]){

                    dist[v.getDestination().getId()] = altDist;
                    prev[v.getDestination().getId()] = current;
                    pqueue.add(v.getDestination().setPriority(altDist));
                }
            }
        }


        return null;
    }











}
