/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jmetis;

/**
 *
 * @author dinhvan
 */
public class MetisStatus {
    
    /**
     * Returned normally
     */
    public static final int METIS_OK = 1;

    /**
     * Returned due to erroneous inputs and/or options
     */
    public static final int METIS_ERROR_INPUT = -2;

    /**
     * Returned due to insufficient memory
     */
    public static final int METIS_ERROR_MEMORY = -3;

    /**
     * Some other errors
     */
    public static final int METIS_ERROR = -4;
    
    /**
     * Returns a string representation of the given constant
     *
     * @param n returned status code of metis api
     * @return A string representation of the given constant
     */
    public static String stringFor(int n)
    {
        switch (n)
        {
            case METIS_OK: return "METIS_OK";
            case METIS_ERROR_INPUT: return "METIS_ERROR_INPUT";
            case METIS_ERROR_MEMORY: return "METIS_ERROR_MEMORY";
            case METIS_ERROR: return "METIS_ERROR";
        }
        return "INVALID cusparseStatus";
    }
}
