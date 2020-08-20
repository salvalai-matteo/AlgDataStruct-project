package ui.commands;

import comportamentale_fa.ComportamentaleState;
import ui.Context;

public class NewState implements CommandInterface, OneParameter{

	@Override
	public boolean run(String[] args, Context context) {
		if(!check(args, context))
			return false;
		String id = args[0];
		boolean added = context.saveState(new ComportamentaleState(id));
		if(added) {
			context.getIOStream().writeln(String.format("Nuovo Stato con id %s creato correttamente!", id));
			context.addStateToNewCFA((ComportamentaleState) context.getSavedStateFromId(id));
		}
		else
			context.getIOStream().writeln(String.format("ERRORE: Uno Stato con id %s è già presente!", id));
		return added;
	}

}