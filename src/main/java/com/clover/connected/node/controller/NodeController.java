package com.clover.connected.node.controller;

import com.clover.connected.node.handler.NodeHandler;
import com.clover.connected.node.model.Edge;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.clover.connected.node.constants.AppConstants.MAX_NODES;

@RestController
@RequestMapping("/node")
@Service
public class NodeController {

    @Autowired
    NodeHandler nodeHandler;

    /**
     * Connects two nodes given as parameter, ignore if already exists
     * @param edge consists of two nodes to be connected
     * @return operation status with appropriate message
     */
    @PostMapping(path = "/add",
            produces = "application/json",
            consumes = "application/json")
    private ResponseEntity<Object> addNodeConnection(@RequestBody Edge edge) {
        try {
            if(!checkEdgeValidity(edge)) {
                return ResponseEntity.badRequest().
                        body(String.format("Node value must be between 1 and %s", MAX_NODES));
            }
            String result = nodeHandler.addNodes(edge);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Checks if the given nodes are connected
     * @param edge an edge consists of two nodes for which connectivity is to be checked
     * @return request status with appropriate message
     */
    @GetMapping(path = "/get",
            produces = "application/json",
            consumes = "application/json")
    private ResponseEntity<Object> getNodeConnectivity(@RequestBody Edge edge) {
        try {
            if(!checkEdgeValidity(edge)) {
                return ResponseEntity.badRequest().
                        body(String.format("Node value must be between 1 and %s", MAX_NODES));
            }
            boolean isConnected = nodeHandler.getConnection(edge);
            if(isConnected) {
                return ResponseEntity.ok().body("Nodes are connected");
            } else {
                return ResponseEntity.ok().body("Nodes are not connected");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    private boolean checkEdgeValidity(@NonNull Edge edge) {
        int nodeOne = edge.getNodeOne();
        int nodeTwo = edge.getNodeTwo();
        /* A node value must be between 1 and MAX_NODES */
        return (nodeOne > 0 && nodeTwo > 0 &&
                nodeOne <= MAX_NODES && nodeTwo <= MAX_NODES);
    }
}
