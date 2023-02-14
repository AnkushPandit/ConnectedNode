package com.clover.connected.node.graph;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.clover.connected.node.constants.AppConstants.MAX_NODES;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;


class UnionFindTest {

    @Mock
    private static Graph graph;

    @InjectMocks
    private UnionFind unionFind;

    private static final int[] mockedParentSet = new int[MAX_NODES+1];

    private static final int[] mockedSizeSet = new int[MAX_NODES+1];

    @BeforeAll
    static void initialize() {
        /* Initializing the mock graph object like the original scenario */
        for(int i = 1; i <= MAX_NODES; i++) {
            mockedParentSet[i] = i;
            mockedSizeSet[i] = 1;
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(graph.getParentSet()).thenReturn(mockedParentSet);
        when(graph.getSizeSet()).thenReturn(mockedSizeSet);
    }

    @Test
    @DisplayName("Test to check connection between two nodes that are not connected")
    public void testConnectivityFalseCase() {
        assertFalse("", unionFind.checkNodeConnectivity(2, 4));
    }

    @Test
    @DisplayName("Test to check connection between two nodes that are connected")
    public void testConnectivityTrueCase() {
        unionFind.connectNodes(2, 4);
        assertTrue("", unionFind.checkNodeConnectivity(2, 4));
    }

    @Test
    @DisplayName("Test to check connection between two nodes that are indirectly connected")
    public void testIndirectConnection() {
        unionFind.connectNodes(2, 4);
        unionFind.connectNodes(2, 7);
        unionFind.connectNodes(9, 7);
        assertTrue("", unionFind.checkNodeConnectivity(9, 4));
    }

    @Test
    @DisplayName("Test to validate new connection creation")
    public void testNewConnectionCreation() {
        assertFalse("", unionFind.checkNodeConnectivity(12, 14));
        unionFind.connectNodes(12, 14);
        unionFind.connectNodes(19, 17);
        assertFalse("", unionFind.checkNodeConnectivity(19, 14));
        unionFind.connectNodes(14, 19);
        assertTrue("", unionFind.checkNodeConnectivity(12, 17));
    }
}