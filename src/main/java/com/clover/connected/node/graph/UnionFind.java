package com.clover.connected.node.graph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnionFind {
    @Autowired
    Graph graph;


    /**
     * Function to find the representative of the set which contain node element
     */

    public int findSetRepresentative(int node) {
        if (node == graph.getParentSet()[node]) {
            return node;
        }

        // Path compression technique to optimize the time complexity
        return graph.getParentSet()[node] = findSetRepresentative(graph.getParentSet()[node]);
    }

    /**
     * Function to merge two different set into a single set by finding the
     * representative of each set and merge the smallest set with the larger one
     */
    public void unionSet(int nodeOne, int nodeTwo) {
        // Finding the set representative of each element
        nodeOne = findSetRepresentative(nodeOne);
        nodeTwo = findSetRepresentative(nodeTwo);

        // Check if they have different set representative
        if (nodeOne != nodeTwo) {
            // Compare the set sizes
            if (graph.getSizeSet()[nodeOne] < graph.getSizeSet()[nodeTwo]) {
                nodeOne = nodeOne + nodeTwo;
                nodeTwo = nodeOne - nodeTwo;
                nodeOne = nodeOne - nodeTwo;
            }

            // Assign parent of smaller set to the larger one
            graph.getParentSet()[nodeTwo] = nodeOne;

            // Add the size of smaller set to the larger one
            graph.getSizeSet()[nodeOne] += graph.getSizeSet()[nodeTwo];
        }
    }

    /**
     * Function to check the vertices are on the same set or not
     */
    public boolean checkNodeConnectivity(int nodeOne, int nodeTwo) {
        nodeOne = findSetRepresentative(nodeOne);
        nodeTwo = findSetRepresentative(nodeTwo);

        // Check if they have same set representative or not
        return (nodeOne == nodeTwo);
    }

}
