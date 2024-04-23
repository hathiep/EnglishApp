package com.example.applayout.core.support.Domains;

import java.util.List;

public class ExerciseDomain {
    private List<Task> tasks;

    public ExerciseDomain(List<Task> tasks) {
        this.tasks = tasks;
    }

    public ExerciseDomain() {
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public static class Task {
        private int animals;
        private int art;
        private int construction;
        private int correspondence;
        private int economic;
        private int entertainment;
        private int environment;
        private int health;
        private int history;
        private int sport;

        public Task() {
        }

        public Task(int animals, int art, int construction, int correspondence, int economic, int entertainment, int environment, int health, int history, int sport) {
            this.animals = animals;
            this.art = art;
            this.construction = construction;
            this.correspondence = correspondence;
            this.economic = economic;
            this.entertainment = entertainment;
            this.environment = environment;
            this.health = health;
            this.history = history;
            this.sport = sport;
        }



        public int getAnimals() {
            return animals;
        }

        public void setAnimals(int animals) {
            this.animals = animals;
        }

        public int getArt() {
            return art;
        }

        public void setArt(int art) {
            this.art = art;
        }

        public int getConstruction() {
            return construction;
        }

        public void setConstruction(int construction) {
            this.construction = construction;
        }

        public int getCorrespondence() {
            return correspondence;
        }

        public void setCorrespondence(int correspondence) {
            this.correspondence = correspondence;
        }

        public int getEconomic() {
            return economic;
        }

        public void setEconomic(int economic) {
            this.economic = economic;
        }

        public int getEntertainment() {
            return entertainment;
        }

        public void setEntertainment(int entertainment) {
            this.entertainment = entertainment;
        }

        public int getEnvironment() {
            return environment;
        }

        public void setEnvironment(int environment) {
            this.environment = environment;
        }

        public int getHealth() {
            return health;
        }

        public void setHealth(int health) {
            this.health = health;
        }

        public int getHistory() {
            return history;
        }

        public void setHistory(int history) {
            this.history = history;
        }

        public int getSport() {
            return sport;
        }

        public void setSport(int sport) {
            this.sport = sport;
        }

        @Override
        public String toString() {
            return "Task{" +
                    "animals='" + animals + '\'' +
                    ", art='" + art + '\'' +
                    ", construction='" + construction + '\'' +
                    ", correspondence='" + correspondence + '\'' +
                    ", economic='" + economic + '\'' +
                    ", entertainment='" + entertainment + '\'' +
                    ", environment='" + environment + '\'' +
                    ", health='" + health + '\'' +
                    ", history='" + history + '\'' +
                    ", sport='" + sport + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ExerciseDomain{" +
                "tasks=" + tasks +
                '}';
    }
}