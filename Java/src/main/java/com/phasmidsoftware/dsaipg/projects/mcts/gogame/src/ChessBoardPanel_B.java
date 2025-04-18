package com.phasmidsoftware.dsaipg.projects.mcts.gogame.src;

import java.awt.*;
import java.util.Vector;

import javax.swing.JPanel;

//棋盘类
public class ChessBoardPanel_B extends JPanel {

	private Vector MapMemery = new Vector();// 参数记忆数组
	
	// 棋盘参数
	// 固定参数
	public final static int extent = 10;// 鼠标精度
	public static int[] xdir = { 0, 0, 1, -1 };
	public static int[] ydir = { 1, -1, 0, 0 };
	public Image boardImg, blackImg, whiteImg;// 棋盘、棋子图像
	private final int ROWS = 19;// 棋盘行列数
	private final int margin = 30;// 划线边距
	private int span;// 棋盘间距
	private int Work = 1;// 落子是否有效

	// 变化参数
	///////////////////////////////////////// 信息块////////////////////////////////////////
	private Vector vAllGroup = new Vector();// 总块
	private Vector CurrentRemovedGruop = new Vector();// 当前被提走的邻居组

	private int[][] ChessMap;// 棋谱坐标
	private Chess_B[][] ChessPoint = new Chess_B[ROWS][ROWS];// 棋盘坐标
	private int blackSize = 0, whiteSize = 0;// 黑白目数
	
	private int step = 0;// 统计手数

	private int Turnflag = 1;// 回合标记
	private int CurrentMapPoint_X, CurrentMapPoint_Y;// 当前鼠标点击对应的交叉点坐标，若值为-1则视为无效点击
	////////////////////////////////////////// 信息块//////////////////////////////////////////

	public int Winner() {
		blackSize=0;
		whiteSize=0;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {

				ChessPoint[i][j].setX_Map(i);
				ChessPoint[i][j].setY_Map(j);
				GenerateGruopforsinglechess(i, j);
			}
		}
		
		for(int k=0;k<vAllGroup.size();k++) {
			Vector v=new Vector();
			Chess_B CH;
			v=(Vector)vAllGroup.elementAt(k);
			CH=(Chess_B)v.elementAt(0);
			if(ChessMap[CH.getX_Map()][CH.getY_Map()]==1) {
				blackSize=blackSize+v.size();
				
			}
			if(ChessMap[CH.getX_Map()][CH.getY_Map()]==-1) {
				whiteSize=whiteSize+v.size();
		}
			
		}
		if(blackSize>whiteSize)
			return 1;
		if(blackSize<whiteSize)
			return -1;
		if(blackSize==whiteSize)
			return 0;
		return -2;
	}
	
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int[][] getCurrentChessMap() {
		return this.ChessMap;
	}

	public Chess_B[][] getCurrentChessPoint() {
		return this.ChessPoint;
	}

	public void AddtoMemery(int[][] chessMap, int step, int Turnflag) {
		MapMemery.addElement(new Memeryunit_B(chessMap, step, Turnflag));
	}

	public void newGame() {
		MapMemery.removeAllElements();
		vAllGroup.removeAllElements();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				ChessMap[i][j] = 0;
			}
		}

		System.out.println("Renew");
		// showChessMap(ChessMap);
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				ChessPoint[i][j].setvCurrentGroup(null);

			}
		}
		step = 0;
		Turnflag = 1;
		renewChessMap();
		repaint();
	}

	public void paint(Graphics g) {
		int imgWidth = boardImg.getWidth(this);
		int imgHeight = boardImg.getHeight(this);
		int Width = getWidth();
		int Height = getHeight();

		int x_left = margin;
		int x_right = Width - margin;
		int y_high = margin;
		int y_low = Height - margin;

		g.drawImage(boardImg, 0, 0, 600, 600, null);// 绘制背景

		int span_x = (y_low - y_high) / (ROWS - 1);// 棋盘间距—x轴
		int span_y = (x_right - x_left) / (ROWS - 1);// 棋盘间距-y轴
		span = span_x;
		int x1 = margin, y1 = margin, x2 = margin, y2 = margin;// 绘点坐标

		// 画横线
		for (int i = 0; i < ROWS; i++) {
			x1 = x_left;
			x2 = x_right;
			y1 = y2 = margin + i * span_y;
			g.drawLine(x1, y1, x2, y2);
		}
		// 画竖线
		for (int i = 0; i < ROWS; i++) {
			y1 = margin;
			y2 = Height - margin;
			x1 = x2 = margin + i * span_x;
			g.drawLine(x1, y1, x2, y2);
		}

		// 绘制星卫位置
		g.setColor(Color.BLACK);  // 星卫标记颜色
		for (int[] star : starPoints) {
			int x = star[0], y = star[1];
			int starX = ChessPoint[x][y].getX_Point();
			int starY = ChessPoint[x][y].getY_Point();
			g.fillOval(starX - 5, starY - 5, 10, 10);  // 绘制红色圆圈标记星卫
		}

		// 依据棋谱绘制棋局
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (ChessMap[j][i] != 0) {
					if (ChessMap[j][i] == 1) {
						if (step < 2) {
							for (int k = 0; k < 2000; k++) {
								g.drawImage(blackImg, ChessPoint[j][i].getX_Point() - span / 2,
										ChessPoint[j][i].getY_Point() - span / 2, span, span, null);
								System.out.println(" ");
							}

						}

						g.drawImage(blackImg, ChessPoint[j][i].getX_Point() - span / 2,
								ChessPoint[j][i].getY_Point() - span / 2, span, span, null);

					}
					if (ChessMap[j][i] == -1) {
						if (step < 3) {
							for (int k = 0; k < 2000; k++) {
								g.drawImage(whiteImg, ChessPoint[j][i].getX_Point() - span / 2,
										ChessPoint[j][i].getY_Point() - span / 2, span, span, null);
								System.out.println(" ");
							}

						}
						g.drawImage(whiteImg, ChessPoint[j][i].getX_Point() - span / 2,
								ChessPoint[j][i].getY_Point() - span / 2, span, span, null);

					}

				}

			}
		}

		System.out.println("Currentstep " + step);
		System.out.println("MapMemerylength " + MapMemery.size() + " " + MapMemery);
	}

	// 棋盘初始化
	public ChessBoardPanel_B() {

		boardImg = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/boardbackground.jpg"));
		whiteImg = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/white.png"));
		blackImg = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/black.png"));
		ChessMap = new int[ROWS][ROWS];
		// 棋谱初始化
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				ChessMap[i][j] = 0;
			}
		}
		// 生成棋盘坐标
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {

				ChessPoint[i][j] = new Chess_B((i + 1) * 30, (j + 1) * 30);
				ChessPoint[i][j].setX_Map(i);
				ChessPoint[i][j].setY_Map(j);

			}
		}
		MapMemery.removeAllElements();
		vAllGroup.removeAllElements();
		// 在控制台打印
		showChessPointMap();
		AddtoMemery(ChessMap, step, Turnflag);
		
	}

	
	// 落子函数
	public int playChess(int x, int y) {// 传入当前鼠标对应的棋谱坐标
		vAllGroup.removeAllElements();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				ChessPoint[i][j].setvCurrentGroup(null);

			}
		}
		// 判断点击位置
		// 无效落子
		if (judgeonMapPoint(x, y) == 0) {
			System.out.println("return");
			return 0;
		}
		System.out.println(CurrentMapPoint_X + "," + CurrentMapPoint_Y);

		// 落子处已经有棋子
		if (ChessMap[CurrentMapPoint_X][CurrentMapPoint_Y] != 0) {
			System.out.println("alreadyhaspoint:  " + CurrentMapPoint_X + "," + CurrentMapPoint_Y);
			return -1;
		}

		// 暂时修改棋谱
		setChessMap(CurrentMapPoint_X, CurrentMapPoint_Y, Turnflag);
		// 为每个落子寻找邻居组
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {

				ChessPoint[i][j].setX_Map(i);
				ChessPoint[i][j].setY_Map(j);
				GenerateGruopforsinglechess(i, j);
			}
		}
		step++;// 手数+1
		// addMap();
		// 有气，落子存活
		if (countAir(CurrentMapPoint_X, CurrentMapPoint_Y) != 0) {
			System.out.println("qi:" + countAir(CurrentMapPoint_X, CurrentMapPoint_Y));

			// 刷新棋谱
			renewChessMap();
			AddtoMemery(ChessMap, step, Turnflag);
			// 重绘
			repaint();

			showChessMap(ChessMap);

			if (Turnflag == 1)
				Turnflag = -1;
			else
				Turnflag = 1;

			return 1;

		}
		// 如果当前位置落子造成落子无气
		if (countAir(CurrentMapPoint_X, CurrentMapPoint_Y) == 0) {

			// 为落子寻找邻居组		
			// 若落子所在组有气
			if (GruopAir(ChessPoint[CurrentMapPoint_X][CurrentMapPoint_Y].getvCurrentGroup()) != 0) {

				System.out.println(
						"group qi:" + GruopAir(ChessPoint[CurrentMapPoint_X][CurrentMapPoint_Y].getvCurrentGroup()));
				// 重绘				
				renewChessMap();
				AddtoMemery(this.ChessMap, this.step, this.Turnflag);
				// AddtoMemery(ChessMap, ChessPoint,vAllGroup,CurrentRemovedGruop, step,

				repaint();

				showChessMap(ChessMap);

				if (Turnflag == 1)
					Turnflag = -1;
				else
					Turnflag = 1;

				return 1;
			}
			
			// 若落子所在组无气，判断落子周围是否有异色邻居组
			if (GruopAir(ChessPoint[CurrentMapPoint_X][CurrentMapPoint_Y].getvCurrentGroup()) == 0) {
				int aliveflag = 0;// 死气标签
				// 获取周围所有异色邻居组
				Vector vGroupunsamearound = getUnSamearound(CurrentMapPoint_X, CurrentMapPoint_Y);
				// 依次判断每个异色邻居组是否有气
				for (int i = 0; i < vGroupunsamearound.size(); i++) {

					Vector vunsamearound = (Vector) vGroupunsamearound.elementAt(i);// 当前异色邻居组
					// 如果当前异色邻居组无气，则将该异色邻居组从棋谱上提走
					if (GruopAir(vunsamearound) == 0) {
						for (int j = 0; j < vunsamearound.size(); j++) {
							int X, Y;
							Chess_B CH = (Chess_B) vunsamearound.elementAt(j);
							X = CH.getX_Map();
							Y = CH.getY_Map();
							setChessMap(X, Y, 0);// 提子
							ChessPoint[X][Y].setvCurrentGroup(null);
							aliveflag = 1;
						}					
					}

				}

				if (aliveflag == 0) {
					// 将落子移除
					// 从棋谱中移除
					setChessMap(CurrentMapPoint_X, CurrentMapPoint_Y, 0);
					System.out.println("无效下棋");
					if (step > 0)
						step--;
					showChessMap(ChessMap);
					// deletMap(AllMaplength);// 移除棋谱
					return -1;
				} else {
					renewChessMap();
					AddtoMemery(ChessMap, step, Turnflag);
					// AddtoMemery(ChessMap, ChessPoint,vAllGroup,CurrentRemovedGruop, step,

					repaint();
					showChessMap(ChessMap);
					if (Turnflag == 1)
						Turnflag = -1;
					else
						Turnflag = 1;
					return 1;
				}

			}

		}
		return 0;
	}

	// 添加记忆单元
	public void c(int[][] m_ChessMap, int m_step, int m_Turnflag) {
		this.MapMemery.addElement(new Memeryunit_B(m_ChessMap, m_step, m_Turnflag));

	}

	// 删除最新的记忆单元
	public void DeletMemery() {
		MapMemery.remove(MapMemery.elementAt(MapMemery.size()));

	}

	// 悔棋
	public int Back() {

		if (step == 0 || MapMemery.size() == 1) {
			System.out.println("无子可悔");
			return -1;
		}
		this.MapMemery.removeElementAt(MapMemery.size() - 1);
		Memeryunit_B memery = (Memeryunit_B) MapMemery.elementAt(MapMemery.size() - 1);
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				ChessMap[i][j] = memery.getM_ChessMap()[i][j];
			}
		}

		int step = memery.getM_step();
		repaint();
		showChessMap(ChessMap);
		if (step % 2 == 0)
			this.Turnflag = 1;
		else
			this.Turnflag = -1;
		this.step--;
		return 1;

		// System.out.println("MapMemerylengthNow " + this.MapMemery.size() + " " +
		// this.MapMemery);

	}

	// 打印棋谱
	public void showChessMap(int[][] Chessmap) {
		System.out.println("@" + Chessmap);
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (Chessmap[j][i] == 0)
					System.out.print(Chessmap[j][i] + "\t\t");
				else
					System.out.print(Chessmap[j][i] + "," + countAir(j, i) + "\t\t");
				if (j == ROWS - 1)
					System.out.println("");
			}

		}
		System.out.println("@" + Chessmap);
	}

	// 计算任意任意位置棋子气数
	public int countAir(int x, int y) {
		int Air = 4;
		for (int l = 0; l < xdir.length; l++) {
			int X = x + xdir[l];
			int Y = y + ydir[l];
			if (X < 0 || X > ROWS - 1 || Y < 0 || Y > ROWS - 1) {
				Air--;
				continue;
			} else if (ChessMap[X][Y] != 0)
				Air--;

		}
		return Air;

	}

	// 打印棋盘坐标
	public void showChessPointMap() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				System.out.print(ChessPoint[j][i].getX_Point() + "," + ChessPoint[j][i].getY_Point() + ","
						+ ChessPoint[j][i].getvCurrentGroup() + "\t");
				if (j == ROWS - 1) {
					System.out.println("  ");
				}
			}
		}
	}

	// 判断鼠标点击是否选中了棋盘上的点
	
	public int judgeonMapPoint(int x, int y) {
		// 计算中心坐标和中心周围四顶点坐标
//		x1     x2
//		    c
//		x4     x3
		int c1 = x / 30, c2 = y / 30;
		int x1 = c1 * 30, y1 = c2 * 30, // 正方形四角，左上
				x2 = (c1 + 1) * 30, y2 = c2 * 30, // 右上
				x3 = (c1 + 1) * 30, y3 = (c2 + 1) * 30, // 右下（基准点）
				x4 = c1 * 30, y4 = (c2 + 1) * 30, // 左下
				center_x = (x1 + x2) / 2, center_y = (y2 + y3) / 2;// 中心点
		System.out.println(center_x + " " + center_y);
// 判定鼠标坐标是否在合法范围
		if (x < center_x && y < center_y) {
			System.out.println("x1");
			if ((x > x1 - extent && x < x1 + extent) && (y > y1 - extent && y < y1 + extent)) {

				CurrentMapPoint_X = x1 / 30 - 1;
				CurrentMapPoint_Y = y1 / 30 - 1;
				return 1;
			} else {

				return 0;
			}
		}

		if (x > center_x && y < center_y) {
			System.out.println("x2");
			if ((x > x2 - extent && x < x2 + extent) && (y > y2 - extent && y < y2 + extent)) {
				System.out.println("OK");
				CurrentMapPoint_X = x2 / 30 - 1;
				CurrentMapPoint_Y = y2 / 30 - 1;
				return 1;
			} else {

				return 0;
			}

		}
		if (x > center_x && y > center_y) {
			System.out.println("x3");
			if ((x > x3 - extent && x < x3 + extent) && (y > y3 - extent && y < y3 + extent)) {
				System.out.println("OK");
				CurrentMapPoint_X = x3 / 30 - 1;
				CurrentMapPoint_Y = y3 / 30 - 1;
				return 1;
			} else {

				return 0;
			}
		}
		if (x < center_x && y > center_y) {
			System.out.println("x4");
			if ((x > x4 - extent && x < x4 + extent) && (y > y4 - extent && y < y4 + extent)) {
				System.out.println("OK");
				CurrentMapPoint_X = x4 / 30 - 1;
				CurrentMapPoint_Y = y4 / 30 - 1;
				return 1;
			} else {

				return 0;
			}
		}

		// System.out.println(CurrentMapPoint_X+","+CurrentMapPoint_Y);
		else
			return 0;
	}

	// 棋谱刷新函数，移走气为零的块
	public void renewChessMap() {
		// CurrentRemovedGruop.removeAllElements();
		for (int i = 0; i < vAllGroup.size(); i++) {
			Vector CurrentGruop = (Vector) vAllGroup.elementAt(i);

			if (GruopAir(CurrentGruop) == 0) {
				for (int j = 0; j < CurrentGruop.size(); j++) {
					Chess_B CH = (Chess_B) CurrentGruop.elementAt(j);
					int X = CH.getX_Map();
					int Y = CH.getY_Map();
					setChessMap(X, Y, 0);
					// ChessPoint[X][Y].setvCurrentGroup(null);
				}
				// CurrentRemovedGruop.addElement(CurrentGruop);
				// vAllGroup.remove(CurrentGruop);
			}
		}
	}

	// 修改棋谱
	public void setChessMap(int x, int y, int flag) {
		ChessMap[x][y] = flag;

	}

	// 获得指定位置棋子四周的异色邻居组
	public Vector getUnSamearound(int x, int y) {
		Vector v = new Vector();
		int X, Y;
		for (int l = 0; l < xdir.length; l++) {
			X = x + xdir[l];
			Y = y + ydir[l];
			if (X < 0 || X > ROWS - 1 || Y < 0 || Y > ROWS - 1)
				continue;
			if (ChessMap[X][Y] == -ChessMap[x][y])
				v.addElement(ChessPoint[X][Y].getvCurrentGroup());// Up

		}
		return v;
	}

// 获得指定位置棋子四周的同色邻居组
	public Vector getSamearound(int x, int y) {

		Vector v = new Vector();
		int X, Y;
		for (int l = 0; l < xdir.length; l++) {
			X = x + xdir[l];
			Y = y + ydir[l];
			if (X < 0 || X > ROWS - 1 || Y < 0 || Y > ROWS - 1)
				continue;
			if (ChessMap[X][Y] == ChessMap[x][y] && ChessPoint[X][Y].getvCurrentGroup() != null)
				v.addElement(ChessPoint[X][Y].getvCurrentGroup());// Up

		}
		return v;
	}

	// 计算邻居组的气
	public int GruopAir(Vector group) {
		int Air = 0;
		int x, y;
		Chess_B CH;
		for (int i = 0; i < group.size(); i++) {
			CH = (Chess_B) group.elementAt(i);
			x = CH.getX_Map();
			y = CH.getY_Map();
			Air = Air + countAir(x, y);
		}
		return Air;
	}

	// 为单个棋子寻找邻居组
	public void GenerateGruopforsinglechess(int x, int y) {
		// 当前位置无棋子
		if (ChessMap[x][y] == 0) {
			System.out.print("No Chesshere");
			return;
		}
		// 当前位置的棋子已经有邻居组
		if (ChessPoint[x][y].getvCurrentGroup() != null) {
			System.out.println("already has gruop" + ChessPoint[x][y].getvCurrentGroup());
			return;
		}

		// 遍历棋子四周，如果周围没有同色棋子则为当前棋子创建新的邻居组
		Vector samevaround = getSamearound(x, y);
		if (samevaround.size() == 0) {
			Vector vnewgroup = new Vector();
			vnewgroup.addElement(ChessPoint[x][y]);
			// 修改棋子邻居组标签
			ChessPoint[x][y].setvCurrentGroup(vnewgroup);
			// 将新的邻居组加入到总块中
			vAllGroup.addElement(vnewgroup);
			// System.out.println("Allgroup " + vAllGroup);
			// System.out.println("new Group" + vnewgroup);
		}
		// 若有邻居组则合并加入
		else if (samevaround.size() != 0) {
			// 获得第一个邻居组，并设为最终邻居组
			Vector finalneighbergroup = (Vector) samevaround.elementAt(0);
			// System.out.println("finalneighbergroup " + finalneighbergroup);
			// System.out.println("group size " + samevaround.size());
			finalneighbergroup.addElement(ChessPoint[x][y]);
			ChessPoint[x][y].setvCurrentGroup(finalneighbergroup);
			// System.out.println("AllgruopBefor" + vAllGroup);

			System.out.println((Chess_B) finalneighbergroup.elementAt(0));
			for (int i = 1; i < samevaround.size(); i++) {
				// 依次得到其余邻居组
				Vector neighbergroup = (Vector) samevaround.elementAt(i);
				if (neighbergroup == finalneighbergroup)
					continue;
				// 依次将其余邻居组内的棋子加入到最终邻居组中
				for (int j = 0; j < neighbergroup.size(); j++) {
					Chess_B CP = (Chess_B) neighbergroup.elementAt(j);
					finalneighbergroup.addElement(CP);
					// 修改棋子邻居组标签
					CP.setvCurrentGroup(finalneighbergroup);
				}
				// 将除最终邻居组外的邻居组从总块中删除

				vAllGroup.remove(neighbergroup);

			}
		}
	}

	// 新增方法：直接以棋谱坐标(row, col)和当前回合（turn）落子
	public int playChessAI(int row, int col, int turn) {
		// 检查目标位置是否有棋子
		if (ChessMap[row][col] != 0) {
			System.out.println("位置已有棋子: " + row + "," + col);
			return -1;
		}
		// 将棋谱更新
		setChessMap(row, col, turn);
		// 为所有棋子更新邻接组（这里使用原有逻辑）
		for (int i = 0; i < getROWS(); i++) {
			for (int j = 0; j < getROWS(); j++) {
				ChessPoint[i][j].setX_Map(i);
				ChessPoint[i][j].setY_Map(j);
				GenerateGruopforsinglechess(i, j);
			}
		}
		step++; // 增加手数

		// 有气，落子有效
		if (countAir(row, col) != 0) {
			renewChessMap();
			AddtoMemery(ChessMap, step, Turnflag);
			repaint();
			showChessMap(ChessMap);
			// 切换回合标记
			if (Turnflag == 1)
				Turnflag = -1;
			else
				Turnflag = 1;
			return 1;
		} else {
			// 若该着法无气，则按原有逻辑处理（例如捕捉对手棋子）
			// ...（这里可参考 playChess 方法中的逻辑）
			// 为简单起见，若无气则撤销此着法
			setChessMap(row, col, 0);
			System.out.println("AI 落子无气，不合法: " + row + "," + col);
			return -1;
		}
	}


	public int getROWS() {
		return ROWS;
	}

	public int getTurnflag() {
		return Turnflag;
	}

	public void setTurnflag(int turnflag) {
		Turnflag = turnflag;
	}

	public Chess_B[][] getChessPoint() {
		return ChessPoint;
	}

	public void setChessPoint(Chess_B[][] chessPoint) {
		ChessPoint = chessPoint;
	}

	public int[][] getChessMap() {
		return ChessMap;
	}

	public void setChessMap(int[][] chessMap) {
		ChessMap = chessMap;
	}

	private final int[][] starPoints = {
			{3, 3}, {9, 9}, {15, 15}, // 标准的星卫位置
			{3, 15}, {15, 3}, {9, 3}, {3, 9}, {15, 9}, {9, 15}
	};


}