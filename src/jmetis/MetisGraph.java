/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetis;

/**
 * Sparse graphs (or matrix) use the CSR format for storing for example a sparse
 * graph with 15 vertices and 22 edges 
 * 0 -1 -2 -3 -4 
 * |  |  |  |  | 
 * 5 -6 -7 -8 -9 
 * |  |  |  |  | 
 * 10-11-12-13-14 
 * xadj(15) = 0 2 5 8 11 13 16 20 24 28 31 33 36 39 42 44
 * adjncy(44) = 1 5 0 2 6 1 3 7 2 4 8 3 9 0 6 10 1 5 7 11 2 6 8 12 3 7 9 13 4 8
 * 14 5 11 6 10 12 7 11 13 8 12 14 9 13
 *
 * All of the graph partitioning and sparse matrix ordering routines in METIS
 * take as input the adjacency structure of the graph and the weights of the
 * vertices and edges (if any). The adjacency structure of the graph is stored
 * using the compressed storage format (CSR). The CSR format is a widely used
 * scheme for storing sparse graphs. In this format the adjacency structure of a
 * graph with n vertices and m edges is represented using two arrays xadj and
 * adjncy. The xadj array is of size n + 1 whereas the adjncy array is of size
 * 2m (this is because for each edge between vertices v and u we actually store
 * both (v; u) and (u; v)). The adjacency structure of the graph is stored as
 * follows. Assuming that vertex numbering starts from 0 (C style), then the
 * adjacency list of vertex i is stored in array adjncy starting at index
 * xadj[i] and ending at (but not including) index xadj[i + 1] (i.e.,
 * adjncy[xadj[i]] through and including adjncy[xadj[i + 1]-1]). That is, for
 * each vertex i, its adjacency list is stored in consecutive locations in the
 * array adjncy, and the array xadj is used to point to where it begins and
 * where it ends.
 *
 * @author dinhvan
 */
public class MetisGraph {

    /**
     * number of vertices
     */
    private int n;
    /**
     * number of edges (single-branch)
     */
    private int m;
    /**
     * pointer to adjacent list, size of (n+1)
     */
    private int[] xadj;
    /**
     * adjacent list ID, size of (2*m)
     */
    private int[] adjncy;
    /**
     * the weights of the vertices is greater or equal to zero, size = n*ncon;
     * The weights of the ith vertex are stored in ncon consecutive entries
     * starting at location vwgt[i ∗ ncon]; if each vertex has only a single
     * weight, then vwgt will contain n elements, and vwgt[i] will store the
     * weight of the ith vertex; The vertex-weights must be integers greater or
     * equal to zero; If all the vertices of the graph have the same weight
     * (i.e., the graph is unweighted), then the vwgt can be set to NULL
     */
    private int[] vwgt;
    /**
     * the number of weights associated with each vertex
     */
    private int ncon;
    /**
     * The weights of the edges; This array contains 2m elements, and the weight
     * of edge adjncy[j] is stored at location adjwgt[j]; The edge-weights must
     * be integers greater than zero, If all the edges of the graph have the
     * same weight (i.e., the graph is unweighted), then the adjwgt can be set
     * to NULL
     */
    private int[] adjwgt;
    
    public MetisGraph(int numVertices, int numEdge){
        this.n = numVertices;
        this.m = numEdge;
        this.xadj = new int[n+1];
        this.adjncy = new int[2*m];
        this.ncon = 1;
        this.adjwgt = null;
        this.vwgt = null;
    }
    public MetisGraph(int[] verticesPtr, int[] adjacentId){
        if(verticesPtr.length < 2 | adjacentId.length < 2 | adjacentId.length % 2 != 0)
            throw new NullPointerException("Can not create a graph");
        this.n = verticesPtr.length-1;
        this.m = adjacentId.length / 2;
        this.xadj = verticesPtr;
        this.adjncy = adjacentId;
        this.ncon = 1;
        this.adjwgt = null;
        this.vwgt = null;
    }
    public int getNumberOfVertices(){
        return this.n;
    }
    public int getNumberOfEdges(){
        return this.m;
    }
    public int[] getVerticesPointer(){
        return this.xadj;
    }
    public int[] getVerticesAdjacentsId()
    {
        return this.adjncy;
    }
    public int getNumberOfVerticesAttributes(){
        return this.ncon;
    }
    public int[] getVerticesWeights(){
        return this.vwgt;
    }
    public int[] getEdgesWeights(){
        return this.adjwgt;
    }
    public void setVerticesWeights(int[] verticesWeights){
        if(verticesWeights.length % n != 0)
            throw new UnsupportedOperationException("Can not set vertices weights " + verticesWeights.length);
        
        this.ncon = verticesWeights.length % n;
        this.vwgt = verticesWeights;
    }
    public void setEdgesWeights(int[] edgesWeights){
        if(edgesWeights.length != 2*m)
            throw new UnsupportedOperationException("Can not set edges weights " + edgesWeights.length);
        this.adjwgt = edgesWeights;
    }
    
}
