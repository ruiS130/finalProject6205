package com.phasmidsoftware.dsaipg.projects.mcts.gogame.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCSTAgent {

    private static final int ITERATION_LIMIT = 4000; // Number of simulation iterations
    private static final int MAX_SIMULATION_DEPTH = 100; // Maximum depth of each simulation
    private static final double UCT_CONSTANT = 1.41; // UCT exploration constant

    // Represents a game state: the board and the current player (1 for black, -1 for white)
    public static class State {
        int[][] board;
        int turn;

        public State(int[][] board, int turn) {
            this.board = cloneBoard(board);
            this.turn = turn;
        }

        private int[][] cloneBoard(int[][] board) {
            int rows = board.length;
            int cols = board[0].length;
            int[][] newBoard = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(board[i], 0, newBoard[i], 0, cols);
            }
            return newBoard;
        }

        // Get all legal moves: positions with 0 (empty)
        public List<int[]> getLegalMoves() {
            List<int[]> moves = new ArrayList<>();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 0) {
                        moves.add(new int[]{i, j});
                    }
                }
            }
            return moves;
        }

        // Check if the game has ended (no legal moves)
        public boolean isTerminal() {
            return getLegalMoves().isEmpty();
        }

        // Apply a move and return the new state (switch player)
        public State applyMove(int[] move) {
            int[][] newBoard = cloneBoard(board);
            newBoard[move[0]][move[1]] = turn;
            return new State(newBoard, -turn);
        }

        // Simple evaluation: return 1 if black wins, -1 if white wins, 0 for draw
        public int evaluate() {
            int black = 0, white = 0;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 1) black++;
                    else if (board[i][j] == -1) white++;
                }
            }
            if (black > white) return 1;
            else if (white > black) return -1;
            else return 0;
        }
    }

    // Represents a node in the MCTS tree
    public static class TreeNode {
        State state;
        TreeNode parent;
        List<TreeNode> children;
        int visits;
        double wins;
        List<int[]> untriedMoves;
        int[] move; // Move from parent state to current state

        public TreeNode(State state, TreeNode parent, int[] move) {
            this.state = state;
            this.parent = parent;
            this.move = move;
            this.children = new ArrayList<>();
            this.visits = 0;
            this.wins = 0.0;
            this.untriedMoves = state.getLegalMoves();
        }

        public boolean isFullyExpanded() {
            return untriedMoves.isEmpty();
        }

        // Calculate UCT value for node selection
        public double getUCTValue() {
            if (visits == 0) return Double.MAX_VALUE;
            return (wins / visits) + UCT_CONSTANT * Math.sqrt(Math.log(parent.visits) / visits);
        }

        // Select the child with the best UCT value
        public TreeNode selectChild() {
            TreeNode best = null;
            double bestValue = -Double.MAX_VALUE;
            for (TreeNode child : children) {
                double uct = child.getUCTValue();
                if (uct > bestValue) {
                    bestValue = uct;
                    best = child;
                }
            }
            return best;
        }
    }

    // Simulate a random game from state `s` until terminal state or depth limit.
    // Returns 1 if favorable to black, -1 if favorable to white, or 0 for draw.
    public static int simulate(State s, int depth) {
        if (depth >= MAX_SIMULATION_DEPTH || s.isTerminal()) {
            return s.evaluate();
        }
        List<int[]> legalMoves = s.getLegalMoves();
        if (legalMoves.isEmpty()) return s.evaluate();
        Random rand = new Random();
        int[] move = legalMoves.get(rand.nextInt(legalMoves.size()));
        State nextState = s.applyMove(move);
        // Reverse result since turns alternate
        return -simulate(nextState, depth + 1);
    }

    // Run MCTS to determine the best move from the current game state.
    // @param currentMap the current board
    // @param turn the current player (1 for black, -1 for white)
    // @return best move as [row, col]
    public static int[] nextMove(int[][] currentmap, int turn) {
        State rootState = new State(currentmap, turn);
        TreeNode root = new TreeNode(rootState, null, null);
        Random rand = new Random();

        for (int i = 0; i < ITERATION_LIMIT; i++) {
            TreeNode node = root;
            State state = rootState;

            // Selection: follow the fully expanded path
            while (node.isFullyExpanded() && !state.isTerminal()) {
                node = node.selectChild();
                state = state.applyMove(node.move);
            }

            // Expansion: if node not fully expanded and not terminal, expand one move
            if (!node.untriedMoves.isEmpty() && !state.isTerminal()) {
                int index = rand.nextInt(node.untriedMoves.size());
                int[] move = node.untriedMoves.remove(index);
                state = state.applyMove(move);
                TreeNode child = new TreeNode(state, node, move);
                node.children.add(child);
                node = child;
            }

            // Simulation: simulate random playout
            int simulationResult = simulate(state, 0);

            // Backpropagation: update visit/win stats up the tree
            while (node != null) {
                node.visits++;
                node.wins += simulationResult;
                simulationResult = -simulationResult;
                node = node.parent;
            }
        }

        // Select child with the most visits
        TreeNode bestChild = null;
        int maxVisits = -1;
        for (TreeNode child : root.children) {
            if (child.visits > maxVisits) {
                maxVisits = child.visits;
                bestChild = child;
            }
        }
        if (bestChild == null) {
            // Fallback: random move if MCTS failed
            List<int[]> legal = rootState.getLegalMoves();
            return legal.get(rand.nextInt(legal.size()));
        }
        return bestChild.move;
    }

    public static int getIterationLimit() {
        return ITERATION_LIMIT;
    }
}
