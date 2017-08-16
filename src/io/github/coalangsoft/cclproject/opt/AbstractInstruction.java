package io.github.coalangsoft.cclproject.opt;

import io.github.coalangsoft.cclproject.opt.analyze.InstructionAnalyze;
import io.github.coalangsoft.cclproject.opt.analyze.InvokeAnalyze;

public abstract class AbstractInstruction {

    public abstract String getRaw();
    public abstract InstructionData getData();
    public abstract String getParameter();

    public int getParameterAsInt(){
        return Integer.parseInt(getParameter());
    }

    public double getParameterAsDouble(){
        return Double.parseDouble(getParameter());
    }

    public boolean isLoad(){
        return getData() == InstructionData.load;
    }

    public boolean isLoad(String... what){
        if(isLoad()){
            for(int i = 0; i < what.length; i++){
                if(getParameter().equals(what[i])){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLoadUnchanged(SystemChange changed, String what){
        if(isLoad() && !changed.getOrDefault(what,false)){
            return isLoad(what);
        }
        return false;
    }

    public boolean isInvoke(){
        return getData().getCategory() == InstructionCategory.INVOKE;
    }

    public boolean isInvoke(int... count){
        if(isInvoke()){
            int parameters = InvokeAnalyze.parameters(this);
            for(int i = 0; i < count.length; i++){
                if(parameters == count[i]){
                    return true;
                }
            }
        }
        return false;
    }

    public String toString(){
        return getRaw();
    }

    public boolean isNumberPut() {
        return InstructionAnalyze.isNumberPut(this);
    }
}
