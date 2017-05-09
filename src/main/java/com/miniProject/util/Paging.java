package com.miniProject.util;

import java.util.ArrayList;  
import java.util.Collection;  
import java.util.Vector;  
  
public class Paging {  
       
    /* 
     * 分页要素： 
     * 1、页总数                pageCount 
     * 2、总共显示的数据条数       rowCount 
     * 3、每页显示的数据条数       pageRowCount 
     * 4、当前页                curPage  
     * 5、得到的数据             data 
     */  

    /** 
     * 1、页总数                pageCount 
     */  
    private int pageCount;  
      
    /** 
     * 2、总共显示的数据条数  rowCount 
     */  
    private int rowCount;  
      
    /** 
     * 3、每页显示的数据条数  pageRowCount 
     */  
    private int pageRowCount;  
      
    /** 
     * 4、当前页                curPage  
     */  
    private int curPage;  
      
    /** 
     * 5、得到的数据          data 
     */  
    private Collection data;  
      
    /** 
     * 默认显示10条数据 
     */  
    public Paging(Collection data, int curPage) {  
        this.data = data;  
        this.curPage = curPage;  
        this.pageRowCount = 10;  
        this.rowCount = data.size();  
        this.pageCount = (int)Math.ceil((double)this.rowCount / (double)this.pageRowCount);  
    }  
      
    /** 
     * 自行设置每页显示的条数 
     */  
    public Paging(Collection data, int curPage, int pageRowCount) {  
        this.data = data;  
        this.curPage = curPage;  
        this.pageRowCount = pageRowCount;  
        this.rowCount = data.size();  
        this.pageCount = (int)Math.ceil((double)this.rowCount / (double)this.pageRowCount);  
    }  
      
    /** 
     * 当前要显示的数据取得 
     * @return 
     */  
    public Collection getData() {  
        Collection curData = null;  
          
        if(data != null) {  
            //定义每一页开始到最后的显示的行数  
            int start, end;  
            start = (this.curPage - 1) * this.pageRowCount;  
            //判断当前显示的数据是否足以显示一整页  
            if(start + this.pageRowCount > this.rowCount) {  
                end = this.rowCount;  
            } else {  
                end = start + this.pageRowCount;  
            }  
              
            ArrayList arrayData = null;  
            Vector vectorData = null;  
            ArrayList arrayCurData = new ArrayList();  
            Vector vectorCurData = new Vector();  
            boolean isArrayData = true;  
            //判断是属于哪一种集合  
            if(data instanceof ArrayList) {  
                arrayData = (ArrayList)data;  
                isArrayData = true;  
            } else if(data instanceof Vector) {  
                vectorData = (Vector)data;  
                isArrayData = false;  
            }  
            //循环得到每一页的数据  
            for(int i = start; i < end; i++) {  
                if(isArrayData) {  
                    arrayCurData.add(arrayData.get(i));  
                } else {  
                    vectorCurData.add(vectorData.get(i));  
                }  
            }  
              
            if(isArrayData) {  
                curData = arrayCurData;  
            } else {  
                curData = vectorCurData;  
            }  
        }  
          
        return curData;  
    }   
}
