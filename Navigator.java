package BrockNavigation;

public class Navigator {

    Map map;
    PathFinder pathFinder;
    User user;

    public Navigator(){

        map = new Map();
        pathFinder = new PathFinder(map);
       // user = new User();
    }


}
