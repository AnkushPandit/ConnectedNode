package com.clover.connected.node.handler;

import com.clover.connected.node.graph.UnionFind;
import com.clover.connected.node.model.Edge;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;


class NodeHandlerTest {

    @Mock
    private UnionFind unionFind;

    @InjectMocks
    private NodeHandler nodeHandler;

    private static final Edge validInput = new Edge(2, 4);

    private final int nodeOne = validInput.getNodeOne();

    private final int nodeTwo = validInput.getNodeTwo();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test when null parameter is received")
    void testNullParameter() {
        Assertions.assertThrows(RuntimeException.class, () -> nodeHandler.addNodes(null));
    }

    @Test
    @DisplayName("Test creating a new connection when connection already exists")
    void testEdgeCreationWhenAlreadyExists() {
        when(unionFind.checkNodeConnectivity(nodeOne, nodeTwo)).thenReturn(true);
        assertEquals("", "Nodes are already connected",
                nodeHandler.addNodes(validInput));
        Mockito.verify(unionFind, times(0)).connectNodes(nodeOne, nodeTwo);
    }

    @Test
    @DisplayName("Test creation of a new connection is successful")
    void testEdgeCreationHappyCase() {
        when(unionFind.checkNodeConnectivity(nodeOne, nodeTwo)).thenReturn(false);
        doNothing().when(unionFind).connectNodes(nodeOne, nodeTwo);
        assertEquals("", "Connection between nodes have been created successfully",
                nodeHandler.addNodes(validInput));
        Mockito.verify(unionFind, times(1)).connectNodes(nodeOne, nodeTwo);
    }

    @Test
    @DisplayName("Test Exception handling while create new connection")
    void testExceptionHandlingWhileNewConnection() {
        when(unionFind.checkNodeConnectivity(nodeOne, nodeTwo)).thenReturn(false);
        doThrow(new RuntimeException()).when(unionFind).connectNodes(nodeOne, nodeTwo);
        Assertions.assertThrows(RuntimeException.class, () -> nodeHandler.addNodes(validInput));
    }

    @Test
    @DisplayName("Test checking existing connection")
    void testConnectionCheckingHappyCase() {
        when(unionFind.checkNodeConnectivity(nodeOne, nodeTwo)).thenReturn(true);
        assertTrue("", nodeHandler.getConnection(validInput));
    }

    @Test
    @DisplayName("Test Exception handling while checking existing connection")
    void testExceptionHandlingWhileConnectionChecking() {
        when(unionFind.checkNodeConnectivity(nodeOne, nodeTwo)).thenThrow(new RuntimeException());
        Assertions.assertThrows(RuntimeException.class, () -> nodeHandler.getConnection(validInput));
    }
}