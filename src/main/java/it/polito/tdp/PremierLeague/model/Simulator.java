package it.polito.tdp.PremierLeague.model;

import java.util.PriorityQueue;

import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Simulator {
	
	private Model model;
	public Simulator(Model m) {
		this.model = m;
	}
	
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	//parametri di input
	private Match match;
	private int N;
	
	//modello del mondo
	private int T;         //istante di tempo in cui avviene l'evento
	private String bpTeam; //best player team
	private int bptFlag;   //flag che dice qual è la squadra del best player
	private String hTeam;  //home team
	private String aTeam;  //away team
	private int hPlayers;  //numero di giocatori casa
	private int aPlayers;  //numero di giocatori trasferta
	
	//valori di output
	private int hGoal; 
	private int aGoal;
	private int hReds;
	private int aReds;
	
	//inizializzazione
	public void init(Match match, int N) {
		this.match = match;
		this.N = N;
		
		//imposto stato iniziale
		this.T = 0;
		
		this.hGoal = 0;
		this.aGoal = 0;
		this.hReds = 0;
		this.aReds = 0;
		
		this.bpTeam = this.model.getTeamFromPlayer(this.model.getMigliore().getP(), this.match);
		this.hTeam = this.match.getTeamHomeNAME();
		this.aTeam = this.match.getTeamAwayNAME();
		if(this.bpTeam.equals(this.hTeam)) //se la squadra del miglior giocatore è quella di casa allora il flag varrà -1
			this.bptFlag = -1;
		else if(this.bpTeam.equals(this.aTeam)) //se è quella in trasferta allora varrà 1
			this.bptFlag = 1;
		else
			this.bptFlag = 0; //altrimenti varrà 0
		
		this.hPlayers = 11;
		this.aPlayers = 11; 
		
		//creo la coda
		this.queue = new PriorityQueue<Event>();
	}
	
	public void run() {
		while(this.N > 0) {
			int probabilita = (int)(Math.random()*100);
			if(probabilita < 50) {
				//c'è un GOAL
				this.queue.add(new Event(this.T++, EventType.GOL));
				
				int diff = this.hPlayers - this.aPlayers;
				if(diff < 0) {
					//away ha più giocatori -> segna
					this.aGoal++;
				}
				else if(diff > 0) {
					//segna home
					this.hGoal++;
				}
				else {
					//hanno lo stesso numero di giocatori -> segna la squadra del miglior giocatore
					if(this.bptFlag > 0)
						this.aGoal++;
					else if(this.bptFlag < 0)
						this.hGoal++;
					else
						System.out.println("Errore squadra miglior giocatore");
				}
			}
			else if(probabilita < 80) {
				//c'è un ESPULSIONE
				this.queue.add(new Event(this.T++, EventType.ESPULSIONE));
				
				int pEsp = (int)(Math.random()*100);
				if(pEsp < 60) {
					//espulsione squadra del miglior giocatore
					if(this.bptFlag < 0) {
						this.hReds++;
						this.hPlayers--;
					}
					else if(this.bptFlag > 0) {
						this.aReds++;
						this.aPlayers--;
					}
					else
						System.out.println("Errore squadra miglior giocatore");
				}
				else {
					//espulsione nell'altra squadra
					if(this.bptFlag < 0) {
						this.aReds++;
						this.aPlayers--;
					}
					else if(this.bptFlag > 0) {
						this.hReds++;
						this.hPlayers--;
					}
					else
						System.out.println("Errore squadra miglior giocatore");
				}
				
			}
			else {
				//c'è un INFORTUNIO
				this.queue.add(new Event(this.T++, EventType.INFORTUNIO));
				
				int pInf = (int)(Math.random()*100);
				if(pInf < 50) {
					//genero altre 2 azioni
					this.N += 2;
				}
				else {
					//genero altre 3 azioni
					this.N += 3;
				}
			}
			this.N--;
		}
		/*for(Event e : this.queue)
			System.out.println(e);*/
	}

	public int gethGoal() {
		return hGoal;
	}

	public int getaGoal() {
		return aGoal;
	}

	public int gethReds() {
		return hReds;
	}

	public int getaReds() {
		return aReds;
	}
}
