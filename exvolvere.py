#!/usr/bin/python

import curses

class Character:
	def __init__(self, icon, y, x, cellnum, isplayable=False):
		self.chricon = icon
		self.playable = isplayable
		self.coords = [y, x]
		self.isalive = True
		self.evopoints = 0
		self.idnum = cellnum

		#Ability flags
		learnedmove = False
		learnedsee = False
		learnedattack = False
		learnedtele = False
		learnedwall = False
		learnedthorns = False
		learnedregen = False
		learnedstealth = False
		learnedchrysalis = False
		learnedassault = False
		learnedpierce = False
		learnedoverpower = False
		learnedsnipe = False

		#Stats, will be able to be dynamically set later
		hp = 10
		maxhp = 10
		atk = 1
		_def = 0 #Should be def but that would conflict with the syntax =__=
		movespd = 1 #No effect yet, movement is currently static and can't be extended
		sightrange = 1 #Cells within radius sightrange will be drawn to the screen if learnedsee == True


	def isPlayable(self):
		return self.playable

	def getIcon(self):
		return self.chricon

	def move(self, direction):	#Doesn't support diagonal movement yet, may be implemented later. I'm not sure.
		if direction == 0:	#North
			if self.coords[0] != 1:
				self.coords[0] -= 1

		if direction == 1:	#South
			if self.coords[0] != 9:
				self.coords[0] += 1

		if direction == 2:	#East
			if self.coords[1] != 1:
				self.coords[1] -= 1

		if direction == 3:	#West
			if self.coords[1] != 9:
				self.coords[1] += 1

	def getCoord(self, whichcoord):
		if whichcoord == 0:
			return self.coords[0]
		if whichcoord == 1:
			return self.coords[1]

	def basic2Cartesian(self, y, x):
		out_x = x
		out_y = 10 - y
		return([x, y])

	def cartesian2Basic(self, x, y):
		out_x = x
		out_y = 10 - y
		return([y, x])

	def calcDist(y1, x1, y2, x2):
		y3 = pow(y2 - y1, 2)
		x3 = pow(x2 - x1, 2)
		ret = pow(x3 + y3, 0.5)
		return(ret)

	def isWithinRange(y1, x1, y2, x2, _range):
		dist = calcDist(y1, x1, y2, x2)
		if dist <= _range:
			return True
		else:
			return False


stdscr = curses.initscr()
stdscr.keypad(1)
curses.curs_set(0)
curses.noecho()

running = True;

begin_x = 0 ; begin_y = 0
height = 11 ; width = 11
win = curses.newwin(height, width, begin_y, begin_x)

player = Character("@", 4, 4, True)
plicon = ord(player.getIcon())

cells = [Character("@", 1, 1, 0, True), Character("#", 1, 9, 1, True), Character("$", 9, 1, 2, False), Character("%", 9, 9, 3, False)]

cellicons = []
for x in cells:
	cellicons.append(x.getIcon())

while running:
	inpt = 0

	for x in range(len(cells)):
		win.erase()
		win.border(0)

		if cells[x].isPlayable() == True:
			coords = [cells[x].getCoord(0), cells[x].getCoord(1)]
			win.addch(coords[0], coords[1], cellicons[x])
			inpt = win.getkey()

			if inpt == 'q':
				running = False
			if inpt == 'w':
				cells[x].move(0)
			if inpt == 's':
				cells[x].move(1)
			if inpt == 'a':
				cells[x].move(2)
			if inpt == 'd':
				cells[x].move(3)

stdscr.keypad(0)
curses.echo()
curses.endwin()
