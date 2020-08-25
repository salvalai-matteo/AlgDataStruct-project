package diagnosticatore.algorithms;

import algorithm_interfaces.Algorithm;
import diagnosticatore.SilentClosure;
import fsm_algorithms.RegexBuilder;
import spazio_comportamentale.BuilderSpaceComportamentale;
import spazio_comportamentale.SpaceState;

public class ClosureDecorator extends Algorithm<SilentClosure>{
	private SilentClosure closure;
	
	public ClosureDecorator(SilentClosure closure) {
		this.closure = closure;
	}

	@Override
	public SilentClosure call() throws Exception {
		log.info(this.getClass().getSimpleName()+"::decorate("+closure.id()+")...");
		
		for(SpaceState s : closure.decorableStates())
			closure.decorate(s, RegexBuilder.relevanceRegex(closure, new BuilderSpaceComportamentale(), s));
		return closure;
	}

	@Override
	public SilentClosure midResult() {
		return closure;
	}
}
