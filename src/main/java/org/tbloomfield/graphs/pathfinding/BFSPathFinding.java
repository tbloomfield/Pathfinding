package org.tbloomfield.graphs.pathfinding;

import java.time.Duration;
import java.util.Deque;
import java.util.List;

import org.tbloomfield.graphs.DelayedDeque;
import org.tbloomfield.graphs.UINode;

/**
 * Uses a breadth first search algorithm to try and find the most optimal path.
 */
public class BFSPathFinding implements PathfindingAlgo {
  private Deque<Pair> bfsQueue;
  private List<List<UINode>> rows;
  private int maxWidth;
  private int maxHeight;
  
  public BFSPathFinding() { 
      bfsQueue = new DelayedDeque<>(Duration.ofMillis(5));
  }

  @Override
  public void init(List<List<UINode>> rows) {
    this.rows = rows;
    this.maxWidth = rows.size();
    this.maxHeight = rows.get(0).size(); 
         
  }
  
  /**
   * O(V+E) runtime
   */
  @Override
  public void find() {
    bfsQueue.add(new Pair(0,0));
    while(!bfsQueue.isEmpty()) {
        
      Pair nodePosition = bfsQueue.pop();        
      
      //boundary checks
      if(nodePosition.column > maxWidth || 
         nodePosition.column < 0 || 
         nodePosition.row > maxHeight ||
         nodePosition.row < 0) {
         continue;
      }
      
      UINode node = rows.get(nodePosition.row).get(nodePosition.column);
      
      //is row open?  
      if(!node.isOpen() || node.isVisited()) { 
          continue;
      } else { 
          node.setVisited(true);
      }
      
      //end column reached.
      if(nodePosition.column == maxWidth && nodePosition.row == maxHeight) { 
          break;
      }
      
      //go right if we haven't already been right.
      if(nodePosition.column < maxWidth-1 && !rows.get(nodePosition.row).get(nodePosition.column+1).isVisited()) { 
          bfsQueue.offer(new Pair(nodePosition.row, nodePosition.column + 1));
      }
      //go left if we haven't already been left.
      if(nodePosition.column > 0 && !rows.get(nodePosition.row).get(nodePosition.column-1).isVisited()) { 
          bfsQueue.offer(new Pair(nodePosition.row, nodePosition.column - 1));
      }
      //go down if we haven't already been down.
      if(nodePosition.row < maxHeight-1 && !rows.get(nodePosition.row+1).get(nodePosition.column).isVisited()) { 
          bfsQueue.offer(new Pair(nodePosition.row+1, nodePosition.column));
      }
      //go up if we haven't already been up.
      if(nodePosition.row > 0 && !rows.get(nodePosition.row-1).get(nodePosition.column).isVisited()) { 
          bfsQueue.offer(new Pair(nodePosition.row-1, nodePosition.column));
      }    
    }      
  }
  
  class Pair { 
      int row, column;
      public Pair(int row, int column) { 
          this.row = row;
          this.column = column;
      }
  }
}
