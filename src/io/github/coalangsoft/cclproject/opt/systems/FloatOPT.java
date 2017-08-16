package io.github.coalangsoft.cclproject.opt.systems;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionData;
import io.github.coalangsoft.cclproject.opt.SystemChange;
import io.github.coalangsoft.cclproject.opt.module.KnownSizeOptimizeRule;

import java.util.List;

/**
 * Created by Matthias on 07.07.2017.
 */
public class FloatOPT extends KnownSizeOptimizeRule {

    public FloatOPT() {
        super(2);
    }

    @Override
    protected Instruction[] act(SystemChange profile, List<Instruction> instructions) {
        Instruction ins1 = instructions.get(0);
        Instruction ins2 = instructions.get(1);

        if(
                (ins1.isNumberPut()) &&
                (ins2.getData() == InstructionData.get)
        ){
            try{
                int dec = Integer.parseInt(ins2.getParameter());
                return new Instruction[]{new Instruction("__float " + (int) ins1.getParameterAsDouble() + "." + dec)};
            }catch(NumberFormatException e){}
        }
        return null;
    }

}
