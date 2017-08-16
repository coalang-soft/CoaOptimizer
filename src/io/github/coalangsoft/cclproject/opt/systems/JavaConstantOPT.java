package io.github.coalangsoft.cclproject.opt.systems;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionData;
import io.github.coalangsoft.cclproject.opt.SystemChange;
import io.github.coalangsoft.cclproject.opt.module.KnownSizeOptimizeRule;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Created by Matthias on 07.07.2017.
 */
public class JavaConstantOPT extends KnownSizeOptimizeRule {

    public JavaConstantOPT() {
        super(2);
    }

    @Override
    protected Instruction[] act(SystemChange profile, List<Instruction> instructions) {
        Instruction ins1 = instructions.get(0);
        Instruction ins2 = instructions.get(1);
        if(ins1.getData() == InstructionData.__java && ins2.getData() == InstructionData.get){
            //Try to load field
            try{
                Field f = Class.forName(ins1.getParameter()).getField(ins2.getParameter());
                if(Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers())){
                    //insert constant
                    if(f.getType() == String.class){
                        return new Instruction[]{new Instruction("putS " + f.get(null))};
                    }
                    if(f.getType() == int.class || f.getType() == byte.class || f.getType() == long.class || f.getType() == short.class){
                        return new Instruction[]{new Instruction("putI " + f.get(null))};
                    }
                    if(f.getType() == double.class || f.getType() == float.class){
                        return new Instruction[]{new Instruction("__float " + f.get(null))};
                    }
                }
            }catch(ClassNotFoundException e){
                System.out.println("Warning: Class " + ins2.getParameter() + " not found at optimize time!");
            }catch(Exception e){}
        }
        return null;
    }

}
