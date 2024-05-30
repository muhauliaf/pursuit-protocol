package edu.uchicago.mauliafirmansyah.controller;

import edu.uchicago.mauliafirmansyah.model.GameObject;

import java.util.concurrent.LinkedBlockingDeque;

// Factory class for creating and managing GameObject instances
public class GameObjectFactory extends LinkedBlockingDeque<GameObject> {

    // Plan to create a GameObject by adding it to the factory
    public void plan(GameObject gameObject) {
        addLast(gameObject);
    }

    // Build a GameObject by removing it from the factory
    public GameObject build() {
        return removeFirst();
    }
}
