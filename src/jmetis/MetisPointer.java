package jmetis;

/**
 * to manage the mesh data (element-NodeIndex) or graph data (Node-AdjacentNodeIndex)
 * @author dinhvan
 */
public class MetisPointer {
    
    private int[] data;
    
    public MetisPointer(){
    }
    
    public MetisPointer(int[] arr){
        this.data = arr;
    }
    
    public int[] getData(){
        return this.data;
    }
    
    public void setData(int[] arr){
        this.data = arr;
    }
}
