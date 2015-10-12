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
def makeWindow(height, width, begin_y, begin_x):
    return curses.newwin(height, width, begin_y, begin_x)

#Cell is a list of form [y, x, icon] where icon is the character to be rendered
def showCell(cell, window):
    window.addch(cell[0], cell[1], cell[2])

#Displays characters on screen, takes a curses window and list of cells
def displayCells(window,cellList):
    window.erase()
    window.border(0)
    for i in cellList:
        showCell(i, window)

#Calls getInput for all playable cells
#Returns False if quitkey is triggered, else returns True
def inputLoop(players, board, window, quitkey):
    inpt = window.getKey()
    if inpt == quitkey:
        return False
    for i in players:
        i.getInput(board, inpt)
    return True
