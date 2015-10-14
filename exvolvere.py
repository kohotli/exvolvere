#!/usr/bin/python

import curses
import cells
import render
import board

stdscr = render.initCurses()
#Height, Width, Y Origin, X Origin
dimensions = [11, 11, 0, 0]
running = True
window = render.makeWindow(dimensions)
board = board.board(dimensions[0],dimensions[1])
player = cells.player(3, 3, "@", 0)
playerList = [player]
renderList = [player.getFormattedList()]

player.learnMove()

while running:
    #render.clearWindow(window)
    window.clear()
    render.renderBoard(board,window)
    render.displayCells(renderList,window)
    window.refresh()
    running = render.inputLoop(playerList, board, window, "q")

render.closeCurses(stdscr)
