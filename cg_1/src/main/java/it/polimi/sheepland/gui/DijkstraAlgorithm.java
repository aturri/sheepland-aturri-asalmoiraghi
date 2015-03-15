package it.polimi.sheepland.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class represents an utility for Dijkstra Algorithm to calculate path between streets.
 * 
 * @author Andrea
 *
 */
public class DijkstraAlgorithm {
	private List<JStreet> unSettledNodes;
	private Map<JStreet, JStreet> predecessors;
	private Map<JStreet, Integer> distance;
	private JStreet source;

	/**
	 * This method sets up the maps and executes the search for every destination node.
	 * 
	 * @param JStreet source
	 */
	public void execute(JStreet source) {
		this.source = source;
		List<JStreet> settledNodes;
		settledNodes = new LinkedList<JStreet>();
		unSettledNodes = new LinkedList<JStreet>();
		distance = new HashMap<JStreet, Integer>();
		predecessors = new HashMap<JStreet, JStreet>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (!unSettledNodes.isEmpty()) {
			JStreet node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	/**
	 * This method updates distance, predecessors and unSettledNodes.
	 * 
	 * @param node
	 */
	private void findMinimalDistances(JStreet node) {
		List<JStreet> adjacentNodes = node.getListAdjStreets();
		for (JStreet target: adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)) {
				distance.put(target, getShortestDistance(node));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}
	}

	/**
	 * This method returns the street at minimum distance from source, in the parameter set.
	 * 
	 * @param set of vertexes
	 * @return vertex ad minimum distance (JStreet)
	 */
	private JStreet getMinimum(List<JStreet> vertexes) {
		JStreet minimum = null;
		for (JStreet vertex: vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex)<getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	/**
	 * This method returns the shortest distance between source and parameter destination.
	 * 
	 * @param destination
	 * @return distance
	 */
	private int getShortestDistance(JStreet destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/**
	 * This method returns the list of streets from the source to the parameter destination.
	 * 
	 * @param destination
	 * @return list of streets
	 */
	public List<JStreet> getPath(JStreet destination) {
		List<JStreet> path = new ArrayList<JStreet>();
		JStreet step = destination;
		//check if a path exists
		if (predecessors.get(step)==null) {
			path.add(destination);
			path.add(source);
			return path;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
	    //put it into the correct order
		Collections.reverse(path);
		return path;
	}
}