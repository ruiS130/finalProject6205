package com.phasmidsoftware.dsaipg.projects.mcts.gogame.src;

import java.io.Serializable;
import java.util.Vector;

//棋子类
public class Chess_B implements Serializable{
	private int Color = 0;// 棋子颜色
	private int x_Map=-1,y_Map=-1;//棋子棋谱坐标    初始值为-1，表示不存在
	private int x_Point, y_Point;// 棋子坐标
	private Vector vCurrentGroup=null;
	public Vector getvCurrentGroup() {
		return vCurrentGroup;
	}

	public void setvCurrentGroup(Vector vBelongto) {
		this.vCurrentGroup = vBelongto;
	}

	private int Qi=4;// 气数
	private int alive=0;//棋子死活标记，0死，1活；
	
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