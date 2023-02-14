package com.clover.connected.node.handler;

import com.clover.connected.node.graph.UnionFind;
import com.clover.connected.node.model.Edge;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NodeHandler {

    private final String messageForDuplicateEdge = "Nodes are already connected";

    private final String messageForSuccessfulConnections =  "Connection between nodes have been created successfully";

    @Autowired
    UnionFind unionFind;

    public String addNodes(@NonNull Edge edge) {
        try {
            int nodeOne = edge.getNodeOne();
            int nodeTwo = edge.getNodeTwo();
            if (unionFind.checkNodeConnectivity(nodeOne, nodeTwo)) {
                return messageForDuplicateEdge;
            }
            unionFind.unionSet(nodeOne, nodeTwo);
            return messageForSuccessfulConnections;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException("Error occurred while processing nodes");
        }
    }

    public boolean getConnection(@NonNull Edge edge) {
        try {
            return unionFind.checkNodeConnectivity(edge.getNodeOne(), edge.getNodeTwo());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException("Error occurred while checking node connectivity");
        }
    }
}
