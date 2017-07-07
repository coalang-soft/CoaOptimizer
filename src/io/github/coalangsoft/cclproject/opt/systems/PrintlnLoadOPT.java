package io.github.coalangsoft.cclproject.opt.systems;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionCategory;
import io.github.coalangsoft.cclproject.opt.SystemChange;
import io.github.coalangsoft.cclproject.opt.module.KnownSizeOptimizeRule;

import java.util.List;

/**
 * Created by Matthias on 07.07.2017.
 */
public class PrintlnLoadOPT extends KnownSizeOptimizeRule {

    public PrintlnLoadOPT() {
        super(1);
    }

    @Override
    protected Instruction[] act(SystemChange profile, List<Instruction> instructions) {
        Instruction ins1 = instructions.get(0);
        if(ins1.getData().getCategory() == InstructionCategory.LOAD){
            if("println".equals(ins1.getParameter()) && (!profile.isPrintlnChanged())){
                return new Instruction[]{new Instruction("__println_f")};
            }
        }
        return null;
    }

}
