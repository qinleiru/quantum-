package com.protocols.HPQIS;

import com.protocols.dao.impl.HpqisDaoImpl;
import com.protocols.pojo.Hpqis;
import com.quantum.measure.Measures;
import com.quantum.measure.ProjectiveMeasure;
import com.quantum.oparate.MathOperation;
import com.quantum.oparate.QuantumOperation;
import com.quantum.state.DoubleState;
import com.quantum.state.MultiState;
import com.quantum.state.QuantumState;
import com.quantum.state.SingleState;
import com.quantum.tools.QuantumTools;
import com.quantum.tools.Tools;
import com.view.component.TextComponent;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

//下面仿真本论文中提出的概率型分层量子信息拆分协议
//todo：目前只实现了1个权限高的代理者2个权限低的代理者
//todo：用于传送的秘密量子信息为固定的
public class HPQIS {
    /**
     * 定义变量用于存储测量结果
     */
    public static ArrayList<MultiState> systemStates = new ArrayList<>();  //记录系统的态
    public static ArrayList<Integer> resultXs = new ArrayList<>();            //对粒子x、粒子1进行Bell态的测量结果
    public static ArrayList<Integer> resultYs = new ArrayList<>();            //对粒子y、粒子5进行Bell态的测量结果
    public static ArrayList<Integer> resultFailPOVM=new ArrayList<>();            //保存对每一对秘密量子比特的测量结果
    public static ArrayList<Integer> resultPOVM=new ArrayList<>();            //保存对每一对秘密量子比特的测量结果
    public static final double[] coefficients = getCoefficients();//构建量子信道的两个簇态的系数，分别对应a、b、c、d
    public static final double OMEGA = 1.5;

    public static DoubleState run(TextComponent textArea, String agents, String binarySecret) {
//        systemStates.clear();
//        resultXs.clear();
//        resultYs.clear();
//        ArrayList<HighAgent> highAgents = new ArrayList<>();   //用于存储权限高的代理者
//        ArrayList<LowAgent> lowAgents = new ArrayList<>();    //用于存储权限低的代理者
//        HighAgent Bob = new HighAgent();
//        highAgents.add(Bob);
//        LowAgent Charlie = new LowAgent();
//        LowAgent David = new LowAgent();
//        lowAgents.add(Charlie);
//        lowAgents.add(David);
//        Sender Alice = new Sender(highAgents, lowAgents, binarySecret);
//        resultFailPOVM.clear();
//        HashMap<Integer,DoubleState> recieveStates = new HashMap<>();   //记录收到粒子的位置，便于对失败的部分进行重新发送
//        Alice.execute();  //Alice进行了操作,包括准备秘密量子比特、准备两个簇态、发送粒子以及测量等操作
//        textArea.setCommText(Alice.printMessage);
//        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        if ("Bob".equals(agents)) {
//            //协议中的权限高的代理者来恢复秘密量子比特
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "权限高的代理者Bob恢复秘密消息");
//            int random = Tools.randInt(1, 2);
//            if (random == 1) {
//                textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie对手中的粒子序列进行Z基测量");
//                Charlie.helpRestore(Measures.Z,Bob);
//                textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie将测量结果发送给Bob");
//            } else {
//                textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David对手中的粒子序列进行Z基测量");
//                David.helpRestore(Measures.Z,Bob);
//                textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David将测量结果发送给Bob");
//            }
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob收到代理者测量结果");
//            Bob.restore();
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob引入辅助粒子m、辅助粒子n执行POVM测量并进行操作恢复秘密量子比特");
//            while(resultFailPOVM.size()!=0){
//                Alice.execute();
//            }
//            if (Bob.POVMResult == false) {
//                textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob进行POVM测量失败,两量子比特传送失败\n");
////                Hpqis hpqis = new Hpqis(coefficients[0], coefficients[1], coefficients[2], coefficients[3], OMEGA, 0, 1);
////                HpqisDaoImpl hpqisDAO = new HpqisDaoImpl();
////                hpqisDAO.addResult(hpqis);
//                return null;
//            }
////            Hpqis hpqis = new Hpqis(coefficients[0], coefficients[1], coefficients[2], coefficients[3], OMEGA, 1, 1);
////            HpqisDaoImpl hpqisDAO = new HpqisDaoImpl();
////            hpqisDAO.addResult(hpqis);
////            recieveState = getOwnSate(systemState, Bob.particleName);
////            if(recieveState==null){
////                System.out.println("返回值是空的");
////            }
////            MathOperation.normalization(recieveState.getState());
////            System.out.println("Bob手中的粒子态变为" + QuantumTools.showBinaryState(recieveState));
////            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob手中的粒子态为" + QuantumTools.showBinaryState(getOwnSate(systemState, Bob.particleName)));
//        } else if ("Charlie".equals(agents)) {
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie来恢复秘密量子比特");
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob对手中的粒子序列进行X基测量");
//            Bob.helpRestore(Measures.X,Charlie);
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob将测量结果发送给Charlie");
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David对手中的粒子序列进行X基测量");
//            David.helpRestore(Measures.X,Charlie);
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David将测量结果发送给Charlie");
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie收到代理者测量结果");
//            Charlie.restore();
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie引入辅助粒子m、辅助粒子n执行POVM测量并进行操作恢复秘密量子比特");
//            if (Charlie.POVMResult == false) {
//                textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie进行POVM测量失败,两量子比特传送失败\n");
//                System.out.println("Charlie进行POVM测量失败");
////                Hpqis hpqis = new Hpqis(coefficients[0], coefficients[1], coefficients[2], coefficients[3], OMEGA, 0, 0);
////                HpqisDaoImpl hpqisDAO = new HpqisDaoImpl();
////                hpqisDAO.addResult(hpqis);
//                return null;
//            }
////            Hpqis hpqis = new Hpqis(coefficients[0], coefficients[1], coefficients[2], coefficients[3], OMEGA, 1, 0);
////            HpqisDaoImpl hpqisDAO = new HpqisDaoImpl();
////            hpqisDAO.addResult(hpqis);
//            recieveState = getOwnSate(systemState, Charlie.particleName);
//            if(recieveState==null){
//                System.out.println("返回值是空的");
//            }
//            MathOperation.normalization(recieveState.getState());
//            System.out.println("Charlie手中粒子态为" + QuantumTools.showBinaryState(recieveState));
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie手中的粒子态为" + QuantumTools.showBinaryState(getOwnSate(systemState, Charlie.particleName)));
//        } else {
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David来恢复秘密量子比特");
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob对手中的粒子进行X基测量");
//            Bob.helpRestore(Measures.X,David);
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob将测量结果发送给David");
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie对手中的粒子进行X基测量");
//            Charlie.helpRestore(Measures.X,David);
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie将测量结果发送给David");
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David收到代理者测量结果");
//            David.restore();
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David引入辅助粒子m、辅助粒子n执行POVM测量并进行操作恢复秘密量子比特");
//            if (David.POVMResult == false) {
//                System.out.println("David进行POVM测量失败,两量子比特传送失败");
//                textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David进行POVM测量失败，两量子比特传送失败\n");
////                Hpqis hpqis = new Hpqis(coefficients[0], coefficients[1], coefficients[2], coefficients[3], OMEGA, 0, 0);
////                HpqisDaoImpl hpqisDAO = new HpqisDaoImpl();
////                hpqisDAO.addResult(hpqis);
//                return null;
//            }
////            Hpqis hpqis = new Hpqis(coefficients[0], coefficients[1], coefficients[2], coefficients[3], OMEGA, 1, 0);
////            HpqisDaoImpl hpqisDAO = new HpqisDaoImpl();
////            hpqisDAO.addResult(hpqis);
//            recieveState = getOwnSate(systemState, David.particleName);
//            if(recieveState==null){
//                System.out.println("返回值是空的");
//            }
//            MathOperation.normalization(recieveState.getState());
//            System.out.println("David手中的粒子态为" + QuantumTools.showBinaryState(recieveState));
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David手中的粒子态为" + QuantumTools.showBinaryState(getOwnSate(systemState, David.particleName)));
//        }
//        System.out.println("----------------------------------------------------");
//        textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "两量子比特传送成功\n");
//        return recieveState;
        return null;
    }

    /**
     * 此函数需要进行验证
     *
     * @param quantumState
     * @param particleNames
     * @return
     */
    public static DoubleState getOwnSate(QuantumState quantumState, ArrayList<String> particleNames) {
        DoubleState state = new DoubleState();
        //设置对应位置的粒子的名称
        state.setParticlesName(1, particleNames.get(0));
        state.setParticlesName(2, particleNames.get(1));
        for(String desc:state.getParticlesName()){
            System.out.println(desc);
        }
        //交换粒子的位置,手中的粒子对应交换到前面
        String particle2 = quantumState.getParticlesName().get(1);
        if (!particle2.equals(particleNames.get(1))) {
            QuantumOperation.quantumSwap(quantumState, particle2, particleNames.get(1));
        }
        String particle1 = quantumState.getParticlesName().get(0);
        if (!particle1.equals(particleNames.get(0))) {
            QuantumOperation.quantumSwap(quantumState, particle1, particleNames.get(0));
        }
        System.out.println(QuantumTools.showBinaryState(quantumState));
        double[] ownState = new double[4];
        int particlesNum= quantumState.getParticles();
        System.out.println("粒子的数目为"+particlesNum);
        for (int i = 0; i < Math.pow(2,particlesNum); i++) {
            if (ProjectiveMeasure.isBitZero(i, 1, particlesNum) && ProjectiveMeasure.isBitZero(i, 2, particlesNum)&&quantumState.getState()[i]!=0) {
                if (ownState[0] == 0) {
                    ownState[0] = quantumState.getState()[i];
                } else {
                    return null;
                }
            }
            if (ProjectiveMeasure.isBitZero(i, 1, particlesNum) && ProjectiveMeasure.isBitOne(i, 2,particlesNum)&&quantumState.getState()[i]!=0) {
                if (ownState[1] == 0) {
                    ownState[1] = quantumState.getState()[i];
                } else {
                    return null;
                }
            }
            if (ProjectiveMeasure.isBitOne(i, 1,particlesNum) && ProjectiveMeasure.isBitZero(i, 2, particlesNum)&&quantumState.getState()[i]!=0) {
                if (ownState[2] == 0) {
                    ownState[2] = quantumState.getState()[i];
                } else {
                    return null;
                }
            }
            if (ProjectiveMeasure.isBitOne(i, 1, particlesNum) && ProjectiveMeasure.isBitOne(i, 2, particlesNum)&&quantumState.getState()[i]!=0) {
                if (ownState[3] == 0) {
                    ownState[3] = quantumState.getState()[i];
                } else {
                    return null;
                }
            }
        }
        state.setState(ownState);
        return state;
    }

    /**
     * 进行编码解码通信的过程
     *
     * @param textArea
     * @param agents
     * @param secret
     * @throws UnsupportedEncodingException
     */
    public static String sendSecret(TextComponent textArea, String agents, String secret) throws UnsupportedEncodingException {
        System.out.println("传送的秘密消息是"+secret);
        System.out.println(strToBinary(secret));
        run(textArea,agents, strToBinary(secret));
        /*String sendMessage[]=strToBinary(secret).split(" ");
        String commResult="";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        textArea.setCommText(" +++++++++++Alice准备纠缠信道++++++++++++");
        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice准备多个四粒子纠缠簇态");
        textArea.setCommText(coefficients[0]+"|0000>+"+coefficients[0]+"|0011>+"+coefficients[1]+"|1100>-"+coefficients[2]+"|1111>");
        textArea.setCommText(coefficients[2]+"|0000>+"+coefficients[2]+"|0011>+"+coefficients[3]+"|1100>-"+coefficients[3]+"|1111>");
        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice准备引诱粒子插入粒子序列中");
        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice将粒子序列D发送给David");
        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice将粒子序列B发送给Bob");
        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice将粒子序列C发送给Charlie");
        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice公布引诱粒子的位置以及量子态对应的基");
        textArea.setCommText(df.format(System.currentTimeMillis())  + " 代理者使用同样基测量引诱粒子并将测量结果发送给Alice");
        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice确认引诱粒子的状态与测量结果一致");
//        textArea.setCommText(df.format(System.currentTimeMillis())  + " 代理者使用Z基手中粒子进行测量并将结果发送给Alice");
        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice引诱粒子检测不通过");
//        textArea.setCommText(df.format(System.currentTimeMillis())  + " 信道中存在攻击，放弃通信");
        return "";*/
//        textArea.setCommText(" +++++++++++纠缠信道准备完成++++++++++++");
        //每一个字符，每个字符对应一个长度为16的二进制串

        /*
           截获重发攻击、伪造纠缠攻击
         */


        /*
           无攻击情况下的仿真
         */
//        for (int i = 0; i < sendMessage.length; i++) {
//            //判断每个字符对应的长度是否为16，若不是16前面补0
//            if (sendMessage[i].length()<16){
//                for(int l=1;l<=16-sendMessage[i].length();l++){
//                    sendMessage[i]="0"+sendMessage[i];
//                }
//            }
//            System.out.println(sendMessage[i]);
//            //每两个二进制取一次
//            for (int j = 0; j < sendMessage[i].length(); j+=2) {
//                DoubleState doubleState;
//                do {
//                    doubleState = run(textArea, agents, sendMessage[i].substring(j, j + 2));
//                }while (doubleState==null);
//                if (ProjectiveMeasure.measureBaseZ(doubleState, doubleState.getParticlesName().get(0)) == 0&&ProjectiveMeasure.measureBaseZ(doubleState, doubleState.getParticlesName().get(1)) == 0) {
//                    commResult += "00";
//                }
//                else if(ProjectiveMeasure.measureBaseZ(doubleState, doubleState.getParticlesName().get(0)) == 0&&ProjectiveMeasure.measureBaseZ(doubleState, doubleState.getParticlesName().get(1)) == 1) {
//                    commResult += "01";
//                }
//                else if(ProjectiveMeasure.measureBaseZ(doubleState, doubleState.getParticlesName().get(0)) == 1&&ProjectiveMeasure.measureBaseZ(doubleState, doubleState.getParticlesName().get(1)) == 0) {
//                    commResult += "10";
//                }
//                else if(ProjectiveMeasure.measureBaseZ(doubleState, doubleState.getParticlesName().get(0)) == 1&&ProjectiveMeasure.measureBaseZ(doubleState, doubleState.getParticlesName().get(1)) == 1) {
//                    commResult += "11";
//                }
//            }
//            commResult+=" ";
//        }

        /**
         * 信息泄露下的仿真
         */
//        for (int i = 0; i < sendMessage.length; i++) {
//            //判断每个字符对应的长度是否为16，若不是16前面补0
//            if (sendMessage[i].length()<16){
//                for(int l=1;l<=16-sendMessage[i].length();l++){
//                    sendMessage[i]="0"+sendMessage[i];
//                }
//            }
//            System.out.println(sendMessage[i]);
//            //每两个二进制取一次
//            for (int j = 0; j <1; j+=2) {
//                textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice检验簇态通过");
//                textArea.setCommText(df.format(System.currentTimeMillis())  + " 确认信道中没有攻击，进行通信");
//                return "";
//            }
//            commResult+=" ";
//        }
//        System.out.println("通信得到的二进制字符串的结果为" + commResult);
//        System.out.println("传送的秘密消息为" + binaryToStr(commResult));
//        return binaryToStr(commResult);
        return null;
    }

    public static String strToBinary(String str){
        char[] strChar=str.toCharArray();
        String result="";
        for(int i=0;i<strChar.length;i++){
            result +=Integer.toBinaryString(strChar[i])+" ";
        }
        return result;
    }

    public static String binaryToStr(String binary){
        String[] tempStr=binary.split(" ");
        char[] tempChar=new char[tempStr.length];
        for(int i=0;i<tempStr.length;i++) {
            tempChar[i]= binstrToChar(tempStr[i]);
        }
        return String.valueOf(tempChar);
    }
    private static int[] binstrToIntArray(String binStr) {
        char[] temp=binStr.toCharArray();
        int[] result=new int[temp.length];
        for(int i=0;i<temp.length;i++) {
            result[i]=temp[i]-48;
        }
        return result;
    }

    private static char binstrToChar(String binStr){
        int[] temp= binstrToIntArray(binStr);
        int sum=0;
        for(int i=0; i<temp.length;i++){
            sum +=temp[temp.length-1-i]<<i;
        }
        return (char)sum;
    }

    /**
     * 此方法用于返回构建信道的两个非最大纠缠态使用的系数
     *
     * @return
     */
    public static double[] getCoefficients() {
//        double a=Math.random();
//        double b=Math.random();
        double a = Math.pow(3,-0.5);
        double b = Math.sqrt(1-Math.pow(a,2));
        double c = Math.pow(3,-0.5);;
        double d = Math.sqrt(1-Math.pow(a,2));
        double clusterState1[] = new double[]{a, a, b, b};
        MathOperation.normalization(clusterState1);
        a = clusterState1[0];
        b = clusterState1[2];
        double clusterState2[] = new double[]{c, c, d, d};
        MathOperation.normalization(clusterState2);
        c = clusterState2[0];
        d = clusterState2[2];
        return new double[]{a, b, c, d};
    }

    /**
     * main函数用于向数据库中插入数据
     * @param args
     */
//    public static void main(String[] args) throws UnsupportedEncodingException {
//        //插入的数据包含a^2=c^2=1/4
//        //a^2=1/3,c^2=1/4i
//        //a^2=1/3,c^2=1/3
//        TextComponent textComponent=new TextComponent();
//        sendSecret(textComponent,"Bob","北邮");
////        System.out.println(binaryToStr("0101001100010111 1001000010101110"));
//    }
}
