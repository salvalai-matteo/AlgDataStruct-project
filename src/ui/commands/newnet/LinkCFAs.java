package ui.commands.newnet;

import comportamental_fsm.ComportamentalFSM;
import ui.commands.general.CommandInterface;
import ui.commands.general.OneParameter;
import ui.context.Context;

public class LinkCFAs implements CommandInterface, OneParameter{

	private static final int CFAS_REQUIRED = 2;
	
	@Override
	public boolean run(String[] args, Context context) {
		if(!check(args, context))
			return false;
		String id = args[0];
		if(!context.getWorkSpace().hasSavedLink(id)) {
			context.getIOStream().writeln(String.format("ERRORE: Non esiste nessun Link con id %s", id));
			return false;
		}
		if(context.getWorkSpace().savedCFAsSize() < CFAS_REQUIRED) {
			context.getIOStream().writeln(String.format("ERRORE: Non sono stati creati CFA sufficienti per poter creare un Link!", id));
			return false;
		}
		context.getIOStream().writeln(context.getWorkSpace().savedCFAsList());
		ComportamentalFSM source = getCFA(context, "Indicare l'id del CFA sorgente (oppure 'exit' per annullare): ");
		if(source == null)
			return false;
		ComportamentalFSM destination = getCFA(context, "Indicare l'id del CFA destinazione (oppure 'exit' per annullare): ");
		if(destination == null)
			return false;
		boolean linked = context.getWorkSpace().linkCFAs(id, source, destination);
		if(linked)
			context.getIOStream().writeln(String.format("CFA %s e %s collegate correttamente tramite link %s!", source.id(), destination.id(), id));
		else
			context.getIOStream().writeln(String.format("ERRORE: Impossibile collegare le CFA!", id));
		return linked;
	}
	
	private ComportamentalFSM getCFA(Context context, String message) {
		boolean found = false;
		String id = null;
		do {
			id = context.getIOStream().read(message);
			if(id.equals("exit"))
				return null;
			found = context.getWorkSpace().hasSavedCFA(id);
		} while(!found);
		return context.getWorkSpace().getSavedCFAfromId(id);
	}

}
