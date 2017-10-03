package testJMetis;

import java.util.Arrays;
import jmetis.JMetis;
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
        // create a Metis array of options
        test.checkMetisOptions();
        // test default options
        test.testSetDefaultOptions();
        // convert a Mesh Partition
        test.testMeshPartNodal();
        // convert a Mesh to a Graph
        test.testMesh2NodalGraph();
    }
    
    private void checkMetisOptions(){
        MetisOptions options = new MetisOptions();
        options.METIS_OPTION_PTYPE = 0;
        System.out.println("Default options " + Arrays.toString(options.getDefaultOptions()));
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
    
    private void testSetDefaultOptions(){
        System.out.println("\n=============================================\n");
        System.out.println("\n test JMetis.METIS_SetDefaultOptions \n");
        MetisOptions options = new MetisOptions();
        
        System.out.println("Options init in Java " + Arrays.toString(options.getOptions()));
        checkResult(JMetis.METIS_SetDefaultOptions(options), "METIS_SetDefaultOptions");
        System.out.println("Options set in C by METIS " + Arrays.toString(options.getOptions()));
    }

    private void testMeshPartNodal() {
        System.out.println("\n=============================================\n");
        System.out.println("\n Test JMetis.partMeshNodal \n");
        int[] nbrElem = {6};
        int[] nbrNode = {7};
        int[] nbrPart = {2};
        int[] elemPtr = new int[]{0, 3, 6, 9, 12, 15, 18};  // dim = nbrElem + 1
        int[] elemInd = new int[]{1, 2, 3, 2, 3, 4, 3, 4, 6, 3, 7, 6, 1, 3, 7, 4, 5, 6};    // dim = nbrElem * nbrNodePerElem
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
        
        int res = JMetis.partMeshNodal(6, 7, elemPtr, elemInd, null, null, 2, null, options.getOptions(), objval, epart, npart);
        checkResult(res, "METIS_PartMeshNodal");
        
        System.out.println("objval: " + Arrays.toString(objval));
        System.out.println("ElemPtr: " + Arrays.toString(elemPtr));
        System.out.println("ElemInd: " + Arrays.toString(elemInd));
        System.out.println("Epart: " + Arrays.toString(epart));
        System.out.println("Npart: " + Arrays.toString(npart));
    }
    
    private void testMesh2NodalGraph() {
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
        graph file
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
        System.out.println("\n testMesh2NodalGraph \n");
        int[] nbrElem = {6};
        int[] nbrNode = {7};
        int[] nbrFlag = {0};
        int[] elemPtr = new int[]{0, 3, 6, 9, 12, 15, 18};  // dim = nbrElem + 1
        int[] elemInd = new int[]{0, 1, 2, 1, 2, 3, 2, 3, 5, 2, 6, 5, 0, 2, 6, 3, 4, 5};
        //int[] xadj = new int[nbrNode[0] + 1];
        //int[] adjncy = new int[nbrElem[0]];
        //int[] xadj = null;
        //int[] adjncy = null;
        
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
        MetisPointer xadj = new MetisPointer();
        MetisPointer adjncy = new MetisPointer();
        
        int res = JMetis.METIS_MeshToNodal(nbrElem, nbrNode, elemPtr, elemInd, nbrFlag, xadj, adjncy);
        checkResult(res, "METIS_PartMeshNodal");
        
        System.out.println("objval: " + Arrays.toString(objval));
        System.out.println("ElemPtr: " + Arrays.toString(elemPtr));
        System.out.println("ElemInd: " + Arrays.toString(elemInd));
        System.out.println("xadj: " + Arrays.toString(xadj.getData()));
        System.out.println("adjncy: " + Arrays.toString(adjncy.getData()));
        
    }
}
