package BrockNavigation;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;

public class PathFinder {

    Map map;

    int dist[];
    Set<Integer> visited;
    PriorityQueue<Node> pqueue;
    int V; // Number of vertices


    public PathFinder(Map map){
        this.map = map;
    }

    public Node[] shortestPath(String start, String end){

        Node s = map.getNode(start);
        Node e = map.getNode(end);

        return shortestPath(s,e);
    }


    public Node[] shortestPath(Node start, Node end){
/*
        dist = new int[V];
        visited = new HashSet<Integer>();
        pqueue = new PriorityQueue<Node>(V, new Node());


        for (int i = 0; i < V; i++)
            dist[i] = Integer.MAX_VALUE;

        // first add source vertex to PriorityQueue
        pqueue.add(new Node(src_vertex, 0));

        // Distance to the source from itself is 0
        dist[src_vertex] = 0;
        while (visited.size() != V) {

            // u is removed from PriorityQueue and has min distance
            int u = pqueue.remove().node;

            // add node to finalized list (visited)
            visited.add(u);
            graph_adjacentNodes(u);


 */

        return null;
    }


        private void graph_adjacentNodes(int u)   {
            int edgeDistance = -1;
            int newDistance = -1;
/*
            // process all neighbouring nodes of u
            for (int i = 0; i < map.graph.size(); i++) {
                Node v = map.graph.get(u);

                //  proceed only if current node is not in 'visited'
                if (!visited.contains(v.node)) {
                    edgeDistance = v.cost;
                    newDistance = dist[u] + edgeDistance;

                    // compare distances
                    if (newDistance < dist[v.node])
                        dist[v.node] = newDistance;

                    // Add the current vertex to the PriorityQueue
                    pqueue.add(new Node(v.node, dist[v.node]));
                }
            }
            }
            */
        }









}
