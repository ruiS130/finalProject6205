/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {

    public static void main(String[] args) {
        MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        Node<TicTacToe> root = mcts.root;

        int iterations = 1000;

        for (int i = 0; i < iterations; i++) {
            simulate(root);
        }


        for (Node<TicTacToe> child : root.children()) {
            System.out.println("Move:\n" + child.state());
            System.out.printf("Wins: %d, Playouts: %d, Win Rate: %.2f\n",
                    child.wins(), child.playouts(), child.playouts() > 0 ?
                            (double) child.wins() / (2 * child.playouts()) : 0.0);
        }


        Node<TicTacToe> best = root.children().stream()
                .max((a, b) -> Double.compare(
                        (double) a.wins() / a.playouts(),
                        (double) b.wins() / b.playouts()))
                .orElse(null);

        if (best != null) {
            System.out.println("Best move chosen:");
            System.out.println(best.state());
        } else {
            System.out.println("No best move found.");
        }
    }

        // This is where you process the MCTS to try to win the game.


    public MCTS(Node<TicTacToe> root) {
        this.root = root;
    }

    private final Node<TicTacToe> root;

    public static void simulate(Node<TicTacToe> node) {
        // Selection & Expansion
        if (!node.isLeaf()) {
            node.explore();
        }
    }

    public Node<TicTacToe> getRoot() {
        return root;
    }
}

