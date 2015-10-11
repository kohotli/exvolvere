/*
 * Exvolvere (c) 2012 Amelia Lide
 * Exvolvere is released under the MIT license, a copy of which may be found in LICENSE.txt
 * 
 * This was begun on 8/24/12 at 9:00 PM, eastern time. It's for Ludum Dare 24, which had the theme "evolution".
 * Its first official version was finished on 8/26/12 at 7:40 PM, eastern time.
 * If this is incomplete while you are reading this, it's because the competition hasn't ended yet,
 * or I was unable to complete it in time. I welcome anyone to modify/make derivatives, but this is some hacky/bad code.
 * Just fair warning.
 */

import classes.Cells;
import java.io.*;

public class exvolvere {

	private static final int numcells = 4;
	private static final int dims = 10;		//Actual board size is dims+1xdims+1 since 0,0 is a valid place. Might need to lower this, a 100 square board is huge for 4 players
	private static boolean running;
	private static Cells[] players;
	private static int[][] board = new int[dims][dims];	//Contains scent information, which will be added later. Unrelated to board[][] in Cells.java
	private static final String[] evolist = {"none", "movement", "attack", "teleportation",
		"sight", "upgrade attack", "upgrade defense",
		"upgrade hp", "wall", "snipe", "regeneration", "assault", "chrysalis", "stealth",
		"overpower", "thorns", "pierce", "upgrade sight", "upgrade speed", "help"};
	private static final String[] comlist = {"quit", "print cell", "move",
											"teleport", "none", "attack", "wall", "help", 
											"wall", "snipe", "assault", "chrysalis", "overpower",
											"pierce"};
	private static final String[] celltypelist = {"Str", "Agi", "Def"};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		running = true;
		players = new Cells[numcells];

		//All cells go in corners, would like to see better way of doing this that handles other quantities
		//Find more elegant way of doing this later.
		//Starting cells as a type that has access to all tech for debugging purposes.

		introMessage();
		getCellTypes();
		while(running){
			mainLoop();
		}

	}

	private static void mainLoop() {
		giveEvoPoints();
		//For now, all cells are player controlled
		for (int i = 0; i < numcells; i++) {
			if (players[i].isAlive() == true) {
				players[i].see(players);
				getEvolve(i);
			}
		}
		for (int i = 0; i < numcells; i++) {
			if (players[i].isAlive() == true) {
				players[i].stopActives();
				players[i].procPassives(players);
				getCommand(i);
				if (running == false) {i = numcells;}
			}
		}
	}

	private static void getCommand(int player) {
		boolean validcom = false;
		String com = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (!validcom) {
			System.out.print("Player " + player + ", ");
			System.out.println("Enter your command:");
			try {
				com = br.readLine();
			} catch (IOException e) {
				System.out.println("IO Error.");
				System.exit(1);
			}

			if (com.equals(comlist[0])) {
				running=false;
				validcom = true;}
			else if (com.equals(comlist[1])) {	//Rename to "attributes" or similar later on
				System.out.println("Player " + String.valueOf(player) + ": ");
				System.out.print("Cell type is: ");
				System.out.println(players[player].getCellType());
				System.out.print("Cell coords are: ");
				System.out.println(players[player].getStringCoords());
				System.out.print("Availablee Mutation Points: ");
				System.out.println(players[player].getEvoPts());
				System.out.print("HP: ");
				System.out.println(players[player].getHP());
				System.out.print("Attack: ");
				System.out.println(players[player].getAtk());
				System.out.print("Defense: ");
				System.out.println(players[player].getDef());
			}
			else if (com.equals(comlist[2])) {
				if (players[player].hasLearnedMove()) {
					players[player].move();
					validcom = true;
					} else {System.out.println("You haven't evolved the ability to move.");}
			}
//			else if (com.equals(comlist[3])) {
//				if (players[player].hasLearnedTele()) {
//					players[player].teleport();
//					validcom = true;
//					} else {System.out.println("You haven't evolved the ability to teleport yet.");}
//			}
			else if (com.equals(comlist[4])) {validcom = true;}
			else if (com.equals(comlist[5])) {
				if (players[player].hasLearnedAtk()) {
					players[player].pickAtkDir(players);
					validcom = true;
				} else {System.out.println("You haven't evolved the ability to attack.");}
			}
			else if (com.equals(comlist[6])) {
				if (players[player].hasLearnedWall()) {
					players[player].wall();
					validcom = true;
				} else {System.out.println("You haven't evolved the ability to imitate the great wall.");}
			}
			else if (com.equals(comlist[7])) {comHelpMsg(player);}
			else if (com.equals(comlist[8])) {
				if (players[player].hasLearnedSnipe()) {
					players[player].snipe(players);
					validcom = true;
				} else {System.out.println("You haven't evolved the ability to kill your enemies at range.");}
			}
			else if (com.equals(comlist[9])) {
				if (players[player].hasLearnedAssault()) {
					players[player].assault(players);
					validcom = true;
				} else {System.out.println("You haven't evolved the ability to assault your foes.");}
			}
			else if (com.equals(comlist[10])) {
				if (players[player].hasLearnedChrysalis()) {
					players[player].chrysalis();
					validcom = true;
				} else {System.out.println("You haven't evolved the ability to evolve more quickly.");}
			}
			else if (com.equals(comlist[11])) {
				if (players[player].hasLearnedOverpower()) {
					players[player].overpower(players);
					validcom = true;
				} else {System.out.println("You haven't evolved the ability to overpower everybody.");}
			}
			else if (com.equals(comlist[12])) {
				if (players[player].hasLearnedPierce()) {
					players[player].pierce(players);
					validcom = true;
				} else {System.out.println("You haven't evolved the ability to pierce your enemy's armor.");}
			}
			else {
				printComErr();
			}
		}
	}

	private static void getEvolve(int player) {
		boolean validcom = false;
		boolean domutate = true;
		String evolve = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int muteerr;
		while (validcom == false) {
			validcom = true;
			while (domutate) {
				validcom = true;
				if (players[player].getEvoPts() != 0) {
					validcom = true;
					System.out.print("Player " + String.valueOf(player) + ", ");
					System.out.println("Enter what ability you would like to mutate:");
					try {
						evolve = br.readLine();
					} catch (IOException e) {
						System.out.println("IO Error.");
						System.exit(1);
					}
					if (evolve.equals(evolist[0]) == true) {
						domutate = false;
					}
					else if (evolve.equals(evolist[1]) == true) {
						muteerr = players[player].learnMove();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}
					}
					else if (evolve.equals(evolist[2]) == true) {
						muteerr = players[player].learnAtk();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}
					}
					//					else if (evolve.equals(evolist[3]) == true) {
					//						muteerr = players[player].learnTele();
					//						if (muteerr == 0) {}
					//						else if (muteerr == 1) {
					//							validcom = false;
					//							errAlreadyKnowsAbility();
					//						}
					//					}
					else if (evolve.equals(evolist[4]) == true) {
						muteerr = players[player].learnSight();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}
					}
					//Stat upgrades
					else if (evolve.equals(evolist[5]) == true) {players[player].upgradeAtkDmg();}
					else if (evolve.equals(evolist[6]) == true) {players[player].upgradeDef();}
					else if (evolve.equals(evolist[7]) == true) {players[player].upgradeHP();}
					//Extra abilities
					else if (evolve.equals(evolist[8]) == true)	{
						muteerr = players[player].learnWall();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}
					}
					else if (evolve.equals(evolist[9])) {
						muteerr = players[player].learnSnipe();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}
					}
					else if (evolve.equals(evolist[10])) {
						muteerr = players[player].learnRegen();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}
					}
					else if (evolve.equals(evolist[11])) {
						muteerr = players[player].learnAssault();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}

					}
					else if (evolve.equals(evolist[12])) {
						muteerr = players[player].learnChrysalis();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}
					}
					else if (evolve.equals(evolist[13])) {
						muteerr = players[player].learnStealth();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}
					} 
					else if (evolve.equals(evolist[14])) {
						muteerr = players[player].learnOverpower();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}
					}
					else if (evolve.equals(evolist[15])) {
						muteerr = players[player].learnThorns();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}
					}
					else if (evolve.equals(evolist[16])) {
						muteerr = players[player].learnPierce();
						if (muteerr == 0) {}
						else if (muteerr == 1) {
							validcom = false;
							errAlreadyKnowsAbility();
						}
					}
					else if (evolve.equals(evolist[17])) {players[player].upgradeSight();}
					else if (evolve.equals(evolist[18])) {players[player].upgradeSpd();}
					else if (evolve.equals(evolist[19])) {evoHelpMsg(player);}
					else {
						validcom = false;
						printEvoErr();
					}
				} else {
					domutate = false;
				}
			}
		}
	}

	private static void getCellTypes() {//Hardcoded for four players? Yuck!
		int[] coords1 = {0,0};
		int[] coords2 = {0,dims - 1};
		int[] coords3 = {dims - 1, 0};
		int[] coords4 = {dims - 1, dims - 1};
		boolean isvalidchoice = false;
		int choiceid;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		do {
			String choice = null;
			getCellTypesMsg(0);
			try {
				choice = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			choiceid = chooseCellTypes(choice);
			if (choiceid != 3) {
				isvalidchoice = true;
				players[0] = new Cells(choiceid, coords1, 0);
			} else {
				invalidCellTypeErr();
				isvalidchoice = false;
			}
		} while(!isvalidchoice);

		do {
			String choice = null;
			getCellTypesMsg(1);
			try {
				choice = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			choiceid = chooseCellTypes(choice);
			if (choiceid != 3) {
				isvalidchoice = true;
				players[1] = new Cells(choiceid, coords2, 1);
			} else {
				invalidCellTypeErr();
				isvalidchoice = false;
				}
		} while(!isvalidchoice);

		do {
			String choice = null;
			getCellTypesMsg(2);
			try {
				choice = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			choiceid = chooseCellTypes(choice);
			if (choiceid != 3) {
				isvalidchoice = true;
				players[2] = new Cells(choiceid, coords3, 2);
			} else {
				invalidCellTypeErr();
				isvalidchoice = false;
			}
		} while(!isvalidchoice);

		do {
			String choice = null;
			getCellTypesMsg(3);
			try {
				choice = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			choiceid = chooseCellTypes(choice);
			if (choiceid != 3) {
				isvalidchoice = true;
				players[3] = new Cells(choiceid, coords4, 3);
			} else {
				invalidCellTypeErr();
				isvalidchoice = false;
			}
		} while(!isvalidchoice);
	}

	private static int chooseCellTypes(String choice) {//Only really here to separate a potentially long if-then-else chain down the road
		//Takes a string, for which valid inputs are "str", "agi", and "def".
		//Returns an int
		if (choice.equals(celltypelist[0])) {return 0;}
		else if (choice.equals(celltypelist[1])) {return 1;}
		else if (choice.equals(celltypelist[2])) {return 2;}
		else {return 3;}
	}

	private static void giveEvoPoints() {
		for (int i = 0; i < numcells; i++){
			players[i].addEvoPts(1);
		}
	}

	//Various messages
	private static void printComErr() {System.out.println("Invalid command.");}
	private static void printEvoErr() {System.out.println("Invalid mutation.");}
	private static void errAlreadyKnowsAbility() {System.out.println("You already have that mutation.");}
	private static void errNoMutPoints() {System.out.println("You don't have any mutation points!");}
	private static void errYoureDead(int id) {
		System.out.print("Player " + id);
		System.out.println(", you may take no actions, for you are dead.");
	}
	private static void introMessage() {
		System.out.print("Hello, and welcome to Exvolvere. The game is simple - you start in opposite corners of ");
		System.out.print("a 10x10 board as a cell that can do nothing at all - except evolve. Evolve the ability to ");
		System.out.print("see, move, and attack your enemies. Mutation points are earned once every turn and are spent ");
		System.out.print("on new abilities for your cell. Once you've unlocked the three basic abilities, the madness ");
		System.out.print("REALLY begins. Unlock further upgrades depending on your cell's type with three different playstyles. ");
		System.out.println("The game is designed for four people sharing a keyboard.");
		System.out.println("If you ever get stuck on what are valid commands, just type \"help\", and you'll get a list of valid options.");
		System.out.println("Any feedback and suggestions can be sent to 19evanlide94@gmail.com.");
	}
	private static void getCellTypesMsg(int plr) {System.out.println("Player " + plr + ", choose your cell type. [Str, Agi, Def]");}
	private static void invalidCellTypeErr() {System.out.println("That is not a valid cell type.");}
	private static void comHelpMsg(int id) {
		System.out.println("Valid commands are: ");
		if (players[id].hasLearnedAtk()) {System.out.print("attack, ");}
		if (players[id].hasLearnedMove()) {System.out.print("move, ");}
		//if (players[id].hasLearnedTele()) {System.out.print("teleport, ");}
		if (players[id].hasLearnedWall()) {System.out.print("wall, ");}
		if (players[id].hasLearnedSnipe()) {System.out.print("snipe, ");}
		if (players[id].hasLearnedChrysalis()) {System.out.print("chrysalis, ");}
		if (players[id].hasLearnedOverpower()) {System.out.print("overpower, ");}
		if (players[id].hasLearnedPierce()) {System.out.print("pierce, ");}
		System.out.println("print cell, none, quit");
	}
	private static void evoHelpMsg(int id) {
		System.out.println("Valid evolutions are: ");
		if (players[id].hasLearnedAtk() == false) {System.out.print("attack, ");}
		else {System.out.print("upgrade attack, ");}
		System.out.print("upgrade defense, ");
		if (players[id].hasLearnedSee() == false) {System.out.print("sight, ");}
		else {System.out.print("upgrade sight, ");}
		if (players[id].hasLearnedMove() == false) {System.out.print("movement, ");}
		else {System.out.print("upgrade speed, ");}
		if (players[id].getCellType().equals("str")) {
			if (players[id].hasLearnedOverpower() == false) {System.out.print("overpower, ");}
			if (players[id].hasLearnedPierce() == false) {System.out.print("pierce, ");}
			if (players[id].hasLearnedAssault() == false) {System.out.print("assault, ");}
		}
		else if (players[id].getCellType().equals("agi")) {
			if (players[id].hasLearnedStealth() == false) {System.out.print("stealth, ");}
			if (players[id].hasLearnedAssault() == false) {System.out.print("assault, ");}
			if (players[id].hasLearnedRegen() == false) {System.out.print("regeneration, ");}
		}
		else if (players[id].getCellType().equals("def")) {
			if (players[id].hasLearnedSnipe() == false) {System.out.print("snipe, ");}
			if (players[id].hasLearnedPierce() == false) {System.out.print("assault, ");}
			if (players[id].hasLearnedRegen() == false) {System.out.print("regeneration, ");}
		}
		if (players[id].hasLearnedChrysalis() == false) {System.out.print("regeneration, ");}
		if (players[id].hasLearnedThorns() == false) {System.out.print("thorns, ");}
		if (players[id].hasLearnedWall() == false) {System.out.print("wall, ");}
		System.out.println("none.");
	}
}
