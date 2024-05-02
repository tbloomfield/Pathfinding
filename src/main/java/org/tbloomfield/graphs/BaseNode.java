package org.tbloomfield.graphs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@EqualsAndHashCode
public class BaseNode {
  @Getter @Setter String nodeText;
  @Getter private final int xPos;
  @Getter private final int yPos;    
}