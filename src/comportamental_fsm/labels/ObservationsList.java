package comportamental_fsm.labels;

import java.io.Serializable;
import java.util.ArrayList;

public class ObservationsList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ObservableLabel> obsList;
	
	public ObservationsList() {
		obsList = new ArrayList<ObservableLabel>();
	}
	
	public boolean add(ObservableLabel obs) {
		return obsList.add(obs);
	}
	
	public ObservableLabel get(int index) {
		return obsList.get(index);
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	public int size() {
		return obsList.size();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null || !this.getClass().isAssignableFrom(obj.getClass()))
			return false;
		ObservationsList other = (ObservationsList) obj;
		if(this.size() != other.size())
			return false;
		for(int i=0; i<size(); i++) {
			if(!this.obsList.get(i).equals(other.obsList.get(i)))
				return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		obsList.forEach((obs)->sb.append(obs.content()+", "));
		if(obsList.size()>0)
			sb.delete(sb.length()-2, sb.length());
		sb.append("]");
		return sb.toString();
	}
}
