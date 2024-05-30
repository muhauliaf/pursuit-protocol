package edu.uchicago.mauliafirmansyah.model;

import java.util.Random;

// This interface defines a constant R of type Random, which can be used by
// classes implementing the Randomable interface to generate random values.

// Interface representing an object that can generate random values
public interface Randomable {

    // Shared Random object for generating random values
    public final static Random R = new Random();
}
