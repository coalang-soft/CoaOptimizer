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
public class PrintlnInvokeOPT extends KnownSizeOptimizeRule {

    public PrintlnInvokeOPT() {
        super(3);
    }

    @Override
    protected Instruction[] act(SystemChange profile, List<Instruction> instructions) {
        Instruction ins1 = instructions.get(0);
        Instruction ins2 = instructions.get(1);
        Instruction ins3 = instructions.get(2);

        if(
                (ins1.getData() == InstructionData.__println_f) &&
                (ins2.getData().getCategory() == InstructionCategory.PUT_NOCHANGE) &&
                (ins3.getData().getCategory() == InstructionCategory.INVOKE)
        ){
            if(InvokeAnalyze.parameters(ins3) == 1){
                String toPrint;
                if(ins2.getData() == InstructionData.putA){
                    toPrint = "[]";
                }else{
                    toPrint = ins2.getParameter();
                }

                return new Instruction[]{
                        new Instruction("__println_c " + toPrint),
                        new Instruction("load undefined")
                };
            }
        }
        return null;
    }

}
