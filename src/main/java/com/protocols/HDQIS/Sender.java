package com.protocols.HDQIS;

import com.protocols.role.AbstractSender;
import com.quantum.measure.ProjectiveMeasure;
import com.quantum.oparate.MathOperation;
import com.quantum.oparate.QuantumOperation;
import com.quantum.state.*;
import com.quantum.tools.QuantumTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.protocols.HDQIS.HDQIS.resultSAs;
import static com.protocols.HDQIS.HDQIS.systemStates;

public class Sender extends AbstractSender {
    public  String printMessage="";
    private ArrayList<HighAgent> highAgents;
    private ArrayList<LowAgent> lowAgents;
    private String binarySecret;

    public Sender(ArrayList<HighAgent> highAgents, ArrayList<LowAgent> lowAgents,String binarySecret){
        this.highAgents=highAgents;
        this.lowAgents=lowAgents;
        this.binarySecret=binarySecret;
        secretStates=new ArrayList<>();
        entangledStates=new ArrayList<>();
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
//        for (int i = 0; i < sendMessage.length; i++) {
//            System.out.println(sendMessage[i]);
//        }
        int index=0;
        for (int i = 0; i < sendMessage.length; i++) {
            for (int j = 0; j < sendMessage[i].length(); j++) {
                if(sendMessage[i].charAt(j)=='0'){
                    SingleState singleState=new SingleState(new double[]{1,0});
                    singleState.setParticlesName(1,"S"+index);
                    secretStates.add(singleState);
                    index++;
                }
                if (sendMessage[i].charAt(j)=='1'){
                    SingleState singleState=new SingleState(new double[]{0,1});
                    singleState.setParticlesName(1,"S"+index);
                    secretStates.add(singleState);
                    index++;
                }
            }
        }
        /**
         * 测试代码
         */
//        //遍历ArrayList中的元素
//        for (int i = 0; i < secretStates.size(); i++) {
//                System.out.println(QuantumTools.showBinaryState(secretStates.get(i)));
//        }
    }

    /**
     * 构造四粒子纠缠态序列，用于构建量子信道
     */
    public void prepareState(){
        //首先构造使用的四粒子纠缠态，根据论文中的公式
        double[]state0=CommonState.Zero_State.getState();
        double[]state1=CommonState.One_State.getState();
        double[] base1= MathOperation.tensor(new double[][]{state0,state0,state0,state0});
        double[] base2=MathOperation.multiple(MathOperation.tensor(new double[][]{state0,state0,state1,state1}),-1);
        double[] base3=MathOperation.multiple(MathOperation.tensor(new  double[][]{state0,state1,state0,state1}),-1);
        double[] base4=MathOperation.tensor(new double[][]{state0,state1,state1,state0});
        double[] base5=MathOperation.tensor(new double[][]{state1,state0,state0,state1});
        double[] base6=MathOperation.tensor(new double[][]{state1,state0,state1,state0});
        double[] base7=MathOperation.tensor(new double[][]{state1,state1,state0,state0});
        double[] base8=MathOperation.tensor(new double[][]{state1,state1,state1,state1});
        double[] state=MathOperation.add(new double[][]{base1,base2,base3,base4,base5,base6,base7,base8});
        state=MathOperation.multiple(state,0.5*Math.pow(2,-0.5));
        for(int i=0;i<secretStates.size();i++){
            ClusterState clusterState=new ClusterState(state,4);
            clusterState.setParticlesName(1,"A"+i);
            clusterState.setParticlesName(2,"B"+i);
            clusterState.setParticlesName(3,"C"+i);
            clusterState.setParticlesName(4,"D"+i);
            entangledStates.add(clusterState);
        }
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        printMessage+=df.format(System.currentTimeMillis())+" "+ "Alice准备量子信道需要的纠缠态序列\n";
    }

    /**
     * 给三个代理者发送需要的粒子序列
     */
    public void send(){
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //发送粒子给高权限的代理者
        ArrayList<String> particlesName1=new ArrayList<>();
        for(int i=0;i<secretStates.size();i++) {
            particlesName1.add("D"+i);
        }
        highAgents.get(0).recieveParticles(particlesName1);
        printMessage+=df.format(System.currentTimeMillis())+" "+ "Alice将粒子序列D发送给权限高的代理者Diana\n";
        //发送粒子给权限低的代理者
        ArrayList<String> particlesName2=new ArrayList<>();
        for(int i=0;i<secretStates.size();i++) {
            particlesName2.add("B"+i);
        }
        lowAgents.get(0).recieveParticles(particlesName2);
        printMessage+=df.format(System.currentTimeMillis())+" "+ "Alice将粒子序列B发送给权限低的代理者Bob\n";
        ArrayList<String> particlesName3=new ArrayList<>();
        for(int i=0;i<secretStates.size();i++) {
            particlesName3.add("C"+i);
        }
        lowAgents.get(1).recieveParticles(particlesName3);
        printMessage+=df.format(System.currentTimeMillis())+" "+ "Alice将粒子序列C发送给权限低的代理者Charlie\n";
    }

    /**
     * 对手中的粒子进行Bell态测量，并公布测量结果
     */
    public void measure() {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //整个系统的初始状态为
        for(int i=0;i<secretStates.size();i++){
            systemStates.add(QuantumOperation.quantumTensor(secretStates.get(i),entangledStates.get(i)));
        }
        /*
           测试代码
         */
//        System.out.println("初始状态系统的态为");
//        System.out.println(systemState.showBinaryState());
//        systemState.showParticleName();
        //Alice对粒子x和粒子A，进行Bell态测量，并公布测量结果
        for(int i=0;i<systemStates.size();i++){
            resultSAs.add(ProjectiveMeasure.measureBeseBell(systemStates.get(i),"S"+i,"A"+i));
        }
        printMessage+=df.format(System.currentTimeMillis())+" "+ "Alice对手中的粒子序列S、粒子序列A进行Bell态测量，并公布测量结果";
        /*
           此部分的代码是测试的内容
         */
//        System.out.println("发送者测量完成之后系统的态为");
//        System.out.println(systemState.showBinaryState());
//        systemState.showParticleName();
    }
}
