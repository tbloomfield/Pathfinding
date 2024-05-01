package org.tbloomfield.graphs.pathfinding;

import java.util.List;

import org.tbloomfield.graphs.UINode;

public interface PathfindingAlgo {
    void init(List<List<UINode>> rows);
    void find();
}
