package io.github.coalangsoft.cclproject.opt.analyze;

import io.github.coalangsoft.cclproject.opt.AbstractInstruction;
import io.github.coalangsoft.cclproject.opt.InstructionData;

public class InstructionAnalyze {

    public static boolean isNumberPut(AbstractInstruction i){
        return i.getData() == InstructionData.putI || i.getData() == InstructionData.__float;
    }

    public static double getNumberPut(AbstractInstruction i){
        if(isNumberPut(i)){
            return i.getParameterAsDouble();
        }
        throw new IllegalArgumentException(i + " is not a number put");
    }

}
