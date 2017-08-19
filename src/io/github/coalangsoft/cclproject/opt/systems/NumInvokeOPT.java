package io.github.coalangsoft.cclproject.opt.systems;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionData;
import io.github.coalangsoft.cclproject.opt.SystemChange;
import io.github.coalangsoft.cclproject.opt.module.KnownSizeOptimizeRule;

import java.util.List;

public class NumInvokeOPT extends KnownSizeOptimizeRule {

    public NumInvokeOPT() {
        super(3);
    }

    @Override
    protected Instruction[] act(SystemChange profile, List<Instruction> instructions) {
        if(instructions.get(0).isNumberPut() && instructions.get(1).is(InstructionData.putS) && instructions.get(2).isInvoke(1)){
            return new Instruction[]{
                    instructions.get(0), //number,
                    new Instruction("get " + instructions.get(1).getParameter()) //get
            };
        }
        return null;
    }

}
