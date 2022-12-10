package sim.solar; 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.util.List;
import java.util.Map;

import sim.solar.planet.PlanetLoader ;
import sim.solar.planet.PlanetInterface;

class Simulation extends JPanel implements Runnable {
  
   
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 8297504161673135890L;
	private int screenSize;       // screen size both x and y
	private int frameDelay;     // millisec delay to slow down animation speed 
	private int maxSolarCount;       // cannot exceed data rows solarconfig.csv 
	private int cyclesPerSolarSystem;   // degrees of rotation allotted to each solar system 
	private int pauseDelay;             // millisec delay between solar systems 
   
   
   
   
   //These variables remain initialized here in the code, dont move them to config file :
   private static final Color colorBlack = new Color(0,0,0);
   private static final Color colorGreen = new Color(30,120,30); 
   private int cycleCount = 1;       // must start at one
   private int solarCounter = 0;   // must start at zero
   private int screenMid ; // mid-screen location 
   
   private PlanetLoader planetLoader = new  PlanetLoader();  
   private SolarSystem solarSystem  ;

   public Simulation() {  
        this.initializeGlobalVariables();
       List<PlanetInterface> planetList = planetLoader.Produce(solarCounter); 
       solarSystem = new SolarSystem(planetList); 
       setBackground(colorBlack);
       setPreferredSize( new Dimension(screenSize, screenSize) );
       final Thread t = new Thread (this);
       t.start ();
  }
  
 

private void NextSolarSystemRun() {
       try {
         // this will slow down display animation
         Thread.sleep(frameDelay);   
         
         // switch to next solar system
         if ((cycleCount % cyclesPerSolarSystem) == 0) {
             solarCounter++; 
             if (solarCounter > maxSolarCount) {
                solarCounter=0; // rollover to first planetary config
             }
             // get next set of planets to view 
             List<PlanetInterface> planetList = planetLoader.Produce(solarCounter);
             solarSystem = new SolarSystem(planetList); 
             Thread.sleep(pauseDelay);  // pause between change to next solar system
         }
      } 
    catch (InterruptedException e) {
     e.printStackTrace();
    }

  }
    
  public void run() {
    while(true)  {
      solarSystem.run(); // calculations for next animation 
      repaint();
      cycleCount++;
      NextSolarSystemRun(); 
    }
  }
  
  public void paintComponent(final Graphics g)  {
    // clear out previous frame of drawings
    g.setColor( colorBlack ); 
    g.fillRect(0, 0, screenSize, screenSize);
    
    // paint planets in the solar system 
    solarSystem.paint(g, screenMid); 

    // repaint x-y axis lines using dark green
    g.setColor( colorGreen );
    g.drawLine( screenMid, 0, screenMid, screenSize); 
    g.drawLine( 0, screenMid, screenSize, screenMid); 
   }
  
  
	/**
	 * 
	 */
	private void initializeGlobalVariables() {
		List<Map<String, Object>> readPlanetConfig = planetLoader.ReadPlanetConfig("classes/variableconfig.csv");
		if (readPlanetConfig != null && readPlanetConfig.size() > 0) {
			Map<String, Object> configMap = readPlanetConfig.get(0);
			this.screenSize = Integer.valueOf(configMap.get("screenSize") + "");
			this.frameDelay = Integer.valueOf(configMap.get("frameDelay") + "");
			this.maxSolarCount = Integer.valueOf(configMap.get("maxSolarCount") + "");
			this.cyclesPerSolarSystem = this.maxSolarCount
					* Integer.valueOf(configMap.get("cyclesPerSolarSystem") + "");
			this.pauseDelay = Integer.valueOf(configMap.get("pauseDelay") + "");

			this.screenMid = this.screenSize / 2;
		} else {
			this.screenSize = 600;
			this.frameDelay = 10;
			this.maxSolarCount = 3;
			this.cyclesPerSolarSystem = this.maxSolarCount * 360;
			this.pauseDelay = 400;

			this.screenMid = this.screenSize / 2;
		}

	}
}

