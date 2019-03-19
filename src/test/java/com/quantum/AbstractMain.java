package com.quantum;

import com.quantum.state.QuantumState;
import com.quantum.state.SingleState;

public abstract class AbstractMain {
    QuantumState secretState;
    public abstract void getInformation(SingleState singleState);
}
