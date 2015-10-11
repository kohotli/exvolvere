#Initializes the curses library
#Returns WindowObject
def initCurses():
    stdscr = curses.initscr()
    stdscr.keypad(1)
    curses.curs_set(0)
    curses.noecho()
    return stdscr

#Make curses window
def makeWindow(height, width, begin_y, begin_x):
    return curses.newwin(height, width, begin_y, begin_x)

#Cell is a list of form [y, x, icon] where icon is the character to be rendered
def showCell(cell, window):
    window.addch(cell[0], cell[1], cell[2])

#Displays characters on screen, takes a curses window and list of cells
def displayCells(window,cellist):
    window.erase()
    window.border(0)
    for i in celllist:
        showCell(i, window)

def getKey(window):
    return window.getkey()
