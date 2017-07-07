package io.github.coalangsoft.cclproject.opt.module;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.SystemChange;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthias on 07.07.2017.
 */
public abstract class KnownSizeOptimizeRule implements OptimizeRule {

    private final int size;
    private int dex;

    public KnownSizeOptimizeRule(int size){
        this.size = size;
    }

    @Override
    public Instruction[] optimize(int index, SystemChange profile, ArrayList<Instruction> instructions) {
        dex = index;
        if(index >= size - 1){
            return act(profile, instructions.subList((1 + index) - size, index + 1));
        }
        return null;
    }

    public int toRemove(){
        return size;
    }

    protected abstract Instruction[] act(SystemChange profile, List<Instruction> instructions);
}
