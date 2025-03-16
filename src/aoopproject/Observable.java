/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoopproject;

/**
 * Observable class for the Observer design pattern.
 * Manages a list of observers and notifies them of changes.
 */
import java.util.ArrayList;
import java.util.List;

public class Observable {
    // List of observers that are watching for changes.
    private List<Observer> observers = new ArrayList<>();

    
    public void addObserver(Observer observer) { // Adds an observer to the list.
        observers.add(observer); 
    }

    public void removeObserver(Observer observer) { //Removes an observer from the list.
        observers.remove(observer);
    }

    public void notifyObservers() { // Notifies all observers of a change.
        for (Observer observer : observers) {
            observer.update();
        }
    }
}

