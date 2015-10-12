class cell:
    def __init__(self, y, x, icon, cellnum):
        self.chricon = icon
        self.coords = [y, x]
        self.isalive = True
        self.evopoints = 0
        self.idnum = cellnum
        self.playable = False
        self.canmove = False
        self.phased = False
        self.currentstatus = 0

        self.statuses = {
                'nostatus' : 0,
                'paralyzed' : 1,
                }

        #TODO: Mutation flags
        #Mutation flags will take the form "self.mutfoo"
        #where "foo" is a one-word description of the mutation
        self.mutmove = False

        #TODO: Learn mutation functions
        #Will take the form "self.learnfoo"
        #Where "foo" is the same word used in the flag
    def self.learnMove(self):
        self.mutmove = True
        if self.currentstatus != self.statuses['paralyzed']:
            self.canmove = True

    #TODO: Stats

    #TODO: Status effects
    def self.clearStatus(self):
        if self.mutmove and not self.canmove:
            self.canmove = True
        self.currentstatus = self.statuses['okay']

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
        tileprev.setOccupied(False)
        if self.phased == False:
            tilenew.setOccupied(True)
        self.updateLocation(dest, board)

    def checkCollision(self, dest, board):
        tile = board.getTile(dest)
        passable = tile.isPassable()
        if passable:
            self.updateLocation(dest, board)

    def moveNorth(self, board):
        dest = [self.coords[0] - 1, self.coords[1]]
        self.checkCollision(dest, board)
    def moveSouth(self, board):
        dest = [self.coords[0] + 1, self.coords[1]]
        self.checkCollision(dest, board)
    def moveEast(self, board):
        dest = [self.coords[0], self.coords[1] - 1]
        self.updateLocation(dest, board)
    def moveWest(self, board):
        dest = [self.coords[0], self.coords[1] + 1]
        self.updateLocation(dest, board)
    def moveNE(self, board):
        dest = [self.coords[0] - 1, self.coords[1] - 1]
        self.checkCollision(dest, board)
    def moveNW(self, board):
        dest = [self.coords[0] - 1, self.coords[1] + 1]
        self.checkCollision(dest, board)
    def moveSE(self, board):
        dest = [self.coords[0] + 1, self.coords[1] - 1]
        self.checkCollision(dest, board)
    def moveSW(self, board):
        dest = [self.coords[0] + 1, self.coords[1] + 1]
        self.checkCollision(dest, board)

class player(cell):
    def __init__(self, y, x, icon, cellnum):
        self.chricon = icon
        self.coords = [y, x]
        self.isalive = True
        self.evopoints = 0
        self.idnum = cellnum
        self.playable = True
        self.canmove = False

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
        self.waitkey == 'r'

    def getInput(self, board, inpt):
        if inpt == self.waitkey:
            break
        elif self.canmove:
            if inpt == self.movekeys['north']:
                self.moveNorth(board)
            elif inpt == self.movekeys['south']:
                self.moveSouth(board)
            elif inpt == self.movekeys['east']:
                self.moveEast(board)
            elif inpt == self.movekeys['west']:
                self.moveWest(board)
            elif inpt == self.movekeys['NE']:
                self.moveNE(board)
            elif inpt == self.movekeys['NW']:
                self.moveNW(board)
            elif inpt == self.movekeys['SE']:
                self.moveSE(board)
            elif inpt == self.movekeys['SW']:
                self.moveSW(board)
