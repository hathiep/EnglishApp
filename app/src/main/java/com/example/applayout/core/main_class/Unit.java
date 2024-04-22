package com.example.applayout.core.main_class;

public class Unit {
    private int Animals, Economic, Health, Correspondence, Entertainment, Art, Environment, History, Sport, Construction;
    public Unit() {
    }
    public Unit(int x) {
        Animals = 0;
        Economic = 0;
        Health = 0;
        Correspondence = 0;
        Entertainment = 0;
        Art = 0;
        Environment = 0;
        History = 0;
        Sport = 0;
        Construction = 0;
    }

    public Unit(int animals, int economic, int health, int correspondence, int entertainment, int art, int environment, int history, int sport, int construction) {
        Animals = animals;
        Economic = economic;
        Health = health;
        Correspondence = correspondence;
        Entertainment = entertainment;
        Art = art;
        Environment = environment;
        History = history;
        Sport = sport;
        Construction = construction;
    }

    public int getAnimals() {
        return Animals;
    }

    public void setAnimals(int animals) {
        Animals = animals;
    }

    public int getEconomic() {
        return Economic;
    }

    public void setEconomic(int economic) {
        Economic = economic;
    }

    public int getHealth() {
        return Health;
    }

    public void setHealth(int health) {
        Health = health;
    }

    public int getCorrespondence() {
        return Correspondence;
    }

    public void setCorrespondence(int correspondence) {
        Correspondence = correspondence;
    }

    public int getEntertainment() {
        return Entertainment;
    }

    public void setEntertainment(int entertainment) {
        Entertainment = entertainment;
    }

    public int getArt() {
        return Art;
    }

    public void setArt(int art) {
        Art = art;
    }

    public int getEnvironment() {
        return Environment;
    }

    public void setEnvironment(int environment) {
        Environment = environment;
    }

    public int getHistory() {
        return History;
    }

    public void setHistory(int history) {
        History = history;
    }

    public int getSport() {
        return Sport;
    }

    public void setSport(int sport) {
        Sport = sport;
    }

    public int getConstruction() {
        return Construction;
    }

    public void setConstruction(int construction) {
        Construction = construction;
    }
}
