class cell:
    def __init__(self, y, x, icon, cellnum):
        self.chricon = icon
        self.coords = [y, x]
        self.evopoints = 0
        self.idnum = cellnum
        self.playable = False

        self.learnedmutations = {
                'move' : False,
                'sight' : False,
                'strike' : False, #Basic damaging strike
                'wall' : False, #Passes turn but doubles def
                'leap' : True #Moves you two spaces in any direction
                }

        self.buffs = {
                'alive' : True,
                'phased' : False,
                'wall' : False,
                'hurt' : False,
                'paralyzed' : False,
                'move' : False
                }

        self.directionIDs = {
                'north' : 0,
                'south' : 1,
                'east' : 2,
                'west' : 3,
                'NE' : 4,
                'NW' : 5,
                'SE' : 6,
                'SW' : 7
                }

        #Stat variables
        self.level = 1
        self.hp = 10
        self.maxhp = 10
        self.damage = 1
        self.attack = 1 #% damage increase
        self.defense = 1 #% damage reduction
        self.agility = 1 #Affects dodge chance and crit damage
        self.critchance = 25 #% chance to crit
        self.critdamage = 200 #% increase to base damage when critting
        self.healmult = 100 #% effectiveness of healing

        #TODO: Learn mutation functions
        #Will take the form "self.learnfoo"
        #Where "foo" is the same word used in the flag
    def learnMove(self):
        self.mutmove = True
        if not self.buffs['paralyzed']:
            self.setBuff('move', True)

    #TODO: Dodge chance
    def hurt(self,amt,board):
        dmg = amt / (self.defense / 100.0)
        if dmg >= self.hp:
            self.kill(board)
        elif dmg > 0:
            self.hp -= dmg

    def heal(self,amt,healmult):
        healamt = amt * (healmult / 100)
        if (self.hp + healamt) > self.maxhp:
            self.hp = self.maxhp
        elif healamt > 0:
            self.hp += healamt

    #TODO: Status effects
    def setParalyzed(self):
        self.setBuff('move', False)
        self.setBuff('paralyzed', True)

    #TODO: Active Ability Effects
    #Just processes effects, doesn't check for range or anything else
    def doStrike(self, targetcell):
        amt = self.dmg * (1 + (self.attack / 100))
        if self.checkCrit:
            amt *= (self.critdamage / 100.0)
        targetcell.hurt(amt)

    def doWall(self):
        self.defense *= 2
        self.setBuff('wall', True)

    def doLeap(self,board,direction):
        if direction == 0:
            self.moveNorth(board,2)
        elif direction == 1:
            self.moveSouth(board,2)
        elif direction == 2:
            self.moveEast(board,2)
        elif direction == 3:
            self.moveWest(board,2)
        elif direction == 4:
            self.moveNE(board,2)
        elif direction == 5:
            self.moveNW(board,2)
        elif direction == 6:
            self.moveSE(board,2)
        elif direction == 7:
            self.moveSW(board,2)



    def checkCrit(self):
        #TODO: Critical strikes
        return False

    def kill(self, board):
        self.setBuff('alive', False)
        tile = board.getTile(self.coords)
        tile.setOccupied(False)

    def clearStatus(self):
        if self.mutmove and not self.buffs['move']:
            self.setBuff('move', True)
        self.setBuff('paralyzed', False)

    def isPlayable(self):
        return self.playable

    def getIcon(self):
        return self.chricon

    #Returns a list of form [y, x, icon], also referred to as a cell type
    def getFormattedList(self):
        return [self.coords[0], self.coords[1], self.chricon]

    def updateLocation(self, dest, board):
        tileprev = board.getTile(self.coords)
        tilenew = board.getTile(dest)
        tileprev.occupied = False
        #if not self.buffs['phased']:
        self.coords = dest
        tilenew.occupied = True

    def checkCollision(self, dest, board):
        oldtile = board.getTile(self.coords)
        tile = board.getTile(dest)
        passable = tile.isPassable()
        if passable:
            self.updateLocation(dest, board)

    def moveNorth(self, board, amt=1):
        dest = [self.coords[0] - amt, self.coords[1]]
        self.checkCollision(dest, board)
    def moveSouth(self, board, amt=1):
        dest = [self.coords[0] + amt, self.coords[1]]
        self.checkCollision(dest, board)
    def moveEast(self, board, amt=1):
        dest = [self.coords[0], self.coords[1] + amt]
        self.checkCollision(dest, board)
    def moveWest(self, board, amt=1):
        dest = [self.coords[0], self.coords[1] - amt]
        self.checkCollision(dest, board)
    def moveNE(self, board, amt=1):
        dest = [self.coords[0] - amt, self.coords[1] + amt]
        self.checkCollision(dest, board)
    def moveNW(self, board, amt=1):
        dest = [self.coords[0] - amt, self.coords[1] - amt]
        self.checkCollision(dest, board)
    def moveSE(self, board, amt=1):
        dest = [self.coords[0] + amt, self.coords[1] + amt]
        self.checkCollision(dest, board)
    def moveSW(self, board, amt=1):
        dest = [self.coords[0] + amt, self.coords[1] - amt]
        self.checkCollision(dest, board)

    #Helper functions
    def calcDist(self, y1, x1, y2, x2):
        y3 = pow(y2 - y1, 2)
        x3 = pow(x2 - x1, 2)
        ret = pow(x3 + y3, 0.5)
        return ret

    def isWithinRange(self, y1, x1, y2, x2, _range):
        dist = calcDist(y1, x1, y2, x2)
        if dist <= _range:
            return True
        else:
            return False

    def setBuff(self, buff, status):
        self.buffs[buff] = status

    def startOfTurn(self):
        if self.buffs['wall']:
            self.setBuff('wall', False)
        if self.buffs['hurt']:
            self.setBuff('hurt', False)

class player(cell):
    def __init__(self, y, x, icon, cellnum=0):
        cell.__init__(self, y, x, icon, cellnum)

        self.playable = True

        #Key definitions
        self.movekeys = {
                'north': 'w',
                'south': 'x',
                'east': 's',
                'west': 'a',
                'NE': 'f',
                'NW': 'q',
                'SE': 'c',
                'SW': 'z'
                }
        self.activekeys = {
                'strike': 'p',
                'leap': 't',
                'wall': 'v'
                }
        self.waitkey = 'r'

    def getInput(self, board, window, inpt):
        if inpt == self.waitkey:
            pass
        elif self.buffs['move']:
            self.move(self.pickDirection(inpt),board)
        if self.learnedmutations['strike']:
            if inpt == self.activekeys['strike']:
                pass
        elif self.learnedmutations['leap']:
            if inpt == self.activekeys['leap']:
                leapinpt = window.getkey()
                direction = self.pickDirection(leapinpt)
                self.doLeap(board,direction)
        elif self.learnedmutations['wall']:
            if inpt == self.activekeys['wall']:
                self.doWall()

    def pickDirection(self, inpt):
        if inpt == self.movekeys['north']:
            return self.directionIDs['north']
        elif inpt == self.movekeys['south']:
            return self.directionIDs['south']
        elif inpt == self.movekeys['east']:
            return self.directionIDs['east']
        elif inpt == self.movekeys['west']:
            return self.directionIDs['west']
        elif inpt == self.movekeys['NE']:
            return self.directionIDs['NE']
        elif inpt == self.movekeys['NW']:
            return self.directionIDs['NW']
        elif inpt == self.movekeys['SE']:
            return self.directionIDs['SE']
        elif inpt == self.movekeys['SW']:
            return self.directionIDs['SW']

    def move(self, direction, board):
        if direction == 0:
            self.moveNorth(board, 1)
        elif direction == 1:
            self.moveSouth(board, 1)
        elif direction == 2:
            self.moveEast(board, 1)
        elif direction == 3:
            self.moveWest(board, 1)
        elif direction == 4:
            self.moveNE(board, 1)
        elif direction == 5:
            self.moveNW(board, 1)
        elif direction == 6:
            self.moveSE(board, 1)
        elif direction == 7:
            self.moveSW(board, 1)
