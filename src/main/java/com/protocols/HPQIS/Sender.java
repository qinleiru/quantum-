package com.protocols.HPQIS;

import com.protocols.role.AbstractSender;
import com.quantum.gate.QuantumGate;
import com.quantum.measure.ProjectiveMeasure;
import com.quantum.oparate.QuantumOperation;
import com.quantum.state.*;
import com.quantum.tools.QuantumTools;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.protocols.HPQIS.HPQIS.*;

//概率型分层量子信息拆分协议
//发送者的角色
public class Sender extends AbstractSender {
    private ArrayList<HighAgent> highAgents;
    private ArrayList<LowAgent> lowAgents;
    private String binarySecret;
    public  String printMessage="";


    public Sender(ArrayList<HighAgent> highAgents, ArrayList<LowAgent> lowAgents,String binarySecret){
        this.highAgents=highAgents;
        this.lowAgents=lowAgents;
        this.binarySecret=binarySecret;
        secretStates=new ArrayList<>();
        entangledStates=new ArrayList<>();
        String sendMessage[]=binarySecret.split(" ");
        for (int i = 0; i < sendMessage.length; i++) {
            while (sendMessage[i].length() < 16) {
                sendMessage[i] = "0" + sendMessage[i];
            }
        }
        for(int i=0;i<sendMessage.length*8;i++){
            resultFailPOVM.add(i);
        }
    }

    /**
     * 发送者准备秘密态用于代理者们的共享
     */
    public void secret() {
        //长度不够的补0
        String sendMessage[]=binarySecret.split(" ");
        for (int i = 0; i < sendMessage.length; i++) {
            while (sendMessage[i].length() < 16) {
                sendMessage[i] = "0" + sendMessage[i];
            }
        }
        int index=0;
        for (int i = 0; i < sendMessage.length; i++) {
            for (int j = 0; j < 8; j++) {
                String temp=sendMessage[i].substring(j*2, j*2 + 1);
                if(!resultFailPOVM.contains(i*8+j))  //index值
                {
                  break;
                }
                if("00".equals(temp)){
                    DoubleState doubleState=new DoubleState(new double[]{1,0,0,0});
                    doubleState.setParticlesName(1,"x"+index);
                    doubleState.setParticlesName(2,"y"+index);
                    secretStates.add(doubleState);
                    index++;
                }
                if ("01".equals(temp)){
                    DoubleState doubleState=new DoubleState(new double[]{0,1,0,0});
                    doubleState.setParticlesName(1,"x"+index);
                    doubleState.setParticlesName(2,"y"+index);
                    secretStates.add(doubleState);
                    index++;
                }
                if("10".equals(temp)){
                    DoubleState doubleState=new DoubleState(new double[]{0,0,1,0});
                    doubleState.setParticlesName(1,"x"+index);
                    doubleState.setParticlesName(2,"y"+index);
                    secretStates.add(doubleState);
                    index++;
                }
                if("11".equals(temp)){
                    DoubleState doubleState=new DoubleState(new double[]{0,0,0,1});
                    doubleState.setParticlesName(1,"x"+index);
                    doubleState.setParticlesName(2,"y"+index);
                    secretStates.add(doubleState);
                    index++;
                }
            }
        }
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        printMessage+=df.format(System.currentTimeMillis())+" "+ "Alice准备两量子比特的纠缠序列";
    }

    /**
     * 准备两个非最大纠缠态多粒子纠缠簇态，用于构建量子信道
     */
    public void prepareState(){
        /**
         * 根据论文指定的方法生成用于构建量子信道的两个簇态
         */
        int highParticle=this.highAgents.size();
        int lowParticle=this.lowAgents.size();
        //非最大纠缠态使用的系数
        double a=coefficients[0];
        double b=coefficients[1];
        double c=coefficients[2];
        double d=coefficients[3];
        DoubleState prepareState1=new DoubleState(new double[]{a,a,b,-b});
        DoubleState prepareState2=new DoubleState(new double[]{c,c,d,-d});
        //簇态1中控制粒子1的粒子名
        prepareState1.setParticlesName(1,"1");
        //簇态1中控制粒子2的粒子名
        prepareState1.setParticlesName(2,"3");
        //簇态2中控制粒子1的粒子名
        prepareState2.setParticlesName(1,"5");
        //簇态2中控制粒子2的粒子名
        prepareState2.setParticlesName(2,"7");
        System.out.println(QuantumTools.showBinaryState(prepareState1));
        System.out.println(QuantumTools.showBinaryState(prepareState2));

        //准备四个初始态为0的两粒子纠缠态
        SingleState singleState1= CommonState.Zero_State;
        singleState1.setParticlesName(1,"2");
        MultiState multiState1=QuantumOperation.quantumTensor(prepareState1,singleState1);
        SingleState singleState2=CommonState.Zero_State;
        singleState2.setParticlesName(1,"4");
        multiState1=QuantumOperation.quantumTensor(multiState1,singleState2);

        SingleState singleState3= CommonState.Zero_State;
        singleState3.setParticlesName(1,"6");
        MultiState multiState2=QuantumOperation.quantumTensor(prepareState2,singleState3);
        SingleState singleState4=CommonState.Zero_State;
        singleState4.setParticlesName(1,"8");
        multiState2=QuantumOperation.quantumTensor(multiState2,singleState4);
        /**
         * 测试
         */
        System.out.println(QuantumTools.showBinaryState(multiState1));
        System.out.println(QuantumTools.showBinaryState(multiState2));
        //进行CNOT操作,高等级的用粒子1做控制门，低等级用粒子2做控制门
        QuantumOperation.quantumDoublePerform(multiState1,"1","2",QuantumGate.Operator_CNOT);
        QuantumOperation.quantumDoublePerform(multiState1,"3","4",QuantumGate.Operator_CNOT);
        QuantumOperation.quantumDoublePerform(multiState2,"5","6",QuantumGate.Operator_CNOT);
        QuantumOperation.quantumDoublePerform(multiState2,"7","8",QuantumGate.Operator_CNOT);
        /**
         * 测试
         */
        System.out.println(QuantumTools.showBinaryState(multiState1));
        System.out.println(QuantumTools.showBinaryState(multiState2));

        for(int i=0;i<secretStates.size();i++){
            ClusterState clusterState1=new ClusterState(multiState1.getState(),multiState1.getParticles());
            clusterState1.setParticlesName(1,"1"+i);
            clusterState1.setParticlesName(2,"2"+i);
            clusterState1.setParticlesName(3,"3"+i);
            clusterState1.setParticlesName(4,"4"+i);
            entangledStates.add(clusterState1);
            ClusterState clusterState2=new ClusterState(multiState1.getState(),multiState1.getParticles());
            clusterState2.setParticlesName(1,"5"+i);
            clusterState2.setParticlesName(2,"6"+i);
            clusterState2.setParticlesName(3,"7"+i);
            clusterState2.setParticlesName(4,"8"+i);
            entangledStates.add(clusterState2);
        }
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        printMessage+=df.format(System.currentTimeMillis())+" "+ "Alice准备量子信道需要的两个簇态序列";
    }

    /**
     * 给相应的代理者发送需要的粒子
     */
    public void send(){
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ArrayList<String> particlesName1=new ArrayList<>();
        for(int i=0;i<secretStates.size();i++) {
            particlesName1.add("2"+i);
            particlesName1.add("6"+i);
        }
        highAgents.get(0).recieveParticles(particlesName1);
        printMessage+=df.format(System.currentTimeMillis())+" "+ "Alice将粒子序列2、粒子序列6发送给权限高的代理者Bob\n";
        ArrayList<String> particlesName2=new ArrayList<>();
        for(int i=0;i<secretStates.size();i++) {
            particlesName2.add("3"+i);
            particlesName2.add("7"+i);
        }
        lowAgents.get(0).recieveParticles(particlesName2);
        printMessage+=df.format(System.currentTimeMillis())+" "+ "Alice将粒子序列3、粒子序列7发送给权限低的代理者Charie\n";
        ArrayList<String> particlesName3=new ArrayList<>();
        for(int i=0;i<secretStates.size();i++) {
            particlesName3.add("4"+i);
            particlesName3.add("8"+i);
        }
        lowAgents.get(1).recieveParticles(particlesName3);
        printMessage+=df.format(System.currentTimeMillis())+" "+ "Alice将粒子序列4、粒子序列8发送给权限低的代理者David\n";
    }

    /**
     * 对手中的粒子进行Bell态测量，并公布测量结果
     */
    public void measure() {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //整个系统的初始状态为
        for(int i=0;i<secretStates.size();i++){
            systemStates.add(QuantumOperation.quantumTensor(secretStates.get(i),entangledStates.get(i)));
            QuantumOperation.quantumTensor(QuantumOperation.quantumTensor(secretStates.get(i),entangledStates.get(i*2)),entangledStates.get(i*2+1));
        }
        for(int i=0;i<systemStates.size();i++){
            resultXs.add(ProjectiveMeasure.measureBeseBell(systemStates.get(i),"x"+i,"1"+i));
            resultYs.add(ProjectiveMeasure.measureBeseBell(systemStates.get(i),"y"+i,"5"+i));
        }
        printMessage+=df.format(System.currentTimeMillis())+" "+ "Alice对手中的粒子序列x、粒子序列1与粒子序列y、粒子序列5进行Bell态测量，并公布测量结果";
    }
}
