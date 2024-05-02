package org.tbloomfield.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.tbloomfield.graphs.pathfinding.BFSPathfinding;
import org.tbloomfield.graphs.pathfinding.DFSPathfinding;
import org.tbloomfield.graphs.pathfinding.PathfindingAlgo;
import org.tbloomfield.graphs.pathfinding.AStar.AStarPathfinding;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent; 

import javafx.scene.Scene;

/**
 * A UI for Rending Pathfinding demonstrations. 
 */
public class Render extends Application {
  
  private final static int WINDOW_SIZE = 800;
  private final static int TARGET_COLUMNS = 20;
  private final int percentageBlocked = 30;
  private List<List<UINode>> rows = new ArrayList<>(); //row,column list.
  
  private GraphicsContext graphicsContext2D;
  private final ExecutorService pathfindingThread = 
          Executors.newFixedThreadPool(1, Thread.ofVirtual().factory());
  
  private PathfindingAlgo algo = new DFSPathfinding();

  public static void main(String[] args) {    
    launch();
  }
  
  /**
   * Primary UI layout
   */
  @Override
  public void start(Stage primaryStage) {      
      //node display
      Canvas canvas = new Canvas();      
      canvas.setHeight(WINDOW_SIZE);
      canvas.setWidth(WINDOW_SIZE);
      
      GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
      VBox vbox = new VBox(canvas);
      Scene scene = new Scene(vbox);
      
      //menu
      MenuBar menuBar = addMenu();
      ((VBox) scene.getRoot()).getChildren().addAll(menuBar);
   
      
      primaryStage.setTitle("Pathfinding Demo");
      primaryStage.setScene(scene);
      primaryStage.show();

      this.graphicsContext2D = graphicsContext2D;
      initializeNodes();
      initializeBlockedNodes();
      renderAll();
  }

  /**
   * Create menubar and actions for each menu item.
   * @return
   */
  private MenuBar addMenu() {
    //traversal selection
    Menu traverse = new Menu("Traversals");    
    MenuItem bfs = new MenuItem("Breadth First Search");
    bfs.setOnAction(new EventHandler<ActionEvent>() { 
        public void handle(ActionEvent e){ 
            algo = new BFSPathfinding();
            resetAll();
        }
    });    
    MenuItem dfs = new MenuItem("Depth First Search");
    dfs.setOnAction(new EventHandler<ActionEvent>() { 
        public void handle(ActionEvent e){ 
            algo = new DFSPathfinding();
            resetAll();
        }
    });    
    MenuItem astar = new MenuItem("A* Search");
    astar.setOnAction(new EventHandler<ActionEvent>() { 
        public void handle(ActionEvent e){ 
            algo = new AStarPathfinding();
            resetAll();
        }
    }); 
    traverse.getItems().addAll(List.of(bfs, dfs, astar));
    //--end traversal selection
    
    //controls
    Menu actions = new Menu("Actions");
    MenuItem start = new MenuItem("Start");
    start.setOnAction(new EventHandler<ActionEvent>() { 
      public void handle(ActionEvent e){ 
          findPath(); 
      } 
    });    
    MenuItem reset = new MenuItem("Reset");
    reset.setOnAction(new EventHandler<ActionEvent>() { 
      public void handle(ActionEvent e){ 
          rows.clear();
          initializeNodes();
          initializeBlockedNodes();
          renderAll();
      } 
    });    
    actions.getItems().addAll(List.of(start, reset));
    //--end controls

    // create a menubar 
    MenuBar mb = new MenuBar(); 
    mb.getMenus().addAll(List.of(actions, traverse));
    return mb;
  }

  /**
   * Executes pathfinding in a separate draw/execution thread to enable the primary
   * draw() thread to run unimpeded. 
   */
  private void findPath() {
    //wait until the first draw of our nodes has completed;
    pathfindingThread.submit( () -> {
        algo.init(rows);
        renderAll(); //update draw canvas if init changes our row UI.
        algo.find();        
    });
  }

  /**
   * Creates initial grid of nodes.
   */
  private void initializeNodes() {
    int cellSize = Math.round(WINDOW_SIZE / (TARGET_COLUMNS));
        
    for (int row = 0; row < TARGET_COLUMNS; row++) {
      List<UINode> columnList = new ArrayList<>();
      for (int col = 0; col < TARGET_COLUMNS; col++) {
        BaseNode node = new BaseNode(col, row);
        UINode uiNode = new UINode(cellSize, cellSize, graphicsContext2D);
        uiNode.setNode(node);
        columnList.add(uiNode);
      }
      rows.add(columnList);
    }
  }
  
  /**
   * Create random blocked nodes
   */
  private void initializeBlockedNodes() {
    int totalColumns = TARGET_COLUMNS*TARGET_COLUMNS; 
    int targetNumberBlocked = Math.round(totalColumns * (((float) percentageBlocked)/100));
    Random r = new Random();
    for(int i = 0; i < targetNumberBlocked; i++) {
        
      int blockedCellIndex = r.nextInt(TARGET_COLUMNS);
      int blockedRowIndex = r.nextInt(TARGET_COLUMNS);
      
      UINode node = rows.get(blockedRowIndex).get(blockedCellIndex);
      if(node.isOpen()) {
          node.setOpen(false);
      } else {
          //try again in the next loop
          i--;
      }
    }
    
    //ensure that (0,0) and (target,target) are unblocked
    rows.get(0).get(0).setOpen(true);
    rows.get(TARGET_COLUMNS-1).get(TARGET_COLUMNS-1).setOpen(true);
  }
  
  /**
   * Draws all nodes.
   */
  private void renderAll() {
    for(List<UINode> row : rows) { 
      for(UINode node : row) { 
        node.draw();
      }
    }
  }
  
  /**
   * Resets all UI nodes back to original draw state.
   */
  private void resetAll() {
      for(List<UINode> row : rows) { 
        for(UINode node : row) {
            //reset any special node types / text overlays (such as A* nodes)
            node.setNode(new BaseNode(node.getNode().getXPos(), node.getNode().getYPos()));
            if(node.isEndNode()) { 
                node.setEnd(false);
            } 
            if(node.isVisited()) {
                node.setVisited(false);
            }
            if(node.isOptimalPath()) { 
                node.setOptimalPath(false);
            }
        }
      }
    }
}