package com.clover.connected.node.graph;

import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.clover.connected.node.constants.AppConstants.MAX_NODES;

@Component
public class Graph {

    private final int[] parentSet = new int[MAX_NODES+1]; // Store the parent of each vertex

    private final int[] sizeSet = new int[MAX_NODES+1]; // Stores the size of each set

    private static Graph graphInstance = null;

    /**
     * A private constructor to prevent object creation from outside
     * Following singleton pattern
     */
    private Graph() {
        /* Initialize all nodes as single set */
        for(int i = 1; i <= MAX_NODES; i++) {
            parentSet[i] = i;
            sizeSet[i] = 1;
        }
    }

    public static Graph getGraphInstance() {
        if (!Optional.ofNullable(graphInstance).isPresent()) {
            graphInstance = new Graph();
        }
        return graphInstance;
    }

    public int[] getParentSet() {
        return parentSet;
    }

    public int[] getSizeSet() {
        return sizeSet;
    }
}
