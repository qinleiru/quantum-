package com.quantum.tools;

import com.quantum.state.DoubleState;
import com.quantum.state.QuantumState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

public class QuantumTools {
    /**
     * 以二进制形式输出量子态，第一个参数符号为正好
     * @return
     */
    public static String showBinaryState(QuantumState quantumState) {
        int particles=quantumState.getParticles();
        double []states=quantumState.getState();
        String result="";
        for(int i=0;i<Math.pow(2,particles);i++){
            String str= Tools.toFixedLenBinary(i,particles);
            if(states[i]>0&&result.length()!=0) {
                result =result+"+"+ states[i] + "|" + str + ">";
            }
            if (states[i]>0&&result.length()==0){
                result =result+ states[i] + "|" + str + ">";
            }
            else if(states[i]<0){
                result =result+ states[i] + "|" + str + ">";
            }
        }
/**
 *      输出对应的粒子的名称
 */
//        ArrayList<String> arrayList=quantumState.getParticlesName();
//        Iterator iterator=arrayList.iterator();
//        result+="  对应的粒子为";
//        while(iterator.hasNext()){
//            result+=iterator.next()+"  ";
//        }
        return result;
    }

    /**
     * 判断一个字符串形式是否表示一个两量子比特的量子态
     * @param doubleStateStr
     * @return
     */
    public static boolean isDoubleState(String doubleStateStr){
        String pattern="[3,5]";
        return Pattern.matches(pattern,doubleStateStr);
    }

    /**
     * 将字符串表示的二进制形式转换为两量子比特
     * @param doubleStateStr
     * @return
     */
    public static DoubleState transferToDoubleState(String doubleStateStr){
        DoubleState doubleState=null;
        return doubleState;
    }

    public static void main(String []args){
        String testStr="-1";
        System.out.println(isDoubleState(testStr));
    }
}
