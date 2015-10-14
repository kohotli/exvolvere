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

        #Tile definitions
        empty=tile(False, 0, " ")
        outerWall=tile(True, 1, "#")
        innerWall=tile(True, 2, "#")

        #Populate outer wall
        for x in range(width-1):
            self.layout.append([outerWall] + ([empty] * (height-3)) + [outerWall])
        for y in range(height-1):
            self.layout[0][y] = outerWall
            self.layout[height-2][y] = outerWall

    #Location is a coordinate list of form [y, x]
    def getTile(self,location):
        return self.layout[location[0]][location[1]]

    def getSize(self):
        return self.dimensions

    def getLayout(self):
        return self.layout
