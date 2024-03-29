package comportamental_fsm;

import java.io.Serializable;

import utility.Constants;

public class Event implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	
	public Event(Event e) {
		this.id = e.id;
	}
	
	public Event(String id) {
		this.id = id;
	}
	
	public Event() {
		this.id = Constants.EPSILON;
	}
	
	public String id() {return id;}
	
	public boolean isEmpty() {
		return id.equals(Constants.EPSILON);
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	@Override
	public boolean equals(Object otherEvent) {
		if(otherEvent==null || !this.getClass().isAssignableFrom(otherEvent.getClass()))
			return false;
		final Event ev = (Event) otherEvent;
		return id.equals(ev.id);
	}
	
	@Override
	public Object clone() {
		Event deepCopy = new Event(this);
		return deepCopy;
	}
		
}