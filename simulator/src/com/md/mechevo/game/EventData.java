package com.md.mechevo.game;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;

/**
 * Event Data holds the information that will be given to the EventObserver to log the simulation.
 * 
 * @see com.md.mechevo.game.EventObserver
 */
public class EventData {

	/**
	 * The title codename to best describe this event
	 */
	private String title;

	/**
	 * Holds all the additional attributes that better describe this event.
	 */
	private ArrayList<SimpleImmutableEntry<String, String>> attributes;


	/**
	 * Constructor for the EventData.
	 */
	public EventData(String title) {
		this.title = title;
		this.attributes = new ArrayList<>();
	}


	/**
	 * Add an attribute to the EventData
	 * 
	 * @param key The Key part of the attribute
	 * @param value The Value part of the attribute
	 */
	public EventData addAttribute(String key, String value) {
		this.attributes.add(new SimpleImmutableEntry<>(key, value));
		return this; // enable chain calling
	}

	/**
	 * @see com.md.mechevo.game.EventData#addAttribute
	 */
	public EventData addAttribute(String key, int value) {
		this.attributes.add(new SimpleImmutableEntry<>(key, Integer.toString(value)));
		return this; // enable chain calling
	}

	/**
	 * @see com.md.mechevo.game.EventData#addAttribute
	 */
	public EventData addAttribute(String key, double value) {
		this.attributes.add(new SimpleImmutableEntry<>(key, Double.toString(value)));
		return this; // enable chain calling
	}

	/**
	 * Title getter.
	 */
	public String getTitle() {
		return this.title;
	}

	public String getAttribute(String key) {
		for (SimpleImmutableEntry<String, String> entry : this.attributes) {
			if (entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * Attribute getter
	 */
	public ArrayList<SimpleImmutableEntry<String, String>> getAttributes() {
		return this.attributes;
	}

}
