package io.github.coalangsoft.cclproject.opt;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Matthias on 06.07.2017.
 */
public class Instruction extends AbstractInstruction{

    private final String raw;
    private final String parameter;
    private InstructionData data;

    public Instruction(String i){
        if(i == null){
            raw = null;
            parameter = null;
            data = InstructionData.UNKNOWN;
        }else{
            this.raw = i.trim();
            String[] split = raw.split(" ");
            String[] param = Arrays.copyOfRange(split, 1, split.length);
            parameter = String.join(" ", param);
            try{
                this.data = InstructionData.valueOf(split[0]);
            }catch(Exception e){
                this.data = InstructionData.UNKNOWN;
            }
        }
    }

    public String getRaw() {
        return raw;
    }

    public InstructionData getData() {
        return data;
    }

    public String getParameter() {
        return parameter;
    }
}
