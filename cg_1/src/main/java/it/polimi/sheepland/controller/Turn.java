package it.polimi.sheepland.controller;

import it.polimi.sheepland.model.Deck;
import it.polimi.sheepland.model.Ovine;
import it.polimi.sheepland.model.Region;
import it.polimi.sheepland.model.Street;
import it.polimi.sheepland.model.TerrainCard;
import it.polimi.sheepland.model.TurnType;
import it.polimi.sheepland.view.*;

import java.util.Observable;
import java.util.Observer;

/**
 * This class represents a single turn (move ovine or move shepherd or...)
 * @author Andrea
 *
 */
public class Turn implements Observer {
	private TurnType turnType;
	private Deck deck;
	private View view;
	
	/**
	 * This is constructor for turn
	 * @param turnType
	 */
	public Turn(TurnType turnType, Deck deck, View view) {
		this.view = view;
		view.addObserver(this);
		this.turnType = turnType;
		this.deck = deck;
		if(turnType.equals(TurnType.MOVE_OVINE) || turnType.equals(TurnType.KILL_OVINE)) {
			view.askOvine(turnType);
		} else if(turnType.equals(TurnType.MOVE_SHEPHERD)) {
			view.askShepherdPosition();
		} else if(turnType.equals(TurnType.BUY_CARD)) {
			view.askCard();
		} else if(turnType.equals(TurnType.COUPLE1) || turnType.equals(TurnType.COUPLE2)) {
			view.askRegion(turnType);
		}
	}
	
	/**
	 * This method waits for updates from view
	 */
	public void update(Observable arg0, Object arg) {
		InputMessage message = (InputMessage) arg;
		if(message.getCode()==InputMessageType.MOVE_OVINE) {
			tryMoveOvine((Ovine)message.getInput());
		} else if(message.getCode()==InputMessageType.KILL_OVINE) {
			tryKillOvine((Ovine)message.getInput());
		} else if(message.getCode()==InputMessageType.MOVE_SHEPHERD) {
			tryMoveShepherd((Street)message.getInput());
		} else if(message.getCode()==InputMessageType.BUY_CARD) {
			tryBuyCard((TerrainCard)message.getInput());
		} else if(message.getCode()==InputMessageType.COUPLE1) {
			tryCouple1((Region)message.getInput());
		} else if(message.getCode()==InputMessageType.COUPLE2) {
			tryCouple2((Region)message.getInput());
		}
		if(message.getCode()!=InputMessageType.PLAYER_DISC) {
			view.deleteObserver(this);
		}
	}
	
	/**
	 * This method tries to perform couple #2
	 * @param region
	 */
	private void tryCouple2(Region region) {
		try {
			deck.couple2(region);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}

	/**
	 * This method tries to perform couple #1
	 * @param region
	 */
	private void tryCouple1(Region region) {
		try {
			if(!deck.couple1(region)) {
				view.showDice();
			}
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}

	/**
	 * This method tries to buy the terrain card
	 * @param terrainnCard
	 */
	private void tryBuyCard(TerrainCard terrainCard) {
		try {
			deck.sellTerrainCard(terrainCard);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}	
	}

	/**
	 * This method tries to move the shepherd to the street
	 * @param street
	 */
	private void tryMoveShepherd(Street street) {
		try {
			deck.setShepherdPosition(street);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}

	/**
	 * This method tries to kill the ovine
	 * @param ovine
	 */
	private void tryKillOvine(Ovine ovine) {
		try {
			deck.killOvine(ovine);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}

	/**
	 * This method tries to move the ovine
	 * @param ovine
	 */
	private void tryMoveOvine(Ovine ovine) {
		try {
			deck.moveOvine(ovine);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}
	}

	/**
	 * This is getter for turn type
	 * @return turn type
	 */
	public TurnType getTurnType() {
		return turnType;
	}
}
