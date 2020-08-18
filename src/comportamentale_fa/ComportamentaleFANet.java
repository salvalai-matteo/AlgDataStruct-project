package comportamentale_fa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import spazio_comportamentale.SpaceState;

import java.util.Map.Entry;

public class ComportamentaleFANet {
	
	private ArrayList<ComportamentaleFA> net;
	private ArrayList<Link> links;
	
	public ComportamentaleFANet(ArrayList<Link> links) {
		this.net = new ArrayList<ComportamentaleFA>();
		this.links = links;
		for(Link link: links) {
			link.setEmptyEvent();
			if(!net.contains(link.getSource()))
				net.add(link.getSource());
			if(!net.contains(link.getDestination()))
				net.add(link.getDestination());
		}
		Collections.reverse(net); // temporaneo, serve solo per far uscire C2 prima di C3 (comodo per confrontare la soluzione)
		for(ComportamentaleFA cfa: net) {
			cfa.setCurrent(cfa.initialState()); //imposta l'actual state allo stato iniziale (magari facciamo metodo setActualToInitial? )
		}
	}
	
	
	public ArrayList<ComportamentaleState> getInitialStates(){
		return  net.stream().map(cfa -> cfa.initialState()).collect(Collectors .toCollection(ArrayList::new));
	}
	
	public ArrayList<ComportamentaleState> getActualStates(){
		return net.stream().map(cfa -> cfa.currentState()).collect(Collectors .toCollection(ArrayList::new));
	}
	
	public ArrayList<Event> getActiveEvents() {
		return links.stream().map(link -> link.getEvent()).collect(Collectors .toCollection(ArrayList::new));
	}
	
	public void restoreState(SpaceState stats) {
		for(int i=0; i<stats.getStates().size();i++) {
			net.get(i).setCurrent(stats.getStates().get(i));
		}
		for(int i=0; i<stats.getEvents().size();i++) {
			links.get(i).setEvent(stats.getEvents().get(i));
		}
	}
	
	public void transitionTo(ComportamentaleTransition transition) {
		for (ComportamentaleFA cfa : net) {
	        if (transition.source().equals(cfa.currentState())) {
	        	cfa.transitionTo(transition);
	            break;
	        }
	    }	
		if(!transition.isInputEventEmpty()) {			
			Link link = links.get(links.indexOf(transition.getInputLink()));
			link.setEmptyEvent();			
		}
		if(!transition.isOutputEventsEmpty()) {
			HashMap<Event, Link> outputEvents = transition.getOutputEvents();
			for(Link link: links) {
				if(outputEvents.containsValue(link)) {
					for (Entry<Event, Link> entry : outputEvents.entrySet()) {
				        if (link.equals(entry.getValue())) {
				            link.setEvent(entry.getKey());
				            break;
				        }
				    }
				}			
			}
		}
	}
	
	public ArrayList<ComportamentaleFA> getCFAs() {
		return net;	
	}
	
	public Set<ComportamentaleTransition> enabledTransitions() {
		Set<ComportamentaleTransition> enabledTransitions = new HashSet<ComportamentaleTransition>();
		for(ComportamentaleFA cfa: net) {
			Set<ComportamentaleTransition> transitions = cfa.from(cfa.currentState());
			for(ComportamentaleTransition transition: transitions) {
				boolean enabled = true;
				if(!transition.isInputEventEmpty()) {
					Link link = links.get(links.indexOf(transition.getInputLink()));
					Event transitionEvent = transition.getInputEvent();
					if(!transitionEvent.equals(link.getEvent()))
						enabled = false;
				}
				if(!transition.isOutputEventsEmpty()) {
					for(Link outlink: transition.getOutputEvents().values()) {
						Link link = links.get(links.indexOf(outlink));
						if(link.hasEvent()) {
							enabled = false;
							break;
						}
					}					
				}
				if(enabled)
					enabledTransitions.add(transition);
			}
		}
		return enabledTransitions;
	}

}
