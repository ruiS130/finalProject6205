package com.phasmidsoftware.dsaipg.projects.mcts.gogame.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCSTAgent {

    private static final int ITERATION_LIMIT = 4000; // 模拟迭代次数
    private static final int MAX_SIMULATION_DEPTH = 100; // 模拟最大步数
    private static final double UCT_CONSTANT = 1.41; // UCT 探索系数

    // 定义状态类，用于保存棋谱和当前落子方（1：黑棋，-1：白棋）
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

        // 获取所有合法着法：空位的坐标 [i, j]
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

        // 判断终局：当没有合法着法时认为棋盘填满
        public boolean isTerminal() {
            return getLegalMoves().isEmpty();
        }

        // 应用着法，生成新状态，注意切换落子方
        public State applyMove(int[] move) {
            int[][] newBoard = cloneBoard(board);
            newBoard[move[0]][move[1]] = turn;
            return new State(newBoard, -turn);
        }

        // 简单评估函数：统计棋子数量，多者获胜（返回 1 表示黑棋优势，-1 表示白棋优势，0 表示平局）
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

    // 定义 MCTS 树节点
    public static class TreeNode {
        State state;
        TreeNode parent;
        List<TreeNode> children;
        int visits;
        double wins;
        List<int[]> untriedMoves;
        int[] move; // 从父节点的状态到本状态的着法

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

        // 计算 UCT 值
        public double getUCTValue() {
            if (visits == 0) return Double.MAX_VALUE;
            return (wins / visits) + UCT_CONSTANT * Math.sqrt(Math.log(parent.visits) / visits);
        }

        // 根据 UCT 值选择最佳子节点
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

    // 随机模拟，从当前状态开始，直至终局或达到最大步数
    // 返回结果：1 表示当前局面对黑棋有利，-1 表示白棋有利，0 表示平局
    public static int simulate(State s, int depth) {
        if (depth >= MAX_SIMULATION_DEPTH || s.isTerminal()) {
            return s.evaluate();
        }
        List<int[]> legalMoves = s.getLegalMoves();
        if (legalMoves.isEmpty()) return s.evaluate();
        Random rand = new Random();
        int[] move = legalMoves.get(rand.nextInt(legalMoves.size()));
        State nextState = s.applyMove(move);
        // 注意：结果反转，因为双方交替着棋
        return -simulate(nextState, depth + 1);
    }

    // nextMove 函数：根据当前棋谱和回合返回最佳落点 [row, col]
    public static int[] nextMove(int[][] currentmap, int turn) {
        State rootState = new State(currentmap, turn);
        TreeNode root = new TreeNode(rootState, null, null);
        Random rand = new Random();

        for (int i = 0; i < ITERATION_LIMIT; i++) {
            TreeNode node = root;
            State state = rootState;

            // 选择阶段：沿已完全扩展路径选择子节点
            while (node.isFullyExpanded() && !state.isTerminal()) {
                node = node.selectChild();
                state = state.applyMove(node.move);
            }

            // 扩展阶段：如果当前节点还有未试探的走法且状态未终局，则扩展一个新节点
            if (!node.untriedMoves.isEmpty() && !state.isTerminal()) {
                int index = rand.nextInt(node.untriedMoves.size());
                int[] move = node.untriedMoves.remove(index);
                state = state.applyMove(move);
                TreeNode child = new TreeNode(state, node, move);
                node.children.add(child);
                node = child;
            }

            // 模拟阶段：对当前状态进行随机走棋模拟
            int simulationResult = simulate(state, 0);

            // 回传阶段：沿路径更新节点统计，并反转结果
            while (node != null) {
                node.visits++;
                node.wins += simulationResult;
                simulationResult = -simulationResult;
                node = node.parent;
            }
        }

        // 选择访问次数最多的子节点作为最佳落法
        TreeNode bestChild = null;
        int maxVisits = -1;
        for (TreeNode child : root.children) {
            if (child.visits > maxVisits) {
                maxVisits = child.visits;
                bestChild = child;
            }
        }
        if (bestChild == null) {
            List<int[]> legal = rootState.getLegalMoves();
            return legal.get(rand.nextInt(legal.size()));
        }
        return bestChild.move;
    }
}
