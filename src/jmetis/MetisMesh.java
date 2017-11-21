/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetis;

/**
 * All of the mesh partitioning and mesh conversion routines in METIS take as
 * input the element node array of a mesh. This element node array is stored
 * using a pair of arrays called eptr and eind, which are similar to the xadj
 * and adjncy arrays used for storing the adjacency structure of a graph. The
 * size of the eptr array is n + 1, where n is the number of elements in the
 * mesh. The size of the eind array is of size equal to the sum of the number of
 * nodes in all the elements of the mesh. The list of nodes belonging to the ith
 * element of the mesh are stored in consecutive locations of eind starting at
 * position eptr[i] up to (but not including) position eptr[i+1].
 *
 * @author dinhvan
 */
public class MetisMesh {

    /**
     * the number of elements in the mesh
     */
    private int n;
    /**
     * The size of the eptr array is n + 1
     */
    private int[] eptr;
    /**
     * The list of nodes belonging to the ith element of the mesh are stored in
     * consecutive locations of eind starting at position eptr[i] up to (but not
     * including) position eptr[i+1]
     */
    private int[] eind;
    
    public MetisMesh(int numElt){
        this.n = numElt;
        this.eptr = new int[n+1];
        this.eind = null;   // unknown size
    }
    
    public MetisMesh(int[] ept, int[] e2n){
        if(ept.length < 3 | ept[ept.length-1]!=e2n.length)
            throw new NullPointerException("Can not create a mesh");
        else{
            this.n = ept.length - 1;
            this.eptr = ept;
            this.eind = e2n;
        }
            
    }
}
