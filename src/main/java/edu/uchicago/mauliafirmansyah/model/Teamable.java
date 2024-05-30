package edu.uchicago.mauliafirmansyah.model;

// Teamable interface representing objects that belong to a team
public interface Teamable {

    // Enum defining possible team values: FRIEND, CIVILIAN, ENEMY
    enum Team {FRIEND, CIVILIAN, ENEMY};

    // GetTeam method to retrieve the team of the object
    Team getTeam();
}
