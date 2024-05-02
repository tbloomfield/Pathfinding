package org.tbloomfield.graphs;

import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents a UI node in a graph.
 */
@RequiredArgsConstructor
public class UINode {
    private final int sizeX;
    private final int sizeY;
    private final int xPos;
    private final int yPos;
    private final GraphicsContext graphicsReference; 

    //node status
    @Getter @Setter private boolean open = true;
    @Getter private boolean visited = false;
    @Getter private boolean endNode = false;
    @Getter private boolean deadend = false;

    public void draw() {
        graphicsReference.setFill(Color.GHOSTWHITE);
        graphicsReference.strokeRect(xPos, yPos, sizeX, sizeY);
        fill();
    }
    
    public void fill() {
        if(!open) {
          graphicsReference.setFill(Color.BLACK);
        } else if(deadend) { 
          graphicsReference.setFill(Color.GREY);  
        } else if(visited){
          graphicsReference.setFill(Color.BLUE);  
        } else if(endNode) {
          graphicsReference.setFill(Color.RED);
        } else { 
          graphicsReference.setFill(Color.WHITE);          
        }
        graphicsReference.fillRect(xPos, yPos, sizeX-1, sizeY-1);
    }
    
    public void setVisited(boolean visited) { 
        this.visited = visited;
        fill();
    }
    
    //a visted node that didn't result in a successful path.
    public void setDeadend(boolean deadend) { 
        this.deadend = deadend;
        fill();
    }
    
    //the last node.
    public void setEnd(boolean end) { 
        this.endNode = end;
        fill();
    }
}