class board:
    #Outer tiles will be filled with impassable terrain
    def __init__(self,width,height):
        self.dimensions = [width,height]
        self.layout = []
        for x in range(width):
            self.layout.append([1] + ([0] * (height-2)) + [1])
        for y in range(height):
            self.layout[0][y] = 1
            self.layout[height-1][y] = 1
