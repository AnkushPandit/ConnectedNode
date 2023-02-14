package com.clover.connected.node.controller;

import com.clover.connected.node.handler.NodeHandler;
import com.clover.connected.node.model.Edge;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;


class NodeControllerTest {
    @Mock
    private NodeHandler nodeHandler;

    @InjectMocks
    private NodeController nodeController;

    private static Edge validInput;

    private static Edge invalidInputLowerLimit;

    private static Edge invalidInputUpperLimit;

    @BeforeAll
    static void initialize() {
        validInput = new Edge(2, 4);
        invalidInputLowerLimit = new Edge(-2, 4);
        invalidInputUpperLimit = new Edge(3, (int)10e8);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test for invalid input smaller than lower limit")
    public void testForInvalidInputLowerLimit() {
        assertEquals("", HttpStatus.BAD_REQUEST,
                nodeController.addNodeConnection(invalidInputLowerLimit).getStatusCode());
    }

    @Test
    @DisplayName("Test for invalid input greater than upper limit")
    public void testForInvalidInputUpperLimit() {
        assertEquals("", HttpStatus.BAD_REQUEST,
                nodeController.getNodeConnectivity(invalidInputUpperLimit).getStatusCode());
    }

    @Test
    @DisplayName("Test when handler throws exception during POST method")
    public void testExceptionDuringNodeAddition() {
        when(nodeHandler.addNodes(validInput)).thenThrow(new RuntimeException());
        assertEquals("", HttpStatus.INTERNAL_SERVER_ERROR,
                nodeController.addNodeConnection(validInput).getStatusCode());
    }

    @Test
    @DisplayName("Test when handler throws exception during GET method")
    public void testExceptionWhileCheckingConnectivity() {
        when(nodeHandler.getConnection(validInput)).thenThrow(new RuntimeException());
        assertEquals("", HttpStatus.INTERNAL_SERVER_ERROR,
                nodeController.getNodeConnectivity(validInput).getStatusCode());
    }

    @Test
    @DisplayName("Test when nodes are not connected")
    public void testConnectivityNegativeCase() {
        when(nodeHandler.getConnection(validInput)).thenReturn(false);
        assertEquals("", HttpStatus.OK,
                nodeController.getNodeConnectivity(validInput).getStatusCode());
        assertEquals("", "Nodes are not connected",
                nodeController.getNodeConnectivity(validInput).getBody().toString());
    }

    @Test
    @DisplayName("Test when nodes are connected")
    public void testConnectivityHappyCase() {
        when(nodeHandler.getConnection(validInput)).thenReturn(true);
        assertEquals("", HttpStatus.OK,
                nodeController.getNodeConnectivity(validInput).getStatusCode());
        assertEquals("", "Nodes are connected",
                nodeController.getNodeConnectivity(validInput).getBody().toString());
    }

    @Test
    @DisplayName("Test when creating a new node connection is successful")
    public void testNodeConnectionHappyCase() {
        when(nodeHandler.addNodes(validInput)).thenReturn("Random Text");
        assertEquals("", HttpStatus.OK,
                nodeController.addNodeConnection(validInput).getStatusCode());
    }
}