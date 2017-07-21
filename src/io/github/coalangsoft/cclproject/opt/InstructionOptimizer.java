package io.github.coalangsoft.cclproject.opt;

import io.github.coalangsoft.cclproject.opt.analyze.InvokeAnalyze;
import io.github.coalangsoft.cclproject.opt.module.OptimizeRule;
import io.github.coalangsoft.cclproject.opt.systems.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthias on 06.07.2017.
 */
public class InstructionOptimizer {

    private final OptimizeRule[] rules;

    public static final InstructionOptimizer DEFAULT = new InstructionOptimizer(
            new FloatOPT(),
            new JavaOPT(),
            new PrintlnInvokeOPT(),
            new PrintlnLoadOPT(),
            new PutPopOPT(),
            new JavaConstantOPT(),
            new WhileOPT()
    );

    public InstructionOptimizer(OptimizeRule... rules){
        this.rules = rules;
    }

    public void optimize(SystemChange profile, ArrayList<Instruction> instructions){
        mainLoop:
        for(int i = 0; i < instructions.size(); i++){

            //Use rules
            for(int k = 0; k < rules.length; k++){
                OptimizeRule rule = rules[k];
                Instruction[] res = rule.optimize(i,profile,instructions);
                if(res != null){
                    //TODO add new
                    instructions.addAll(i + 1, Arrays.asList(res));
                    //TODO remove old
                    int toRemove = rule.toRemove();
                    for(int m = 0; m < toRemove; m++){
                        instructions.remove((i - toRemove) + 1);
                    }
                    i = 0;
                    continue mainLoop;
                }
            }

        }
    }

}
