package com.phasmidsoftware.dsaipg.projects.mcts.gogame.src;

import java.util.Vector;

public class Memeryunit_B {

   
	private final int ROWS = 19;// Board size (19x19)
	private int M_Turnflag ;// Turn flag (1 for Black, -1 for White)
	private int[][] M_ChessMap=new int[ROWS][ROWS];// Board state
	private int M_step=0 ;// Number of steps/moves

	// Default constructor
	Memeryunit_B() {
		
	}

	// Constructor with state copy
	Memeryunit_B(int[][] m_ChessMap,int m_step,int m_Turnflag){
		for(int i=0;i<ROWS;i++) {
			for(int j=0;j<ROWS;j++) {
				M_ChessMap[i][j]=m_ChessMap[i][j];
			}			
		}
		
		this.M_Turnflag=m_Turnflag;
		this.M_step=m_step;
	}

	// Getters and setters
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