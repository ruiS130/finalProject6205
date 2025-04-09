package com.phasmidsoftware.dsaipg.projects.mcts.gogame.src;

import java.util.Vector;

public class Memeryunit_B {

   
	private final int ROWS = 19;// 棋盘行列数
	private int M_Turnflag ;// 回合标记
	private int[][] M_ChessMap=new int[ROWS][ROWS];//
	private int M_step=0 ;// 统计手数

	
	Memeryunit_B() {
		
	}
	
	Memeryunit_B(int[][] m_ChessMap,int m_step,int m_Turnflag){
		for(int i=0;i<ROWS;i++) {
			for(int j=0;j<ROWS;j++) {
				M_ChessMap[i][j]=m_ChessMap[i][j];
			}			
		}
		
		this.M_Turnflag=m_Turnflag;
		this.M_step=m_step;
	}

	
	public int getM_Turnflag() {
		return M_Turnflag;
	}

	public void setM_Turnflag(int m_Turnflag) {
		M_Turnflag = m_Turnflag;
	}

	public int[][] getM_ChessMap() {
		return M_ChessMap;
	}

	public void setM_ChessMap(int[][] m_ChessMap) {
		M_ChessMap = m_ChessMap;
	}

	public int getM_step() {
		return M_step;
	}

	public void setM_step(int m_step) {
		M_step = m_step;
	}

	
	
	
	
	
}