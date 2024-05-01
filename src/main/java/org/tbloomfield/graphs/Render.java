package org.tbloomfield.graphs;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.tbloomfield.graphs.pathfinding.BFSPathFinding;
import org.tbloomfield.graphs.pathfinding.PathfindingAlgo;

public class Render extends JPanel implements ActionListener {
  
  private final static int WINDOW_SIZE = 600;
  private final static int TARGET_COLUMNS = 70;
  private final int percentageBlocked = 20;
  private List<List<UINode>> rows = new ArrayList<>(); //row,column list.
  private static Render render;
  private static boolean initialScreenRendered = false;
  
  public static void main(String[] args) {
    render = new Render();
    render.createFrame();
    render.initializeNodes();
    render.initializeBlockedNodes();
    render.startRefresh();
    
    while(!initialScreenRendered) {
        try {
            Thread.sleep(Duration.ofMillis(10));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    render.findPath();
  }
  
  private void startRefresh() {
      Timer _timer = new Timer(50, this);
      _timer.start();
  }
  
  private void findPath() { 
    PathfindingAlgo algo = new BFSPathFinding();
    algo.init(rows);
    algo.find();
  }
  
  private void createFrame() { 
    JFrame frame = new JFrame("Disjoint Set Demo");
    frame.setSize(WINDOW_SIZE, WINDOW_SIZE);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.add(this);
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (render != null) {
        paint((Graphics2D) g);
    }
  }
  
  /**
   * Ticked by the timer thread to update our canvas.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
      repaint();
  }
  
  private void paint(Graphics2D g) {
    for(List<UINode> columns: rows) {
      for(UINode node: columns) { 
        node.draw(g);
      }
    }
    initialScreenRendered = true;
  }

  private void initializeNodes() {
    int cellSize = Math.round(WINDOW_SIZE / (TARGET_COLUMNS));
    
    for (int row = 0; row <= TARGET_COLUMNS; row++) {
      List<UINode> columnList = new ArrayList<>();
      for (int col = 0; col <= TARGET_COLUMNS; col++) {
        columnList.add(new UINode(cellSize, cellSize, row * cellSize, col * cellSize, row+col));
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
    rows.get(TARGET_COLUMNS).get(TARGET_COLUMNS).setOpen(true);
  }
}