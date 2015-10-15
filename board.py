class tile:
    #Represents board tiles
    def __init__(self,solid,ident,ch=" "):
        self.solid = solid
        self.pid = ident
        self.tile = ch
        self.occupied = False

    #Check if the tile is passable
    def isPassable(self):
        if self.occupied:
            passable = False
        elif self.solid:
            passable = False
        else:
            passable = True
        return passable

    def getPid(self):
        return self.pid
    def getIcon(self):
        return self.tile

    def isOccupied(self):
        return self.occupied
    def setOccupied(self, x):
        self.occupied = x

class board:
    #Outer tiles will be filled with impassable terrain
    def __init__(self,height,width):
        self.dimensions = [height,width]
        self.layout = []
        self.units = []

        #Tile definitions
        #empty=tile(False, 0, " ")
        #outerWall=tile(True, 1, "#")
        #innerWall=tile(True, 2, "#")

        #Populate outer wall
        #for x in range(width-1):
            #self.layout.append([outerWall] + ([empty] * (height-3)) + [outerWall])
        #for y in range(height-1):
            #self.layout[0][y] = outerWall
            #self.layout[height-2][y] = outerWall
        for x in range(width-1):
            self.layout.append([])
            for y in range(height-1):
                if x == 0 or x == (height - 2):
                    self.layout[x].append(tile(True, 1, "#"))
                elif y == 0 or y == (width - 2):
                    self.layout[x].append(tile(True, 1, "#"))
                else:
                    self.layout[x].append(tile(False, 0, " "))

    #Location is a coordinate list of form [y, x]
    def getTile(self,location):
        return self.layout[location[0]][location[1]]

    def getSize(self):
        return self.dimensions

    def getLayout(self):
        return self.layout

    def addUnit(self,cell):
        self.units.append(cell)

    def clearUnitList(self):
        self.units = []

    #Returns a list of cell objects that occupy a particular tile
    #List will be empty if there are no cells
    def getCells(self,coords):
        ret = []
        for i in self.units:
            if i.getCoords() == coords:
                ret.append(i)
        return ret
