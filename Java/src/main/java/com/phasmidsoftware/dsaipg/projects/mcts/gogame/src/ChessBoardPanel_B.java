package com.phasmidsoftware.dsaipg.projects.mcts.gogame.src;

import java.awt.*;
import java.util.Vector;

import javax.swing.JPanel;

// Chessboard panel class
public class ChessBoardPanel_B extends JPanel {

	private Vector MapMemery = new Vector(); // Memory array for storing states

	// Chessboard parameters
	// Fixed parameters
	public final static int extent = 10;// Mouse precision
	public static int[] xdir = { 0, 0, 1, -1 };
	public static int[] ydir = { 1, -1, 0, 0 };
	public Image boardImg, blackImg, whiteImg;// Board, black piece, white piece
	private final int ROWS = 19;// Number of rows/columns
	private final int margin = 30;// Line margin
	private int span;// Board spacing
	private int Work = 1;// Whether move is valid


	///////////////////////////////////////// Info block ////////////////////////////////////////
	private Vector vAllGroup = new Vector(); // All groups
	private Vector CurrentRemovedGruop = new Vector(); // Currently removed neighbor group

	private int[][] ChessMap; // Board coordinates
	private Chess_B[][] ChessPoint = new Chess_B[ROWS][ROWS]; // Board coordinate objects
	private int blackSize = 0, whiteSize = 0; // Black and white territory counts

	private int step = 0; // Step counter

	private int Turnflag = 1; // Turn flag
	private int CurrentMapPoint_X, CurrentMapPoint_Y; // Mouse-click grid position, -1 = invalid
	////////////////////////////////////////// Info block ////////////////////////////////////////

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

		g.drawImage(boardImg, 0, 0, 600, 600, null);// Draw the background image

		int span_x = (y_low - y_high) / (ROWS - 1);// Board spacing - x-axis
		int span_y = (x_right - x_left) / (ROWS - 1);// Board spacing - y-axis
		span = span_x;
		int x1 = margin, y1 = margin, x2 = margin, y2 = margin;// Coordinates for drawing

		// Draw horizontal lines
		for (int i = 0; i < ROWS; i++) {
			x1 = x_left;
			x2 = x_right;
			y1 = y2 = margin + i * span_y;
			g.drawLine(x1, y1, x2, y2);
		}
		// Draw vertical lines
		for (int i = 0; i < ROWS; i++) {
			y1 = margin;
			y2 = Height - margin;
			x1 = x2 = margin + i * span_x;
			g.drawLine(x1, y1, x2, y2);
		}

		// Draw star point positions
		g.setColor(Color.BLACK);  // Star point marker color
		for (int[] star : starPoints) {
			int x = star[0], y = star[1];
			int starX = ChessPoint[x][y].getX_Point();
			int starY = ChessPoint[x][y].getY_Point();
			g.fillOval(starX - 5, starY - 5, 10, 10); // Draw black circle to mark star point
		}

		// Draw the chessboard state based on the game record
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (ChessMap[j][i] != 0) {
					if (ChessMap[j][i] == 1) { // Black piece
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
					if (ChessMap[j][i] == -1) { // White piece
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

	// Initialize the board
	public ChessBoardPanel_B() {

		boardImg = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/boardbackground.jpg"));
		whiteImg = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/white.png"));
		blackImg = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/black.png"));
		ChessMap = new int[ROWS][ROWS];
		// Initialize the board map
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				ChessMap[i][j] = 0;
			}
		}
		// Generate board coordinates
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {

				ChessPoint[i][j] = new Chess_B((i + 1) * 30, (j + 1) * 30);
				ChessPoint[i][j].setX_Map(i);
				ChessPoint[i][j].setY_Map(j);

			}
		}
		MapMemery.removeAllElements();
		vAllGroup.removeAllElements();
		// Print to console
		showChessPointMap();
		AddtoMemery(ChessMap, step, Turnflag);
		
	}


	// Place a piece
	public int playChess(int x, int y) {// Receives coordinates from the current mouse click
		vAllGroup.removeAllElements();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {
				ChessPoint[i][j].setvCurrentGroup(null);

			}
		}
		// Check if the clicked position is valid
		// Invalid move
		if (judgeonMapPoint(x, y) == 0) {
			System.out.println("return");
			return 0;
		}
		System.out.println(CurrentMapPoint_X + "," + CurrentMapPoint_Y);

		// A piece already exists at the move location
		if (ChessMap[CurrentMapPoint_X][CurrentMapPoint_Y] != 0) {
			System.out.println("alreadyhaspoint:  " + CurrentMapPoint_X + "," + CurrentMapPoint_Y);
			return -1;
		}

		// Temporarily update the board map
		setChessMap(CurrentMapPoint_X, CurrentMapPoint_Y, Turnflag);
		// Find neighbor groups for each piece
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < ROWS; j++) {

				ChessPoint[i][j].setX_Map(i);
				ChessPoint[i][j].setY_Map(j);
				GenerateGruopforsinglechess(i, j);
			}
		}
		step++;// Increase move count
		// addMap();
		// If the piece has liberties, it's a valid move
		if (countAir(CurrentMapPoint_X, CurrentMapPoint_Y) != 0) {
			System.out.println("qi:" + countAir(CurrentMapPoint_X, CurrentMapPoint_Y));

			renewChessMap();
			AddtoMemery(ChessMap, step, Turnflag);
			repaint();

			showChessMap(ChessMap);

			if (Turnflag == 1)
				Turnflag = -1;
			else
				Turnflag = 1;

			return 1;

		}
		// If the placed piece has no liberties
		if (countAir(CurrentMapPoint_X, CurrentMapPoint_Y) == 0) {

			// Find the neighbor group of the placed piece
			// If the group has liberties
			if (GruopAir(ChessPoint[CurrentMapPoint_X][CurrentMapPoint_Y].getvCurrentGroup()) != 0) {

				System.out.println(
						"group qi:" + GruopAir(ChessPoint[CurrentMapPoint_X][CurrentMapPoint_Y].getvCurrentGroup()));
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

			// If the group has no liberties, check for enemy groups around
			if (GruopAir(ChessPoint[CurrentMapPoint_X][CurrentMapPoint_Y].getvCurrentGroup()) == 0) {
				int aliveflag = 0;// Dead stone flag
				// Get all enemy neighbor groups
				Vector vGroupunsamearound = getUnSamearound(CurrentMapPoint_X, CurrentMapPoint_Y);
				// Check if any enemy neighbor groups have liberties
				for (int i = 0; i < vGroupunsamearound.size(); i++) {

					Vector vunsamearound = (Vector) vGroupunsamearound.elementAt(i);// Current enemy neighbor group
					// If enemy group has no liberties, remove it from the board
					if (GruopAir(vunsamearound) == 0) {
						for (int j = 0; j < vunsamearound.size(); j++) {
							int X, Y;
							Chess_B CH = (Chess_B) vunsamearound.elementAt(j);
							X = CH.getX_Map();
							Y = CH.getY_Map();
							setChessMap(X, Y, 0);// Capture
							ChessPoint[X][Y].setvCurrentGroup(null);
							aliveflag = 1;
						}					
					}

				}

				if (aliveflag == 0) {
					// Remove the placed piece (suicide move)
					setChessMap(CurrentMapPoint_X, CurrentMapPoint_Y, 0);
					System.out.println("无效下棋");
					if (step > 0)
						step--;
					showChessMap(ChessMap);
					// deletMap(AllMaplength)
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

	// Add memory unit
	public void c(int[][] m_ChessMap, int m_step, int m_Turnflag) {
		this.MapMemery.addElement(new Memeryunit_B(m_ChessMap, m_step, m_Turnflag));

	}

	// Delete the most recent memory unit
	public void DeletMemery() {
		MapMemery.remove(MapMemery.elementAt(MapMemery.size()));

	}

	// Undo move
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

	// Print the board state
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

	// Count liberties at a given position
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

	// Print board coordinate info
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

	// Determine if a mouse click hit a valid board point

	
	public int judgeonMapPoint(int x, int y) {
		// Compute the center and corners of the clicked square
//		x1     x2
//		    c
//		x4     x3
		int c1 = x / 30, c2 = y / 30;
		int x1 = c1 * 30, y1 = c2 * 30, // Top-left corner of the square
				x2 = (c1 + 1) * 30, y2 = c2 * 30, // Top-right corner
				x3 = (c1 + 1) * 30, y3 = (c2 + 1) * 30, // Bottom-right corner (reference point)
				x4 = c1 * 30, y4 = (c2 + 1) * 30, // Bottom-left corner
				center_x = (x1 + x2) / 2, center_y = (y2 + y3) / 2;// Center point of the square
		System.out.println(center_x + " " + center_y);
		// Check which quadrant the click is in and validate
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

	// Refresh board state and remove groups with no liberties
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

	// Update board map with a new move
	public void setChessMap(int x, int y, int flag) {
		ChessMap[x][y] = flag;

	}

	// Get enemy groups adjacent to a piece
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

	// Get enemy groups adjacent to a piece
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

	// Count liberties for a group
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

	// Assign a group to a single stone
	public void GenerateGruopforsinglechess(int x, int y) {
		// No stone at current position
		if (ChessMap[x][y] == 0) {
			System.out.print("No Chesshere");
			return;
		}
		// Stone already has a group
		if (ChessPoint[x][y].getvCurrentGroup() != null) {
			System.out.println("already has gruop" + ChessPoint[x][y].getvCurrentGroup());
			return;
		}

		// Check surroundings: if no same-colored neighbors, create a new group
		Vector samevaround = getSamearound(x, y);
		if (samevaround.size() == 0) {
			Vector vnewgroup = new Vector();
			vnewgroup.addElement(ChessPoint[x][y]);
			// Set the group reference for the stone
			ChessPoint[x][y].setvCurrentGroup(vnewgroup);
			// Add the new group to the list of all groups
			vAllGroup.addElement(vnewgroup);
			// System.out.println("Allgroup " + vAllGroup);
			// System.out.println("new Group" + vnewgroup);
		}
		// If there are existing neighbor groups, merge them
		else if (samevaround.size() != 0) {
			// Use the first neighbor group as the final group
			Vector finalneighbergroup = (Vector) samevaround.elementAt(0);
			// System.out.println("finalneighbergroup " + finalneighbergroup);
			// System.out.println("group size " + samevaround.size());
			finalneighbergroup.addElement(ChessPoint[x][y]);
			ChessPoint[x][y].setvCurrentGroup(finalneighbergroup);
			// System.out.println("AllgruopBefor" + vAllGroup);

			System.out.println((Chess_B) finalneighbergroup.elementAt(0));
			for (int i = 1; i < samevaround.size(); i++) {
				// Get each remaining neighbor group
				Vector neighbergroup = (Vector) samevaround.elementAt(i);
				if (neighbergroup == finalneighbergroup)
					continue;
				// Add each stone from the neighbor group into the final group
				for (int j = 0; j < neighbergroup.size(); j++) {
					Chess_B CP = (Chess_B) neighbergroup.elementAt(j);
					finalneighbergroup.addElement(CP);
					// Update group reference
					CP.setvCurrentGroup(finalneighbergroup);
				}
				// Remove merged group from the group list

				vAllGroup.remove(neighbergroup);

			}
		}
	}

	// New method: place a move directly using board coordinates (row, col) and turn
	public int playChessAI(int row, int col, int turn) {
		// Check if the target location already has a stone/
		if (ChessMap[row][col] != 0) {
			System.out.println("位置已有棋子: " + row + "," + col);
			return -1;
		}
		// Update board state
		setChessMap(row, col, turn);
		// Update groups for all stones (using existing logic)
		for (int i = 0; i < getROWS(); i++) {
			for (int j = 0; j < getROWS(); j++) {
				ChessPoint[i][j].setX_Map(i);
				ChessPoint[i][j].setY_Map(j);
				GenerateGruopforsinglechess(i, j);
			}
		}
		step++; // Increase move count

		// If the stone has liberties, the move is valid
		if (countAir(row, col) != 0) {
			renewChessMap();
			AddtoMemery(ChessMap, step, Turnflag);
			repaint();
			showChessMap(ChessMap);
			// Switch turns
			if (Turnflag == 1)
				Turnflag = -1;
			else
				Turnflag = 1;
			return 1;
		} else {
			// If move has no liberties, treat it as invalid (simplified)
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
			{3, 3}, {9, 9}, {15, 15}, // Star points for standard 19x19 board
			{3, 15}, {15, 3}, {9, 3}, {3, 9}, {15, 9}, {9, 15}
	};


}