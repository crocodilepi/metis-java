/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetis;

import java.util.Arrays;

/**
 * to manage options of MetisAPI
 *
 * @author dinhvan
 */
public class MetisOptions {

    public MetisOptions() {}

    public int[] getDefaultOptions() {
        int[] defaultValue = new int[METIS_NOPTIONS];
        Arrays.fill(defaultValue, -1);
        return defaultValue;
    }

    public int[] getOptions() {
        int[] options = new int[METIS_NOPTIONS];
        Arrays.fill(options, -1);
        options[0] = METIS_OPTION_PTYPE;
        options[1] = METIS_OPTION_OBJTYPE;
        options[2] = METIS_OPTION_CTYPE;
        options[3] = METIS_OPTION_IPTYPE;
        options[4] = METIS_OPTION_RTYPE;
        options[5] = METIS_OPTION_DBGLVL;
        options[6] = METIS_OPTION_NITER;
        options[7] = METIS_OPTION_NCUTS;
        options[8] = METIS_OPTION_SEED;
        options[9] = METIS_OPTION_NO2HOP;
        options[10] = METIS_OPTION_MINCONN;
        options[11] = METIS_OPTION_CONTIG;
        options[12] = METIS_OPTION_COMPRESS;
        options[13] = METIS_OPTION_CCORDER;
        options[14] = METIS_OPTION_PFACTOR;
        options[15] = METIS_OPTION_NSEPS;
        options[16] = METIS_OPTION_UFACTOR;
        options[17] = METIS_OPTION_NUMBERING;
        options[18] = METIS_OPTION_HELP;
        options[19] = METIS_OPTION_TPWGTS;
        options[20] = METIS_OPTION_NCOMMON;
        options[21] = METIS_OPTION_NOOUTPUT;
        options[22] = METIS_OPTION_BALANCE;
        options[23] = METIS_OPTION_GTYPE;
        options[24] = METIS_OPTION_UBVEC;
        return options;
    }

    /* The maximum length of the options[] array */
    static final int METIS_NOPTIONS = 40;

    /**
     * options[METIS OPTION PTYPE]
     *
     * Specifies the partitioning method. Possible values are:
     *
     * METIS PTYPE RB Multilevel recursive bisectioning.
     *
     * METIS PTYPE KWAY Multilevel k-way partitioning.
     *
     */
    public int METIS_OPTION_PTYPE = METIS_PTYPE_RB;
    /**
     * options[METIS OPTION OBJTYPE]
     *
     * Specifies the type of objective. Possible values are:
     *
     * METIS OBJTYPE CUT Edge-cut minimization.
     *
     * METIS OBJTYPE VOL Total communication volume minimization.
     *
     */
    public int METIS_OPTION_OBJTYPE = METIS_OBJTYPE_CUT;
    /**
     * options[METIS OPTION CTYPE]
     *
     * Specifies the matching scheme to be used during coarsening. Possible
     * values are:
     *
     * METIS CTYPE RM Random matching.
     *
     * METIS CTYPE SHEM Sorted heavy-edge matching.
     *
     */
    public int METIS_OPTION_CTYPE = METIS_CTYPE_RM;
    /**
     * options[METIS OPTION IPTYPE]
     *
     * Determines the algorithm used during initial partitioning. Possible
     * values are:
     *
     * METIS IPTYPE GROW Grows a bisection using a greedy strategy.
     *
     * METIS IPTYPE RANDOM Computes a bisection at random followed by a
     * refinement.
     *
     * METIS IPTYPE EDGE Derives a separator from an edge cut.
     *
     * METIS IPTYPE NODE Grow a bisection using a greedy node-based strategy.
     *
     */
    public int METIS_OPTION_IPTYPE = METIS_IPTYPE_GROW;

    /**
     * options[METIS OPTION RTYPE]
     *
     * Determines the algorithm used for refinement. Possible values are:
     *
     * METIS RTYPE FM FM-based cut refinement.
     *
     * METIS RTYPE GREEDY Greedy-based cut and volume refinement.
     *
     * METIS RTYPE SEP2SIDED Two-sided node FM refinement.
     *
     * METIS RTYPE SEP1SIDED One-sided node FM refinement.
     */
    public int METIS_OPTION_RTYPE = METIS_RTYPE_GREEDY;
    /**
     * options[METIS OPTION DBGLVL]
     *
     * Specifies the amount of progress/debugging information will be printed
     * during the execution of the algo- rithms. The default value is 0 (no
     * debugging/progress information). A non-zero value can be supplied that is
     * obtained by a bit-wise OR of the following values.
     *
     * METIS DBG INFO (1) Prints various diagnostic messages.
     *
     * METIS DBG TIME (2) Performs timing analysis.
     *
     * METIS DBG COARSEN (4) Displays various statistics during coarsening.
     *
     * METIS DBG REFINE (8) Displays various statistics during refinement.
     *
     * METIS DBG IPART (16) Displays various statistics during initial
     * partitioning.
     *
     * METIS DBG MOVEINFO (32) Displays detailed information about vertex moves
     * during refine- ment. METIS DBG SEPINFO (64) Displays information about
     * vertex separators.
     *
     * METIS DBG CONNINFO (128) Displays information related to the minimization
     * of subdomain connectivity.
     *
     * METIS DBG CONTIGINFO (256) Displays information related to the
     * elimination of connected com- ponents.
     *
     */
    public int METIS_OPTION_DBGLVL = METIS_DBG_TIME;
    /**
     * options[METIS OPTION NITER]
     *
     * Specifies the number of iterations for the refinement algorithms at each
     * stage of the uncoarsening process. Default is 10.
     *
     */
    public int METIS_OPTION_NITER = 10;

    /**
     * options[METIS OPTION NCUTS]
     *
     * Specifies the number of different partitionings that it will compute. The
     * final partitioning is the one that achieves the best edgecut or
     * communication volume. Default is 1.
     */
    public int METIS_OPTION_NCUTS = 1;
    /**
     * options[METIS OPTION SEED]
     *
     * Specifies the seed for the random number generator.
     */
    public int METIS_OPTION_SEED = -1;
    /**
     * options[METIS OPTION NO2HOP]
     *
     * Specifies that the coarsening will not perform any 2–hop matchings when
     * the standard matching approach fails to sufficiently coarsen the graph.
     * The 2–hop matching is very effective for graphs with power-law degree
     * distributions.
     *
     * 0 Performs a 2–hop matching.
     *
     * 1 Does not perform a 2–hop matching.
     */
    public int METIS_OPTION_NO2HOP = 1;
    /**
     * options[METIS OPTION MINCONN]
     *
     * Specifies that the partitioning routines should try to minimize the
     * maximum degree of the subdomain graph, i.e., the graph in which each
     * partition is a node, and edges connect subdomains with a shared
     * interface.
     *
     * 0 Does not explicitly minimize the maximum connectivity.
     *
     * 1 Explicitly minimize the maximum connectivity.
     *
     */
    public int METIS_OPTION_MINCONN = 0;
    /**
     * options[METIS OPTION CONTIG]
     *
     * Specifies that the partitioning routines should try to produce partitions
     * that are contiguous. Note that if the input graph is not connected this
     * option is ignored.
     *
     * 0 Does not force contiguous partitions.
     *
     * 1 Forces contiguous partitions.
     *
     */
    public int METIS_OPTION_CONTIG = 0;
    /**
     * options[METIS OPTION COMPRESS]
     *
     * Specifies that the graph should be compressed by combining together
     * vertices that have identical adjacency lists.
     *
     * 0 Does not try to compress the graph.
     *
     * 1 Tries to compress the graph.
     */
    public int METIS_OPTION_COMPRESS = 0;
    /**
     * options[METIS OPTION CCORDER]
     *
     * Specifies if the connected components of the graph should first be
     * identified and ordered separately.
     *
     * 0 Does not identify the connected components.
     *
     * 1 Identifies the connected components.
     */
    public int METIS_OPTION_CCORDER = 0;
    /**
     * options[METIS OPTION PFACTOR]
     *
     * Specifies the minimum degree of the vertices that will be ordered last.
     * If the specified value is x superior 0, then any vertices with a degree greater
     * than 0.1*x*(average degree) are removed from the graph, an ordering of
     * the rest of the vertices is computed, and an overall ordering is computed
     * by ordering the removed vertices at the end of the overall ordering. For
     * example if x = 40, and the average degree is 5, then the algorithm will
     * remove all vertices with degree greater than 20. The vertices that are
     * removed are ordered last (i.e., they are automatically placed in the
     * top-level separator). Good values are often in the range of 60 to 200
     * (i.e., 6 to 20 times more than the average). Default value is 0,
     * indicating that no vertices are removed. Used to control whether or not
     * the ordering algorithm should remove any vertices with high degree (i.e.,
     * dense columns). This is particularly helpful for certain classes of LP
     * matrices, in which there a few vertices that are connected to many other
     * vertices. By removing these vertices prior to ordering, the quality and
     * the amount of time required to do the ordering improves.
     *
     */
    public int METIS_OPTION_PFACTOR = 0;
    /**
     * options[METIS OPTION NSEPS]
     *
     * Specifies the number of different separators that it will compute at each
     * level of nested dissection. The final separator that is used is the
     * smallest one. Default is 1.
     */
    public int METIS_OPTION_NSEPS = 1;
    /**
     * options[METIS OPTION UFACTOR]
     *
     * Specifies the maximum allowed load imbalance among the partitions. A
     * value of x indicates that the allowed load imbalance is (1 + x)/1000. The
     * load imbalance for the jth constraint is defined to be maxi (w[j,
     * i])/t[j, i]), where w[j, i] is the fraction of the overall weight of the
     * jth constraint that is as- signed to the ith partition and t[j, i] is the
     * desired target weight of the jth constraint for the ith partition (i.e.,
     * that specified via -tpwgts). For -ptype=rb, the default value is 1 (i.e.,
     * load imbalance of 1.001) and for -ptype=kway, the default value is 30
     * (i.e., load imbalance of 1.03).
     */
    public int METIS_OPTION_UFACTOR = 30;
    /**
     * options[METIS OPTION NUMBERING]
     *
     * Used to indicate which numbering scheme is used for the adjacency
     * structure of a graph or the element- node structure of a mesh. The
     * possible values are:
     *
     * 0 C-style numbering is assumed that starts from 0.
     *
     * 1 Fortran-style numbering is assumed that starts from 1.
     */
    public int METIS_OPTION_NUMBERING = 0;

    /* Used for command-line parameter purposes */
    /**
     *
     */
    public int METIS_OPTION_HELP;
    /**
     *
     */
    public int METIS_OPTION_TPWGTS;
    /**
     *
     */
    public int METIS_OPTION_NCOMMON = 1;
    /**
     *
     */
    public int METIS_OPTION_NOOUTPUT;
    /**
     *
     */
    public int METIS_OPTION_BALANCE;
    /**
     *
     */
    public int METIS_OPTION_GTYPE = METIS_GTYPE_NODAL;
    /**
     *
     */
    public int METIS_OPTION_UBVEC;

    /*! Operation type codes */
    public static final int METIS_OP_PMETIS = 0;
    public static final int METIS_OP_KMETIS = 1;
    public static final int METIS_OP_OMETIS = 2;

    /**
     * options[METIS_OPTION_PTYPE]
     * Partitioning Schemes Multilevel recursive bisectioning
     */
    public static final int METIS_PTYPE_RB = 0;
    /**
     * options[METIS_OPTION_PTYPE]
     * Partitioning Schemes Multilevel k-way partitioning
     */
    public static final int METIS_PTYPE_KWAY = 1;

    /**
     * Graph types for meshes 
     */
    public static final int METIS_GTYPE_DUAL = 0;
    /**
     * Graph types for meshes 
     */
    public static final int METIS_GTYPE_NODAL = 1;

    /**
     * options[METIS OPTION CTYPE]
     * Coarsening Schemes Random matching
     */
    public static final int METIS_CTYPE_RM = 0;
    /**
     * options[METIS OPTION CTYPE]
     * Coarsening Schemes Sorted heavy-edge matching
     */
    public static final int METIS_CTYPE_SHEM = 1;

    /**
     * options[METIS OPTION IPTYPE]
     * Initial partitioning schemes Grows a bisection using a greedy strategy
     */
    public static final int METIS_IPTYPE_GROW = 0;
    /**
     * options[METIS OPTION IPTYPE]
     * Initial partitioning schemes Computes a bisection at random followed by a
     * refinement
     */
    public static final int METIS_IPTYPE_RANDOM = 1;
    /**
     * options[METIS OPTION IPTYPE]
     * Initial partitioning schemes Derives a separator from an edge cut
     */
    public static final int METIS_IPTYPE_EDGE = 2;
    /**
     * options[METIS OPTION IPTYPE]
     * Initial partitioning schemes Grow a bisection using a greedy node-based strategy
     */
    public static final int METIS_IPTYPE_NODE = 3;
    /**
     * options[METIS OPTION IPTYPE]
     * Initial partitioning schemes 
     */
    public static final int METIS_IPTYPE_METISRB = 4;


    /**
     * options[METIS OPTION RTYPE]
     * Refinement schemes FM-based cut refinement
     */
    public static final int METIS_RTYPE_FM = 0;
    /**
     * options[METIS OPTION RTYPE]
     * Refinement schemes Greedy-based cut and volume refinement
     */
    public static final int METIS_RTYPE_GREEDY = 1;
    /**
     * options[METIS OPTION RTYPE]
     * Refinement schemes Two-sided node FM refinement
     */
    public static final int METIS_RTYPE_SEP2SIDED = 2;
    /**
     * options[METIS OPTION RTYPE]
     * Refinement schemes One-sided node FM refinement
     */
    public static final int METIS_RTYPE_SEP1SIDED = 3;


    /**
     * options[METIS OPTION DBGLVL]
     * Debug Levels Shows various diagnostic messages
     */
    public static final int METIS_DBG_INFO = 1;
    /**
     * options[METIS OPTION DBGLVL]
     * Debug Levels Perform timing analysis
     */
    public static final int METIS_DBG_TIME = 2;
    /**
     * options[METIS OPTION DBGLVL]
     * Debug Levels Show the coarsening progress 
     */
    public static final int METIS_DBG_COARSEN = 4;
    /**
     * options[METIS OPTION DBGLVL]
     * Debug Levels Show the refinement progress 
     */
    public static final int METIS_DBG_REFINE = 8;
    /**
     * options[METIS OPTION DBGLVL]
     * Debug Levels Show info on initial partitioning
     */
    public static final int METIS_DBG_IPART = 16;
    /**
     * options[METIS OPTION DBGLVL]
     * Debug Levels Show info on vertex moves during refinement
     */
    public static final int METIS_DBG_MOVEINFO = 32;
    /**
     * options[METIS OPTION DBGLVL]
     * Debug Levels Show info on vertex moves during sep refinement 
     */
    public static final int METIS_DBG_SEPINFO = 64;
    /**
     * options[METIS OPTION DBGLVL]
     * Debug Levels Show info on minimization of subdomain connectivity
     */
    public static final int METIS_DBG_CONNINFO = 128;
    /**
     * options[METIS OPTION DBGLVL]
     * Debug Levels Show info on elimination of connected components
     */
    public static final int METIS_DBG_CONTIGINFO = 256;
    /**
     * options[METIS OPTION DBGLVL]
     * Debug Levels Show info related to wspace allocation
     */
    public static final int METIS_DBG_MEMORY = 2048;

    /**
     * options[METIS OPTION OBJTYPE]
     * Types of objectives Edge-cut minimization
     */
    public static final int METIS_OBJTYPE_CUT = 0;
    /**
     * options[METIS OPTION OBJTYPE]
     * Types of objectives Total communication volume minimization
     */
    public static final int METIS_OBJTYPE_VOL = 1;
    /**
     * options[METIS OPTION OBJTYPE]
     * Types of objectives
     */
    public static final int METIS_OBJTYPE_NODE = 2;

}
