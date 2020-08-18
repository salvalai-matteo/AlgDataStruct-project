package finite_state_automata.spazio_comportamentale;

import java.util.ArrayList;

import finite_state_automata.comportamental.Event;
import finite_state_automata.comportamental.State;

public class SpaceState {
	
	private String id;
	private ArrayList<State> actualStates;
	private ArrayList<Event> linkEvents;
	
	
	public SpaceState(String id, ArrayList<State> actualStates, ArrayList<Event> linkEvents) {
		this.id = id;
		this.actualStates = actualStates;
		this.linkEvents = new ArrayList<Event>();
		for(Event event: linkEvents) {
			this.linkEvents.add(new Event(event.id()));
		}
	}
	
	public String id() {
		return id;
	}
	
	public boolean isFinalState() {
		for(Event event: linkEvents) {
			if(!event.isEmpty())
				return false;
		}
		return true;
	}
	
	public ArrayList<State> getStates() {
		return actualStates;
	}
	
	public ArrayList<Event> getEvents() {
		return linkEvents;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(String.format("Stato %s => %s",id, actualStates.get(0).id()));
		for(int i=1; i<actualStates.size(); i++) {
			sb.append(" ").append(actualStates.get(i).id());
		}
		sb.append(" |");
		for(Event event: linkEvents) {
			sb.append(" ").append(event.id());
		}
		sb.append(isFinalState()? "\t[Stato Finale]": "");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object otherStatus) {
		SpaceState other = (SpaceState) otherStatus;
		for(int i=0; i < actualStates.size(); i++){
			if(!actualStates.get(i).equals(other.actualStates.get(i)))
				return false;
		}
		for(int i=0; i < linkEvents.size(); i++){
			if(!linkEvents.get(i).equals(other.linkEvents.get(i)))
				return false;
		}
		return true;
	}
}