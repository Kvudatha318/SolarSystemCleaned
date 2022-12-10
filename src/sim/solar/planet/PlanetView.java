package sim.solar.planet;

import java.awt.Graphics;
import java.awt.Color;

public class PlanetView  {
  private final static Color lineColor = new Color(30, 120, 30);
  
  public void paint(Graphics g, int x, int y, int planetSize, Color color, int center) {
    g.setColor(color);
    g.fillOval( x+center, y+center, planetSize,  planetSize);
    g.setColor( lineColor  );
    g.drawLine( center, center, x+center, y+center); 
  }
}

