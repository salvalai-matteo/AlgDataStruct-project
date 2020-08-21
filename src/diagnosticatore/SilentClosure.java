package diagnosticatore;

import java.util.HashMap;
import java.util.Set;

import spazio_comportamentale.SpaceAutomaComportamentale;
import spazio_comportamentale.SpaceState;
import utility.Constants;

/**
 * Descrive una chiusura silenziosa. Si comporta come uno spazio comportamentale
 * per tutti gli stati e le transizioni interne alla chiusura. Possiedono, inoltre, per
 * ogni stato finale|d'uscita una decorazione
 * @author Matteo
 *
 */
public class SilentClosure extends SpaceAutomaComportamentale{
	private HashMap<SpaceState, String> decorations;
	
	public SilentClosure(String id) {
		super(id);
		decorations = new HashMap<SpaceState, String>();
	}
	
	public boolean insert(SpaceState s) {
		if(super.insert(s)) {
			if(s.isFinal())
				decorations.put(s, Constants.EPSILON);
			return true;
		}
		return false;
	}
	
	public boolean remove(SpaceState s) {
		if(super.remove(s)) {
			decorations.remove(s);
			return true;
		}
		return false;
	}
	
	public boolean setDecorable(SpaceState s) {
		if(super.hasState(s))
			if(!decorations.containsKey(s)) {
				decorations.put(s, Constants.EPSILON);
				return true;
			}
		return false;
	}
	
	public Set<SpaceState> decorableStates(){
		return decorations.keySet();
	}
	
	public boolean decore(SpaceState s, String decoration) {
		if(decorations.containsKey(s)) {
			decorations.put(s, decoration);
			return true;
		}
		return false;
	}
	
	public String getDecorationOf(SpaceState s) {
		return decorations.get(s);
	}
}
