/*
 * Su Doku Solver
 *
 * Copyright (C) act365.com August 2005
 *
 * Web site: http://act365.com/sudoku
 * E-mail: developers@act365.com
 *
 * The Su Doku Solver solves Su Doku problems - see http://www.sudoku.com.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.act365.sudoku;

import java.util.ArrayList;  // jcc - replaced import for Vector with ArrayList

/**
 * StateStack stores state grids in a dynamically-expanding vector. The class
 * should be used for memory-intensive state grids that are rarely references
 * (so that performance isn't critical), such as LinearSystemState. Note that
 * this implementation extends ArrayList and hence is not thread-safe. // jcc
 */
public class StateStack extends ArrayList<Object>  // jcc - replaced parent class Vector with ArrayList<Object>
{

    int nMovesStored;

    int[] moves;

    /**
     * Creates a StateStack to store at most maxMoves moves.
     *
     * @param maxMoves The maximum number of moves this StateStack will be able
     * to hold // jcc
     * @throws java.util.ConcurrentModificationException // jcc
     */
    public StateStack(int maxMoves)
    {
        nMovesStored = 0;
        moves = new int[maxMoves];
        ensureCapacity(maxMoves);  // jcc - replaced setSize with ensureCapacity
    }

    /**
     * Adds a state object to this stack at the specified position, 
     * corresponding to move number. // jcc
     *
     * @param obj the state object to add to this StateStack // jcc
     * @param nMoves the position in the stack at which to insert (or
     * append) the state object // jcc
     * @throws java.util.ConcurrentModificationException // jcc
     * @see com.act365.sudoku.IState#pushState(int)
     */
    public void pushState(Object obj, int nMoves)
    {
        int i = 0;
        while (i < nMovesStored && moves[i] != nMoves)
        {
            ++i;
        }
        if (i < nMovesStored)
        {
            set(i, obj);  // jcc - replaced setElementAt with set
        } else
        {
            add(obj);  // jcc - replaced addElement with add
            moves[nMovesStored++] = nMoves;
        }
    }

    /**
     * Get the state object from this stack at the specified position,
     * corresponding to move number. // jcc
     *
     * @param nMoves the position in the stack at which to retrieve the state
     * object (null if not present) // jcc
     * @return the state object at the specified position // jcc
     * @throws java.util.ConcurrentModificationException // jcc
     * @see com.act365.sudoku.IState#popState(int)
     */
    public Object popState(int nMoves)
    {
        int i = 0;
        while (i < nMovesStored && moves[i] != nMoves)
        {
            ++i;
        }
        if (i < nMovesStored)
        {
            return get(i);  // jcc - replaced elementAt with get
        } else
        {
            return null;
        }
    }
}
