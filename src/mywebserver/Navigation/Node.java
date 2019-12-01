package mywebserver.Navigation;

public class Node {
    private String city;
    private String street;

    public Node(String city, String street) {
        this.city = city;
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public String getStreet() {
        return this.street;
    }
}
