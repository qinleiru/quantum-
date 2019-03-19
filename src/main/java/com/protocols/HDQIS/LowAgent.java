package com.protocols.HDQIS;

import com.protocols.role.AbstractAgent;
import com.quantum.gate.QuantumGate;
import com.quantum.measure.Measures;
import com.quantum.measure.ProjectiveMeasure;
import com.quantum.oparate.MathOperation;
import com.quantum.oparate.QuantumOperation;

import java.util.ArrayList;
import java.util.HashMap;

import static com.protocols.HDQIS.HDQIS.resultSAs;
import static com.protocols.HDQIS.HDQIS.systemStates;

public class LowAgent extends AbstractAgent {

    /**
     * 代理者对手中的粒子序列进行单粒子测量
     * @param measures
     * @return
     */
    @Override
    public void measure(Measures measures) {
        int index=measures.getIndex();
        switch (index){
            case 1:
                for (int i=0;i<particleName.size();i++){
                    measureResult.put(particleName.get(i), ProjectiveMeasure.measureBaseX(systemStates.get(i), particleName.get(i)));
                }
                break;
            case 2:
                for (int i=0;i<particleName.size();i++){
                    measureResult.put(particleName.get(i), ProjectiveMeasure.measureBaseZ(systemStates.get(i),particleName.get(i)));
                }
                break;
        }
    }


    /**
     * 秘密量子比特的恢复
     */
    @Override
    public void restore() {
        for (int i = 0; i < systemStates.size(); i++) {
            int resultAliceSA = resultSAs.get(i);   //粒子S、粒子A的Bell态的测量结果
            int lowAgentParticle;   //权限低的粒子的测量结果
            int highAgentParticle;  //权限高的粒子的测量结果
            String particle1Name;   //要恢复秘密消息的代理者手中的粒子
            if (measureResult.keySet().contains("B" + i)) {
                lowAgentParticle = measureResult.get("B" + i);
                particle1Name="C"+i;
            } else {
                lowAgentParticle = measureResult.get("C" + i);
                particle1Name="B"+i;
            }
            highAgentParticle = measureResult.get("D"+i);
        /*
           用于测试的代码
         */
            System.out.println("++++++++Bell态的测量结果为" + resultAliceSA);
            System.out.println("++++++++权限低的代理者的测量结果为" + lowAgentParticle);
            System.out.println("++++++++权限高的代理者的测量结果为" + highAgentParticle);
            if (resultAliceSA == 2) {
                if (lowAgentParticle == 0 && highAgentParticle == 0) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_X);
                }
                if (lowAgentParticle == 0 && highAgentParticle == 1) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_Z);
                }
                if (lowAgentParticle == 1 && highAgentParticle == 0) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_I);
                }
                if (lowAgentParticle == 1 && highAgentParticle == 1) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_iY);

                }
            }
            if (resultAliceSA == 1) {
                if (lowAgentParticle == 0 && highAgentParticle == 0) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_I);
                }
                if (lowAgentParticle == 0 && highAgentParticle == 1) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_iY);
                }
                if (lowAgentParticle == 1 && highAgentParticle == 0) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_X);
                }
                if (lowAgentParticle == 1 && highAgentParticle == 1) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_Z);

                }
            }
            if (resultAliceSA == 4) {
                if (lowAgentParticle == 0 && highAgentParticle == 0) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_iY);
                }
                if (lowAgentParticle == 0 && highAgentParticle == 1) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_I);
                }
                if (lowAgentParticle == 1 && highAgentParticle == 0) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_Z);
                }
                if (lowAgentParticle == 1 && highAgentParticle == 1) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_X);

                }
            }
            if (resultAliceSA == 3) {
                if (lowAgentParticle == 0 && highAgentParticle == 0) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_Z);
                }
                if (lowAgentParticle == 0 && highAgentParticle == 1) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_X);
                }
                if (lowAgentParticle == 1 && highAgentParticle == 0) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_iY);
                }
                if (lowAgentParticle == 1 && highAgentParticle == 1) {
                    QuantumOperation.quantumSinglePerform(systemStates.get(i), particle1Name, QuantumGate.Operator_I);

                }
            }
            MathOperation.normalization(systemStates.get(i).getState());
        }
    }
}
