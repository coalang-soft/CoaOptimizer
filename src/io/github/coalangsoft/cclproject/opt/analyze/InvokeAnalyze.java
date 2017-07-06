package io.github.coalangsoft.cclproject.opt.analyze;

import io.github.coalangsoft.cclproject.opt.Instruction;

/**
 * Created by Matthias on 06.07.2017.
 */
public class InvokeAnalyze {

    public static int parameters(Instruction instruction) {
        switch(instruction.getData()){
            case __invoke0: return 0;
            case __invoke1: return 1;
            case __invoke2: return 2;
            case invoke: return Integer.parseInt(instruction.getParameter());
            default: throw new RuntimeException("Unknown invoke instruction: " + instruction.getData());
        }
    }

}
