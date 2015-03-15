
package it.polimi.sheepland.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**This class is the creator of the map (regions and streets)
 * @author Andrea
 */
public class IslandCreator implements Serializable {
	private static final long serialVersionUID = 5082444109098951598L;
	
	private Region sheepsburg;
	private Map<Integer, Region> regions = new HashMap<Integer, Region>();
	private Map<Integer, Street> streets = new HashMap<Integer, Street>();

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private static final String ADJ_STREET = "adjacentStreet";
	private static final String STREET = "street";
	private static final String ID = "id";
	private static final String NUMBER = "number";
	private static final String SHEEPSBURG = "sheepsburg";
	private static final String REGION = "region";

	/**
	 * This method is the constructor
	 * @param relativeUrl
	 * @throws IOException
	 */
	public IslandCreator(String relativeUrl) {
		LOGGER.setLevel(Level.INFO);
		/*carico file xml
		 *leggo strada -> creo strada(id,number)
		 *ciclo:
		 *	leggo strada adiacente
		 *	cerco se l'ho già creata
		 *	NO: continuo con prossima strada adiacente
		 *	SI: stradaAttuale.addStrada(stradaAdiacente)
		 *		stradaAdiacente.addStrada(stradaAttuale)
		 *proseguo con le regioni
		 *leggo regione -> creo regione(id, tipo)
		 *ciclo:
		 *	leggo strada adiacente
		 *	addRegione(stradaAdiacente)
		 *	addStradaAdiacente(regione)
		 */
		
		try {
			//read the xml
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = documentFactory.newDocumentBuilder();
			Document document = builder.parse(new File(relativeUrl));
			
			//get the streets
			NodeList streetsXML = document.getElementsByTagName(STREET);
			
			for(int i=0; i<streetsXML.getLength(); i++) { 
				Node nodeStreet = streetsXML.item(i);
				if(nodeStreet.getNodeType() == Node.ELEMENT_NODE) { 
					Element street = (Element) nodeStreet;
					
					String id = street.getElementsByTagName(ID).item(0).getFirstChild().getNodeValue();
					String number = street.getElementsByTagName(NUMBER).item(0).getFirstChild().getNodeValue();
					
					//create the street
					Street myStreet = createStreet(Integer.parseInt(id),Integer.parseInt(number));
					streets.put(Integer.parseInt(id), myStreet);
					
					//get the adjacent streets of this street
					NodeList adjacentStreetsXML = street.getElementsByTagName(ADJ_STREET);
					for(int k=0; k<adjacentStreetsXML.getLength(); k++) { 
						Node nodeAdjacentStreet = adjacentStreetsXML.item(k);
						if(nodeAdjacentStreet.getNodeType() == Node.ELEMENT_NODE) { 
							Element adjacentStreet = (Element) nodeAdjacentStreet;
							
							String adjacentId = adjacentStreet.getFirstChild().getNodeValue();

							//check if the adjacent street exist
							if(streets.containsKey(Integer.parseInt(adjacentId))){
								Street myAdjacentStreet = streets.get(Integer.parseInt(adjacentId));
								myStreet.addNextStreet(myAdjacentStreet);
								myAdjacentStreet.addNextStreet(myStreet);
							}
						}
					}
				}
			}
			
			//get sheepsburg
			NodeList sheepsburgXML = document.getElementsByTagName(SHEEPSBURG);
			Node nodeSheepsburg = sheepsburgXML.item(0);
			if(nodeSheepsburg.getNodeType() == Node.ELEMENT_NODE) { 
				Element sheepsburgNode = (Element) nodeSheepsburg;
				
				String id = sheepsburgNode.getElementsByTagName(ID).item(0).getFirstChild().getNodeValue();
				
				//create the region
				this.sheepsburg = createSheepsburg(Integer.parseInt(id));
				
				//get the adjacent streets of this region
				NodeList adjacentStreetsOfRegionXML = sheepsburgNode.getElementsByTagName(ADJ_STREET);
				for(int k=0; k<adjacentStreetsOfRegionXML.getLength(); k++) { 
					Node nodeAdjacentStreet = adjacentStreetsOfRegionXML.item(k);
					if(nodeAdjacentStreet.getNodeType() == Node.ELEMENT_NODE) { 
						Element adjacentStreet = (Element) nodeAdjacentStreet;
						
						String adjacentId = adjacentStreet.getFirstChild().getNodeValue();

						Street myAdjacentStreet = streets.get(Integer.parseInt(adjacentId));
						this.sheepsburg.addStreet(myAdjacentStreet);
						myAdjacentStreet.setRegion(this.sheepsburg);
					}
				}
			}
				
			
			//get the regions
			NodeList regionsXML = document.getElementsByTagName(REGION);
			
			for(int j=0; j<regionsXML.getLength(); j++) { 
				Node nodeRegion = regionsXML.item(j);
				if(nodeRegion.getNodeType() == Node.ELEMENT_NODE) { 
					Element region = (Element) nodeRegion;
					
					String id = region.getElementsByTagName(ID).item(0).getFirstChild().getNodeValue();
					String type = region.getElementsByTagName("type").item(0).getFirstChild().getNodeValue();
					
					//create the region
					Region myRegion = createNormalRegion(TerrainType.valueOf(type), Integer.parseInt(id));
					regions.put(Integer.parseInt(id), myRegion);
					
					//get the adjacent streets of this region
					NodeList adjacentStreetsOfRegionXML = region.getElementsByTagName(ADJ_STREET);
					for(int k=0; k<adjacentStreetsOfRegionXML.getLength(); k++) { 
						Node nodeAdjacentStreet = adjacentStreetsOfRegionXML.item(k);
						if(nodeAdjacentStreet.getNodeType() == Node.ELEMENT_NODE) { 
							Element adjacentStreet = (Element) nodeAdjacentStreet;
							
							String adjacentId = adjacentStreet.getFirstChild().getNodeValue();

							Street myAdjacentStreet = streets.get(Integer.parseInt(adjacentId));
							myRegion.addStreet(myAdjacentStreet);
							myAdjacentStreet.setRegion(myRegion);
						}
					}
				}
			}
		} catch(Exception e) { 
			LOGGER.log(Level.WARNING, "Unexpected error creating the island "+e.getMessage(),e);
			throw new IllegalArgumentException(e);
		}
	}

	/**This method creates a new street
	 * @param id the id of the street
	 * @param num the number of the street
	 * @return Street street
	 */
	private Street createStreet(int id, int num) {
		return new Street(id, num);
	}

	/**This method create a new nromal region
	 * @param type the type of the region
	 * @param id the id of the region
	 * @return Region region
	 */
	private Region createNormalRegion(TerrainType type, int id) {
		return new NormalRegion(type, id);
	}
	
	/**This method create a new sheepsburg
	 * @param type the type of the region
	 * @param id the id of the region
	 * @return Region region
	 */
	private Region createSheepsburg(int id) {
		return new Sheepsburg(id);
	}

	/**This method returns the HashMap<key,region> of the regions
	 * @return Map<Integer, Region>
	 */
	public Map<Integer, Region> getHashMapRegions() {
		return regions;
	}

	/**This method returns the HashMap<key,street> of the streets
	 * @return Map<Integer, Street>
	 */
	public Map<Integer, Street> getHashMapStreets() {
		return streets;
	}

	/**This Method returns the region Sheepsburg
	 * @return Region sheepsburg
	 */
	public Region getSheepsburg() {
		return sheepsburg;
	}
}
