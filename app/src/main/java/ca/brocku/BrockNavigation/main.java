package ca.brocku.BrockNavigation;

public class main {


    public static void main(String[] args) {
        Navigator nav = new Navigator("points.csv","edges.csv");

        // examples
        for (Node n: nav.getClosestNode(-79.247719,43.119404,5)) {
            System.out.println(n);
        }



    }

}
