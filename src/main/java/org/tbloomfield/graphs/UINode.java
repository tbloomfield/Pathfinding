package org.tbloomfield.graphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents a UI node in a graph.
 */
@RequiredArgsConstructor
public class UINode {
    private Color color;
    private final int sizeX;
    private final int sizeY;
    private final int xPos;
    private final int yPos;
    @Getter @Setter private boolean open = true;
    @Getter @Setter private boolean visited = false;
    private final int id;
    private Graphics2D graphicsReference;
    
    public void draw(Graphics2D g) {
        Stroke stroke1 = new BasicStroke(1f);
        g.setStroke(stroke1);
        g.drawRect(xPos, yPos, sizeX, sizeY);
        this.graphicsReference = g;
        fill();
    }
    
    public void fill() {
        if(!open) {
          graphicsReference.setColor(Color.black);
        } else if(visited){
          graphicsReference.setColor(Color.blue);  
        } else { 
          graphicsReference.setColor(Color.white);
        }
        graphicsReference.fillRect(xPos, yPos, sizeX, sizeY);
    }
    
    public void setVisited(boolean visited) { 
        this.visited = visited;
        fill();
    }
}