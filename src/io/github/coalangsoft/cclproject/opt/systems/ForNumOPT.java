package io.github.coalangsoft.cclproject.opt.systems;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionData;
import io.github.coalangsoft.cclproject.opt.SystemChange;
import io.github.coalangsoft.cclproject.opt.analyze.InstructionAnalyze;
import io.github.coalangsoft.cclproject.opt.module.KnownSizeOptimizeRule;

import java.util.List;

public class ForNumOPT extends KnownSizeOptimizeRule {

    public ForNumOPT() {
        super(7);
    }

    /*
    load for
    putI 0
    putI 1000
    __invoke2
    putM _b0_.cl0
    __invoke1
    nnr2
     */

    @Override
    protected Instruction[] act(SystemChange profile, List<Instruction> instructions) {
        Instruction
                i1 = instructions.get(0), //for
                i2 = instructions.get(1), //from
                i3 = instructions.get(2), //to
                i4 = instructions.get(3), //invoke 2
                i5 = instructions.get(4), //method
                i6 = instructions.get(5), //invoke 1
                i7 = instructions.get(6); //nnr2

        //instruction 1
        if(i1.isLoadUnchanged(profile,"for")){
            //instruction 2 and 3
            if(i2.isNumberPut() && i3.isNumberPut()){
                //instruction 4 and 5
                if(i4.isInvoke(2) && i5.getData() == InstructionData.putM){
                    //instruction 6 and 7
                    if(i6.isInvoke(1) && i7.getData() == InstructionData.nnr2){
                        return new Instruction[]{
                                new Instruction("putI " + (int) i2.getParameterAsDouble()),
                                new Instruction("putI " + (int) i3.getParameterAsDouble()),
                                i5, new Instruction("__fornum")
                        };
                    }
                }
            }
        }

        return null;
    }

}
