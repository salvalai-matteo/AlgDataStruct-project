package ui;
/**
 * Classe enum rappresentante la descrizione di un comando disponibile all'utente
 * @author Matteo Salvalai [715827], Lorenzo Maestrini[715780], Jacopo Mora [715149]
 *
 */
public enum CommandDescription{
	
	EXIT("exit", "Esci dal programma","exit"),
	ANNULLA("annulla", "Annulla l'operazione in corso e torna al menù principale","annulla"),
	BACK("back", "Torna al menù precedente","back"),
	NEWNET("newnet", "Crea una nuova rete di FA Comportamentali","newnet"), 
	LOADNET("loadnet", "Carica una rete di FA Comportamentali da file","loadnet"), 
	NEWCFA("newcfa", "Crea un nuovo FA Comportamentale","newcfa [id]"),
	NEWLINK("newlink", "Crea un nuovo Link","newlink [id]"),
	LINKCFAS("linkcfas", "Collega due CFA tramite un link","linkcfas [id]"),
	NEWSTATE("newstate", "Crea un nuovo Stato Comportamentale","newstate [id]"),
	NEWEVENT("newevent", "Crea un nuovo Evento","newevent [id]"),
	NEWTRANSITION("newtransition", "Crea una nuova Transizione Comportamentale","newtransition [id]"),
	SETINITIAL("setinitial", "Imposta lo stato con l'id specificato come iniziale per la CFA","setinitial [id]"),
	SAVECFA("savecfa", "Salva la CFA su cui si sta lavorando","savecfa"),
	SHOWCFAS("showcfas", "Mostra i CFA attualmente creati","showcfas"),
	SHOWLINKS("showlinks", "Mostra i Link attualmente creati","showlinks"),
	SHOWSTATES("showstates", "Mostra gli Stati attualmente creati","showstates"),
	SHOWEVENTS("showevents", "Mostra gli Event attualmente creati","showevents"),
	SHOWTRANSITIONS("showtransitions", "Mostra le Transizioni attualmente create","showtransitions");
	
	
	/**
	 * Il nome del comando
	 */
	private String name;
	/**
	 * La descrizione del comando
	 */
	private String description;
	/**
	 * La sintassi per l'esecuzione del comando
	 */
	private String syntax;
	
	/**
	 * Costruttore
	 * @param name il nome del comando
	 * @param description la descrizione del comando
	 * @param syntax la sintassi del comando
	 */
	private CommandDescription(String name, String description, String syntax) {
		this.name = name;
		this.description = description;
		this.syntax = syntax == "" ? "" : String.format("Sintassi: %s", syntax);
	}

	/**
	 * Restituisce il nome del comando
	 * @return il nome del comando
	 */
	public String getName() {
		return name;
	}

	/**
	 * Restituisce la descrizione del comando
	 * @return la descrizione del comando
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Restituisce la sintassi del comando
	 * @return sintassi del comando
	 */
	public String getSyntax() {
		return syntax;
	}
	

	/**
	 * Controlla se il comando ha il nome inserito
	 * @param comando il presunto nome del comando
	 * @return True - il comando ha il nome inserito<br>False - il comando non ha il nome inserito
	 */
	public boolean hasName(String comando) {
		return this.name.equals(comando);
	}

	
}
