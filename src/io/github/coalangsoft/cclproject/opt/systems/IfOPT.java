package io.github.coalangsoft.cclproject.opt.systems;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionCategory;
import io.github.coalangsoft.cclproject.opt.InstructionData;
import io.github.coalangsoft.cclproject.opt.SystemChange;
import io.github.coalangsoft.cclproject.opt.analyze.InvokeAnalyze;
import io.github.coalangsoft.cclproject.opt.module.KnownSizeOptimizeRule;

import java.util.List;

public class IfOPT extends KnownSizeOptimizeRule
{
    public IfOPT()
    {
        super(6);
    }

    @Override
    protected Instruction[] act(SystemChange profile, List<Instruction> instructions)
    {
        Instruction i1, i2, i3, i4, i5, i6;
        i1 = instructions.get(0); // !
        i2 = instructions.get(1); // !
        i3 = instructions.get(2);
        i4 = instructions.get(3); // !
        i5 = instructions.get(4);
        i6 = instructions.get(5);

        if(i3.getData().getCategory() == InstructionCategory.INVOKE){
            if(InvokeAnalyze.parameters(i3) != 1){
                return null;
            }
        }else{
            return null;
        }
        if(i5.getData().getCategory() == InstructionCategory.INVOKE){
            if(InvokeAnalyze.parameters(i5) != 1){
                return null;
            }
        }else{
            return null;
        }
        if(i6.getData() != InstructionData.nnr2){
            return null;
        }

        if (i1.isLoadUnchanged(profile, "if") &&
                i4.getData() == InstructionData.putM)
        {
            if(i2.isNumberPut()){
                double num = i2.getParameterAsDouble();
                if(num == 1){
                    return new Instruction[]{i4, new Instruction("__invoke0")};
                }else{
                    return new Instruction[0];
                }
            }
        }

        return null;
    }
}