package spazio_comportamentale;

import java.util.Set;
import java.util.concurrent.Callable;

import comportamental_fsm.CFSMnetwork;
import comportamental_fsm.ComportamentalTransition;

public class SpaceCompCallable implements Callable<SpaceAutomaComportamentale>{
	
	private CFSMnetwork net;
	private SpaceAutomaComportamentale spazioComp;
	
	public SpaceCompCallable(CFSMnetwork net) {
		this.net = net;
		spazioComp = new SpaceAutomaComportamentale("Spazio Comportamentale");		
	}
	
	@Override
	public SpaceAutomaComportamentale call() throws Exception {
		if(spazioComp.states().isEmpty()) {
			SpaceState initial = new SpaceState(net.getInitialStates(), net.getActiveEvents());
			spazioComp.insert(initial);
			spazioComp.setInitial(initial);
			buildSpace(initial, net.enabledTransitions()); 
			if(Thread.currentThread().isInterrupted())
			     return spazioComp;
			net.restoreInitial();
		}
		return spazioComp;
	}
	
	private void buildSpace(SpaceState state, Set<ComportamentalTransition> enabledTransitions) {
		if(Thread.currentThread().isInterrupted())
		      return;
		if(enabledTransitions.size()>1) {
			for(ComportamentalTransition transition: enabledTransitions) {
				 if (Thread.currentThread().isInterrupted()) {
				        return;
				   }
				net.restoreState(state);
				scattoTransizione(state, transition); 
			}		
		} else if (enabledTransitions.size()==1)
			scattoTransizione(state, enabledTransitions.iterator().next());
		else 
			spazioComp.insert(state);
	}
	
	private void scattoTransizione(SpaceState source, ComportamentalTransition transition) {
		if(Thread.currentThread().isInterrupted())
		    return;
		net.transitionTo(transition);	
		SpaceState destination = new SpaceState(net.getCurrentStates(), net.getActiveEvents());
		if(!spazioComp.insert(destination)) {
			SpaceState toSearch = destination;
			destination = spazioComp.states().stream().filter(s -> s.equals(toSearch)).iterator().next();
		}
		SpaceTransition<SpaceState> spaceTransition = new SpaceTransition<SpaceState>(source, destination, transition);
		if(spazioComp.add(spaceTransition))
			buildSpace(destination, net.enabledTransitions());	
	}

	
}