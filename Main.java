package com.blinakfrootable.clicc;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.text.DecimalFormat;

public class Main extends Applet implements ActionListener, ItemListener {

    private int cliccValue;
    private double cloocciesPerSecond;
    private long clooccies;
    private Button cliccButton;

    //generator-related vars
    private int buyButtonsCount;
    private Timer[] generatorTimers;
    private Button[] buyButtons;
    private int[] generatorCounts, generatorProduction = {1, 1, 4, 94, 13, 350, 78, 132000, 1300000};
    private long[] generatorCost;
    private final int[] generatorTimerDelays = {10000, 1000, 500, 2000, 50, 250, 10, 3000, 5000};
    private final long[] generatorBaseCost = {15L, 100L, 1100L, 12000L, 130000L, 1400000L, 20000000L, 330000000L, 5100000000L};
    
    //display graphics
    private Image bgi;
    private Graphics bgg;

    //applet attributes
    private int appletWidth, appletHeight;

    @Override
    public void init() {
        clooccies = 10000000000L;
        appletWidth = 1280; appletHeight = 720;
        super.resize(appletWidth, appletHeight);
        super.setLayout(null);
        
        //set vars
        cliccValue = 1;
        cliccButton = new Button("clicc"); cliccButton.addActionListener(this); cliccButton.setBounds(50, 450, 250, 250); super.add(cliccButton);
        
        //set up UI
        this.setUpBuyButtons();
        bgi = super.createImage(appletWidth, appletHeight);
        bgg = bgi.getGraphics();
    }

    @Override
    public void update(Graphics g) {
        this.paint(g);
    }

    @Override
    public void paint(Graphics g) {
        bgg.setColor(Color.white);
        bgg.fillRect(0, 0, appletWidth, appletHeight);

        bgg.setColor(Color.black);
        bgg.setFont(new Font("arial", Font.BOLD, 32));
        DecimalFormat formatter = new DecimalFormat("###,###.###");
        bgg.drawString(formatter.format(clooccies) + " clooccies", 50, 100);
        bgg.setFont(new Font("arial", Font.BOLD, 20));
        bgg.drawString("Clooccies/sec: " + formatter.format(Math.round(cloocciesPerSecond * 100.0) / 100.0), 50, 125);

        bgg.setFont(new Font("arial", Font.BOLD, 32));
        bgg.drawString("# Owned:", 525, 100);
        bgg.drawString("Cliccinators:", 770, 100);
        bgg.drawString("Cost:", 1100, 100);
        bgg.setFont(new Font("arial", Font.BOLD, 20));
        for (int i = 0; i < buyButtonsCount; i++) {
            bgg.drawString(formatter.format(generatorCounts[i]), 510, 165+i*50);
            bgg.drawString(formatter.format(generatorCost[i]), 1050, 165+i*50);
        }

        g.drawImage(bgi, 0, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent a) {
        Object source = a.getSource();

        if (source == cliccButton) {
            clooccies += cliccValue;
        }
        for (int i = 0; i < buyButtonsCount; i++) { //check if the event is caused by a buy button
            if (source == buyButtons[i] && clooccies >= generatorCost[i]) {
                generatorCounts[i]++;
                clooccies -= generatorCost[i];
                generatorCost[i] = Math.round(generatorBaseCost[i] * Math.pow(1.15, generatorCounts[i]));
                if (!generatorTimers[i].isRunning()) //start the timer if it hasn't started already
                    generatorTimers[i].start();
                if (buyButtons.length-1 != i) {
                    if (!buyButtons[i + 1].isEnabled())
                        buyButtons[i + 1].setEnabled(true);
                }
            }
        }
        for (int i = 0; i < buyButtonsCount; i++) { //check if the event is caused by a timer
            if (source == generatorTimers[i]) {
                clooccies += generatorProduction[i] * generatorCounts[i]; //add the production value of the timer's generator times the total amount of generators purchased
            }
        }
        this.updateCloocciesPerSecond();
        super.repaint();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        super.repaint();
    }

    private void setUpBuyButtons() {
        String[] buyButtonsNames = {"Cliccy Hand", "Granny Clicc", "Cliccriculture", "Cliccity Cavern", "Carbon Cliccoxide", "Clooccie Vault", "Shrine to the Clicc", "Black Cliccritry", "Intergalactic Clicc"};

        buyButtonsCount = buyButtonsNames.length;

        buyButtons = new Button[buyButtonsCount];
        generatorCounts = new int[buyButtonsCount];
        generatorTimers = new Timer[buyButtonsCount];
        generatorCost = generatorBaseCost.clone();
        for (int i = 0; i < buyButtonsCount; i++) { //set up buy buttons
            Button b = new Button(buyButtonsNames[i]);
            b.addActionListener(this);
            b.setBounds(725, 125+i*50, 300, 50);
            super.add(b);
            buyButtons[i] = b;
        }
        for (int i = 0; i < buyButtonsCount; i++) { //set each generator count to 0
            generatorCounts[i] = 0;
        }
        for (int i = 0; i < buyButtonsCount; i++) { //set up timers
            generatorTimers[i] = new Timer(generatorTimerDelays[i], this);
        }
        for (int i = 1; i < buyButtonsCount; i++) {
            buyButtons[i].setEnabled(false);
        }

    }

    private void updateCloocciesPerSecond() {
        cloocciesPerSecond = 0;
        for (int i = 0; i < buyButtonsCount; i++) {
            cloocciesPerSecond += ((double)generatorCounts[i] * (double)generatorProduction[i]) / ((double)generatorTimerDelays[i] / 1000.0);
        }
    }

}
