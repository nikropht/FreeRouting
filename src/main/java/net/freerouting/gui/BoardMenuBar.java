/*
 *  Copyright (C) 2014  Alfons Wirtz  
 *   website www.freerouting.net
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License at <http://www.gnu.org/licenses/> 
 *   for more details.
 *
 * BoardMenuBar.java
 *
 * Created on 11. Februar 2005, 10:17
 */

package net.freerouting.gui;

/**
 * Creates the menu bar of a board frame together with its menu items.
 *
 * @author Alfons Wirtz
 */
class BoardMenuBar extends javax.swing.JMenuBar {

    private BoardMenuFile file_menu;

    /**
     * Creates a new BoardMenuBar together with its menus
     */
    static BoardMenuBar get_instance(BoardFrame p_board_frame,
                                     boolean p_help_system_used, boolean p_session_file_option) {
        BoardMenuBar menubar = new BoardMenuBar();
        menubar.file_menu = BoardMenuFile.get_instance(p_board_frame, p_session_file_option);
        menubar.add(menubar.file_menu);
        menubar.add(BoardMenuDisplay.get_instance(p_board_frame));
        menubar.add(BoardMenuParameter.get_instance(p_board_frame));
        menubar.add(BoardMenuRules.get_instance(p_board_frame));
        menubar.add(BoardMenuInfo.get_instance(p_board_frame));
        menubar.add(BoardMenuOther.get_instance(p_board_frame));
        menubar.add(p_help_system_used ? new BoardMenuHelp(p_board_frame) : new BoardMenuHelpReduced(p_board_frame));
        return menubar;
    }

    void add_design_dependent_items() {
        this.file_menu.add_design_dependent_items();
    }
}
