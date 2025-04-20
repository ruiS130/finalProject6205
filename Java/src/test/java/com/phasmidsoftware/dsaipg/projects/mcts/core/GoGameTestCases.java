package com.phasmidsoftware.dsaipg.projects.mcts.core;

import com.phasmidsoftware.dsaipg.projects.mcts.gogame.src.ChessBoardPanel_B;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class GoGameTestCases {
    private ChessBoardPanel_B board;

    @Before
    public void setUp() throws Exception {
        board = new ChessBoardPanel_B();
        board.newGame();
    }

    @Test
    public void TestNewGameState1(){
        int[][] map = board.getChessMap();
        for(int[] row : map) {
            for(int c : row) {
                assertEquals(0, c);
            }
        }
        assertEquals(1, board.getTurnflag());
        assertEquals(0, board.getStep());
    }

    @Test
    public void testInvalidMove(){
        int result = board.playChess(10,10);
        assertEquals(0, result);
    }

    @Test
    public void testMoveSurround(){
        board.getChessMap()[0][1] = 1;
        board.getChessMap()[1][0] = 1;
        board.getChessMap()[1][2] = 1;
        board.getChessMap()[2][1] = 1;
        board.setTurnflag(-1);

        int result = board.playChess(60,60);
        assertEquals(-1, result);
    }

//    @Test
//    public void testDrawCond(){
//        int[][] map = board.getChessMap();
//        for(int i = 0; i < map.length; i++) {
//            for (int j = 0; j < map[i].length; j++) {
//                map[i][j] = (i+j)%2 == 0 ? 1:-1;
//            }
//        }
//
//        int result = board.Winner();
//        assertEquals(0, result);
//    }

    @Test
    public void testCapture(){
        board.getChessMap()[0][1] = 1;
        board.getChessMap()[1][0] = 1;
        board.getChessMap()[1][2] = 1;
        board.setTurnflag(1);
        int result = board.playChess(60,90);
        assertEquals(0, board.getChessMap()[1][1]);
    }

}
