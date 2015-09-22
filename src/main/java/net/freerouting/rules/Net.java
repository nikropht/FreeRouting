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
 * Net.java
 *
 * Created on 11. Juni 2004, 08:17
 */
package net.freerouting.rules;

import net.freerouting.board.Item;
import net.freerouting.board.ObjectInfoPanel.Printable;
import net.freerouting.datastructures.UndoableObjects;

/**
 * Describes properties for an individual electrical net.
 *
 * @author Alfons Wirtz
 */
public class Net implements Comparable<Net>, net.freerouting.board.ObjectInfoPanel.Printable, java.io.Serializable {

    /**
     * The name of the net
     */
    public final String name;
    /**
     * Used only if a net is divided internally because of fromto rules for example
     * For normal nets it is always 1.
     */
    public final int subnet_number;
    /**
     * The unique strict positive number of the net
     */
    public final int net_number;
    /**
     * The net list, where this net belongs to.
     */
    public final Nets net_list;
    /**
     * Indicates, if this net contains a power plane
     */
    private boolean contains_plane;
    /**
     * The routing rule of this net
     */
    private NetClass net_class;

    /**
     * Creates a new instance of Net.
     * p_net_list is the net list, where this net belongs to.
     */
    public Net(String p_name, int p_subnet_number, int p_no, Nets p_net_list, boolean p_contains_plane) {
        name = p_name;
        subnet_number = p_subnet_number;
        net_number = p_no;
        contains_plane = p_contains_plane;
        net_list = p_net_list;
        net_class = p_net_list.get_board().rules.get_default_net_class();
    }

    public String toString() {
        return this.name;
    }

    /**
     * Compares 2 nets by name.
     * Useful for example to display nets in alphabetic order.
     */
    public int compareTo(Net p_other) {
        return this.name.compareToIgnoreCase(p_other.name);
    }

    /**
     * Returns the class of this net.
     */
    public NetClass get_class() {
        return this.net_class;
    }

    /**
     * Sets the class of this net
     */
    public void set_class(NetClass p_rule) {
        this.net_class = p_rule;
    }

    /**
     * Returns the pins and conduction areas of this net.
     */
    public java.util.Collection<Item> get_terminal_items() {
        java.util.Collection<Item> result = new java.util.LinkedList<Item>();
        net.freerouting.board.BasicBoard board = this.net_list.get_board();
        java.util.Iterator<UndoableObjects.UndoableObjectNode> it = board.item_list.start_read_object();
        for (; ; ) {
            Item curr_item = (Item) board.item_list.read_object(it);
            if (curr_item == null) {
                break;
            }
            if (curr_item instanceof net.freerouting.board.Connectable) {
                if (curr_item.contains_net(this.net_number) && !curr_item.is_route()) {
                    result.add(curr_item);
                }
            }
        }
        return result;
    }

    /**
     * Returns the pins of this net.
     */
    public java.util.Collection<net.freerouting.board.Pin> get_pins() {
        java.util.Collection<net.freerouting.board.Pin> result = new java.util.LinkedList<net.freerouting.board.Pin>();
        net.freerouting.board.BasicBoard board = this.net_list.get_board();
        java.util.Iterator<UndoableObjects.UndoableObjectNode> it = board.item_list.start_read_object();
        for (; ; ) {
            Item curr_item = (Item) board.item_list.read_object(it);
            if (curr_item == null) {
                break;
            }
            if (curr_item instanceof net.freerouting.board.Pin) {
                if (curr_item.contains_net(this.net_number)) {
                    result.add((net.freerouting.board.Pin) curr_item);
                }
            }
        }
        return result;
    }

    /**
     * Returns all items of this net.
     */
    public java.util.Collection<net.freerouting.board.Item> get_items() {
        java.util.Collection<net.freerouting.board.Item> result = new java.util.LinkedList<net.freerouting.board.Item>();
        net.freerouting.board.BasicBoard board = this.net_list.get_board();
        java.util.Iterator<UndoableObjects.UndoableObjectNode> it = board.item_list.start_read_object();
        for (; ; ) {
            Item curr_item = (Item) board.item_list.read_object(it);
            if (curr_item == null) {
                break;
            }
            if (curr_item.contains_net(this.net_number)) {
                result.add(curr_item);
            }
        }
        return result;
    }

    /**
     * Returns the cumulative trace length of all traces on the board belonging to this net.
     */
    public double get_trace_length() {
        double cumulative_trace_length = 0;
        java.util.Collection<Item> net_items = net_list.get_board().get_connectable_items(this.net_number);
        for (Item curr_item : net_items) {

            if (curr_item instanceof net.freerouting.board.Trace) {
                cumulative_trace_length += ((net.freerouting.board.Trace) curr_item).get_length();
            }
        }
        return cumulative_trace_length;
    }

    /**
     * Returns the count of vias on the board belonging to this net.
     */
    public int get_via_count() {
        int result = 0;
        java.util.Collection<Item> net_items = net_list.get_board().get_connectable_items(this.net_number);
        for (Item curr_item : net_items) {
            if (curr_item instanceof net.freerouting.board.Via) {
                ++result;
            }
        }
        return result;
    }

    public void set_contains_plane(boolean p_value) {
        contains_plane = p_value;
    }

    /**
     * Indicates, if this net contains a power plane.
     * Used by the autorouter for setting the via costs to the cheap plane via costs.
     * May also be true, if a layer covered with a conduction_area of this net is
     * is a signal layer.
     */
    public boolean contains_plane() {
        return contains_plane;
    }

    public void print_info(net.freerouting.board.ObjectInfoPanel p_window, java.util.Locale p_locale) {
        Integer via_count = this.get_via_count();
        double cumulative_trace_length = this.get_trace_length();
        java.util.Collection<Item> terminal_items = this.get_terminal_items();
        java.util.Collection<Printable> terminals = new java.util.LinkedList<Printable>();
        terminals.addAll(terminal_items);
        Integer terminal_item_count = terminals.size();

        java.util.ResourceBundle resources =
                java.util.ResourceBundle.getBundle("net.freerouting.board.ObjectInfoPanel", p_locale);
        p_window.append_bold(resources.getString("net") + " ");
        p_window.append_bold(this.name);
        p_window.append_bold(": ");
        p_window.append(resources.getString("class") + " ");
        p_window.append(net_class.get_name(), resources.getString("net_class"), net_class);
        p_window.append(", ");
        p_window.append_objects(terminal_item_count.toString(), resources.getString("terminal_items_2"), terminals);
        p_window.append(" " + resources.getString("terminal_items"));
        p_window.append(", " + resources.getString("via_count") + " ");
        p_window.append(via_count.toString());
        p_window.append(", " + resources.getString("trace_length") + " ");
        p_window.append(cumulative_trace_length);
        p_window.newline();

    }
}
