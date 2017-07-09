package io.github.coalangsoft.cclproject.opt;

/**
 * Created by Matthias on 06.07.2017.
 */
public enum InstructionData {

    UNKNOWN(null),
    pop(InstructionCategory.POP),
    putI(InstructionCategory.PUT), putA(InstructionCategory.PUT), putS(InstructionCategory.PUT),
    __invoke0(InstructionCategory.INVOKE), __invoke1(InstructionCategory.INVOKE), __invoke2(InstructionCategory.INVOKE),
    load(InstructionCategory.LOAD), invoke(InstructionCategory.INVOKE),
    __println_f(InstructionCategory.PUT), get(InstructionCategory.GET), __float(InstructionCategory.PUT),
    __java(InstructionCategory.JAVA), putM(InstructionCategory.PUT);

    private final InstructionCategory category;

    InstructionData(InstructionCategory cat) {
        this.category = cat;
    }

    public InstructionCategory getCategory(){
        return category;
    }
}
