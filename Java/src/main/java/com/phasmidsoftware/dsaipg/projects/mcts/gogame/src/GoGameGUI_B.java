package com.phasmidsoftware.dsaipg.projects.mcts.gogame.src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GoGameGUI_B implements ActionListener {
	private ChessBoardPanel_B CBPanel;
	JButton bt_Back;
	JButton bt_newGame;
	JButton bt_Win;
	JLabel jl_Turn1,jl_Turn2,jl_Step1,jl_Step2,jl_Message1,jl_Message2;
	JLabel jl_AI_ComputationTime, jl_AI_Simulations;
	
	GoGameGUI_B() {
		JFrame jf = new JFrame("Game of GO");
		// Create game panel
		this.CBPanel =new ChessBoardPanel_B();
		// Initialize buttons and labels
		bt_newGame = new JButton("New Game");
		bt_Back = new JButton("Undo");
		bt_Win=new JButton("Game Status");
		jl_Turn1=new JLabel("Now Moving");
	    jl_Turn2=new JLabel("Black");
	    jl_Step1=new JLabel("Round Number\n");
	    jl_Step2=new JLabel("0");
	    jl_Message1=new JLabel("");
	    jl_Message2=new JLabel("");

		jl_AI_ComputationTime = new JLabel("AI Time: 0 ms");
		jl_AI_Simulations      = new JLabel("Simulations: 0");


		// Side panel
		JPanel jp = new JPanel();
		//jp.setLayout(new GridLayout(2, 1, 3, 3));
		jp.setPreferredSize(new Dimension(92,600));
		
		//jp.setLayout(new GridLayout(8,1,5,10));
		jp.add(bt_newGame);
		jp.add(bt_Back);
		jp.add(bt_Win);
		jp.add(jl_Turn1);
		jp.add(jl_Turn2);
		jp.add(jl_Step1);
		jp.add(jl_Step2);
		jp.add(jl_Message1);jp.add(jl_Message2);
		// Register button listeners
		bt_Back.addActionListener(this);
		bt_newGame.addActionListener(this);
		bt_Win.addActionListener(this);
		// Add components to frame
		jf.add(jp, BorderLayout.WEST);
		jf.add(CBPanel, BorderLayout.CENTER);

		jp.add(jl_AI_ComputationTime);
		jp.add(jl_AI_Simulations);

//		CBPanel.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				// 获取当前鼠标坐标
//				int x = e.getX();
//				int y = e.getY();
//				System.out.println(x + "  " + y);
//				jl_Message2.setText("Welcome");
//				// 棋盘外落子无效
//				if (x < 30 || x > 570 || y < 30 || y > 570)
//					return;
//				// 落子
//				int work=CBPanel.playChess(x, y);
//
//				int turn=0;
//				int step=0;
//				turn=CBPanel.getTurnflag();
//				step=CBPanel.getStep();
//				if(step%2==0)
//				   jl_Turn2.setText("黑方");
//				else
//					 jl_Turn2.setText("白方");
//				jl_Step2.setText(step+"");
//				if(work==-1)
//					jl_Message2.setText("无效下棋");
//
//			}
//		});

		// Mouse listener for placing a move
		CBPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// Get mouse coordinates
				int x = e.getX();
				int y = e.getY();
				System.out.println(x + "  " + y);
				jl_Message2.setText("");

				// Ignore clicks outside the board
				if (x < 30 || x > 570 || y < 30 || y > 570)
					return;

				// Player move
				int work = CBPanel.playChess(x, y);
				int turn = CBPanel.getTurnflag();
				int step = CBPanel.getStep();
				if (step % 2 == 0)
					jl_Turn2.setText("Black");
				else
					jl_Turn2.setText("White");
				jl_Step2.setText(step + "");
				if (work == -1)
					jl_Message2.setText("Invalid Move");

				// AI move logic (after player move if AI's turn)
				if (CBPanel.getTurnflag() == -1) {
					// Get current board state
					int[][] currentMap = CBPanel.getChessMap();
					int currentTurn = CBPanel.getTurnflag();

					long start = System.currentTimeMillis();
					// Call MCSTAgent to get the best move [row, col]
					int[] aiMove = MCSTAgent.nextMove(currentMap, currentTurn);
					long elapsed = System.currentTimeMillis() - start;
					int sims = MCSTAgent.getIterationLimit();

					updateAIDisplay(elapsed, sims);

					if (aiMove != null) {
						System.out.println("AI placed a piece at: " + aiMove[0] + "," + aiMove[1]);
						int aiResult = CBPanel.playChessAI(aiMove[0], aiMove[1], currentTurn);
						if (aiResult != -1) {
							// Update UI after a valid AI move
							int newTurn = CBPanel.getTurnflag();
							int newStep = CBPanel.getStep();
							if (newStep % 2 == 0)
								jl_Turn2.setText("Black");
							else
								jl_Turn2.setText("White");
							jl_Step2.setText(newStep + "");
							jl_Message2.setText("AI Finished");
						} else {
							jl_Message2.setText("AI Invalid Move");
						}
					} else {
						jl_Message2.setText("AI Cannot Move");
					}
				}
			}
		});


		jf.setSize(706, 637);
		jf.setVisible(true);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("h "+jp.getHeight()+" w "+jp.getWidth());
		// System.out.println(CB.getWidth() + " " + CB.getHeight());

	}

	public void updateAIDisplay(long timeMs, int sims) {
		jl_AI_ComputationTime.setText("AI Time: " + timeMs + " ms");
		jl_AI_Simulations.setText("Simulations: " + sims);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == bt_Back) {
			System.out.println("ButtonBack");
			int work=CBPanel.Back();
			int turn=0;
			int step=0;
			turn=CBPanel.getTurnflag();
			step=CBPanel.getStep();
			if(step%2==0) {
			   jl_Turn2.setText("Black");
			   jl_Message2.setText("Black Regret");
			}
			else {
				 jl_Turn2.setText("White");
				 jl_Message2.setText("White Regret");
				 }
			jl_Step2.setText(step+"");
			if(work==-1) 
				jl_Message2.setText("No Regret");
			
		}
		if(e.getSource()==bt_newGame) {
			System.out.println("ButtonNewGame");
	       	CBPanel.newGame();
	       	int turn=0;
			int step=0;
			turn=CBPanel.getTurnflag();
			step=CBPanel.getStep();
			if(turn==1)
			   jl_Turn2.setText("Black");
			else
				 jl_Turn2.setText("White");
			jl_Step2.setText(step+"");
			jl_Message2.setText("New Game Start");
		
		}
		if(e.getSource()==bt_Win) {
			int i=CBPanel.Winner();
			if(i==1) {
				jl_Message2.setText("Black Won!");
			}
			if(i==-1) {
				jl_Message2.setText("White Won!");
			}
			if(i==0) {
				jl_Message2.setText("Draw");
			}
		}
	}

	public static void main(String[] args) {
		new GoGameGUI_B();
	}
}