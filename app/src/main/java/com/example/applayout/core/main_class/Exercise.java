package com.example.applayout.core.main_class;

public class Exercise {
    private Unit Advanced;
    private Unit Basic;

    public Exercise() {
    }

    public Exercise(Unit advanced, Unit basic) {
        Advanced = advanced;
        Basic = basic;
    }

    public Unit getAdvanced() {
        return Advanced;
    }

    public void setAdvanced(Unit advanced) {
        Advanced = advanced;
    }

    public Unit getBasic() {
        return Basic;
    }

    public void setBasic(Unit basic) {
        Basic = basic;
    }
}
