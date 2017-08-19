package io.github.coalangsoft.cclproject.opt.systems;

import ccl.v2_1.operators.Operators;
import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionCategory;
import io.github.coalangsoft.cclproject.opt.InstructionData;
import io.github.coalangsoft.cclproject.opt.SystemChange;
import io.github.coalangsoft.cclproject.opt.analyze.InstructionAnalyze;
import io.github.coalangsoft.cclproject.opt.analyze.InvokeAnalyze;
import io.github.coalangsoft.cclproject.opt.module.KnownSizeOptimizeRule;
import io.github.coalangsoft.cclproject.opt.utils.NumberOperator;
import io.github.coalangsoft.cclproject.opt.utils.StringOperator;

import java.util.List;

public class StringOperationOPT extends KnownSizeOptimizeRule {

    private final String opName;
    private final StringOperator impl;

    public StringOperationOPT(String op, StringOperator impl) {
        super(6);
        this.opName = op;
        this.impl = impl;
    }

    /*
    putS 1
    load concat
    get bind
    invoke1 1
    putS 2
    __invoke1
     */

    @Override
    protected Instruction[] act(SystemChange profile, List<Instruction> instructions) {
        Instruction
                i1 = instructions.get(0), //first
                i2 = instructions.get(1), //operator
                i3 = instructions.get(2), //bind
                i4 = instructions.get(3), //invoke1 1
                i5 = instructions.get(4), //second
                i6 = instructions.get(5); //invoke 1

        //instruction 1
        if (i1.getData() == InstructionData.putS) {
            //instruction 2 to 4
            if (i2.getData() == InstructionData.load && i3.getRaw().trim().equals("get bind") && i4.getRaw().equals("invoke1 1")) {
                if(profile.getOrDefault(i2.getParameter(), false) || (!opName.equals(i2.getParameter()))){
                    //operator modified
                    return null;
                }

                //instruction 5
                if (i5.getData() == InstructionData.putS) {
                    //instruction 6
                    if (i6.getData().getCategory() == InstructionCategory.INVOKE && InvokeAnalyze.parameters(i6) == 1) {
                        return optimize(i1.getParameter(), i5.getParameter());
                    }
                }
            }
        }
        return null;
    }

    private Instruction[] optimize(String a, String b) {
        return new Instruction[]{new Instruction("putS " + impl.operate(a,b))};
    }

}
