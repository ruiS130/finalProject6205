package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MCTSTest {

    @Test
    public void testStartStateIsNotTerminal() {
        State<TicTacToe> state = new TicTacToe().start();
        assertFalse(state.isTerminal());
    }

    @Test
    public void testInitialPlayerIsX() {
        State<TicTacToe> state = new TicTacToe().start();
        assertEquals(TicTacToe.X, state.player());
    }

    @Test
    public void testNodeIsLeafWhenTerminal() {
        Position position = Position.parsePosition("X X X\nO O .\n. . .", TicTacToe.X);
        TicTacToe.TicTacToeState terminalState = new TicTacToe().new TicTacToeState(position);
        Node<TicTacToe> node = new TicTacToeNode(terminalState);
        assertTrue(node.isLeaf());
    }

    @Test
    public void testExploreAddsChildren() {
        Node<TicTacToe> root = new TicTacToeNode(new TicTacToe().new TicTacToeState());
        assertTrue(root.children().isEmpty());
        root.explore();
        assertFalse(root.children().isEmpty());
    }

    @Test
    public void testExploreBackPropagatesCorrectly() {
        Node<TicTacToe> root = new TicTacToeNode(new TicTacToe().new TicTacToeState());
        root.explore();
        int totalPlayouts = root.children().stream().mapToInt(Node::playouts).sum();
        assertEquals(root.playouts(), totalPlayouts);
    }

//    @Test
//    public  void testMCTSFindsBestMove() {
//        MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
//        Node<TicTacToe> root = mcts.getRoot();
//        for (int i = 0; i < 100; i++) MCTS.simulate(root);
//        Optional<Node<TicTacToe>> best = root.children().stream()
//                .max((a, b) -> Double.compare(
//                        (double) a.wins() / a.playouts(),
//                        (double) b.wins() / b.playouts()));
//        assertTrue(best.isPresent());
//    }
//
//    @Test
//    public  void testSimulateMultipleTimes() {
//        Node<TicTacToe> root = new TicTacToeNode(new TicTacToe().new TicTacToeState());
//        for (int i = 0; i < 50; i++) MCTS.simulate(root);
//        assertTrue(root.playouts() > 0);
//    }

    @Test
    public  void testDrawStateScore() {
        Position draw = Position.parsePosition("X O X\nX O O\nO X X", TicTacToe.X);
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState(draw);
        Node<TicTacToe> node = new TicTacToeNode(state);
        assertTrue(state.isTerminal());
        assertEquals(1, node.wins()); // draw worth 1 point
    }

    @Test
    public  void testWinnerDetection() {
        Position win = Position.parsePosition("O O O\n. X X\n. . .", TicTacToe.O);
        TicTacToe.TicTacToeState state = new TicTacToe().new TicTacToeState(win);
        assertTrue(state.winner().isPresent());
        assertEquals(Optional.of(TicTacToe.O), state.winner());
    }

    @Test
    public  void testGameCanReachEnd() {
        TicTacToe game = new TicTacToe();
        State<TicTacToe> state = game.start();
        int steps = 0;
        while (!state.isTerminal() && steps < 20) {
            state = state.next(state.chooseMove(state.player()));
            steps++;
        }
        assertTrue(state.isTerminal());
    }
}
