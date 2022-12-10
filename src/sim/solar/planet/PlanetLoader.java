package sim.solar.planet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;  
import java.io.IOException;  
import com.opencsv.CSVReader; 

public class PlanetLoader {

    private final static String planetConfigFile = "classes/solarconfig.csv";
    
    
    
    public List<PlanetInterface> Produce(int row) {
        
        List<PlanetInterface> planetList = new ArrayList<>();
        
        List<Map<String, Object>> readPlanetConfig = this.ReadPlanetConfig(planetConfigFile);
        
        Map<String, Object> solarConfigValues = readPlanetConfig.get(row);
        
		int numplanets = Integer.valueOf(solarConfigValues.get("numplanets")+"");
		int orbit = Integer.valueOf(solarConfigValues.get("orbit")+"");
		int increment = Integer.valueOf(solarConfigValues.get("increment")+"");
		int angleinc = Integer.valueOf(solarConfigValues.get("angleinc")+"");
		int planetsize = Integer.valueOf(solarConfigValues.get("planetsize")+"");
		int redbase = Integer.valueOf(solarConfigValues.get("redbase")+"");
		int greenbase = Integer.valueOf(solarConfigValues.get("greenbase")+"");
		int bluebase = Integer.valueOf(solarConfigValues.get("bluebase")+"");
		int redinc = Integer.valueOf(solarConfigValues.get("redinc")+"");
		int greeninc = Integer.valueOf(solarConfigValues.get("greeninc")+"");
		int blueinc = Integer.valueOf(solarConfigValues.get("blueinc")+"");  
        
 
        int angle     = 0;
        int red ;
        int green;
        int blue; 
        
        for (int i=0; i<numplanets; i++) {
            angle    = angle + angleinc;   // controls offset between planets
            red      = redbase   + redinc*i;   // planet color 
            green    = greenbase + greeninc*i;   // planet color
            blue     = bluebase  + blueinc*i;   // planet color
            try{
            planetList.add(new Planet (angle, orbit, increment, planetsize, red, green, blue));
            }catch (IllegalArgumentException e) {
            	e.printStackTrace();
			}
         }
         
        return planetList; 
   }
      
   
   
   
   /**
 * @param fileName
 * @return
 */
public  List<Map<String, Object>> ReadPlanetConfig(String fileName)  {  
      CSVReader reader = null; 
		List<Map<String, Object>> configDataList = new ArrayList<>();
	      try {  

         reader = new CSVReader(new FileReader(fileName));
         String [] nextLine;  
         nextLine = reader.readNext();  // read the header line 
			List<String> columns = new ArrayList<>();
         for ( String token : nextLine)  {  
        	 columns.add(token);
         }

         while ((nextLine = reader.readNext()) != null) {
        	 int columnIndex=0;
        	 Map<String,Object> configDataMap=new ConcurrentHashMap<>();
            for ( String token : nextLine)  {  
            	configDataMap.put(columns.get(columnIndex), token);
            	columnIndex++;
            }
				configDataList.add(configDataMap);
         }
         reader.close(); 
      }  
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return configDataList;  
   } 
}

