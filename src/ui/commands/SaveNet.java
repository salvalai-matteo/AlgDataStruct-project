package ui.commands;

import java.io.File;
import java.util.ArrayList;

import comportamentale_fa.ComportamentaleFANet;
import comportamentale_fa.Link;
import ui.Context;
import utility.FileHandler;

public class SaveNet implements CommandInterface, OneParameter{
	
	private static final String PARENT = "Saved Nets/";

	/*
	 * (non-Javadoc)
	 * @see main.commands.strategy.Commands#run(java.lang.String[], main.commands.Context)
	 */
	@Override
	public boolean run(String[] args, Context context) {
		if(!check(args, context))
			return false;
		if(context.savedLinksSize() == 0) {
			context.getIOStream().writeln("ERRORE: Creare almeno un link!");
			return false;
		}
		ArrayList<Link> links = new ArrayList<Link>(context.getSavedLinks());
		for(Link link: links) {
			if(link.getSource() == null || link.getDestination() == null) {
				context.getIOStream().writeln(String.format("ERRORE: Il link %s non ha uno stato sorgente o di destinazione!", link.id()));
				return false;
			}
		}
		ComportamentaleFANet net = new ComportamentaleFANet(links);
		String fileName = PARENT.concat(args[0]);
		File file = new File(fileName);
		if(file.exists()) {
			String ans = context.getIOStream().yesOrNo("File con nome indicato è già esistente, vuoi sovrascriverlo?");
			if(ans.equalsIgnoreCase("n"));
				fileName = fileName.concat(" (Copy)");
		}
		boolean saved = new FileHandler().save(fileName.concat(".ser"), net);
		if(saved)
			context.getIOStream().writeln(String.format("Rete di CFA salvata correttamente nel percorso %s!", fileName));
		else
			context.getIOStream().writeln(String.format("ERRORE: Impossibile salvare la rete CFA sul percorso %s!", fileName));
		return saved;
	}

}