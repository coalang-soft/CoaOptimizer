package io.github.coalangsoft.cclproject.opt.systems;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionCategory;
import io.github.coalangsoft.cclproject.opt.SystemChange;
import io.github.coalangsoft.cclproject.opt.module.KnownSizeOptimizeRule;

import java.util.List;

/**
 * Created by Matthias on 07.07.2017.
 */
public class PutPopOPT extends KnownSizeOptimizeRule {

    public PutPopOPT() {
        super(2);
    }

    @Override
    protected Instruction[] act(SystemChange profile, List<Instruction> instructions) {
        Instruction ins1 = instructions.get(0);
        Instruction ins2 = instructions.get(1);
        if(
                (
                        ins1.getData().getCategory() == InstructionCategory.PUT ||
                        ins1.getData().getCategory() == InstructionCategory.LOAD
                ) &&
                (ins2.getData().getCategory() == InstructionCategory.POP)
        ){
            return new Instruction[0];
        }
        return null;
    }

}
