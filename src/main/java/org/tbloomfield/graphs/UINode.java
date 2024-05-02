package org.tbloomfield.graphs;

import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents a UI node in a graph.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UINode {
    private final int sizeX;
    private final int sizeY;
    @EqualsAndHashCode.Include @Setter @Getter private BaseNode node;

    private final GraphicsContext graphicsReference;

    //node status
    @Getter @Setter private boolean open = true;
    @Getter private boolean visited = false;
    @Getter private boolean endNode = false;
    @Getter private boolean deadend = false;
    @Getter private boolean optimalPath = false;

    public void draw() {
      graphicsReference.setFill(Color.GHOSTWHITE);
      graphicsReference.strokeRect(node.getXPos()*sizeX, node.getYPos()*sizeY, sizeX, sizeY);
      fill();
      drawText();
    }
    
    public void drawText() {
      if(node != null && node.getNodeText() != null && node.getNodeText().length() > 0) { 
        graphicsReference.setFill(Color.BLACK);
        //center text vertically
        graphicsReference.strokeText(node.getNodeText(), node.getXPos()*sizeX, node.getYPos()*sizeY+(sizeY/2), sizeX);
      }
    }
    
    public void fill() {
      if(!open) {
        graphicsReference.setFill(Color.BLACK);
      } else if(deadend) { 
        graphicsReference.setFill(Color.GREY);  
      } else if(optimalPath) { 
        graphicsReference.setFill(Color.RED);  
      } else if(visited){
        graphicsReference.setFill(Color.BLUE);  
      } else if(endNode) {
        graphicsReference.setFill(Color.RED);
      } else { 
        graphicsReference.setFill(Color.WHITE);          
      }
      graphicsReference.fillRect(node.getXPos()*sizeX, node.getYPos()*sizeY, sizeX-1, sizeY-1);
    }
    
    public void setVisited(boolean visited) { 
        this.visited = visited;
        draw();
    }
    
    //a visted node that didn't result in a successful path.
    public void setDeadend(boolean deadend) { 
        this.deadend = deadend;
        draw();
    }
    
    //the last node.
    public void setEnd(boolean end) { 
        this.endNode = end;
        draw();
    }
    
    //the last node.
    public void setOptimalPath(boolean optimalPath) { 
        this.optimalPath = optimalPath;
        draw();
    }
}