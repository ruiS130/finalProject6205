package com.phasmidsoftware.dsaipg.projects.mcts.gogame.src;

import java.io.Serializable;
import java.util.Vector;

//Represents a stone (chess piece) on the Go board.
public class Chess_B implements Serializable{
	private int Color = 0;// Color of the piece (1 = Black, -1 = White, 0 = Empty)
	private int x_Map=-1,y_Map=-1;// Coordinates on the board (index in board array), -1 = not placed
	private int x_Point, y_Point;// Pixel coordinates for drawing
	private Vector vCurrentGroup=null;// The group this stone currently belongs to
	public Vector getvCurrentGroup() {
		return vCurrentGroup;
	}

	public void setvCurrentGroup(Vector vBelongto) {
		this.vCurrentGroup = vBelongto;
	}

	private int Qi=4;// Number of liberties (initially 4)
	private int alive=0;// Life status: 0 = dead, 1 = alive (optional usage)
	
	public int getX_Map() {
		return x_Map;
	}

	public void setX_Map(int x_Map) {
		this.x_Map = x_Map;
	}

	public int getY_Map() {
		return y_Map;
	}

	public void setY_Map(int y_Map) {
		this.y_Map = y_Map;
	}

	Chess_B()
	{
		
	}
	Chess_B(int x, int y) {
		this.x_Point = x;
		this.y_Point = y;
		//this.vBelongto=new Vector();
	}
	
	
	Chess_B(int x, int y, int qi) {
		this.x_Point = x;
		this.y_Point = y;
		this.Qi = qi;
		//this.vBelongto=new Vector();
	}

	public int getColor() {
		return Color;
	}

	public void setColor(int color) {
		Color = color;
	}

	public int getX_Point() {
		return x_Point;
	}

	public void setX_Point(int x_Point) {
		this.x_Point = x_Point;
	}

	public int getY_Point() {
		return y_Point;
	}

	public void setY_Point(int y_Point) {
		this.y_Point = y_Point;
	}

	public int getQi() {
		return Qi;
	}

	public void setQi(int qi) {
		Qi = qi;
	}

}