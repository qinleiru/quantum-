package com.quantum;

import com.quantum.state.QuantumState;
import com.quantum.state.SingleState;
import org.junit.Test;
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Random;

public class MainTest extends AbstractMain{
    /**
     * 随机生成指定范围内固定位数的小数
     * @param args
     */
    public static void main(String[] args) {
        MainTest mainTest=new MainTest();
        mainTest.getInformation(new SingleState());
        System.out.println(mainTest.secretState.getParticles());
    }

    public static double randomDecimal(double min,double max,int scl){
        int pow = (int) Math.pow(10, scl);//指定小数位
        double one = Math.floor((Math.random() * (max - min) + min) * pow) / pow;
        return one;
    }

    @Override
    public void getInformation(SingleState singleState) {
        secretState=singleState;
    }
}
