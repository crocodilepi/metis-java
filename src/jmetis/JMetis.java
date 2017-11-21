package jmetis;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * implementation JAVA of METIS (seeing Metis v5.1 Manual)
 * @author dinhvan
 */
public class JMetis {

    /**
     * The flag that indicates whether the native library has been loaded
     */
    private static boolean initialized = false;

    /**
     * Whether a RuntimeException should be thrown if a method is about to
     * return a result code that is not metisStatus.METIS_OK
     */
    private static boolean exceptionsEnabled = false;

    // Initialize the native library.
    static {
        initialize();
    }

    /**
     * Initializes the native library. Note that this method does not have to be
     * called explicitly, since it will be called automatically when this class
     * is loaded.
     */
    public static void initialize() {
        if (!initialized) {
            
            String libname="jmetis";
            if(System.getProperty("os.name").contains("Windows")){
                libname += "-windows";
                if(System.getProperty("os.arch").contains("64"))
                    libname += "-x64";
                else
                    libname += "-x86";
            }else
                System.err.println("Can not load the lib on not-windows system");
            libname += ".dll";
            /*
            try{
                String path = System.getProperty("user.dir")+"/resources/";
                System.load(path + libname);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                loadLib(libname);
            }
            */
            loadLib(libname);
            initialized = true;
        }
    }
    
    private static void loadLib(String libName){
        try {
            // get resources from .dll
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(libName);
            File fileout = new File(System.getProperty("java.io.tmpdir")+"/"+libName);
            OutputStream out = FileUtils.openOutputStream(fileout);
            IOUtils.copy(in, out);
            in.close();
            out.close();
            System.load(fileout.toString());
            System.out.println("Loading " + fileout.toString() + " ... SUCCES!");
        } catch (Exception e) {
            throw new NullPointerException("Can not load library " + libName);
        }
        
    }

    /**
     * @param enabled Whether exceptions are enabled
     */
    public static void setExceptionsEnabled(boolean enabled) {
        exceptionsEnabled = enabled;
    }

    /**
     * If the given result is not metisStatus.METIS_OK and exceptions have been
     * enabled, this method will throw a RuntimeException with an error message
     * that corresponds to the given result code. Otherwise, the given result is
     * simply returned.
     *
     * @param result The result to check
     * @return The result that was given as the parameter
     * @throws RuntimeException If exceptions have been enabled and the given
     * result code is not metisStatus.METIS_OK
     */
    private static int checkResult(int result) {
        if (exceptionsEnabled && result != MetisStatus.METIS_OK) {
            throw new RuntimeException(MetisStatus.stringFor(result));
        }
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    /////////////////////// Graph partitioning routines ////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This function computes a partitioning of a graph based on multilevel
     * recursive bisection. It can be used to partition a graph into \e k parts.
     * The objective of the partitioning is to minimize the edgecut subject to
     * one or more balancing constraints.
     *
     * @param nvtxs [in] nvtxs is the number of vertices in the graph.
     * @param ncon [in] ncon is the number of balancing constraints. For the
     * standard partitioning problem in which each vertex is either unweighted
     * or has a single weight, ncon should be 1.
     * @param xadj [in] xadj is an array of size nvtxs+1 used to specify the
     * starting positions of the adjacency structure of the vertices in the
     * adjncy array.
     * @param adjncy [in] adjncy is an array of size to the sum of the degrees
     * of the graph that stores for each vertex the set of vertices that is
     * adjancent to.
     * @param vwgt [in] vwgt is an array of size nvtxs*ncon that stores the
     * weights of the vertices for each constraint. The ncon weights for the ith
     * vertex are stored in the ncon consecutive locations starting at
     * vwgt[i*ncon]. When ncon==1, a NULL value can be passed indicating that
     * all the vertices in the graph have the same weight.
     * @param vsize The size of the vertices for computing the total
     * communication volume, can be 'null'
     * @param adjwgt [in] adjwgt is an array of size equal to adjncy, specifying
     * the weight for each edge (i.e., adjwgt[j] corresponds to the weight of
     * the edge stored in adjncy[j]). A NULL value can be passed indicating that
     * all the edges in the graph have the same weight.
     * @param nparts [in] nparts is the number of desired partitions.
     * @param tpwgts [in] tpwgts is an array of size nparts*ncon that specifies
     * the desired weight for each part and constraint. The \e{target partition
     * weight} for the ith part and jth constraint is specified at
     * tpwgts[i*ncon+j] (the numbering of i and j starts from 0). For each
     * constraint, the sum of the tpwgts[] entries must be 1.0 (i.e., \f$ \sum_i
     * tpwgts[i*ncon+j] = 1.0 \f$). A NULL value can be passed indicating that
     * the graph should be equally divided among the parts.
     * @param ubvec [in] ubvec is an array of size ncon that specifies the
     * allowed load imbalance tolerance for each constraint. For the ith part
     * and jth constraint the allowed weight is the ubvec[j]*tpwgts[i*ncon+j]
     * fraction of the jth's constraint total weight. The load imbalances must
     * be greater than 1.0. A NULL value can be passed indicating that the load
     * imbalance tolerance for each constraint should be 1.001 (for ncon==1) or
     * 1.01 (for ncon superior 1).
     * @param options [in] options is the array for passing additional
     * parameters in order to customize the behaviour of the partitioning
     * algorithm.
     * @param objval [out] edgecut stores the cut of the partitioning.
     * @param part [out] part is an array of size nvtxs used to store the
     * computed partitioning. The partition number for the ith vertex is stored
     * in part[i]. Based on the numflag parameter, the numbering of the parts
     * starts from either 0 or 1.
     * @return METIS_OK indicates that the function returned normally
     * METIS_ERROR_INPUT indicates an input error. METIS_ERROR_MEMORY indicates
     * that it could not allocate the required memory. METIS_ERROR
     *
     */
    public static int partGraphRecursive(int nvtxs, int ncon, int[] xadj, 
            int[] adjncy, int[] vwgt, int[] vsize, int[] adjwgt, int nparts,
            double tpwgts[], double ubvec, int[] options, int objval[], int part[]) {
        return checkResult(METIS_PartGraphRecursiveNative(new int[]{nvtxs}, 
                new int[]{ncon}, xadj, adjncy, vwgt, vsize, adjwgt, 
                new int[]{nparts}, tpwgts, ubvec, options, objval, part));
    }
    public static int METIS_PartGraphRecursive(int nvtxs[], int ncon[], 
            int xadj[], int adjncy[], int vwgt[], int vsize[], int adjwgt[], 
            int nparts[], double tpwgts[], double ubvec, int[] options, 
            int objval[], int part[]) {
        return checkResult(METIS_PartGraphRecursiveNative(nvtxs, ncon, xadj, 
                adjncy, vwgt, vsize, adjwgt, nparts, tpwgts, ubvec, options, 
                objval, part));
    }

    private static native int METIS_PartGraphRecursiveNative(int nvtxs[], int ncon[], 
            int xadj[], int adjncy[], int vwgt[], int vsize[], int adjwgt[],
            int nparts[], double tpwgts[], double ubvec, int[] options,
            int objval[], int part[]);

    /**
     * This function computes a partitioning of a graph based on multilevel
     * k-way.It can be used to partition a graph into \e k parts. The objective
     * of the partitioning is to minimize the edgecut subject to one or more
     * balancing constraints.
     *
     * @param nvtxs [in] nvtxs is the number of vertices in the graph.
     * @param ncon [in] ncon is the number of balancing constraints. For the
     * standard partitioning problem in which each vertex is either unweighted
     * or has a single weight, ncon should be 1.
     * @param xadj [in] xadj is an array of size nvtxs+1 used to specify the
     * starting positions of the adjacency structure of the vertices in the
     * adjncy array.
     * @param adjncy [in] adjncy is an array of size to the sum of the degrees
     * of the graph that stores for each vertex the set of vertices that is
     * adjancent to.
     * @param vwgt [in] vwgt is an array of size nvtxs*ncon that stores the
     * weights of the vertices for each constraint. The ncon weights for the ith
     * vertex are stored in the ncon consecutive locations starting at
     * vwgt[i*ncon]. When ncon==1, a NULL value can be passed indicating that
     * all the vertices in the graph have the same weight.
     * @param vsize The size of the vertices for computing the total
     * communication volume, can be 'null'
     * @param adjwgt [in] adjwgt is an array of size equal to adjncy, specifying
     * the weight for each edge (i.e., adjwgt[j] corresponds to the weight of
     * the edge stored in adjncy[j]). A NULL value can be passed indicating that
     * all the edges in the graph have the same weight.
     * @param nparts [in] nparts is the number of desired partitions.
     * @param tpwgts [in] tpwgts is an array of size nparts*ncon that specifies
     * the desired weight for each part and constraint. The \e{target partition
     * weight} for the ith part and jth constraint is specified at
     * tpwgts[i*ncon+j] (the numbering of i and j starts from 0). For each
     * constraint, the sum of the tpwgts[] entries must be 1.0 (i.e., \f$ \sum_i
     * tpwgts[i*ncon+j] = 1.0 \f$). A NULL value can be passed indicating that
     * the graph should be equally divided among the parts.
     * @param ubvec [in] ubvec is an array of size ncon that specifies the
     * allowed load imbalance tolerance for each constraint. For the ith part
     * and jth constraint the allowed weight is the ubvec[j]*tpwgts[i*ncon+j]
     * fraction of the jth's constraint total weight. The load imbalances must
     * be greater than 1.0. A NULL value can be passed indicating that the load
     * imbalance tolerance for each constraint should be 1.001 (for ncon==1) or
     * 1.01 (for ncon superior 1).
     * @param options [in] options is the array for passing additional
     * parameters in order to customize the behaviour of the partitioning
     * algorithm.
     * @param objval [out] edgecut stores the cut of the partitioning.
     * @param part [out] part is an array of size nvtxs used to store the
     * computed partitioning. The partition number for the ith vertex is stored
     * in part[i]. Based on the numflag parameter, the numbering of the parts
     * starts from either 0 or 1.
     * @return METIS_OK indicates that the function returned normally
     * METIS_ERROR_INPUT indicates an input error. METIS_ERROR_MEMORY indicates
     * that it could not allocate the required memory. METIS_ERROR
     *
     */
    public static int partGraphKway(int nvtxs, int ncon, int xadj[],
            int adjncy[], int vwgt[], int vsize[], int adjwgt[], int nparts,
            double tpwgts[], double ubvec, int[] options, int objval[], int part[]) {
        return checkResult(METIS_PartGraphKwayNative(new int[]{nvtxs}, 
                new int[]{ncon}, xadj, adjncy, vwgt, vsize, adjwgt, 
                new int[]{nparts}, tpwgts, ubvec, options, objval, part));
    }
    
    public static int METIS_PartGraphKway(int nvtxs[], int ncon[], int xadj[],
            int adjncy[], int vwgt[], int vsize[], int adjwgt[], int nparts[],
            double tpwgts[], double ubvec, int[] options, int objval[], int part[]) {
        return checkResult(METIS_PartGraphKwayNative(nvtxs, ncon, xadj, adjncy, vwgt,
                vsize, adjwgt, nparts, tpwgts, ubvec, options, objval, part));
    }

    private static native int METIS_PartGraphKwayNative(int nvtxs[], int ncon[],
            int xadj[], int adjncy[], int vwgt[], int vsize[], int adjwgt[],
            int nparts[], double tpwgts[], double ubvec, int[] options,
            int objval[], int part[]);

    ////////////////////////////////////////////////////////////////////////////
    /////////////////////// Mesh partitioning routines /////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Partition a mesh into k parts based on a partitioning of the mesh’s dual
     * graph
     *
     * @param ne The number of elements in the mesh
     * @param nn The number of nodes in the mesh
     * @param eptr The pair of arrays storing the mesh
     * @param eind The pair of arrays storing the mesh
     * @param vwgt An array of size ne specifying the weights of the elements,
     * can be 'null'
     * @param vsize An array of size ne specifying the size of the elements that
     * is used for computing the total communication volume, can be 'null'
     * @param ncommon Speciﬁes the number of common nodes that two elements must
     * have in order to put an edge between them in the dual graph
     * @param nparts The number of parts to partition the mesh
     * @param tpwgts This is an array of size nparts that speciﬁes the desired
     * weight for each partition, can be 'null'
     * @param options This is the array of options, can be 'null'
     * @param objval the edgecut or the total communication volume of the dual
     * graph’s partitionin
     * @param epart This is a vector of size ne that upon successful completion
     * stores the partition vector for the elements of the mesh. The numbering
     * of this vector starts from either 0 or 1, depending on the value of
     * options[METIS_OPTION_NUMBERING]
     * @param npart This is a vector of size nn that upon successful completion
     * stores the partition vector for the nodes of the mesh
     * @return METIS_OK, METIS_ERROR_INPUT, METIS_ERROR_MEMORY, METIS_ERROR
     */
    public static int partMeshDual(int ne, int nn, int[] eptr, int[] eind,
            int[] vwgt, int[] vsize, int ncommon, int nparts, double[] tpwgts,
            int[] options, int[] objval, int[] epart, int[] npart) {
        return checkResult(METIS_PartMeshDualNative(new int[]{ne}, new int[]{nn},
                eptr, eind, vwgt, vsize, new int[]{ncommon}, new int[]{nparts},
                tpwgts, options, objval, epart, npart));
    }
    
    public static int METIS_PartMeshDual(int[] ne, int[] nn, int[] eptr, int[] eind,
            int[] vwgt, int[] vsize, int[] ncommon, int[] nparts, double[] tpwgts,
            int[] options, int[] objval, int[] epart, int[] npart) {
        return checkResult(METIS_PartMeshDualNative(ne, nn, eptr, eind, vwgt, vsize, ncommon,
                nparts, tpwgts, options, objval, epart, npart));
    }

    private static native int METIS_PartMeshDualNative(int[] ne, int[] nn, int[] eptr,
            int[] eind, int[] vwgt, int[] vsize, int[] ncommon, int[] nparts,
            double[] tpwgts, int[] options, int[] objval, int[] epart, int[] npart);

    /**
     * Partition a mesh into k parts based on a partitioning of the mesh’s nodal
     * graph
     *
     * @param ne The number of elements in the mesh
     * @param nn The number of nodes in the mesh
     * @param eptr The pair of arrays storing the mesh
     * @param eind The pair of arrays storing the mesh
     * @param vwgt An array of size ne specifying the weights of the elements,
     * can be 'null'
     * @param vsize An array of size ne specifying the size of the elements that
     * is used for computing the total communication volume, can be 'null'
     * @param nparts The number of parts to partition the mesh
     * @param tpwgts This is an array of size nparts that speciﬁes the desired
     * weight for each partition, can be 'null'
     * @param options This is the array of options, can be 'null'
     * @param objval the edgecut or the total communication volume of the dual
     * graph’s partitionin
     * @param epart This is a vector of size ne that upon successful completion
     * stores the partition vector for the elements of the mesh. The numbering
     * of this vector starts from either 0 or 1, depending on the value of
     * options[METIS_OPTION_NUMBERING]
     * @param npart This is a vector of size nn that upon successful completion
     * stores the partition vector for the nodes of the mesh
     * @return METIS_OK, METIS_ERROR_INPUT, METIS_ERROR_MEMORY, METIS_ERROR
     */
    public static int partMeshNodal(int ne, int nn, int[] eptr,
            int[] eind, int[] vwgt, int[] vsize, int nparts, double[] tpwgts,
            int[] options, int[] objval, int[] epart, int[] npart) {
        return checkResult(METIS_PartMeshNodalNative(new int[]{ne}, new int[]{nn},
                eptr, eind, vwgt, vsize, new int[]{nparts}, tpwgts, options, objval, epart, npart));
    }
    
    public static int METIS_PartMeshNodal(int[] ne, int[] nn, int[] eptr,
            int[] eind, int[] vwgt, int[] vsize, int[] nparts, double[] tpwgts,
            int[] options, int[] objval, int[] epart, int[] npart) {
        return checkResult(METIS_PartMeshNodalNative(ne, nn, eptr, eind, vwgt, vsize, nparts,
                tpwgts, options, objval, epart, npart));
    }

    private static native int METIS_PartMeshNodalNative(int[] ne, int[] nn, int[] eptr,
            int[] eind, int[] vwgt, int[] vsize, int[] nparts, double[] tpwgts,
            int[] options, int[] objval, int[] epart, int[] npart);

    ////////////////////////////////////////////////////////////////////////////
    //////////////// Sparse Matrix Reordering Routines /////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Computes ﬁll reducing orderings of sparse matrices using the multilevel
     * nested dissection algorithm. This function is the entry point for the
     * multilevel nested dissection ordering code. At each bisection, a
     * node-separator is computed using a node-based refinement approach.
     *
     * @param nvtxs is the number of vertices in the graph.
     * @param xadj is of length nvtxs+1 marking the start of the adjancy list of
     * each vertex in adjncy.
     * @param adjncy stores the adjacency lists of the vertices. The adjnacy
     * list of a vertex should not contain the vertex itself.
     * @param vwgt is an array of size nvtxs storing the weight of each vertex.
     * If vwgt is NULL, then the vertices are considered to have unit weight.
     * @param options is an array of size METIS_NOPTIONS used to pass various
     * options impacting the of the algorithm. A NULL value indicates use of
     * default options.
     * @param perm is an array of size nvtxs such that if A and A' are the
     * original and permuted matrices, then A'[i] = A[perm[i]].
     * @param iperm is an array of size nvtxs such that if A and A' are the
     * original and permuted matrices, then A[i] = A'[iperm[i]].
     * @return METIS_OK, METIS_ERROR_INPUT, METIS_ERROR_MEMORY, METIS_ERROR
     */
    public static int METIS_NodeND(int[] nvtxs, int[] xadj, int[] adjncy, int[] vwgt,
            int[] options, int[] perm, int[] iperm) {
        return checkResult(METIS_NodeNDNative(nvtxs, xadj, adjncy, vwgt, options, perm, iperm));
    }

    private static native int METIS_NodeNDNative(int[] nvtxs, int[] xadj, int[] adjncy,
            int[] vwgt, int[] options, int[] perm, int[] iperm);

    ////////////////////////////////////////////////////////////////////////////
    //////////////// Mesh-to-graph conversion routines /////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This function creates a graph corresponding to the dual of a finite
     * element mesh
     *
     * @param ne is the number of elements in the mesh
     * @param nn is the number of nodes in the mesh
     * @param eptr is an array of size ne+1 used to mark the start and end
     * locations in the nind array
     * @param eind is an array that stores for each element the set of node IDs
     * (indices) that it is made off. The length of this array is equal to the
     * total number of nodes over all the mesh elements
     * @param ncommon is the minimum number of nodes that two elements must
     * share in order to be connected via an edge in the dual graph
     * @param numflag is either 0 or 1 indicating if the numbering of the nodes
     * starts from 0 or 1, respectively. The same numbering is used for the
     * returned graph as well
     * @param xadj indicates where the adjacency list of each vertex is stored
     * in r_adjncy. The memory for this array is allocated by this routine. It
     * can be freed by calling METIS_free()
     * @param adjncy stores the adjacency list of each vertex in the generated
     * dual graph. The memory for this array is allocated by this routine. It
     * can be freed by calling METIS_free()
     * @return METIS_OK, METIS_ERROR_INPUT, METIS_ERROR_MEMORY, METIS_ERROR
     */
    public static int meshToDual(int ne, int nn, int[] eptr, int[] eind,
            int ncommon, int numflag, MetisPointer xadj, MetisPointer adjncy) {
        return checkResult(METIS_MeshToDualNative(new int[]{ne}, new int[]{nn}, 
                eptr, eind, new int[]{ncommon}, new int[]{numflag}, xadj, adjncy));
    }
    
    public static int METIS_MeshToDual(int[] ne, int[] nn, int[] eptr, int[] eind,
            int[] ncommon, int[] numflag, MetisPointer xadj, MetisPointer adjncy) {
        return checkResult(METIS_MeshToDualNative(ne, nn, eptr, eind, ncommon,
                numflag, xadj, adjncy));
    }

    private static native int METIS_MeshToDualNative(int[] ne, int[] nn, int[] eptr,
            int[] eind, int[] ncommon, int[] numﬂag, MetisPointer xadj, MetisPointer adjncy);

    /**
     * This function creates a graph corresponding to (almost) the nodal of a
     * finite element mesh. In the nodal graph, each node is connected to the
     * nodes corresponding to the union of nodes present in all the elements in
     * which that node belongs
     *
     * @param ne is the number of elements in the mesh
     * @param nn is the number of nodes in the mesh
     * @param eptr is an array of size ne+1 used to mark the start and end
     * locations in the nind array
     * @param eind is an array that stores for each element the set of node IDs
     * (indices) that it is made off. The length of this array is equal to the
     * total number of nodes over all the mesh elements
     * @param numflag is either 0 or 1 indicating if the numbering of the nodes
     * starts from 0 or 1, respectively. The same numbering is used for the
     * returned graph as well
     * @param xadj indicates where the adjacency list of each vertex is stored
     * in r_adjncy. The memory for this array is allocated by this routine. It
     * can be freed by calling METIS_free()
     * @param adjncy stores the adjacency list of each vertex in the generated
     * dual graph. The memory for this array is allocated by this routine. It
     * can be freed by calling METIS_free()
     * @return METIS_OK, METIS_ERROR_INPUT, METIS_ERROR_MEMORY, METIS_ERROR
     */
    public static int meshToNodal(int ne, int nn, int[] eptr, int[] eind,
            int numflag, MetisPointer xadj, MetisPointer adjncy) {
        return checkResult(METIS_MeshToNodalNative(new int[]{ne}, new int[]{nn},
                eptr, eind, new int[]{numflag}, xadj, adjncy));
    }
    
    public static int METIS_MeshToNodal(int[] ne, int[] nn, int[] eptr, int[] eind,
            int[] numflag, MetisPointer xadj, MetisPointer adjncy) {
        return checkResult(METIS_MeshToNodalNative(ne, nn, eptr, eind, numflag, xadj, adjncy));
    }

    private static native int METIS_MeshToNodalNative(int[] ne, int[] nn, int[] eptr,
            int[] eind, int[] numflag, MetisPointer xadj, MetisPointer adjncy);

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////// Utility routines ////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Initializes the options array into its default values
     *
     * @param options The array of options that will be initialized. It’s size
     * should be at least METIS_NOPTIONS
     * @return METIS_OK
     */
    public static int METIS_SetDefaultOptions(MetisOptions options) {
        return checkResult(METIS_SetDefaultOptionsNative(options));
    }

    private static native int METIS_SetDefaultOptionsNative(MetisOptions options);

    /**
     * Frees the memory that was allocated by either the METIS MeshToDual or the
     * METIS MeshToNodal routines for returning the dual or nodal graph of a
     * mesh
     *
     * @param ptr The pointer to be freed. This pointer should be one of the
     * xadj or adjncy returned by METIS’ API routines
     * @return METIS_OK
     */
    public static int METIS_Free(MetisPointer ptr) {
        return checkResult(METIS_FreeNative(ptr));
    }

    private static native int METIS_FreeNative(MetisPointer ptr);

}
