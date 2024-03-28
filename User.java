package BrockNavigation;

public class User {

    double longitude;
    double latitude;
    Node currentNode;

    public User(double longitude, double latitude){
    this.longitude = longitude;
    this.latitude = latitude;
    }

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
}
