package io.github.coalangsoft.cclproject.opt;

import io.github.coalangsoft.cclproject.opt.analyze.InvokeAnalyze;

import java.util.List;

/**
 * Created by Matthias on 06.07.2017.
 */
public class InstructionOptimizer {

    public static void optimize(SystemChange profile, List<Instruction> instructions){
        for(int i = 0; i < instructions.size(); i++){
            System.out.println(instructions);

            Instruction ins1 = instructions.get(i);
            if(ins1.getData().getCategory() == InstructionCategory.LOAD){
                if("println".equals(ins1.getParameter()) && (!profile.isPrintlnChanged())){
                    instructions.set(i, new Instruction("__println_f"));
                }
            }

            Instruction ins2 = null;

            if(i >= 1){
                ins1 = instructions.get(i);
                ins2 = instructions.get(i - 1);

                //check for put-pop pattern
                if(
                        (
                                ins2.getData().getCategory() == InstructionCategory.PUT_NOCHANGE ||
                                ins2.getData().getCategory() == InstructionCategory.LOAD
                        ) &&
                        (ins1.getData().getCategory() == InstructionCategory.POP_NOUSE)
                ){
                    instructions.remove(i);
                    instructions.remove(i - 1);
                    i = 0;
                    continue;
                }
            }
            if(i >= 2){
                Instruction ins3 = instructions.get(i - 2);
                //check for putS-load(java)-__invoke1
                if(
                        (ins3.getData().getCategory() == InstructionCategory.LOAD) &&
                        (ins2.getData() == InstructionData.putS) &&
                        (ins1.getData().getCategory() == InstructionCategory.INVOKE)
                ){
                    if("java".equals(ins3.getParameter()) && InvokeAnalyze.parameters(ins1) == 1 && (!profile.isJavaChanged())){
                        instructions.set(i, new Instruction("__java " + ins2.getParameter()));
                        instructions.remove(i - 1);
                        instructions.remove(i - 2);
                        i = 0;
                        continue;
                    }
                }

                //check for __println_f-putS-__invoke1
                if(
                        (ins3.getData() == InstructionData.__println_f) &&
                        (ins2.getData() == InstructionData.putS) &&
                        (ins1.getData().getCategory() == InstructionCategory.INVOKE)
                ){
                    if(InvokeAnalyze.parameters(ins1) == 1){
                        instructions.set(i - 1, new Instruction("__println_c " + ins2.getParameter()));
                        instructions.set(i, new Instruction("load undefined"));
                        instructions.remove(i - 2);
                        i = 0;
                        continue;
                    }
                }
            }
        }
    }

}
