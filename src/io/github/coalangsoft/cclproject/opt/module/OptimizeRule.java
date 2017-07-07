package io.github.coalangsoft.cclproject.opt.module;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.SystemChange;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthias on 07.07.2017.
 */
public interface OptimizeRule {

    Instruction[] optimize(int index, SystemChange profile, ArrayList<Instruction> instructions);
    int toRemove();

}
