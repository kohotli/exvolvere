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
                'leap' : False #Moves you two spaces in any direction
                }

        self.buffs = {
                'alive' : True,
                'phased' : False,
                'wall' : False,
                'hurt' : False,
                'paralyzed' : False,
                'move' : False
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
    def doStrike(self, cell):
        amt = self.dmg * (1 + (self.attack / 100))
        if self.checkCrit:
            amt *= (self.critdamage / 100.0)
        cell.hurt(amt)

    def doWall(self):
        self.defense *= 2
        self.setBuff('wall', True)

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

    #Movement functions
    #wtb switch statements
    #def move(self, direction, board):
    #    if direction == 0:
    #        self.moveNorth(board)
    #    elif direction == 1:
    #        self.moveSouth(board)
    #    elif direction == 2:
    #        self.moveEast(board)
    #    elif direction == 3:
    #        self.moveWest(board)
    #    elif direction == 4:
    #        self.moveNE(board)
    #    elif direction == 5:
    #        self.moveNW(board)
    #    elif direction == 6:
    #        self.moveSE(board)
    #    elif direction == 7:
    #        self.moveSW(board)

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
        self.waitkey = 'r'

    def getInput(self, board, inpt, amt=1):
        if inpt == self.waitkey:
            pass
        elif self.buffs['move']:
            if inpt == self.movekeys['north']:
                self.moveNorth(board, amt)
            elif inpt == self.movekeys['south']:
                self.moveSouth(board, amt)
            elif inpt == self.movekeys['east']:
                self.moveEast(board, amt)
            elif inpt == self.movekeys['west']:
                self.moveWest(board, amt)
            elif inpt == self.movekeys['NE']:
                self.moveNE(board, amt)
            elif inpt == self.movekeys['NW']:
                self.moveNW(board, amt)
            elif inpt == self.movekeys['SE']:
                self.moveSE(board, amt)
            elif inpt == self.movekeys['SW']:
                self.moveSW(board, amt)
