package com.etiennelawlor.tinderstack.bus.events;

public class TopCardMovedEvent {

    // Fields
    private final float posX;

    // Constructors
    public TopCardMovedEvent(float posX) {
        this.posX = posX;
    }

    // Getters
    public float getPosX() {
        return posX;
    }
}
