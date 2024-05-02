package org.tbloomfield.graphs.pathfinding.AStar;

import org.tbloomfield.graphs.BaseNode;

import lombok.Getter;
import lombok.Setter;

public class AStarNode extends BaseNode {
  @Getter private final int gCost;   //distance from start
  @Getter private final int fCost;   //total cost to t his point.
  @Getter @Setter AStarNode parentNode; //for tracking the optimal path
  
  public AStarNode(int xPos, int yPos, int gCost, int fCost) {
    super(xPos, yPos);    
    this.gCost = gCost;
    this.fCost = fCost;
  }
  
  public String getNodeText() {
      return String.format("F: %d\nG: %d", fCost, gCost);
  }
}
