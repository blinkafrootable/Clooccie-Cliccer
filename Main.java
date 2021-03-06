package clicc;

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
    private final Font arialB32 = new Font("arial", Font.BOLD, 32), arialB18 = new Font("arial", Font.BOLD, 20);

    //saving-related vars
    private Button saveButton, wipeSaveButton;
    private SaveLoad saveLoad;

    //generator-related vars
    private Generator[] generators;
    private String[] generatorNames = {"Cliccy Hand", "Granny Clicc", "Cliccriculture", "Cliccity Cavern",
            "Carbon Cliccoxide", "Clooccie Vault", "Shrine to the Clicc", "Black Cliccritry", "Intergalactic Clicc",
            "Clicc Potion"};
    private int[] generatorTimerDelays = {10000, 1000, 500, 2000, 50, 250, 10, 3000, 5000, 1},
    		generatorCounts;
    private long[] generatorBaseCost = {15L, 100L, 1100L, 12000L, 130000L, 1400000L, 20000000L, 330000000L, 5100000000L, 75000000000L};
    private long[] generatorProduction = {1L, 1L, 4L, 94L, 13L, 350L, 78L, 132000L, 1300000L, 1600L};
    
    //display graphics
    private Image bgi;
    private Graphics bgg;

    //applet attributes
    private int appletWidth, appletHeight;

    @Override
    public void init() {
        clooccies = 0L;
        appletWidth = 1280; appletHeight = 720;
        super.resize(appletWidth, appletHeight);
        super.setLayout(null);
        
        //set vars
        saveLoad = new SaveLoad();
        generators = new Generator[10];
        cliccValue = 1;
        cliccButton = new Button("clicc"); cliccButton.addActionListener(this); cliccButton.setBounds(50, 450, 250, 250); super.add(cliccButton);
        saveButton = new Button("Save Game"); saveButton.addActionListener(this); saveButton.setBounds(310, 650, 100, 50); super.add(saveButton);
        wipeSaveButton = new Button("Wipe Save"); wipeSaveButton.addActionListener(this); wipeSaveButton.setBounds(420, 650, 100, 50); super.add(wipeSaveButton);
        
        //set up UI
        this.setUpGenerators();
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
        bgg.setFont(arialB32);
        DecimalFormat formatter = new DecimalFormat("###,###.###");
        bgg.drawString(formatter.format(clooccies) + " clooccies", 50, 100);
        bgg.setFont(arialB18);
        bgg.drawString("Clooccies/sec: " + formatter.format(Math.round(cloocciesPerSecond * 100.0) / 100.0), 50, 125);

        bgg.setFont(arialB32);
        bgg.drawString("# Owned:", 525, 100);
        bgg.drawString("Cliccinators:", 770, 100);
        bgg.drawString("Cost:", 1100, 100);
        bgg.setFont(arialB18);
        for (int i = 0; i < generators.length; i++) {
            bgg.drawString(formatter.format(generators[i].getCount()), 510, 165+i*50);
            bgg.drawString(formatter.format(generators[i].getCost()), 1050, 165+i*50);
        }

        g.drawImage(bgi, 0, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent a) {
        Object source = a.getSource();

        if (source == cliccButton) {
            clooccies += cliccValue;
        }
        if (source == saveButton) {
            saveLoad.saveGame(generators, clooccies);
        }
        if (source == wipeSaveButton) {
        	if (saveLoad.saveDataExists()) {
        		saveLoad.wipeSave();
        	}
        }
        for (Generator g : generators) { //check if the event is caused by a buy button
            if (source == g && clooccies >= g.getCost()) {
                g.addCount();
                clooccies -= g.getCost();
                g.setCost(Math.round(g.getBaseCost() * Math.pow(1.15, g.getCount())));
                if (!g.getTimer().isRunning()) //start the timer if it hasn't started already
                    g.getTimer().start();
                if (g.getIndex() != generators.length-1) {
                    if (!generators[g.getIndex()+1].isEnabled())
                        generators[g.getIndex()+1].setEnabled(true);
                }
            }
        }
        for (Generator g : generators) { //check if the event is caused by a timer
            if (source == g.getTimer()) {
                clooccies += g.getProduction() * g.getCount(); //add the production value of the timer's generator times the total amount of generators purchased
            }
        }
        this.updateCloocciesPerSecond();
        super.repaint();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        super.repaint();
    }

    private void setUpGenerators() {
    	if (saveLoad.saveDataExists()) {
    		System.out.println();
    		clooccies = (long) saveLoad.fetchData("cl");
    		generatorNames = (String[]) saveLoad.fetchData("l");
    		generatorProduction = (long[]) saveLoad.fetchData("p");
    		generatorBaseCost = (long[]) saveLoad.fetchData("bc");
    		generatorCounts = (int[]) saveLoad.fetchData("c");
    		generatorTimerDelays = (int[]) saveLoad.fetchData("td");
    		generators = new Generator[generatorNames.length];
    		
    		for (int i = 0; i < generators.length; i++) {
    			Generator g = new Generator(generatorNames[i], generatorBaseCost[i], generatorProduction[i], generatorTimerDelays[i], i, this);
    			g.setCount(generatorCounts[i]);
    			g.setCost(Math.round(g.getBaseCost() * Math.pow(1.15, g.getCount())));
    			if (g.getCount() > 0) {
    				g.getTimer().start();
    			}
    			g.setBounds(725, 125+i*50, 300, 50);
    			super.add(g);
    			generators[i] = g;
    		}
    		
    		for (int i = generators.length-1; i > 0; i--) {
    			if (generators[i].getCount() == 0 && generators[i-1].getCount() == 0) {
    				generators[i].setEnabled(false);
    			}
    		}
    		this.updateCloocciesPerSecond();
    	} else {
	        for (int i = 0; i < generators.length; i++) { //set up buy buttons
	            Generator g = new Generator(generatorNames[i], generatorBaseCost[i], generatorProduction[i], generatorTimerDelays[i], i, this);
	            g.setBounds(725, 125+i*50, 300, 50);
	            super.add(g);
	            generators[i] = g;
	        }
	        for (int i = 1; i < generators.length; i++) {
	            generators[i].setEnabled(false);
	        }
    	}

    }

    private void updateCloocciesPerSecond() {
        cloocciesPerSecond = 0;
        for (Generator g : generators) {
            cloocciesPerSecond += ((double)g.getCount() * (double)g.getProduction()) / ((double)g.getTimer().getDelay() / 1000.0);
        }
    }

}
