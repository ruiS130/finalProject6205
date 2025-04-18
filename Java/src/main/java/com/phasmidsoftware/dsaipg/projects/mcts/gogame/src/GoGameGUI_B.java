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
	
	GoGameGUI_B() {
		JFrame jf = new JFrame("Game of GO");
        //创建棋盘绘制面板
		this.CBPanel =new ChessBoardPanel_B();
		//添加鼠标点击事件	
		bt_newGame = new JButton("New Game");
		bt_Back = new JButton("Undo");
		bt_Win=new JButton("Game Status");
		jl_Turn1=new JLabel("Now Moving");
	    jl_Turn2=new JLabel("Black");
	    jl_Step1=new JLabel("Round Num");
	    jl_Step2=new JLabel("0");
	    jl_Message1=new JLabel("System:");
	    jl_Message2=new JLabel("Welcome!");
	
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
		bt_Back.addActionListener(this);
		bt_newGame.addActionListener(this);
		bt_Win.addActionListener(this);
		jf.add(jp, BorderLayout.WEST);
		jf.add(CBPanel, BorderLayout.CENTER);
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
		CBPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// 获取鼠标坐标
				int x = e.getX();
				int y = e.getY();
				System.out.println(x + "  " + y);
				jl_Message2.setText("Welcome");

				// 棋盘外落子无效
				if (x < 30 || x > 570 || y < 30 || y > 570)
					return;

				// 玩家落子
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

				// 当玩家落子后，若游戏还未结束且当前回合为 AI（例如 turn == -1 表示 AI 落子），
				// 则自动调用 MCSTAgent.nextMove
				if (CBPanel.getTurnflag() == -1) {
					// 从棋盘获取当前棋谱状态
					int[][] currentMap = CBPanel.getChessMap();
					int currentTurn = CBPanel.getTurnflag();
					// 调用 MCSTAgent 的 nextMove 得到最佳落子 [row, col]
					int[] aiMove = MCSTAgent.nextMove(currentMap, currentTurn);
					if (aiMove != null) {
						System.out.println("AI 落子点: " + aiMove[0] + "," + aiMove[1]);
						int aiResult = CBPanel.playChessAI(aiMove[0], aiMove[1], currentTurn);
						if (aiResult != -1) {
							// 更新界面显示：根据新的手数和回合更新标签
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