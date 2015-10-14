import curses

#Initializes the curses library
#Returns WindowObject
def initCurses():
    stdscr = curses.initscr()
    stdscr.keypad(1)
    curses.curs_set(0)
    curses.noecho()
    return stdscr

def closeCurses(stdscr):
    stdscr.keypad(0)
    curses.echo()
    curses.endwin()

#Make curses window, returns curses window
#Takes a list of form [height, width, y origin, x origin]
def makeWindow(dimensions):
    return curses.newwin(dimensions[0],dimensions[1],dimensions[2],dimensions[3])

#Clear curses window
def clearWindow(window):
    window.erase()
    print("window cleared")
    window.border(0)

#Cell is a list of form [y, x, icon] where icon is the character to be rendered
def showCell(cell, window):
    window.addch(cell[0], cell[1], cell[2])

#Displays characters on screen, takes a curses window and list of cells
def displayCells(cellList,window):
    for i in cellList:
        showCell(i, window)

def renderBoard(board,window):
    dims = board.getSize()
    for y in range(dims[0] - 1):
        for x in range(dims[1] - 1):
            tile = board.getTile([y, x])
            character = tile.getIcon()
            window.addch(y, x, character)

#Calls getInput for all playable cells
#Returns False if quitkey is triggered, else returns True
def inputLoop(players, board, window, quitkey):
    inpt = window.getkey()
    if inpt == quitkey:
        return False
    for i in players:
        i.getInput(board, inpt)
    return True
