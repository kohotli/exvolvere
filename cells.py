class cell:
    def __init__(self, y, x, icon, cellnum):
        self.chricon = icon
        self.coords = [y, x]
        self.isalive = True
        self.evopoints = 0
        self.idnum = cellnum
        self.playable = False
        self.canmove = False

        #TODO: Ability flags

        #TODO: Stats

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

        def updateLocation(dest, board):
            tileprev = board.getTile(self.coords)
            tilenew = board.getTile(dest)
            tileprev.setOccupied(False)
            tilenew.setOccupied(True)
            self.coords = dest

        def moveNorth(board):
            dest = [self.coords[0] - 1, self.coords[1]]
            tile = board.getTile(dest)
            passable = tile.isPassable()
            if passable:
                self.coords = dest
        def moveSouth(board):
            dest = [self.coords[0] + 1, self.coords[1]]
            tile = board.getTile(dest)
            passable = tile.isPassable()
            if passable:
                self.coords = dest
        def moveEast(board):
            dest = [self.coords[0], self.coords[1] - 1]
            tile = board.getTile(dest)
            passable = tile.isPassable()
            if passable:
                self.coords = dest
        def moveWest(board):
            dest = [self.coords[0], self.coords[1] + 1]
            tile = board.getTile(dest)
            passable = tile.isPassable()
            if passable:
                self.coords = dest
        def moveNE(board):
            dest = [self.coords[0] - 1, self.coords[1] - 1]
            tile = board.getTile(dest)
            passable = tile.isPassable()
            if passable:
                self.coords = dest
        def moveNW(board):
            dest = [self.coords[0] - 1, self.coords[1] + 1]
            tile = board.getTile(dest)
            passable = tile.isPassable()
            if passable:
                self.coords = dest
        def moveSE(board):
            dest = [self.coords[0] + 1, self.coords[1] - 1]
            tile = board.getTile(dest)
            passable = tile.isPassable()
            if passable:
                self.coords = dest
        def moveSW(board):
            dest = [self.coords[0] + 1, self.coords[1] + 1]
            tile = board.getTile(dest)
            passable = tile.isPassable()
            if passable:
                self.coords = dest

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
        northkey == 'w'
        southkey == 'x'
        eastkey == 's'
        westkey == 'a'
        NEkey == 'f'
        NWkey == 'q'
        SEkey == 'c'
        SWkey == 'z'
        waitkey == 'r'

    def getInput(board, inpt):
        if inpt == self.waitkey:
            break
        elif self.canmove:
            if inpt == self.northkey:
                self.moveNorth(board)
            elif inpt == self.southkey:
                self.moveSouth(board)
            elif inpt == self.eastkey:
                self.moveEast(board)
            elif inpt == self.westkey:
                self.moveWest(board)
            elif inpt == self.NEkey:
                self.moveNE(board)
            elif inpt == self.NWkey:
                self.moveNW(board)
            elif inpt == self.SEkey:
                self.moveSE(board)
            elif inpt == self.SWkey:
                self.moveSW(board)
