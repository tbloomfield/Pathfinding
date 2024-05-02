package org.tbloomfield.graphs.pathfinding.AStar;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tbloomfield.graphs.BaseNode;
import org.tbloomfield.graphs.DelayedPriorityQueue;
import org.tbloomfield.graphs.Pair;
import org.tbloomfield.graphs.UINode;
import org.tbloomfield.graphs.pathfinding.PathfindingAlgo;

public class AStarPathfinding implements PathfindingAlgo {    
    private Pair endNode;
    private List<List<UINode>> rows;
    private int maxWidth;
    private int maxHeight;
    
    //nodes which have been evaluated and shouldn't be again
    private Set<UINode> closedNodes;
    
    /**
     * A priority queue with a delay when calling poll() to help with easier visualizations
     */
    private final DelayedPriorityQueue<UINode> candidateNodes = new DelayedPriorityQueue<>(Duration.ofMillis(10), new Comparator<UINode>() {
        
        //best nodes will have the lowest F and G cost.  Those nodes should
        //be closest to polling to prevent unecessary iterations.
        @Override
        public int compare(UINode uiNode1, UINode uiNode2) {
          AStarNode baseNode1 = (AStarNode) uiNode1.getNode();
          AStarNode baseNode2 = (AStarNode) uiNode2.getNode();
          
          int fCostComparison = baseNode1.getFCost() - baseNode2.getFCost();
          
          //nodes with equal fcost should use the gcost as a tiebreaker (if possible)
          if(fCostComparison == 0) {             
              return baseNode1.getGCost() - baseNode2.getGCost();
          } else { 
              return fCostComparison;
          }
        }
    });
    
    @Override
    public void init(List<List<UINode>> rows) {
      maxWidth = rows.size();
      maxHeight = rows.get(0).size();
      endNode = new Pair(maxHeight, maxWidth);
      this.rows = rows;
      closedNodes = new HashSet<>();
      candidateNodes.clear();
    }
    
    private void toAStarNode(UINode parent, UINode uinode) { 
      BaseNode node = uinode.getNode();
      
      //distance from start node to this node (equals to the number of iterations to this
      //node)
      int gCost = 0, fCost = 0;
      
      //not the start node:
      if(parent != null) {
        gCost = ((AStarNode)parent.getNode()).getGCost() + 1;
        
        //distance from end node
        int xDistance = Math.abs(node.getXPos() - endNode.column);
        int yDistance = Math.abs(node.getYPos() - endNode.row);
        int hCost = xDistance + yDistance;
        
        //total move cost to this node
        fCost = gCost + hCost;
      }
        
      AStarNode anode = new AStarNode(node.getXPos(), node.getYPos(), gCost, fCost);
      uinode.setNode(anode); 
    }

    @Override
    public void find() {
      //start with evaluating nodes at the start position
      UINode rootNode = rows.get(0).get(0);
      toAStarNode(null, rootNode);
      candidateNodes.offer(rootNode);      
      
      while(!candidateNodes.isEmpty()) {
        //the head node will always contain the best "next" node to evaluate
        UINode uinode =  candidateNodes.poll();
        
        if(closedNodes.contains(uinode)) { 
            continue;
        }
        closedNodes.add(uinode);
        uinode.setVisited(true);
            
        AStarNode node = (AStarNode)uinode.getNode();                
        int curColumn = node.getXPos();
        int curRow = node.getYPos();
        
        //target destination reached, stop iterating.
        if(curColumn == maxWidth-1 && curRow == maxHeight-1) {
            //trace back to start to show the optimal path
            showOptimalPath(uinode);            
            break;
        }
        
        if(node.getXPos() < maxWidth-1) { 
          evalCandidateNode(uinode, rows.get(curRow).get(curColumn+1));
        }
        if(node.getXPos() > 0) { 
          evalCandidateNode(uinode, rows.get(curRow).get(curColumn-1));
        }
        if(node.getYPos() > 0) { 
          evalCandidateNode(uinode, rows.get(curRow-1).get(curColumn));
        }
        if(node.getYPos() < maxHeight - 1) {
          evalCandidateNode(uinode, rows.get(curRow+1).get(curColumn));
        }
      }
    }
    
    private void evalCandidateNode(UINode parent, UINode node) {
      //don't evaluate blocked or already visited nodes.  
      if(!node.isOpen() || closedNodes.contains(node)) {
        return;  
      } else {
        //convert to astar node and calculate distance:
        toAStarNode(parent, node);
          
        //set the parent to allow a traceback for the actual path to get to the end  
        ((AStarNode)node.getNode()).setParentNode((AStarNode)parent.getNode());  
        candidateNodes.offer(node);
      }
    }
    
    private void showOptimalPath(UINode uinode) {
        //don't evaluate blocked or already visited nodes.
        while(uinode.getNode().getXPos() != 0 || uinode.getNode().getYPos() != 0) {
          uinode.setOptimalPath(true);
          AStarNode parentNode = ((AStarNode)uinode.getNode()).getParentNode();
          uinode = rows.get(parentNode.getYPos()).get(parentNode.getXPos());
        }
    }
}