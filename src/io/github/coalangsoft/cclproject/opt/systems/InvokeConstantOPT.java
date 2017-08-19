package io.github.coalangsoft.cclproject.opt.systems;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionCategory;
import io.github.coalangsoft.cclproject.opt.InstructionData;
import io.github.coalangsoft.cclproject.opt.SystemChange;
import io.github.coalangsoft.cclproject.opt.module.KnownSizeOptimizeRule;

import java.util.List;

public class InvokeConstantOPT extends KnownSizeOptimizeRule {

    public InvokeConstantOPT() {
        super(2);
    }

    @Override
    protected Instruction[] act(SystemChange profile, List<Instruction> instructions) {
        if(instructions.get(0).is(InstructionData.putI, InstructionData.putS, InstructionData.__float)){
            if(instructions.get(1).isInvoke(0)){
                return new Instruction[]{instructions.get(0)};
            }
        }
        return null;
    }

}
