package org.tbloomfield.graphs.pathfinding;

import java.time.Duration;
import java.util.Deque;
import java.util.List;

import org.tbloomfield.graphs.DelayedDeque;
import org.tbloomfield.graphs.Pair;
import org.tbloomfield.graphs.UINode;

/**
 * Uses a breadth first search algorithm to try and find the most optimal path.
 */
public class BFSPathfinding implements PathfindingAlgo {
  private Deque<Pair> bfsQueue;
  private List<List<UINode>> rows;
  private int maxWidth;
  private int maxHeight;
  
  public BFSPathfinding() { 
      bfsQueue = new DelayedDeque<>(Duration.ofMillis(10));
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
      } else if(nodePosition.column == maxWidth-1 && nodePosition.row == maxHeight-1) {
          node.setEnd(true);
          bfsQueue.clear();
          break;
      } else { 
          node.setVisited(true);
      }
      
      //go right if we haven't already been right.
      if(nodePosition.column < maxWidth-1 && shouldVisit(rows.get(nodePosition.row).get(nodePosition.column+1))) { 
          bfsQueue.offer(new Pair(nodePosition.row, nodePosition.column + 1));
      }
      //go down if we haven't already been down.
      if(nodePosition.row < maxHeight-1 && shouldVisit(rows.get(nodePosition.row+1).get(nodePosition.column))) { 
          bfsQueue.offer(new Pair(nodePosition.row+1, nodePosition.column));
      }
      //go left if we haven't already been left.
      if(nodePosition.column > 0 && shouldVisit(rows.get(nodePosition.row).get(nodePosition.column-1))) { 
          bfsQueue.offer(new Pair(nodePosition.row, nodePosition.column - 1));
      }      
      //go up if we haven't already been up.
      if(nodePosition.row > 0 && shouldVisit(rows.get(nodePosition.row-1).get(nodePosition.column))) { 
          bfsQueue.offer(new Pair(nodePosition.row-1, nodePosition.column));
      }    
    }
  }
  
  private boolean shouldVisit(UINode node) { 
      return node.isOpen() && !node.isVisited();
  }
}
