package test.comportamental_nets;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.Test;

import comportamental_fsm.ComportamentalFSM;
import comportamental_fsm.CFSMnetwork;
import comportamental_fsm.ComportamentalState;
import comportamental_fsm.ComportamentalTransition;
import comportamental_fsm.Event;
import comportamental_fsm.Link;
import comportamental_fsm.labels.ObservableLabel;
import comportamental_fsm.labels.ObservationsList;
import comportamental_fsm.labels.RelevantLabel;
import diagnosticatore.ClosureSpace;
import diagnosticatore.algorithms.DiagnosticatoreBuilder;
import diagnosticatore.algorithms.LinearDiagnosis;
import fsm_algorithms.MultipleRelRegexBuilder;
import spazio_comportamentale.SpaceAutomaComportamentale;
import spazio_comportamentale.SpaceState;
import spazio_comportamentale.SpaceTransition;
import spazio_comportamentale.SpazioComportamentale;
import spazio_comportamentale.oss_lineare.BuilderSpaceComportamentaleObsLin;
import spazio_comportamentale.oss_lineare.SpaceAutomaObsLin;
import spazio_comportamentale.oss_lineare.SpaceStateObs;
import spazio_comportamentale.oss_lineare.SpazioComportamentaleObs;
import utility.Constants;
import utility.RegexSimplifier;

class TestOtherNet {
	
	private ComportamentalState s0;
	private ComportamentalState s1;
	private ComportamentalState b0;
	private ComportamentalState b1;
	
	
	private ComportamentalTransition ts1;
	private ComportamentalTransition ts2;
	private ComportamentalTransition ts3;
	private ComportamentalTransition ts4;
	private ComportamentalTransition tb1;
	private ComportamentalTransition tb2;
	private ComportamentalTransition tb3;
	private ComportamentalTransition tb4;
	private ComportamentalTransition tb5;
	private ComportamentalTransition tb6;
	private ComportamentalTransition tb7;
	private ComportamentalTransition tb8;
	
	private Event op;
	private Event cl;
	private Event emptyEv;
	private ComportamentalFSM s;
	private ComportamentalFSM b;
	private Link l;
	
	
	CFSMnetwork initialize() {
		//AUTOMA s
		s = new ComportamentalFSM("s");
		s0 = new ComportamentalState("_0");
		s1 = new ComportamentalState("_1");
		s.insert(s0);
		s.insert(s1);
		s.setInitial(s0);
		
		//AUTOMA b
		b = new ComportamentalFSM("b");
		b0 = new ComportamentalState("0_");
		b1 = new ComportamentalState("1_");
		b.insert(b0);
		b.insert(b1);
		b.setInitial(b0);
		
		op = new Event("op");
		cl = new Event("cl");
		emptyEv = new Event();
		l = new Link("L", s, b);
		HashMap<Event, Link> outOP = new HashMap<Event, Link>();
		outOP.put(op, l);
		HashMap<Event, Link> outCL = new HashMap<Event, Link>();
		outCL.put(cl, l);
		
		ts1 = new ComportamentalTransition("s1", s0, s1, outOP, new ObservableLabel("act"), new RelevantLabel());
		ts2 = new ComportamentalTransition("s2", s1, s0, outCL, new ObservableLabel("sby"), new RelevantLabel());
		ts3 = new ComportamentalTransition("s3", s0, s0, outCL, new ObservableLabel(), new RelevantLabel("f1"));
		ts4 = new ComportamentalTransition("s4", s1, s1, outOP, new ObservableLabel(), new RelevantLabel("f2"));
				
		tb1 = new ComportamentalTransition("b1", b0, b1, op, l, new ObservableLabel("opn"), new RelevantLabel());
		tb2 = new ComportamentalTransition("b2", b1, b0, cl, l, new ObservableLabel("cls"), new RelevantLabel());
		tb3 = new ComportamentalTransition("b3", b0, b0, op, l, new ObservableLabel(), new RelevantLabel("f3"));
		tb4 = new ComportamentalTransition("b4", b1, b1, cl, l, new ObservableLabel(), new RelevantLabel("f4"));
		tb5 = new ComportamentalTransition("b5", b0, b0, cl, l, new ObservableLabel("nop"), new RelevantLabel());
		tb6 = new ComportamentalTransition("b6", b1, b1, op, l, new ObservableLabel("nop"), new RelevantLabel());
		tb7 = new ComportamentalTransition("b7", b0, b1, cl, l, new ObservableLabel("opn"), new RelevantLabel("f5"));
		tb8 = new ComportamentalTransition("b8", b1, b0, op, l, new ObservableLabel("cls"), new RelevantLabel("f6"));
		
		
		s.add(ts1);
		s.add(ts2);
		s.add(ts3);
		s.add(ts4);
		
		b.add(tb1);
		b.add(tb2);
		b.add(tb3);
		b.add(tb4);
		b.add(tb5);
		b.add(tb6);
		b.add(tb7);
		b.add(tb8);
		
		
		ArrayList<Link> listLink = new ArrayList<Link>();
		listLink.add(l);
		
		return new CFSMnetwork(listLink);
	}
	
	private HashMap<String, ComportamentalState> statesToHashMap(ComportamentalState states, ComportamentalState stateb) {
		HashMap<String , ComportamentalState> map = new HashMap<String , ComportamentalState>();
		map.put(s.id(), states);
		map.put(b.id(), stateb);
		return map;
	}
	
	private HashMap<Link, Event> eventToHashMap(Event event) {
		HashMap<Link , Event> map = new HashMap<Link , Event>();
		map.put(l, event);
		return map;
	}
	
	@Test
	void spazioComportamentale() throws Exception{		
		CFSMnetwork net = initialize();
		SpazioComportamentale sc = new SpazioComportamentale(net);
		SpaceAutomaComportamentale toMatch = new SpaceAutomaComportamentale("Test");
		SpaceAutomaComportamentale computedSpace = null;
		try {
			computedSpace = sc.call();
		} catch (Exception e) {
			assertFalse(false);
		}
		SpaceState sc0 = new SpaceState(statesToHashMap(s0, b0), eventToHashMap(emptyEv));
		SpaceState sc1 = new SpaceState(statesToHashMap(s1, b0), eventToHashMap(op));
		SpaceState sc2 = new SpaceState(statesToHashMap(s0, b0), eventToHashMap(cl));
		SpaceState sc3 = new SpaceState(statesToHashMap(s1, b1), eventToHashMap(emptyEv));
		SpaceState sc4 = new SpaceState(statesToHashMap(s1, b0), eventToHashMap(emptyEv));
		SpaceState sc5 = new SpaceState(statesToHashMap(s0, b1), eventToHashMap(cl));
		SpaceState sc6 = new SpaceState(statesToHashMap(s1, b1), eventToHashMap(op));
		SpaceState sc7 = new SpaceState(statesToHashMap(s0, b1), eventToHashMap(emptyEv));
		
		toMatch.insert(sc0);
		toMatch.insert(sc1);
		toMatch.insert(sc2);
		toMatch.insert(sc3);
		toMatch.insert(sc4);
		toMatch.insert(sc5);
		toMatch.insert(sc6);
		toMatch.insert(sc7);
		
		SpaceTransition<SpaceState> t01 = new SpaceTransition<SpaceState>(sc0, sc1, ts1);
		SpaceTransition<SpaceState> t02 = new SpaceTransition<SpaceState>(sc0, sc2, ts3);
		SpaceTransition<SpaceState> t14 = new SpaceTransition<SpaceState>(sc1, sc4, tb3);
		SpaceTransition<SpaceState> t20 = new SpaceTransition<SpaceState>(sc2, sc0, tb5);
		SpaceTransition<SpaceState> t42 = new SpaceTransition<SpaceState>(sc4, sc2, ts2);
		SpaceTransition<SpaceState> t41 = new SpaceTransition<SpaceState>(sc4, sc1, ts4);
		SpaceTransition<SpaceState> t13 = new SpaceTransition<SpaceState>(sc1, sc3, tb1);
		SpaceTransition<SpaceState> t36 = new SpaceTransition<SpaceState>(sc3, sc6, ts4);
		SpaceTransition<SpaceState> t63 = new SpaceTransition<SpaceState>(sc6, sc3, tb6);
		SpaceTransition<SpaceState> t35 = new SpaceTransition<SpaceState>(sc3, sc5, ts2);
		SpaceTransition<SpaceState> t50 = new SpaceTransition<SpaceState>(sc5, sc0, tb2);
		SpaceTransition<SpaceState> t57 = new SpaceTransition<SpaceState>(sc5, sc7, tb4);
		SpaceTransition<SpaceState> t75 = new SpaceTransition<SpaceState>(sc7, sc5, ts3);
		SpaceTransition<SpaceState> t76 = new SpaceTransition<SpaceState>(sc7, sc6, ts1);
		SpaceTransition<SpaceState> t64 = new SpaceTransition<SpaceState>(sc6, sc4, tb8);
		SpaceTransition<SpaceState> t27 = new SpaceTransition<SpaceState>(sc2, sc7, tb7);
		
		toMatch.add(t01);
		toMatch.add(t02);
		toMatch.add(t14);
		toMatch.add(t20);
		toMatch.add(t42);
		toMatch.add(t41);
		toMatch.add(t13);
		toMatch.add(t36);
		toMatch.add(t63);
		toMatch.add(t35);
		toMatch.add(t50);
		toMatch.add(t57);
		toMatch.add(t75);
		toMatch.add(t76);
		toMatch.add(t64);
		toMatch.add(t27);				
		
		toMatch.ridenominazione();
		
		ArrayList<SpaceState> computedStates = new ArrayList<SpaceState>(computedSpace.states());
		ArrayList<SpaceState> matchStates = new ArrayList<SpaceState>(toMatch.states());
		ArrayList<SpaceState> computedStatesCopy = new ArrayList<SpaceState>(computedStates);
		
		ArrayList<SpaceTransition<SpaceState>> computedTr = new ArrayList<SpaceTransition<SpaceState>>(computedSpace.transitions());
		ArrayList<SpaceTransition<SpaceState>> matchTr = new ArrayList<SpaceTransition<SpaceState>>(toMatch.transitions());
		ArrayList<SpaceTransition<SpaceState>> computedTrCopy = new ArrayList<SpaceTransition<SpaceState>>(computedTr);
		
		System.out.println(computedSpace.toString());
		
		computedStates.removeAll(matchStates);
		matchStates.removeAll(computedStatesCopy);
		computedTr.removeAll(matchTr);
		matchTr.removeAll(computedTrCopy);
		
		
		assertTrue(computedStates.isEmpty() && matchStates.isEmpty() &&
				computedTr.isEmpty() && matchTr.isEmpty());		

	}
	
	@Test

	void spazioComportamentaleOsservazioni() {

		CFSMnetwork net = initialize();
		ObservationsList obsList = new ObservationsList();
		obsList.add(new ObservableLabel("act"));
		obsList.add(new ObservableLabel("sby"));
		obsList.add(new ObservableLabel("nop"));
		SpazioComportamentaleObs sc = new SpazioComportamentaleObs(net, obsList);
		
		SpaceAutomaObsLin toMatch = new SpaceAutomaObsLin("Test con osservazione");
		SpaceAutomaObsLin computedSpace = null;
		try {
			computedSpace = sc.call();
		} catch (Exception e) {
			assertFalse(false);
		}
		SpaceStateObs sc0 = new SpaceStateObs(statesToHashMap(s0, b0), eventToHashMap(emptyEv), 0, obsList.size());
		SpaceStateObs sc1 = new SpaceStateObs(statesToHashMap(s1, b0), eventToHashMap(op), 1, obsList.size());
		SpaceStateObs sc2 = new SpaceStateObs(statesToHashMap(s1, b0), eventToHashMap(emptyEv), 1, obsList.size());
		SpaceStateObs sc3 = new SpaceStateObs(statesToHashMap(s0, b0), eventToHashMap(cl), 2, obsList.size());
		SpaceStateObs sc4 = new SpaceStateObs(statesToHashMap(s0, b0), eventToHashMap(emptyEv), 3, obsList.size());

		toMatch.insert(sc0);
		toMatch.insert(sc1);
		toMatch.insert(sc2);
		toMatch.insert(sc3);
		toMatch.insert(sc4);
		
		SpaceTransition<SpaceStateObs> t01 = new SpaceTransition<SpaceStateObs>(sc0, sc1, ts1);
		SpaceTransition<SpaceStateObs> t12 = new SpaceTransition<SpaceStateObs>(sc1, sc2, tb3);
		SpaceTransition<SpaceStateObs> t21 = new SpaceTransition<SpaceStateObs>(sc2, sc1, ts4);
		SpaceTransition<SpaceStateObs> t23 = new SpaceTransition<SpaceStateObs>(sc2, sc3, ts2);
		SpaceTransition<SpaceStateObs> t34 = new SpaceTransition<SpaceStateObs>(sc3, sc4, tb5);

		toMatch.add(t01);
		toMatch.add(t12);
		toMatch.add(t21);
		toMatch.add(t23);	
		toMatch.add(t34);
		
		ArrayList<SpaceStateObs> computedStates = new ArrayList<SpaceStateObs>(computedSpace.states());
		ArrayList<SpaceStateObs> matchStates = new ArrayList<SpaceStateObs>(toMatch.states());
		ArrayList<SpaceStateObs> computedStatesCopy = new ArrayList<SpaceStateObs>(computedStates);
		
		ArrayList<SpaceTransition<SpaceStateObs>> computedTr = new ArrayList<SpaceTransition<SpaceStateObs>>(computedSpace.transitions());
		ArrayList<SpaceTransition<SpaceStateObs>> matchTr = new ArrayList<SpaceTransition<SpaceStateObs>>(toMatch.transitions());
		ArrayList<SpaceTransition<SpaceStateObs>> computedTrCopy = new ArrayList<SpaceTransition<SpaceStateObs>>(computedTr);
		
		computedStates.removeAll(matchStates);
		matchStates.removeAll(computedStatesCopy);
		computedStates.contains(sc3);
		computedTr.removeAll(matchTr);
		matchTr.removeAll(computedTrCopy);
		
		
		assertTrue(computedStates.isEmpty() && matchStates.isEmpty()
				&& computedTr.isEmpty()	&& matchTr.isEmpty());		
	}
	

	@Test
	void diagnosi() throws Exception{
		CFSMnetwork net = initialize();
		ArrayList<RelevantLabel> relList = new ArrayList<RelevantLabel>();
		relList.add(new RelevantLabel("f1"));
		relList.add(new RelevantLabel("f2"));
		relList.add(new RelevantLabel("f3"));
		relList.add(new RelevantLabel("f4"));
		relList.add(new RelevantLabel("f5"));
		relList.add(new RelevantLabel("f6"));
		ObservationsList obsLin = new ObservationsList();
		obsLin.add(new ObservableLabel("act"));
		obsLin.add(new ObservableLabel("sby"));
		obsLin.add(new ObservableLabel("nop"));	

		SpazioComportamentaleObs sc = new SpazioComportamentaleObs(net, obsLin);
		SpaceAutomaObsLin computedSpace = sc.call();
		HashMap<SpaceStateObs, String> output = new MultipleRelRegexBuilder<SpaceStateObs, SpaceTransition<SpaceStateObs>>(computedSpace, new BuilderSpaceComportamentaleObsLin()).call();
		Iterator<String> iterator = output.values().iterator();
		String relDiagnosi = null;
		if(iterator.hasNext()) {
			StringBuilder sbDiagn = new StringBuilder(iterator.next());
			while(iterator.hasNext())
				sbDiagn.append("|").append(iterator.next());
			relDiagnosi = new RegexSimplifier().simplify(sbDiagn.toString(), relList);		
		} else 
			relDiagnosi = Constants.EPSILON;
		System.out.println("Result: "+relDiagnosi);
		assertTrue(relDiagnosi.equals("(f3((f2f3))*ε)"));
	}
	
	@Test
	void diagnosiDiagnosticatore() throws Exception{
		CFSMnetwork net = initialize();
		ArrayList<RelevantLabel> relList = new ArrayList<RelevantLabel>();
		relList.add(new RelevantLabel("f1"));
		relList.add(new RelevantLabel("f2"));
		relList.add(new RelevantLabel("f3"));
		relList.add(new RelevantLabel("f4"));
		relList.add(new RelevantLabel("f5"));
		relList.add(new RelevantLabel("f6"));
		ObservationsList obsLin = new ObservationsList();
		obsLin.add(new ObservableLabel("act"));
		obsLin.add(new ObservableLabel("sby"));
		obsLin.add(new ObservableLabel("nop"));	

		SpazioComportamentale sc = new SpazioComportamentale(net);
		SpaceAutomaComportamentale spazioC = sc.call();
		ClosureSpace diagnosticatore = new DiagnosticatoreBuilder(spazioC).call();
		String output = new LinearDiagnosis(diagnosticatore, obsLin).call();
		output = new RegexSimplifier().simplify(output.toString(), relList);		
		System.out.println("Result: "+output);
		assertTrue(output.equals("(f3((f2f3))*)"));
	}

	
	@Test
	void enabledTransitions() {
		CFSMnetwork net = initialize();
		Set<ComportamentalTransition> enabledT = new HashSet<ComportamentalTransition>();
		net.transitionTo(ts1);
		enabledT.add(tb1);
		enabledT.add(tb3);
		assertTrue(net.enabledTransitions().equals(enabledT));
	}
}
