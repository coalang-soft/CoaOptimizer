package io.github.coalangsoft.cclproject.opt;

/**
 * Created by Matthias on 06.07.2017.
 */
public enum InstructionData {

    UNKNOWN(null),
    pop(InstructionCategory.POP_NOUSE),
    putI(InstructionCategory.PUT_NOCHANGE), putA(InstructionCategory.PUT_NOCHANGE), putS(InstructionCategory.PUT_NOCHANGE),
    __invoke0(InstructionCategory.INVOKE), __invoke1(InstructionCategory.INVOKE), __invoke2(InstructionCategory.INVOKE),
    load(InstructionCategory.LOAD), invoke(InstructionCategory.INVOKE),
    __println_f(InstructionCategory.PUT_NOCHANGE), get(InstructionCategory.GET), __float(InstructionCategory.PUT_NOCHANGE);

    private final InstructionCategory category;

    InstructionData(InstructionCategory cat) {
        this.category = cat;
    }

    public InstructionCategory getCategory(){
        return category;
    }
}
