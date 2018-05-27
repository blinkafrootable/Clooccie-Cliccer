package com.blinakfrootable.clicc;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionListener;

class Generator extends Button {
    private Timer timer;
    private int count, index;
    private long cost, baseCost, production;

    Generator(String label, long baseCost, long production, int timerDelay, int index, ActionListener a) {
        this.setLabel(label);
        this.addActionListener(a);
        this.timer = new Timer(timerDelay, a);
        this.baseCost = baseCost;
        this.cost = baseCost;
        this.production = production;
        this.count = 0;
        this.index = index;
    }

    public void addCount() {
        this.count++;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public void setProduction(long production) {
        this.production = production;
    }

    public Timer getTimer() {
        return timer;
    }

    public int getCount() {
        return count;
    }

    public int getIndex() {
        return index;
    }

    public long getCost() {
        return cost;
    }

    public long getBaseCost() {
        return baseCost;
    }

    public long getProduction() {
        return production;
    }

}
