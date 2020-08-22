package spazio_comportamentale.oss_lineare;

import java.util.ArrayList;

import comportamental_fsm.ComportamentalState;
import comportamental_fsm.Event;
import spazio_comportamentale.SpaceState;

public class SpaceStateObs extends SpaceState{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int index;
	private int ossSize;

	public SpaceStateObs(ArrayList<ComportamentalState> actualStates, ArrayList<Event> linkEvents, int index, int ossSize) {
		super(actualStates, linkEvents);
		this.index = index;
		this.ossSize = ossSize;	
	}
	
	public SpaceStateObs(String id) {
		super(id);
	}

	@Override
	public boolean isFinal() {
		return super.isFinal() && index == ossSize;
	}

	@Override
	public String toString() {
		String base = super.toString().replace(" [Stato Finale]", "");
		StringBuilder sb = new StringBuilder(base);
		sb.append(String.format(" | %d", index));
		sb.append(isFinal()? " [Stato Finale]": "");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		SpaceStateObs other = (SpaceStateObs) obj;
		return super.equals(other) && this.index == other.index && this.ossSize == other.ossSize;
	}
}
