package ui;

import java.io.IOException;

import ui.commandshandler.CommandsHandler;
import ui.stream.SimpleStreamAdapter;
import utility.Constants;


/**
 * Classe contente il punto di partenza da cui far iniziare il programam
 * @author Matteo Salvalai [715827], Jacopo Mora [715149]
 *
 */
public class Main {

	private static CommandsHandler handler;
	
	/**
	 * Il punto da cui far iniziare il programma
	 * @param args lista di argomenti da passare
	 */
	public static void main(String[] args) {
		SimpleStreamAdapter ssa = new SimpleStreamAdapter();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {	
			try {
				handler.close();
				ssa.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}));
		
		ssa.write(Constants.WELCOME);
		handler = new CommandsHandler(ssa);
		do {
			String command = ssa.read(Constants.EMPTY_STRING);			
			handler.run(command.trim());
		}while(true);
	}	
}
