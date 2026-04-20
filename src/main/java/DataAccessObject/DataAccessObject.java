package DataAccessObject;

import java.util.ArrayList;

public class DataAccessObject<T> {
    private ArrayList<T> dataList = new ArrayList<>();
    
    // CREATE (tambah data)
    public void add(T data){
        dataList.add(data);
    }
    
    // READ (ambil semua data)
    public ArrayList<T> getAll(){
        return dataList;
    }
    
    // DELETE (hapus data)
    public void delete(T data){
        dataList.remove(data);
    }
}
