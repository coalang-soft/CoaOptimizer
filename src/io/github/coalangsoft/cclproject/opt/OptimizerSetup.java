package io.github.coalangsoft.cclproject.opt;

import io.github.coalangsoft.cclproject.opt.systems.NumOperationOPT;

public class OptimizerSetup {

    public static <T extends INumberOperatorOptimizer> T initNumOps(T t){
        t.addNumOp("add", (a, b) -> a+b);
        t.addNumOp("mul", (a,b) -> a*b);
        t.addNumOp("div", (a,b) -> a/b);
        t.addNumOp("sub", (a,b) -> a-b);
        t.addNumOp("equals", (a,b) -> a==b?1:0);
        t.addNumOp("pow", Math::pow);
        return t;
    }

}
