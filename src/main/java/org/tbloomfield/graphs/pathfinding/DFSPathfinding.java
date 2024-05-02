package org.tbloomfield.graphs.pathfinding;

import java.time.Duration;
import java.util.List;

import org.tbloomfield.graphs.Pair;
import org.tbloomfield.graphs.UINode;

public class DFSPathfinding implements PathfindingAlgo {
  private List<List<UINode>> rows;
  private int maxWidth;
  private int maxHeight;

  @Override
  public void init(List<List<UINode>> rows) {
    this.rows = rows;
    this.maxWidth = rows.size();
    this.maxHeight = rows.get(0).size();
  }

  @Override
  public void find() {
      dfs(new Pair(0,0));
  }  
  
  private boolean dfs(Pair nextPair) {      
    delay();
    
    //boundary checks
    if(nextPair.column > maxWidth || 
       nextPair.column < 0 || 
       nextPair.row > maxHeight ||
       nextPair.row < 0) {
       return false;
    } 
    
    UINode node = rows.get(nextPair.row).get(nextPair.column);
    if (nextPair.column == maxWidth-1 && nextPair.row == maxHeight-1) {
        node.setEnd(true);
        return true;
    } else { 
        node.setVisited(true);
    }
    
    //go down if we haven't already been down.
    if(nextPair.row < maxHeight-1 && shouldVisit(rows.get(nextPair.row+1).get(nextPair.column))) { 
        if(dfs(new Pair(nextPair.row+1, nextPair.column))) { 
            return true;
        }
    }

    //go right if we haven't already been right.
    if(nextPair.column < maxWidth-1 && shouldVisit(rows.get(nextPair.row).get(nextPair.column+1))) { 
        if(dfs(new Pair(nextPair.row, nextPair.column + 1))) {
            return true;
        }
    }
    
    //go left if we haven't already been left.
    if(nextPair.column > 0 && shouldVisit(rows.get(nextPair.row).get(nextPair.column-1))) { 
        if(dfs(new Pair(nextPair.row, nextPair.column - 1))) { 
            return true;
        }
    }      
    //go up if we haven't already been up.
    if(nextPair.row > 0 && shouldVisit(rows.get(nextPair.row-1).get(nextPair.column))) { 
        if(dfs(new Pair(nextPair.row-1, nextPair.column))) { 
            return true;
        }
    }
    
    return false;
    
  }
  
  private boolean shouldVisit(UINode node) { 
      return node.isOpen() && !node.isVisited();
  }
  
  private void delay() {
    try {
        Thread.sleep(Duration.ofMillis(10));
    } catch (InterruptedException e) {
        //TODO
    }
  }
}
