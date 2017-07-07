package io.github.coalangsoft.cclproject.opt.systems;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionCategory;
import io.github.coalangsoft.cclproject.opt.InstructionData;
import io.github.coalangsoft.cclproject.opt.SystemChange;
import io.github.coalangsoft.cclproject.opt.analyze.InvokeAnalyze;
import io.github.coalangsoft.cclproject.opt.module.KnownSizeOptimizeRule;

import java.util.List;

/**
 * Created by Matthias on 07.07.2017.
 */
public class JavaOPT extends KnownSizeOptimizeRule {

    public JavaOPT() {
        super(3);
    }

    @Override
    protected Instruction[] act(SystemChange profile, List<Instruction> instructions) {
        Instruction ins1 = instructions.get(0);
        Instruction ins2 = instructions.get(1);
        Instruction ins3 = instructions.get(2);

        if(
                (ins1.getData().getCategory() == InstructionCategory.LOAD) &&
                (ins2.getData() == InstructionData.putS) &&
                (ins3.getData().getCategory() == InstructionCategory.INVOKE)
        ){
            if("java".equals(ins1.getParameter()) && InvokeAnalyze.parameters(ins3) == 1 && (!profile.isJavaChanged())){
                return new Instruction[]{new Instruction("__java " + ins2.getParameter())};
            }
        }
        return null;
    }

}
