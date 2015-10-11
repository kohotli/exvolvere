class tile:
    #Represents board tiles
    def __init__(self,solid,ident,ch=" "):
        self.isSolid = solid
        self.pid = ident
        self.tile = ch

    #Get various attributes
    def isPassable():
        return self.isSolid
    def getPid():
        return self.pid
    def getTile():
        return self.tile

class board:
    #Outer tiles will be filled with impassable terrain
    def __init__(self,width,height):
        self.dimensions = [width,height]
        self.layout = []

        #Tile definitions
        empty=tile(False, 0, " ")
        outerWall=tile(True, 1, "#")
        innerWall=tile(True, 2, "#")

        #Populate outer wall
        for x in range(width):
            self.layout.append([outerWall] + ([empty] * (height-2)) + [outerWall])
        for y in range(height):
            self.layout[0][y] = outerWall
            self.layout[height-1][y] = outerWall

    #Location is a coordinate list of form [y, x]
    def getTile(self,location):
        return self.layout[location[1]][location[0]]
