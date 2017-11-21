package test;

import java.util.Arrays;
import jmetis.JMetis;
import jmetis.MetisGraph;
import jmetis.MetisOptions;
import jmetis.MetisPointer;
import jmetis.MetisStatus;

/**
 * Test jmetis package
 * @author dinhvan
 */
public class MetisDemo {
    public static void main(String[] args) {
        MetisDemo test = new MetisDemo();
        JMetis.setExceptionsEnabled(true);
        test.testMETIS_SetDefaultOptions();
        test.testMETIS_Free();
        test.testMETIS_PartGraphRecursive();
        test.testMETIS_PartGraphKway();
        test.testMETIS_PartMeshDual();
        test.testMETIS_PartMeshNodal();
        test.testMETIS_NodeND();
        test.testMETIS_MeshToNodal();
        test.testMETIS_MeshToDual();
    }
    
    private void checkResult(int res, String msg) {
        switch (res) {
            case MetisStatus.METIS_OK: 
                System.out.println(msg + " Success");
                break;
            case MetisStatus.METIS_ERROR_INPUT:
                System.out.println(msg + " Error input");
                break;
            case MetisStatus.METIS_ERROR_MEMORY:
                System.out.println(msg + " Error Memory");
                break;
            case MetisStatus.METIS_ERROR:
                System.out.println(msg + " Error");
                break;
        }
    }
    
    // test OK
    private void testMETIS_SetDefaultOptions(){
        System.out.println("\n=============================================\n");
        System.out.println("\n test JMetis.METIS_SetDefaultOptions \n");
        MetisOptions options = new MetisOptions();
        
        System.out.println("Options init in Java ");options.print();
        checkResult(JMetis.METIS_SetDefaultOptions(options), "METIS_SetDefaultOptions");
        System.out.println("Options set in C by METIS");options.print();
    }
    // test OK
    private void testMETIS_Free(){
        System.out.println("\n=============================================\n");
        System.out.println("\n test JMetis.METIS_Free \n");
        int[] xadj = {0,2,5,8,11,13,16,20,24,28,31,33,36,39,42,44};
        int[] adjncy ={1,5,0,2,6,1,3,7,2,4,8,3,9,0,6,10,1,5,7,11,2,6,8,12,3,7,9,13,4,8};
        MetisPointer ptr = new MetisPointer(xadj);
        JMetis.METIS_Free(ptr);
        if(ptr.getData() == null)
            System.out.println("METIS_Free... SUCCES!");
        else
            System.out.println("data " + Arrays.toString(ptr.getData()));
    }
    
    //test OK
    private void testMETIS_PartGraphRecursive(){
        System.out.println("\n=============================================\n");
        System.out.println("\n test JMetis.METIS_PartGraphRecursive \n");
        /*
        0 -1 -2 -3 -4 
        |  |  |  |  | 
        5 -6 -7 -8 -9 
        |  |  |  |  | 
        10-11-12-13-14 
        xadj(15) = 0 2 5 8 11 13 16 20 24 28 31 33 36 39 42 44
        adjncy(44) = 1 5 0 2 6 1 3 7 2 4 8 3 9 0 6 10 1 5 7 11 2 6 8 12 3 7 9 13 4 8 14 5 11 6 10 12 7 11 13 8 12 14 9 13
        */
        int numVertices = 15;
        int numEdges = 22;
        int[] xadj = {0,2,5,8,11,13,16,20,24,28,31,33,36,39,42,44};
        int[] adjncy ={1,5,0,2,6,1,3,7,2,4,8,3,9,0,6,10,1,5,7,11,2,6,8,12,3,7,9,13,4,8,14,5,11,6,10,12,7,11,13,8,12,14,9,13};
        MetisOptions options = new MetisOptions();
        options.METIS_OPTION_CTYPE = MetisOptions.METIS_CTYPE_SHEM;
        options.METIS_OPTION_IPTYPE = MetisOptions.METIS_IPTYPE_GROW;
        options.METIS_OPTION_RTYPE = MetisOptions.METIS_RTYPE_GREEDY;
        options.METIS_OPTION_NO2HOP = 1;
        options.METIS_OPTION_NCUTS = 2;
        options.METIS_OPTION_NITER = 10;
        options.METIS_OPTION_SEED = 10;
        options.METIS_OPTION_UFACTOR = 30;
        options.METIS_OPTION_UBVEC = 30;
        options.METIS_OPTION_DBGLVL = MetisOptions.METIS_DBG_INFO;
        
        int[] objval = {0};
        int[] part = new int[numVertices];
        // example for graph structure in the manuel of Metis v5.0.1, page 24
        MetisGraph graph = new MetisGraph(xadj, adjncy);
        checkResult(JMetis.partGraphRecursive(
                graph.getNumberOfVertices(), 
                graph.getNumberOfVerticesAttributes(), 
                graph.getVerticesPointer(), 
                graph.getVerticesAdjacentsId(), 
                graph.getVerticesWeights(),
                null, 
                graph.getEdgesWeights(), 
                2, 
                null, 
                1.001, 
                options.getOptions(), 
                objval, 
                part)
        ,"partGraphRecursive");
        System.out.println("objval " + objval[0]);
        System.out.println("part " +Arrays.toString(part));
    }
    
    // test OK
    private void testMETIS_PartGraphKway(){
        System.out.println("\n=============================================\n");
        System.out.println("\n test JMetis.METIS_PartGraphKway \n");
        /*
        0 -1 -2 -3 -4 
        |  |  |  |  | 
        5 -6 -7 -8 -9 
        |  |  |  |  | 
        10-11-12-13-14 
        xadj(15) = 0 2 5 8 11 13 16 20 24 28 31 33 36 39 42 44
        adjncy(44) = 1 5 0 2 6 1 3 7 2 4 8 3 9 0 6 10 1 5 7 11 2 6 8 12 3 7 9 13 4 8 14 5 11 6 10 12 7 11 13 8 12 14 9 13
        */
        int numVertices = 15;
        int numEdges = 22;
        int[] xadj = {0,2,5,8,11,13,16,20,24,28,31,33,36,39,42,44};
        int[] adjncy ={1,5,0,2,6,1,3,7,2,4,8,3,9,0,6,10,1,5,7,11,2,6,8,12,3,7,9,13,4,8,14,5,11,6,10,12,7,11,13,8,12,14,9,13};
        MetisOptions options = new MetisOptions();
        options.METIS_OPTION_OBJTYPE = MetisOptions.METIS_OBJTYPE_CUT;
        options.METIS_OPTION_CTYPE = MetisOptions.METIS_CTYPE_SHEM;
        options.METIS_OPTION_IPTYPE = MetisOptions.METIS_IPTYPE_GROW;
        options.METIS_OPTION_RTYPE = MetisOptions.METIS_RTYPE_GREEDY;
        options.METIS_OPTION_NO2HOP = 1;
        options.METIS_OPTION_NCUTS = 2;
        options.METIS_OPTION_NITER = 10;
        options.METIS_OPTION_SEED = 10;
        options.METIS_OPTION_UFACTOR = 30;
        options.METIS_OPTION_UBVEC = 30;
        options.METIS_OPTION_DBGLVL = MetisOptions.METIS_DBG_INFO;
        
        int[] objval = {0};
        int[] part = new int[numVertices];
        // example for graph structure in the manuel of Metis v5.0.1, page 24
        MetisGraph graph = new MetisGraph(xadj, adjncy);
        checkResult(JMetis.partGraphKway(
                graph.getNumberOfVertices(), 
                graph.getNumberOfVerticesAttributes(), 
                graph.getVerticesPointer(), 
                graph.getVerticesAdjacentsId(), 
                graph.getVerticesWeights(),
                null, 
                graph.getEdgesWeights(), 
                2, 
                null, 
                1.001, 
                options.getOptions(), 
                objval, 
                part)
        ,"partGraphKway");
        System.out.println("objval " + objval[0]);
        System.out.println("part " +Arrays.toString(part));
    }
    
    // test OK
    private void testMETIS_PartMeshDual(){
        System.out.println("\n=============================================\n");
        System.out.println("\n Test JMetis.METIS_PartMeshDual \n");
        /*
        1--------------3
        | \    e1    / | \ 
        |   \      /   |   \ 
        |     \  /     |    \
        |  e0   2   e2 | e5  4
        |     / | \    |     /
        |   /   |   \  |   /
        | / e4  | e3  \| /
        0-------6------5
        mesh file
        6 7 // 6 elements, 7 vertices
        0, 1, 2 // e0
        1, 2, 3 // e1
        2, 3, 5 // e2
        2, 6, 5 // e3
        0, 2, 6 // e4
        3, 4, 5 // e5
        */
        int[] nbrElem = {6};
        int[] nbrNode = {7};
        int[] nbrPart = {2};
        int[] nCommon = {1};
        int[] elemPtr = new int[]{0, 3, 6, 9, 12, 15, 18};  // dim = nbrElem + 1
        int[] elemInd = new int[]{0, 1, 2, 1, 2, 3, 2, 3, 5, 2, 6, 5, 0, 2, 6, 3, 4, 5};
        int[] epart = new int[nbrElem[0]];
        int[] npart = new int[nbrNode[0]];
        int[] objval = {0};
        
        MetisOptions options = new MetisOptions();
        options.METIS_OPTION_PTYPE = MetisOptions.METIS_PTYPE_KWAY;
        options.METIS_OPTION_OBJTYPE = MetisOptions.METIS_OBJTYPE_CUT;
        options.METIS_OPTION_CTYPE = MetisOptions.METIS_CTYPE_SHEM;
        options.METIS_OPTION_IPTYPE = MetisOptions.METIS_IPTYPE_METISRB;
        options.METIS_OPTION_RTYPE = MetisOptions.METIS_RTYPE_GREEDY;
        options.METIS_OPTION_NCUTS = 1;
        options.METIS_OPTION_NITER = 10;
        options.METIS_OPTION_CONTIG = 0;
        options.METIS_OPTION_MINCONN= 0;
        options.METIS_OPTION_NOOUTPUT = 0;
        options.METIS_OPTION_SEED = -1;
        options.METIS_OPTION_GTYPE = MetisOptions.METIS_GTYPE_NODAL;
        options.METIS_OPTION_NCOMMON = 1;
        options.METIS_OPTION_UFACTOR = 30;
        options.METIS_OPTION_NUMBERING = 0;
        options.METIS_OPTION_DBGLVL = MetisOptions.METIS_DBG_TIME;
        
        System.out.println("Options " + Arrays.toString(options.getOptions()));
        JMetis.setExceptionsEnabled(true);
        int res = JMetis.METIS_PartMeshDual(nbrElem, nbrNode, elemPtr, elemInd, null, null, nCommon, nbrPart, null, options.getOptions(), objval, epart, npart);
        checkResult(res, "METIS_PartMeshDual");
        
        System.out.println("objval: " + Arrays.toString(objval));
        System.out.println("ElemPtr: " + Arrays.toString(elemPtr));
        System.out.println("ElemInd: " + Arrays.toString(elemInd));
        System.out.println("Epart: " + Arrays.toString(epart));
        System.out.println("Npart: " + Arrays.toString(npart));
    }
    
    // test OK
    private void testMETIS_PartMeshNodal() {
        System.out.println("\n=============================================\n");
        System.out.println("\n Test JMetis.partMeshNodal \n");
        /*
        1--------------3
        | \    e1    / | \ 
        |   \      /   |   \ 
        |     \  /     |    \
        |  e0   2   e2 | e5  4
        |     / | \    |     /
        |   /   |   \  |   /
        | / e4  | e3  \| /
        0-------6------5
        mesh file
        6 7 // 6 elements, 7 vertices
        0, 1, 2 // e0
        1, 2, 3 // e1
        2, 3, 5 // e2
        2, 6, 5 // e3
        0, 2, 6 // e4
        3, 4, 5 // e5
        */
        int[] nbrElem = {6};
        int[] nbrNode = {7};
        int[] nbrPart = {2};
        int[] elemPtr = new int[]{0, 3, 6, 9, 12, 15, 18};  // dim = nbrElem + 1
        int[] elemInd = new int[]{0, 1, 2, 1, 2, 3, 2, 3, 5, 2, 6, 5, 0, 2, 6, 3, 4, 5};
        int[] epart = new int[nbrElem[0]];
        int[] npart = new int[nbrNode[0]];
        int[] objval = {0};
        
        MetisOptions options = new MetisOptions();
        options.METIS_OPTION_PTYPE = MetisOptions.METIS_PTYPE_KWAY;
        options.METIS_OPTION_OBJTYPE = MetisOptions.METIS_OBJTYPE_CUT;
        options.METIS_OPTION_CTYPE = MetisOptions.METIS_CTYPE_SHEM;
        options.METIS_OPTION_IPTYPE = MetisOptions.METIS_IPTYPE_METISRB;
        options.METIS_OPTION_RTYPE = MetisOptions.METIS_RTYPE_GREEDY;
        options.METIS_OPTION_NCUTS = 1;
        options.METIS_OPTION_NITER = 10;
        options.METIS_OPTION_CONTIG = 0;
        options.METIS_OPTION_MINCONN= 0;
        options.METIS_OPTION_NOOUTPUT = 0;
        options.METIS_OPTION_SEED = -1;
        options.METIS_OPTION_GTYPE = MetisOptions.METIS_GTYPE_NODAL;
        options.METIS_OPTION_NCOMMON = 1;
        options.METIS_OPTION_UFACTOR = 30;
        options.METIS_OPTION_NUMBERING = 0;
        options.METIS_OPTION_DBGLVL = MetisOptions.METIS_DBG_TIME;
        
        System.out.println("Options " + Arrays.toString(options.getOptions()));
        JMetis.setExceptionsEnabled(true);
        int res = JMetis.partMeshNodal(nbrElem[0], nbrNode[0], elemPtr, elemInd, null, null, nbrPart[0], null, options.getOptions(), objval, epart, npart);
        //int res = JMetis.METIS_PartMeshNodal(nbrElem, nbrNode, elemPtr, elemInd, null, null, nbrPart, null, options.getOptions(), objval, epart, npart);
        checkResult(res, "METIS_PartMeshNodal");
    }
    
    // test OK
    private void testMETIS_NodeND(){
        System.out.println("\n=============================================\n");
        System.out.println("\n test JMetis.METIS_NodeND \n");
        /*
        0 -1 -2 -3 -4 
        |  |  |  |  | 
        5 -6 -7 -8 -9 
        |  |  |  |  | 
        10-11-12-13-14 
        xadj(15) = 0 2 5 8 11 13 16 20 24 28 31 33 36 39 42 44
        adjncy(44) = 1 5 0 2 6 1 3 7 2 4 8 3 9 0 6 10 1 5 7 11 2 6 8 12 3 7 9 13 4 8 14 5 11 6 10 12 7 11 13 8 12 14 9 13
        */
        int numVertices = 15;
        int numEdges = 22;
        int[] xadj = {0,2,5,8,11,13,16,20,24,28,31,33,36,39,42,44};
        int[] adjncy ={1,5,0,2,6,1,3,7,2,4,8,3,9,0,6,10,1,5,7,11,2,6,8,12,3,7,9,13,4,8,14,5,11,6,10,12,7,11,13,8,12,14,9,13};
        int[] perm = new int[numVertices];
        int[] iperm = new int[numVertices];
        MetisOptions options = new MetisOptions();
        options.METIS_OPTION_OBJTYPE = MetisOptions.METIS_OBJTYPE_NODE;
        options.METIS_OPTION_IPTYPE = MetisOptions.METIS_IPTYPE_EDGE;
        options.METIS_OPTION_CTYPE = MetisOptions.METIS_CTYPE_SHEM;
        options.METIS_OPTION_RTYPE = MetisOptions.METIS_RTYPE_SEP1SIDED;
        options.METIS_OPTION_NO2HOP = 1;
        options.METIS_OPTION_NSEPS = 10;
        options.METIS_OPTION_NITER = 10;
        options.METIS_OPTION_SEED = 10;
        options.METIS_OPTION_UFACTOR = 30;
        options.METIS_OPTION_COMPRESS = 0;
        options.METIS_OPTION_CCORDER = 0;
        options.METIS_OPTION_PFACTOR = 60;
        options.METIS_OPTION_NUMBERING = 0;
        options.METIS_OPTION_DBGLVL = MetisOptions.METIS_DBG_INFO;
        
        checkResult(
                JMetis.METIS_NodeND(new int[]{numVertices}, xadj, adjncy, null, options.getOptions(), perm, iperm),
                "METIS_NodeND");
        System.out.println("perm " + Arrays.toString(perm));
        System.out.println("iperm " + Arrays.toString(iperm));
    }
    
    // test OK
    private void testMETIS_MeshToDual(){
        /*
        1--------------3
        | \    e1    / | \ 
        |   \      /   |   \ 
        |     \  /     |    \
        |  e0   2   e2 | e5  4
        |     / | \    |     /
        |   /   |   \  |   /
        | / e4  | e3  \| /
        0-------6------5
        mesh file
        6 7 // 6 elements, 7 vertices
        0, 1, 2 // e0
        1, 2, 3 // e1
        2, 3, 5 // e2
        2, 6, 5 // e3
        0, 2, 6 // e4
        3, 4, 5 // e5
        dual graph file (elementary graph)
        6 10 // n=6 elements, m=13 edges
        1 2 3 4 // e0
        0 2 3 4 5 // e1
        0 1 3 5 6 // e2
        0 1 2 5 4 // e3
        0 1 2 3 // e4
        1 2 3 // e5
        */
        
        System.out.println("\n=============================================\n");
        System.out.println("\n test METIS_MeshToDual \n");
        int[] nbrElem = {6};
        int[] nbrNode = {7};        
        int[] elemPtr = new int[]{0, 3, 6, 9, 12, 15, 18};  // dim = nbrElem + 1
        int[] elemInd = new int[]{0, 1, 2, 1, 2, 3, 2, 3, 5, 2, 6, 5, 0, 2, 6, 3, 4, 5};
        int[] ncommon = {1};
        int[] nbrFlag = {0};
        /*
        int[] objval = {0};
        
        MetisOptions options = new MetisOptions();
        
        options.METIS_OPTION_PTYPE = MetisOptions.METIS_PTYPE_KWAY;
        options.METIS_OPTION_OBJTYPE = MetisOptions.METIS_OBJTYPE_CUT;
        options.METIS_OPTION_CTYPE = MetisOptions.METIS_CTYPE_SHEM;
        options.METIS_OPTION_IPTYPE = MetisOptions.METIS_IPTYPE_METISRB;
        options.METIS_OPTION_RTYPE = MetisOptions.METIS_RTYPE_GREEDY;
        options.METIS_OPTION_NCUTS = 1;
        options.METIS_OPTION_NITER = 10;
        options.METIS_OPTION_CONTIG = 0;
        options.METIS_OPTION_MINCONN= 0;
        options.METIS_OPTION_NOOUTPUT = 0;
        options.METIS_OPTION_SEED = -1;
        options.METIS_OPTION_GTYPE = MetisOptions.METIS_GTYPE_NODAL;
        options.METIS_OPTION_NCOMMON = 1;
        options.METIS_OPTION_UFACTOR = 30;
        options.METIS_OPTION_NUMBERING = 0;
        options.METIS_OPTION_DBGLVL = MetisOptions.METIS_DBG_INFO;
        
        System.out.println("Options " + Arrays.toString(options.getOptions()));
        */
        MetisPointer xadj = new MetisPointer();
        MetisPointer adjncy = new MetisPointer();
        
        int res = JMetis.METIS_MeshToDual(nbrElem, nbrNode, elemPtr, elemInd, ncommon, nbrFlag, xadj, adjncy);
        checkResult(res, "METIS_MeshToDual");
        
        System.out.println("ElemPtr: " + Arrays.toString(elemPtr));
        System.out.println("ElemInd: " + Arrays.toString(elemInd));
        System.out.println("xadj: " + Arrays.toString(xadj.getData()));
        System.out.println("adjncy: " + Arrays.toString(adjncy.getData()));
        
    }
    
    // test OK
    private void testMETIS_MeshToNodal() {
        /*
        1--------------3
        | \    e1    / | \ 
        |   \      /   |   \ 
        |     \  /     |    \
        |  e0   2   e2 | e5  4
        |     / | \    |     /
        |   /   |   \  |   /
        | / e4  | e3  \| /
        0-------6------5
        mesh file
        6 7 // 6 elements, 7 vertices
        0, 1, 2 // e0
        1, 2, 3 // e1
        2, 3, 5 // e2
        2, 6, 5 // e3
        0, 2, 6 // e4
        3, 4, 5 // e5
        nodal graph file
        7 10 // 7 vertives, 10 edges
        1 2 6 // v0
        0 2 3 // v1
        0 1 3 5 6 // v2
        1 2 5 4 // v3
        3 5 // v4
        2 3 6 4 // v5
        2 5 0 // v6
        */
        
        System.out.println("\n=============================================\n");
        System.out.println("\n test METIS_MeshToNodal \n");
        int[] nbrElem = {6};
        int[] nbrNode = {7};
        int[] nbrFlag = {0};
        int[] elemPtr = new int[]{0, 3, 6, 9, 12, 15, 18};  // dim = nbrElem + 1
        int[] elemInd = new int[]{0, 1, 2, 1, 2, 3, 2, 3, 5, 2, 6, 5, 0, 2, 6, 3, 4, 5};
        /*
        int[] objval = {0};
        
        MetisOptions options = new MetisOptions();
        
        options.METIS_OPTION_PTYPE = MetisOptions.METIS_PTYPE_KWAY;
        options.METIS_OPTION_OBJTYPE = MetisOptions.METIS_OBJTYPE_CUT;
        options.METIS_OPTION_CTYPE = MetisOptions.METIS_CTYPE_SHEM;
        options.METIS_OPTION_IPTYPE = MetisOptions.METIS_IPTYPE_METISRB;
        options.METIS_OPTION_RTYPE = MetisOptions.METIS_RTYPE_GREEDY;
        options.METIS_OPTION_NCUTS = 1;
        options.METIS_OPTION_NITER = 10;
        options.METIS_OPTION_CONTIG = 0;
        options.METIS_OPTION_MINCONN= 0;
        options.METIS_OPTION_NOOUTPUT = 0;
        options.METIS_OPTION_SEED = -1;
        options.METIS_OPTION_GTYPE = MetisOptions.METIS_GTYPE_NODAL;
        options.METIS_OPTION_NCOMMON = 1;
        options.METIS_OPTION_UFACTOR = 30;
        options.METIS_OPTION_NUMBERING = 0;
        options.METIS_OPTION_DBGLVL = MetisOptions.METIS_DBG_TIME;
        
        System.out.println("Options " + Arrays.toString(options.getOptions()));
        */
        MetisPointer xadj = new MetisPointer();
        MetisPointer adjncy = new MetisPointer();
        
        int res = JMetis.METIS_MeshToNodal(nbrElem, nbrNode, elemPtr, elemInd, nbrFlag, xadj, adjncy);
        checkResult(res, "METIS_PartMeshNodal");
        
        System.out.println("ElemPtr: " + Arrays.toString(elemPtr));
        System.out.println("ElemInd: " + Arrays.toString(elemInd));
        System.out.println("xadj: " + Arrays.toString(xadj.getData()));
        System.out.println("adjncy: " + Arrays.toString(adjncy.getData()));
        
    }
}
