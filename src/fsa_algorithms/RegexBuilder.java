package fsa_algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import commoninterfaces.AutomaInterface;
import commoninterfaces.Builder;
import commoninterfaces.State;
import commoninterfaces.Transition;
import comportamentale_fa.labels.Regex;

public class RegexBuilder<S extends State, T extends Transition<S>> {
	public String compute(AutomaInterface<S, T> N, Builder<S, T> builder) {
		Logger log = loggerSetup();
		log.info(RegexBuilder.class.getSimpleName()+"::"+RegexBuilder.class.getMethods()[0].getName()+"...");
		log.fine("initial: "+N.toString());
		
		//Initialization of N
		S n0 = null;
		if(N.to(N.initialState()).size() > 0) {
			n0 = builder.newState("n0");
			N.insert(n0);
			T t = builder.newTransition("n0-"+N.initialState().id(), n0, N.initialState());
			t.setRegex(new Regex());
			N.add(t);
			N.setInitial(n0);
		} else {
			n0 = N.initialState();
		}
		S nq = builder.newState("nq");
		N.insert(nq);
		N.acceptingStates().forEach((s)->{
			T t = builder.newTransition(s.id()+"-"+nq.id(), s, nq);
			t.setRegex(new Regex());
			N.add(t);
			});
		
		log.info("Post initialization: "+N.toString());
		
		//Initialization of markings
		HashMap<String, LinkedList<T>> markings = new HashMap<String, LinkedList<T>>();
		
		//To differentiate the id of the new transitions
		int counter = 0;
		
		//Buffer to compose the regex of the new transition
		StringBuilder regexBuilder = new StringBuilder();
		
		//main procedure
		while(N.states().size() > 2 || markings.values().stream().anyMatch(l->l.size()>1)) {
			regexBuilder.setLength(0);
			
			//Find a path of a single transition to and from a state of the sequence
			LinkedList<T> transitions = new TransitionFinder<S,T>().oneWayPath(N);
			StringBuilder mark = new StringBuilder();
			if(transitions.size() > 0) {
				log.info("Found one way path: "+transitions);
				T last = transitions.pollLast();
				N.remove(last);
				
				S source = transitions.getFirst().source();
				S sink = last.sink();
				regexBuilder.append("(");
				transitions.forEach(t->{
					N.remove(t);
					regexBuilder.append(t.regex());
				});
				T tmp = builder.newTransition(source.id()+"-"+sink.id()+"_"+counter,
						source,
						sink);
				if(!last.sink().equals(nq) && !last.source().isAccepting()) {
					regexBuilder.append(last.regex()+")");
				}else {
					regexBuilder.append(")");
					if(!markings.containsKey(last.source().id()))
						markings.put(last.source().id(), new LinkedList<T>());
					markings.get(last.source().id()).add(tmp);
				}
				tmp.setRegex(new Regex(regexBuilder.toString()));
				N.add(tmp);
				log.info("New transition: "+tmp.toString());
			//Find transitions that are parallels
			}else if(findParallelTransitions(N, transitions)){
				log.info("Found parallels: "+transitions);
				T head = transitions.pop();
				N.remove(head);
				regexBuilder.append("("+head.regex());
				transitions.forEach(t->{
					N.remove(t);
					regexBuilder.append("|"+t.regex());
				});
				regexBuilder.append(")");
				String id = head.source().id()+"-"+head.sink().id()+"_"+counter;
				T union = builder.newTransition(id,
						head.source(),
						head.sink());
				union.setRegex(new Regex(regexBuilder.toString()));
				N.add(union);
				log.info("New transition: "+union.toString());
			//Find transitions that have the same mark and are parallels
			}else if(findSameMarkParallelTransitions(markings, transitions, mark)) {
				log.info("Found same mark, parallels: "+transitions);
				T head = transitions.pop();
				N.remove(head);
				markings.get(mark.toString()).remove(head);
				
				regexBuilder.append("("+head.regex());
				transitions.forEach(t->{
					N.remove(t);
					markings.get(mark.toString()).remove(t);
					regexBuilder.append("|"+t.regex());
				});
				regexBuilder.append(")");
				T union = builder.newTransition(head.source().id()+"-"+head.sink().id()+"_"+counter,
						head.source(),
						head.sink());
				union.setRegex(new Regex(regexBuilder.toString()));
				N.add(union);
				markings.get(mark.toString()).add(union);
				log.info("New transition: "+union.toString());
			}else {
				log.info("Default procedure");
				LinkedList<S> states = new LinkedList<S>(N.states());
				S n_tmp = states.pop();
				//n!=n0 && n!=nq
				while(n_tmp.equals(n0) || n_tmp.equals(nq)) 
					n_tmp = states.pop();
				S n = n_tmp;
				log.info("Selected state: "+n);
				N.to(n)
					.stream()
					.filter(r1->!r1.isAuto())
					.forEach(r1->{
						N.from(n)
							.stream()
							.filter(r2->!r2.isAuto())
							.forEach(r2->{
								String id = "t"+r1.id()+"- t"+r2.id();
								T tmp = builder.newTransition(id,
										r1.source(),
										r2.sink());
								if(r2.sink().equals(nq) && n.isAccepting()) {
									if(N.hasAuto(n)) {
										regexBuilder.append("(");
										regexBuilder.append(r1.regex());
										regexBuilder.append("("+N.getAuto(n).regex()+")*");
										regexBuilder.append(")");
									}else {
										regexBuilder.append(r1.regex());
									}
									if(!markings.containsKey(n.id()))
										markings.put(n.id(), new LinkedList<T>());
									markings.get(n.id()).add(tmp);
								}else if(N.hasAuto(n)) {
									regexBuilder.append("(");
									regexBuilder.append(r1.regex());
									regexBuilder.append("("+N.getAuto(n).regex()+")*");
									regexBuilder.append(r2.regex());
									regexBuilder.append(")");
								}else {
									regexBuilder.append("(");
									regexBuilder.append(r1.regex());
									regexBuilder.append(r2.regex());
									regexBuilder.append(")");
								}
								tmp.setRegex(new Regex(regexBuilder.toString()));
								N.add(tmp);
								log.info("New transition: "+tmp.toString());
							});
					});
				N.remove(n);
			}
			counter++;
			log.info("After procedure: "+N.toString());
		}
		
		//output building
		StringBuilder finalRegex = new StringBuilder("");
		N.transitions().forEach(t->finalRegex.append(t.regex()+"|"));
		finalRegex.deleteCharAt(finalRegex.length()-1);
		return finalRegex.toString();
	}
	
	private static Logger loggerSetup() {
		Logger log = Logger.getLogger(RegexBuilder.class.getSimpleName());
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
		log.setLevel(Level.INFO);
		//log.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		return log;
	}
	
	private boolean findSameMarkParallelTransitions(HashMap<String, LinkedList<T>> marks,
			LinkedList<T> transitions, StringBuilder key) {
		transitions.clear();
		key.setLength(0);
		Iterator<String> iter = marks.keySet().iterator();
		while(iter.hasNext()) {
			key.setLength(0);
			
			if(transitions.size() > 1)
				break;
			key.append(iter.next());
			LinkedList<T> marked = new LinkedList<T>(marks.get(key.toString()));
			while(marked.size() > 1) {
				T t = marked.pop();
				marked.forEach(t1->{if(t1.isParallelTo(t)) transitions.add(t1);});
				transitions.add(t);
				
				if(transitions.size() > 1)
					break;
				else
					transitions.clear();
			}
		}
		return transitions.size() > 0;
	}
	
	private boolean findParallelTransitions(AutomaInterface<S, T> N, LinkedList<T> transitions) {
		transitions.clear();
		transitions.addAll(new TransitionFinder<S, T>().parallelTransitions(N));
		return transitions.size() > 0;
	}
}
