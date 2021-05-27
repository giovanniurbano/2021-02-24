package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event>{
	public enum EventType {
		GOL,
		ESPULSIONE,
		INFORTUNIO
	}
	
	public int t;
	public EventType type;
	
	public Event(int t, EventType type) {
		this.t = t;
		this.type = type;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Evento [t=" + t + ", type=" + type + "]";
	}

	@Override
	public int compareTo(Event o) {
		return this.t - o.t;
	}
	
	
}
