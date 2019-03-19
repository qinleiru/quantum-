package com.quantum.gate;

public class MeasureResult {
    //返回结果是1表示测量结果|Ф>+态  返回结果是2表示测量结果|𝝭>+态   返回结果是3表示测量结果|Ф>-态   返回结果是4表示测量结果|𝝭>-态
    public static final String[] BellResult= new String[]{"|Ф>+","|𝝭>+","|Ф>-","|𝝭>-"};
    //返回结果是0表示测量结果|+>态  返回结果是1表示测量结果|->
    public static final String[] ZResult= new String[]{"|0>","|1>"};
    //返回结果是0表示测量结果|0>态  返回结果是1表示测量结果|1>
    public static final String[] XResult= new String[]{"|+>","|->"};
}
