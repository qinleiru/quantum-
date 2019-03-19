package com.protocols.HDQIS;

import com.quantum.gate.QuantumGate;
import com.quantum.measure.Measures;
import com.quantum.measure.ProjectiveMeasure;
import com.quantum.oparate.QuantumOperation;
import com.quantum.state.*;
import com.quantum.state.QuantumState;
import com.quantum.tools.QuantumTools;
import com.quantum.tools.Tools;
import com.view.component.TextComponent;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.quantum.measure.ProjectiveMeasure.isBitZero;

/*
    此协议有三个代理者，一个发送者,发送者Alice，代理者Bob，Charlie，David，其中David的权限高的代理者
 */
//下面仿真了2010年Wang等人提出的确定型分层量子信息通信过程
public class HDQIS {
    public static ArrayList<MultiState> systemStates = new ArrayList<>();  //记录系统的态
    public static ArrayList<Integer> resultSAs = new ArrayList<>();            //记录对粒子S、粒子A进行Bell态的测量结果

    public static ArrayList<SingleState> run(TextComponent textArea, String agents, String binarySecret) {
        systemStates.clear();
        resultSAs.clear();
        ArrayList<HighAgent> highAgents = new ArrayList<>();    //用于存储权限高的代理者
        HighAgent David = new HighAgent();
        highAgents.add(David);
        ArrayList<LowAgent> lowAgents = new ArrayList<>();        //用于存储权限低的代理者
        LowAgent Bob = new LowAgent();
        LowAgent Charlie = new LowAgent();
        lowAgents.add(Bob);
        lowAgents.add(Charlie);
        Sender Alice = new Sender(highAgents, lowAgents, binarySecret);
        Alice.execute();
        ArrayList<SingleState> recieveStates = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //对发送者Alice手中的两个粒子进行Bell态的测量
        textArea.setCommText(Alice.printMessage);
        if ("David".equals(agents)) {
            //协议中的权限高的代理者来恢复秘密量子比特
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David恢复秘密量子比特");
            int random = Tools.randInt(1, 2);
            if (random == 1) {
                System.out.println("Bob对手中的粒子序列进行X基测量,并将测量结果发送给David");
                Bob.helpRestore(Measures.X, David);
                textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob对手中的粒子序列进行X基测量");
                textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob将测量结果发送给David");
                for (int i = 0; i < systemStates.size(); i++) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), "C"+i, QuantumGate.Operator_H);
                }
            } else {
                System.out.println("Charlie对手中的粒子序列进行X基测量,并将测量结果发送给David");
                Charlie.helpRestore(Measures.X, David);
                textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie对手中的粒子序列进行X基测量");
                textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie将测量结果发送给David");
                for (int i = 0; i < systemStates.size(); i++) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), "B"+i, QuantumGate.Operator_H);
                }
            }
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David收到代理者测量结果");
            System.out.println("David对手中的粒子序列进行操作，恢复秘密量子比特");
            David.restore();
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David对手中的粒子序列进行操作，恢复秘密量子比特");
            for (int i = 0; i < systemStates.size(); i++) {
                ArrayList<String> particleName = new ArrayList<>();
                particleName.add("D" + i);
                recieveStates.add(getOwnSate(systemStates.get(i), particleName));
            }
            //遍历ArrayList中的元素
            for (int i = 0; i < recieveStates.size(); i++) {
                System.out.println(QuantumTools.showBinaryState(recieveStates.get(i)));
            }
//            recieveState = getOwnSate(systemState, David.particleName);
//            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David恢复的秘密量子比特" + QuantumTools.showBinaryState(recieveState));
        } else if ("Bob".equals(agents)) {
            //协议中权限低的代理者来恢复秘密量子比特
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob恢复秘密量子比特");
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie对手中的粒子序列进行Z基测量");
            Charlie.helpRestore(Measures.Z, Bob);
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie将测量结果发送给Bob");
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David对手中的粒子序列进行Z基测量");
            David.helpRestore(Measures.Z, Bob);
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David将测量结果发送给Bob");
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob收到代理者测量结果");
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob对手中的粒子序列进行操作，恢复秘密量子比特");
            Bob.restore();
            for (int i = 0; i < systemStates.size(); i++) {
                ArrayList<String> particleName = new ArrayList<>();
                particleName.add("B" + i);
                recieveStates.add(getOwnSate(systemStates.get(i), particleName));
            }
            //遍历ArrayList中的元素
            for (int i = 0; i < recieveStates.size(); i++) {
                System.out.println(QuantumTools.showBinaryState(recieveStates.get(i)));
            }
        } else {
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie恢复秘密量子比特");
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob对手中的粒子序列进行Z基测量");
            Bob.helpRestore(Measures.Z, Charlie);
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Bob将测量结果发送给Charlie");
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David对手中的粒子序列进行Z基测量");
            David.helpRestore(Measures.Z, Charlie);
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "David将测量结果发送给Charlie");
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie收到代理者测量结果");

            System.out.println("Charlie收到代理者测量结果");
            textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "Charlie对手中的粒子序列进行操作，恢复秘密量子比特");
            Charlie.restore();
            for (int i = 0; i < systemStates.size(); i++) {
                ArrayList<String> particleName = new ArrayList<>();
                particleName.add("C" + i);
                recieveStates.add(getOwnSate(systemStates.get(i), particleName));
            }
            //遍历ArrayList中的元素
            for (int i = 0; i < recieveStates.size(); i++) {
                System.out.println(QuantumTools.showBinaryState(recieveStates.get(i)));
            }
        }
        textArea.setCommText(df.format(System.currentTimeMillis()) + " " + "秘密消息传送完成");
        System.out.println("----------------------------------------------------");
        return recieveStates;
    }

    public static SingleState getOwnSate(QuantumState quantumState, ArrayList<String> particleNames) {
        SingleState state = new SingleState();
        QuantumOperation.quantumSwap(quantumState, particleNames.get(0), quantumState.getParticlesName().get(quantumState.getParticlesName().size() - 1));
        //交换位置之后系统的态为
        System.out.println(QuantumTools.showBinaryState(quantumState));
        double[] ownState = new double[2];
        try {
            for (int i = 0; i < Math.pow(2, quantumState.getParticles()); i++) {
                if (quantumState.getState()[i] != 0) {
                    if (isBitZero(i, quantumState.getParticles(), quantumState.getParticles())) {
                        if (ownState[0] == 0) {
                            ownState[0] = quantumState.getState()[i];
                        } else {
                            throw new Exception("恢复秘密消息失败");
                        }
                    } else {
                        if (ownState[1] == 0) {
                            ownState[1] = quantumState.getState()[i];
                        } else {
                            throw new Exception("恢复秘密消息失败");
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("恢复秘密消息失败");
            e.printStackTrace();
        }
        state.setState(ownState);
        state.setParticlesName(1, particleNames.get(0));
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
        System.out.println("传送的秘密消息是" + secret);
        System.out.println(strToBinary(secret));
        ArrayList<SingleState> receiveStates = run(textArea, agents, strToBinary(secret));
        String commResult = "";
        int flag = 0;
        for (int i = 0; i < receiveStates.size(); i++) {
            if (ProjectiveMeasure.measureBaseZ(receiveStates.get(i), receiveStates.get(i).getParticlesName().get(0)) == 0) {
                commResult += "0";
                flag++;
            } else {
                commResult += "1";
                flag++;
            }
            if (flag == 16) {
                flag = 0;
                commResult += " ";
            }
        }
        System.out.println(commResult);
        return binaryToStr(commResult);
    }
//        String sendMessage[]=strToBinary(secret).split(" ");
//        String commResult="";
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        textArea.setCommText(" +++++++++++Alice准备纠缠信道++++++++++++");
//        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice准备多个四粒子纠缠簇态");
//        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice将粒子序列D发送给David");
//        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice将粒子序列B发送给Bob");
//        textArea.setCommText(df.format(System.currentTimeMillis())  + " Alice将粒子序列C发送给Charlie");
//        textArea.setCommText(" +++++++++++纠缠信道准备完成++++++++++++");
//
//        //每一个字符
//        for (int i = 0; i < sendMessage.length; i++) {
//            //每个二进制位
//            for (int j = 0; j < sendMessage[i].length(); j++) {
//                SingleState singleState = run(textArea, agents, sendMessage[i].charAt(j));
//                if (ProjectiveMeasure.measureBaseZ(singleState, singleState.getParticlesName().get(0)) == 0) {
//                    commResult += "0";
//                } else {
//                    commResult += "1";
//                }
//            }
//            commResult+=" ";
//        }
//        System.out.println("通信得到的二进制字符串的结果为" + commResult);
//        System.out.println("传送的秘密消息为" + binaryToStr(commResult));
//        return binaryToStr(commResult);
//        return null;
//    }

    public static String strToBinary(String str) {
        char[] strChar = str.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
            result += Integer.toBinaryString(strChar[i]) + " ";
        }
        return result;
    }

    public static String binaryToStr(String binary) {
        String[] tempStr = binary.split(" ");
        char[] tempChar = new char[tempStr.length];
        for (int i = 0; i < tempStr.length; i++) {
            tempChar[i] = binstrToChar(tempStr[i]);
        }
        return String.valueOf(tempChar);
    }

    private static int[] binstrToIntArray(String binStr) {
        char[] temp = binStr.toCharArray();
        int[] result = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            result[i] = temp[i] - 48;
        }
        return result;
    }

    private static char binstrToChar(String binStr) {
        int[] temp = binstrToIntArray(binStr);
        int sum = 0;
        for (int i = 0; i < temp.length; i++) {
            sum += temp[temp.length - 1 - i] << i;
        }
        return (char) sum;
    }

    public static void main(String args[]) {
        //对字符串进行补传
        System.out.println(binaryToStr("0101001100010111"));
    }
}
