package ui.commands.general;

import ui.context.Context;

/**
 * Interfaccia funzionale con il compito di definire il comportamento dei comandi attraverso l'utilizzo di parametri
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 */
public interface CommandInterface {
	
	/**
	 * Il metodo che deve essere completato per eseguire un comando specificato dall'utente, dati eventuali parametri
	 * @param args parametri del comando
	 * @param iOStream 
	 * @param context il contesto nel quale deve agire il comando
	 * @return l'esito del comando
	 */
	public boolean run(String[] args, Context context);
}
