package classes;

import java.io.*;

public class Cells {
	private static final int dims = 10; //Disgusting but it works
	private static final int cellnums = 4; //See comment above >_>
	private int id;
	private int[] coords;
	private int[][] board;	//Placeholder for enemy cell locations
	private int type;
	private int evopoints;
	private boolean learnedmove;
	private boolean learnedsee;
	private boolean learnedattack;
	private boolean learnedtele;
	private boolean learnedwall;
	private boolean learnedthorns;
	private boolean learnedregen;
	private boolean learnedstealth;
	private boolean learnedchrysalis;
	private boolean learnedassault;
	private boolean learnedpierce;
	private boolean learnedoverpower;
	private boolean learnedsnipe;
	private int hp;
	private int maxhp;
	private int atk;
	private int def;
	private int movespd;
	private boolean isalive;
	private int sightrange;
	private boolean iswalling;

	//Messy, organize later
	
	public Cells(int cellclass, int[] loc, int cellnum) {
		type = cellclass;	//0 is str, 1 is agi, 2 is def
		evopoints = 0;
		coords = new int[2];
		coords = loc;
		board = new int[dims][dims];
		id = cellnum;
		
		//Ability learned flags
		learnedmove = false;
		learnedsee = false;
		learnedattack = false;
		learnedtele = false;
		learnedwall = false;
		learnedthorns = false;
		learnedregen = false;
		learnedstealth = false;
		learnedchrysalis = false;
		learnedassault = false;
		learnedpierce = false;
		learnedoverpower = false;
		learnedsnipe = false;
		
		isalive = true;
		
		//Combat stats
		atk = 1;
		def = 0;
		hp = 10;
		maxhp = 10;
		movespd = 1;
		
		sightrange = 1;
		
		clearBoard();
	}
	
	public void move() {
		String xcoord = null;
		String ycoord = null;
		int xcoordint = 0;
		int ycoordint = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean isvalidcoord = false;
		while (!isvalidcoord){
			System.out.println("Your coordinates: " + coords[0] + ", " + coords[1]);
			System.out.print("Destination X coordinate: ");
			try {
				xcoord = br.readLine();
			} catch (IOException e) {
				System.out.println("IO Error.");
				System.exit(1);
			}
			System.out.print("Destination Y coordinate: ");
			try {
				ycoord = br.readLine();
			} catch (IOException e) {
				System.out.println("IO Error.");
				System.exit(1);
			}
			xcoordint = Integer.parseInt(xcoord);
			ycoordint = Integer.parseInt(ycoord);
			if (isWithinRange(coords[0], coords[1], xcoordint, ycoordint, movespd) == false) {
				isvalidcoord = false;
				System.out.println("Coordinates are outside of range, please enter valid coordinates.");
			}
			else if (xcoordint > dims - 1) {
				isvalidcoord = false;
				System.out.println("There is an impossibly tall wall blocking your access to that tile, please choose a different tile.");
			}
			else if (xcoordint < 0) {
				isvalidcoord = false;
				System.out.println("There is an impossibly tall wall blocking your access to that tile, please choose a different tile.");
			}
			else if (ycoordint > dims - 1) {
				isvalidcoord = false;
				System.out.println("There is an impossibly tall wall blocking your access to that tile, please choose a different tile.");
			}
			else if (ycoordint < 0) {
				isvalidcoord = false;
				System.out.println("There is an impossibly tall wall blocking your access to that tile, please choose a different tile.");
			}
			else {isvalidcoord = true;}
		}
		coords[0] = xcoordint;
		coords[1] = ycoordint;
	}
	private boolean isWithinRange(int x1, int y1, int x2, int y2, int range) {
		int dist = calcDist(x1, y1, x2, y2);
		if (dist <= range) {return true;}
		else {return false;}
	}
	private void attack(int dir, Cells[] players) {
		//0 is north, 1 is south, 2 is east, 3 is west,
		//4 is northeast, 5 is southeast, 6 is northwest, 7 is southwest
		//Only works with one-tile radius, fix later
		if (learnedattack == true) {
			int[] modNorth = {0, 1};
			int[] modSouth = {0, -1};
			int[] modEast = {1, 0};
			int[] modWest = {-1, 0};
			int[] modNorthEast = {1, 1};
			int[] modSouthEast = {1, -1};
			int[] modNorthWest = {-1, 1};
			int[] modSouthWest = {-1, -1};
			int[] modDir = new int[2];
			int[] plrcoords = new int[2];
			int[] atkdest = new int[2];
			int targetid = 0;
			
			if (dir == 0) {modDir = modNorth;}
			else if (dir == 1) {modDir = modSouth;}
			else if (dir == 2) {modDir = modEast;}
			else if (dir == 3) {modDir = modWest;}
			else if (dir == 4) {modDir = modNorthEast;}
			else if (dir == 5) {modDir = modSouthEast;}
			else if (dir == 6) {modDir = modNorthWest;}
			else {modDir = modSouthWest;}
			
			atkdest = getCoords();
			atkdest[0] += modDir[0];
			atkdest[1] += modDir[1];
			if (atkdest[0] > 0) {
				if (atkdest[0] < dims) {
					if (atkdest[1] > 0) {
						if (atkdest[1] < dims) {
							for (int i = 0; i < cellnums; i++) {
								plrcoords = players[i].getCoords();
								board[plrcoords[0]][plrcoords[1]] = i;
							}
							
							targetid = board[atkdest[0]][atkdest[1]];
							
							if (targetid == 0) {atkNoEnemy();}
							else {doCombatDmg(players[targetid],1);}
						} else {atkWall();}
					} else {atkWall();}
				} else {atkWall();}
			} else {atkWall();}
		} else {atkNoLearn();}
	}
	public void smell() {//TODO: Not yet implemented, maybe for later version
		
	}
	public void teleport() {//TODO: Allows you to go past the boundaries of the game, causing array out of bounds errors. Fix today.
		String xcoord = null;
		String ycoord = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("X coordinate: ");
		try {
			xcoord = br.readLine();
		} catch (IOException e) {
			System.out.println("IO Error.");
			System.exit(1);
		}
		System.out.print("Y coordinate: ");
		try {
			ycoord = br.readLine();
		} catch (IOException e) {
			System.out.println("IO Error.");
			System.exit(1);
		}
		coords[0] = Integer.parseInt(xcoord);
		coords[1] = Integer.parseInt(ycoord);
	}
	
	public void addEvoPts(int qty) {evopoints += qty;}
	
	public int[] getCoords() {
		int[] ret = new int[2];
		ret[0] = coords[0];
		ret[1] = coords[1];
		return ret;
	}
	
	public String getStringCoords() {
		String ret;
		ret = String.valueOf(coords[0]);
		ret = ret.concat(",");
		ret = ret.concat(String.valueOf(coords[1]));
		return ret;
	}
	public String getCellType() {
		String ret;
		
		if (type == 0) {ret = "str";}
		else if (type == 1) {ret = "agi";}
		else if (type == 2) {ret = "def";}
		else if (type == 3) {ret = "all";}
		else {ret = "type error";}
		
		return ret;
	}
	private void modCoords(int[] mod) {
		coords[0] += mod[0];
		coords[1] += mod[1];
	}
	public int getEvoPts() {return evopoints;}
	private void clearBoard() {
		for (int x = 0; x < dims; x++) {
			for (int y = 0; y < dims; y++) {board[x][y] = 0;}
		}
	}
	public int getID() {return id;}
	public boolean isAlive() {return isalive;}
	
	//Combat functions
	public void pickAtkDir(Cells[] players) {
		//0 is north, 1 is south, 2 is east, 3 is west,
		//4 is northeast, 5 is southeast, 6 is northwest, 7 is southwest
		String dir = null;
		System.out.println("Which direction?");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			dir = br.readLine();
		} catch (IOException e) {
			System.out.println("IO Error.");
			System.exit(1);
		}
		if (dir.equals("N")) {attack(0, players);}
		if (dir.equals("S")) {attack(1, players);}
		if (dir.equals("E")) {attack(2, players);}
		if (dir.equals("W")) {attack(3, players);}
		if (dir.equals("NE")) {attack(4, players);}
		if (dir.equals("SE")) {attack(5, players);}
		if (dir.equals("NW")) {attack(6, players);}
		if (dir.equals("SW")) {attack(7, players);}
	}
	private void doCombatDmg(Cells dfndr, float mult) {
		if (dfndr.getWallStatus() == true) {mult *= 0.5;}
		float dmg = (getAtk() - dfndr.getDef()) * mult;	//Damage = attack minus defense times a multiplier that will matter later
		if (dmg < 0) {dmg = 0;}
		atkDoDamage((int) dmg, dfndr.getID());
		dfndr.Hurt((int) dmg);
	}
	private void doCombatDmg(Cells dfndr) {		//Call only if an attack ignores multipliers
		int dmg = getAtk() - dfndr.getDef();
		atkDoDamage(dmg, dfndr.getID());
		dfndr.Hurt(dmg);
	}
	private void Hurt(int dmg) {
		hp -= dmg;
		if (hp <= 0) {
			cellHasDied();
			isalive = false;
		}
	}
	
	//Various messages
	private void atkNoEnemy() {System.out.println("You attack the tile, but there's no one there!");}
	private void atkDoDamage(int dmg, int plr) {System.out.println("You attack player " + plr + " and deal " + dmg + " damage!");}
	private void atkNoLearn() {System.out.println("You haven't evolved the ability to attack.");}
	private void cellHasDied() {System.out.println("Player " + id + " has been slain!");}
	private void seePlayer(int ident, int[] coord) {
		if (id != ident){
			System.out.print("You see player " + ident + " at " );
			System.out.println(coord[0] + ", " + coord[1]);
		}
	}
	private void atkWall() {System.out.println("You attack, but you hit an insurmountable wall.");}
	
	private void upgradeAttrib(String attrib, int i) {
		if (attrib.equals("atk")) {atk += i;}
		else if (attrib.equals("def")) {def += i;}
		else if (attrib.equals("hp")) {
			hp += i;
			maxhp += i;
		}
		else if (attrib.equals("vision")) {sightrange += i;}
		else if (attrib.equals("spd")) {movespd += i;}
	}
	
	//Get Attrib functions
	public int getHP() {return hp;}
	public int getAtk() {return atk;}
	public int getDef() {return def;}
	
	//Learn ability functions. Return 0 if ability was not yet learned, return 1 if ability was already learned. Would be boolean but it's a fragment from earlier code that would return 2
	public int learnMove() {
		if (learnedmove == false) {
			learnedmove = true;
			System.out.println("Learned how to move!");
			addEvoPts(-1);
			return 0;
		} else {return 1;}
	}
	public int learnAtk() {
		if (learnedattack == false) {
			learnedattack = true;
			System.out.println("Learned how to attack!");
			addEvoPts(-1);
			return 0;
		} else {return 1;}
	}
	public int learnTele() {//Likely won't be an ability in the final game, just need it to test combat code. Might work for a revised movement cmd that can scale
		if (learnedtele == false) {
			learnedtele = true;
			System.out.println("Learned how to teleport!");
			addEvoPts(-1);
			return 0;
		} else {return 1;}
	}
	public int learnSight() {
		if (learnedsee == false) {
			learnedsee = true;
			System.out.println("You can now see!");
			addEvoPts(-1);
			return 0;
		} else {return 1;}
	}
	public int upgradeAtkDmg() {
		if (type == 0) {upgradeAttrib("atk", 2);}
		else {upgradeAttrib("atk", 1);}
		System.out.println("Upgraded your attack to " + atk);
		addEvoPts(-1);
		return 0;
	}
	public int upgradeDef() {
		if (type == 2) {upgradeAttrib("def", 2);}
		else {upgradeAttrib("def", 1);}
		System.out.println("Upgraded your defense to " + def);
		addEvoPts(-1);
		return 0;
	}
	public int upgradeHP() {
		upgradeAttrib("hp", 1);
		System.out.println("Increased your health to " + hp);
		addEvoPts(-1);
		return 0;
	}
	public int upgradeSight() {
		if (type == 1) {upgradeAttrib("vision", 2);}
		else {upgradeAttrib("vision", 1);}
		System.out.println("Upgraded your vision radius to " + sightrange);
		addEvoPts(-1);
		return 0;
	}
	public int upgradeSpd() {
		if (type == 1) {upgradeAttrib("spd", 2);}
		else {upgradeAttrib("spd", 1);}
		System.out.println("Upgraded your movement speed to " + movespd + " blocks per turn.");
		addEvoPts(-1);
		return 0;
	}
	
	//Boolean functions to get if an ability has been learned (true), or not (false).
	public boolean hasLearnedMove() {return learnedmove;}
	public boolean hasLearnedAtk() {return learnedattack;}
	public boolean hasLearnedSee() {return learnedsee;}
	public boolean hasLearnedTele() {return learnedtele;}
	public boolean hasLearnedWall() {return learnedwall;}
	public boolean hasLearnedThorns() {return learnedthorns;}
	public boolean hasLearnedRegen() {return learnedregen;}
	public boolean hasLearnedStealth() {return learnedstealth;}
	public boolean hasLearnedChrysalis() {return learnedchrysalis;}
	public boolean hasLearnedAssault() {return learnedassault;}
	public boolean hasLearnedPierce() {return learnedpierce;}
	public boolean hasLearnedOverpower() {return learnedoverpower;}
	public boolean hasLearnedSnipe() {return learnedsnipe;}
	
	//Math functions
	public int calcDist(int x1, int y1, int x2, int y2) {	//Return distance between two points on a 2D grid
		double x3 = Math.pow(x2 - x1, 2);
		double y3 = Math.pow(y2 - y1, 2);
		int ret = (int) Math.sqrt(x3 + y3);
		return ret;
	}

	//Functions related to sight
	public void see(Cells[] players) {
		int dist;
		if (learnedsee == true) {
			for (int i = 0; i < cellnums; i++) {
				int[] plrcoords = players[i].getCoords();
				dist = calcDist(getCoords()[0], getCoords()[1], plrcoords[0], plrcoords[1]);
				if (players[i].hasLearnedStealth()) {
					if (dist == 1) {seePlayer(i, plrcoords);}
					if (dist <= sightrange / 2) {seePlayer(i, plrcoords);}
				}
				if (dist <= sightrange) {seePlayer(i, plrcoords);}
			}
		}
	}
	
	public void stopActives() { //Stop actives that would last a turn, just a timesaver
		stopWalling();
	}
	public void procPassives(Cells[] players) { //Proc passive abilities
		if (hasLearnedThorns()) {thorns(players);}
		if (hasLearnedRegen()) {regen();}
	}
	
	//Wall - take 50% damage during the next turn, but do nothing this turn
	public void wall() {
		//Do nothing for one turn, but take half damage in this time
		iswalling = true;
	}
	public int learnWall() {
		if (learnedwall == false) {
			learnedwall = true;
			System.out.println("Learned how to stand sturdy as the great wall!");
			addEvoPts(-1);
			return 0;
		} else {return 1;}
	}
		public boolean getWallStatus() {return iswalling;}
		public void stopWalling() {if (iswalling == true) {iswalling = false;}}
	
	//Thorns - do damage every turn an enemy cell is in one of the 8 tiles surrounding the player
	private void thorns(Cells[] players) {
		for (int i = 0; i < 8; i++) {atkNoMsg(i, players, 1);}	
	}
	public int learnThorns() {
		if (learnedthorns == false) {
			learnedthorns = true;
			System.out.println("Evolved menacing thorns, so none may approach you unharmed.");
			addEvoPts(-1);
			return 0;
		} else {return 1;}
	}
	
	//Regeneration - recover 1 hp/turn
	private void regen() {
		if (hp < maxhp) {hp += 1;}
	}
	public int learnRegen() {
		if (type != 0) {
			if (learnedregen == false) {
				learnedregen = true;
				System.out.println("Evolved a healing mechanism, so that you recover.");
				addEvoPts(-1);
				return 0;
			} else {return 1;}
		} else {
			System.out.println("Strength-class cells can't evolve the Regeneration ability.");
			return 0;
		}
	}
	
	//Stealth - be unseen unless you're within half the enemy cell's vision radius
	public int learnStealth() {
		if (type == 1){
			if (learnedstealth == false) {
				learnedstealth = true;
				System.out.println("Evolved the ability to blend into your environment, so you are harder to see.");
				addEvoPts(-1);
				return 0;
			} else {return 1;}
		} else {
			System.out.println("Only Agile-class can evolve the Stealth ability.");
			return 0;
		}
	}
	
	//Chrysalis - do nothing for a turn, but gain an evo-point
	public int learnChrysalis() {
		if (learnedchrysalis == false) {
			learnedchrysalis = true;
			System.out.println("Evolved the ability to mutate faster in a chrysalis.");
			addEvoPts(-1);
			return 0;
		} else {return 1;}
	}
	public void chrysalis() {
		System.out.println("You enter a protective chrysalis for a turn and gain an evo-point. Beware of predators!");
		addEvoPts(1);
	}

	//Assault - move and attack in the same turn
	public int learnAssault() {
		if (type != 2) {
			if (learnedassault == false) {
				learnedassault = true;
				System.out.println("Swift and deadly as the wind, you've evolved the ability to move and assault an enemy.");
				addEvoPts(-1);
				return 0;
			} else {return 1;}
		} else {
			System.out.println ("Defense-class cells can't evolve the Assault ability.");
			return 0;
		}
	}
	public void assault(Cells[] players) {
		move();
		see(players);
		pickAtkDir(players);
	}
	
	//Pierce - do an attack that ignores damage multipliers
	public int learnPierce() {
		if (type != 1) {
			if (learnedpierce == false) {
				learnedpierce = true;
				System.out.println("With precise aim, you have evolved the ability to perform a piercing attack that ignores damage multipliers.");
				addEvoPts(-1);
				return 0;
			} else {return 1;}
		} else {
			System.out.println("Agile-class cells can't evolve the Pierce ability.");
			return 0;
		}
	}
	public void pierce(Cells[] players) {pickAtkDirNoMult(players);}

	//Overpower - deal incredible damage in an area based on your atk and hp, but take half of that damage yourself
	public int learnOverpower() {
		if (type == 0) {
			if (learnedoverpower == false) {
				learnedoverpower = true;
				System.out.println("Your rage knows now bounds as you have evolved the ability to perform an overpowering attack at a cost to your health.");
				addEvoPts(-1);
				return 0;
			} else {return 1;}
		} else {
			System.out.println("Only Strength-class cells can evolve the Overpower ability.");
			return 0;
		}
	}
	public void overpower(Cells[] players) {
		int mult = hp / 2;
		Hurt(atk * mult);
		for (int i = 0; i < 8; i++) {atkNoMsg(i, players, mult);}
	}
	
	//Snipe - attack from range and deal true damage based on your atk and def
	public int learnSnipe() {
		if (type != 2) {
			if (learnedsnipe == false) {
				learnedsnipe = true;
				System.out.println("Your keen eye allows you to spot the most hidden flaws in an enemy's defenses as you snipe them with a powerful ranged attack.");
				addEvoPts(-1);
				return 0;
			} else {return 1;}
		} else {
			System.out.println("Only Defense-class cells can evolve the Snipe ability.");
			return 0;
		}
	}
	public void snipe(Cells[] players) {
		int target = 0;
		int mult;
		int dmg;
		mult = def / 4;
		if (mult < 1) {mult = 1;}
		dmg = atk * mult;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean validtgt = false;
		while (!validtgt) {
			System.out.print("Target player ");
			try {
				target = br.read();
			} catch (IOException e) {
				System.out.println("IO Error.");
				System.exit(1);
			}
			if (target > cellnums) {
				System.out.println("Invalid target.");
			} else {validtgt = true;}
		}
		players[target].Hurt(dmg);
	}
	
	private void atkNoMsg(int dir, Cells[] players, int mult) {
		//0 is north, 1 is south, 2 is east, 3 is west,
		//4 is northeast, 5 is southeast, 6 is northwest, 7 is southwest
		int[] modNorth = {0, 1};
		int[] modSouth = {0, -1};
		int[] modEast = {1, 0};
		int[] modWest = {-1, 0};
		int[] modNorthEast = {1, 1};
		int[] modSouthEast = {1, -1};
		int[] modNorthWest = {-1, 1};
		int[] modSouthWest = {-1, -1};
		int[] modDir = new int[2];
		int[] plrcoords = new int[2];
		int[] atkdest = new int[2];
		int targetid = 0;

		if (dir == 0) {modDir = modNorth;}
		else if (dir == 1) {modDir = modSouth;}
		else if (dir == 2) {modDir = modEast;}
		else if (dir == 3) {modDir = modWest;}
		else if (dir == 4) {modDir = modNorthEast;}
		else if (dir == 5) {modDir = modSouthEast;}
		else if (dir == 6) {modDir = modNorthWest;}
		else {modDir = modSouthWest;}

		atkdest = getCoords();
		atkdest[0] += modDir[0];
		atkdest[1] += modDir[1];
		if (atkdest[0] > 0) {
			if (atkdest[0] < dims) {
				if (atkdest[1] > 0) {
					if (atkdest[1] < dims) {
						for (int i = 0; i < cellnums; i++) {
							plrcoords = players[i].getCoords();
							board[plrcoords[0]][plrcoords[1]] = i;
						}

						targetid = board[atkdest[0]][atkdest[1]];

						if (targetid == 0) {}
						else {doCombatDmg(players[targetid],mult);}
					}
				}
			}
		}
	}
	private void atkNoMult(int dir, Cells[] players) {
		//0 is north, 1 is south, 2 is east, 3 is west,
		//4 is northeast, 5 is southeast, 6 is northwest, 7 is southwest
		//Only works with one-tile radius, fix later
		int[] modNorth = {0, 1};
		int[] modSouth = {0, -1};
		int[] modEast = {1, 0};
		int[] modWest = {-1, 0};
		int[] modNorthEast = {1, 1};
		int[] modSouthEast = {1, -1};
		int[] modNorthWest = {-1, 1};
		int[] modSouthWest = {-1, -1};
		int[] modDir = new int[2];
		int[] plrcoords = new int[2];
		int[] atkdest = new int[2];
		int targetid = 0;

		if (dir == 0) {modDir = modNorth;}
		else if (dir == 1) {modDir = modSouth;}
		else if (dir == 2) {modDir = modEast;}
		else if (dir == 3) {modDir = modWest;}
		else if (dir == 4) {modDir = modNorthEast;}
		else if (dir == 5) {modDir = modSouthEast;}
		else if (dir == 6) {modDir = modNorthWest;}
		else {modDir = modSouthWest;}

		atkdest = getCoords();
		atkdest[0] += modDir[0];
		atkdest[1] += modDir[1];
		if (atkdest[0] > 0) {
			if (atkdest[0] < dims) {
				if (atkdest[1] > 0) {
					if (atkdest[1] < dims) {
						for (int i = 0; i < cellnums; i++) {
							plrcoords = players[i].getCoords();
							board[plrcoords[0]][plrcoords[1]] = i;
						}

						targetid = board[atkdest[0]][atkdest[1]];

						if (targetid == 0) {atkNoEnemy();}
						else {doCombatDmg(players[targetid]);}
					} else {atkWall();}
				} else {atkWall();}
			} else {atkWall();}
		} else {atkWall();}
	}
	private void pickAtkDirNoMult(Cells[] players) {
		//0 is north, 1 is south, 2 is east, 3 is west,
		//4 is northeast, 5 is southeast, 6 is northwest, 7 is southwest
		String dir = null;
		System.out.println("Which direction?");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			dir = br.readLine();
		} catch (IOException e) {
			System.out.println("IO Error.");
			System.exit(1);
		}
		if (dir.equals("N")) {atkNoMult(0, players);}
		if (dir.equals("S")) {atkNoMult(1, players);}
		if (dir.equals("E")) {atkNoMult(2, players);}
		if (dir.equals("W")) {atkNoMult(3, players);}
		if (dir.equals("NE")) {atkNoMult(4, players);}
		if (dir.equals("SE")) {atkNoMult(5, players);}
		if (dir.equals("NW")) {atkNoMult(6, players);}
		if (dir.equals("SW")) {atkNoMult(7, players);}
	}
	
}
