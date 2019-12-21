package mywebserver.Navigation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StreetCollection {
    private static Map<String, LinkedList<String>> StreetColl;
    private static StreetCollection streetCollection = null;
    private static Lock lock = new ReentrantLock();

    public static synchronized boolean setLock() throws IllegalAccessException {
        if (lock.tryLock()) return true;
        else throw new IllegalAccessException("busy"); // if lock is already owned by a thread throw ex
    }

    public static synchronized void unLock() {
        lock.unlock();
    }

    public static synchronized StreetCollection newStreetCollectionInstance() {
        if (streetCollection != null) {
        } else {
            streetCollection = new StreetCollection();
        }
        return streetCollection;
    }

    public StreetCollection() {
        StreetColl = new HashMap<String, LinkedList<String>>();
    }

    public synchronized void handleNode(Node n) {
        String city = n.getCity();
        String street = n.getStreet();
        if (StreetColl.containsKey(street)) {
            if (StreetColl.get(street) != null && !StreetColl.get(street).contains(city)) {
                StreetColl.get(street).add(city);
                // System.out.println("adding " + city + " to " + street);
            }
        } else {
            StreetColl.put(street, new LinkedList<String>());
            StreetColl.get(street).add(city);
            // System.out.println("addding " + street +" " + city);
        }
    }

    public LinkedList<String> getCitiesOfStreet(String street) {
        if (StreetColl.containsKey(street)) {
            if (StreetColl.get(street) != null) {
                return StreetColl.get(street);
            }
        }
        return null;
    }

    public boolean isInitialized() {
        return !this.StreetColl.isEmpty();
    }
}
