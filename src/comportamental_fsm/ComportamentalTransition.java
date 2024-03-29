package comportamental_fsm;

import java.util.HashMap;

import comportamental_fsm.labels.ObservableLabel;
import comportamental_fsm.labels.RelevantLabel;
import fsm_interfaces.Transition;

public class ComportamentalTransition extends Transition<ComportamentalState>{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Event in;
	private Link inputLink;
	private HashMap<Event, Link> out;
	
	public ComportamentalTransition(String id, ComportamentalState source, ComportamentalState destination, Event in, Link inputLink, HashMap<Event, Link> out, ObservableLabel omega, RelevantLabel f) {
		super(id, source, destination);
		super.setObservableLabel(omega);
		super.setRelevantLabel(f);
		this.in = in;
		this.inputLink = inputLink;
		this.out = out;
	}
	
	public ComportamentalTransition(String id, ComportamentalState source, ComportamentalState destination, Event in, Link inputLink, ObservableLabel omega, RelevantLabel f) {
		super(id, source, destination);
		super.setObservableLabel(omega);
		super.setRelevantLabel(f);
		this.in = in;
		this.inputLink = inputLink;
		this.out = new HashMap<Event, Link>();
	}
	
	public ComportamentalTransition(String id, ComportamentalState source, ComportamentalState destination, HashMap<Event, Link> out, ObservableLabel omega, RelevantLabel f) {
		super(id, source, destination);
		super.setObservableLabel(omega);
		super.setRelevantLabel(f);
		this.in = new Event();
		this.inputLink = null;
		this.out = out;
	}
	
	public ComportamentalTransition(String id, ComportamentalState source, ComportamentalState destination, ObservableLabel omega, RelevantLabel f) {
		super(id, source, destination);
		super.setObservableLabel(omega);
		super.setRelevantLabel(f);
		this.in = new Event();
		this.inputLink = null;
		this.out = new HashMap<Event, Link>();
	}
	
	public Link getInputLink() {
		return inputLink;
	}
	
	public Event getInputEvent() {
		return in;
	}
	
	public boolean isInputEventEmpty() {
		return in.isEmpty();
	}
	
	public boolean isOutputEventsEmpty() {
		return out.isEmpty();
	}
	
	public HashMap<Event, Link> getOutputEvents() {
		return out;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(id().concat(":["));
		sb.append(source().id()+"->"+sink().id());
		sb.append(", "+observableLabel().toString()+", "+relevantLabel().toString()+"], ");
		if(!isInputEventEmpty())
			sb.append(String.format("%s(%s)", in.id(), inputLink.id()));
		if(!isOutputEventsEmpty()) {
			Event[] keySet = new Event[out.size()];
			keySet = out.keySet().toArray(keySet);
			sb.append(String.format("/{%s(%s)", keySet[0].id(), out.get(keySet[0]).id()));
			for(int i=1; i<keySet.length; i++) {
				sb.append(String.format(",%s(%s)", keySet[i], out.get(keySet[i]).id()));
			}
			sb.append("}");			
		}
		return sb.toString();
	}

}
