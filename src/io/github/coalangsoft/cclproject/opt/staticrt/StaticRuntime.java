package io.github.coalangsoft.cclproject.opt.staticrt;

import ccl.csy.context.GlobalSettings;
import io.github.coalangsoft.cclproject.opt.*;
import io.github.coalangsoft.cclproject.opt.utils.NumberOperator;
import io.github.coalangsoft.lib.data.ImmutablePair;
import io.github.coalangsoft.lib.log.TimeLogger;

import java.util.ArrayList;
import java.util.List;

public class StaticRuntime implements INumberOperatorOptimizer{

    private List<StaticVariable> variables;
    private StaticStack stack;

    {
        variables = new ArrayList<>();
        stack = new StaticStack();
    }

    public boolean eval(List<Instruction> instructions){
        boolean changed = false;

        for(int i = 0; i < instructions.size(); i++){
            Instruction ins = instructions.get(i);
            if(ins.getData() == InstructionData.UNKNOWN){
                System.err.println(ins);
                return changed;
            }

            switch(ins.getData()){
                case putI: stack.push(StaticValue.number(ins.getParameterAsInt())); break;
                case __float: stack.push(StaticValue.number(ins.getParameterAsDouble())); break;
                case __mkvar: makeVar(ins.getParameter()); break;
                case __println_f: stack.push(new StaticValue(StaticType.PRINTLN_FUNCTION, null)); break;
                case load: changed = loadVar(i, instructions, ins.getParameter()) || changed; break;
                case __println_c: break;
                case __invoke0: StaticValue val1 = stack.pop(); stack.push(val1.invoke()); handleVariableInvoke(val1); break;
                case __invoke1: StaticValue[] parameters3 = new StaticValue[]{stack.pop()}; StaticValue val3 = stack.pop(); stack.push(val3.invoke(parameters3)); handleVariableInvoke(val3); break;
                case __invoke2: StaticValue[] parameters5 = new StaticValue[]{stack.pop(), stack.pop()}; StaticValue val5 = stack.pop(); stack.push(val5.invoke(parameters5)); handleVariableInvoke(val5); break;
                case invoke1:
                    StaticValue val2 = stack.pop();
                    StaticValue[] args = new StaticValue[ins.getParameterAsInt()];
                    for(int count = 0; count < ins.getParameterAsInt(); count++){
                        args[count] = stack.pop();
                    }
                    stack.push(val2.invoke(args));
                    handleVariableInvoke(val2);
                    break;
                case get:
                    StaticValue val4 = stack.pop();
                    if(ins.getParameter().equals("bind")){
                        stack.push(new StaticValue(StaticType.PROPERTY_BIND, new StaticBindFunc(val4)));
                    }else{
                        stack.push(val4.property(StaticValue.string(ins.getParameter())));
                    }
                    break;
                case pop: stack.pop(); break;
                case __setvar: setVar(ins.getParameter()); break;
                case __java: stack.push(StaticValue.java(ins.getParameter())); break;
                case putS: stack.push(StaticValue.string(ins.getParameter())); break;
                case putM: stack.push(StaticValue.unknown()); break;
                case nnr2: break;
                case invoke: {
                    StaticValue[] parameters6 = new StaticValue[ins.getParameterAsInt()];
                    for(int k = 0; k < parameters6.length; k++){
                        parameters6[k] = stack.pop();
                    }
                    stack.push(stack.pop().invoke(parameters6));
                    break;
                }
                case putA: stack.push(StaticValue.array()); break;
                default: throw new RuntimeException(ins + "");
            }

            if(!stack.isEmpty()){
                StaticValue current = stack.peek();
                if(isConstant(current)){
                    Instruction n = getConstant(current);
                    if(n.getData() != ins.getData()){
                        if(ins.getData() == InstructionData.get){
                            instructions.add(i, new Instruction("pop"));
                            instructions.set(i + 1, n);
                            System.out.println(ins + ";" + n);
                            changed = true;
                        }
                    }
                }
            }

//          System.out.println(stack);
        }

        if(changed){
            make().eval(instructions);
        }
        return changed;
    }

    private void resetVariables() {
        for(int i = 0; i < variables.size(); i++){
            variables.set(i, new StaticVariable(variables.get(i).getName(), StaticValue.unknown()));
        }
    }

    private boolean loadVar(final int i, List<Instruction> instructions, String name) {
        for(int k = 0; k < variables.size(); k++){
            StaticVariable var = variables.get(k);
            if(var.getName().equals(name)){
                stack.push(StaticValue.variable(var));
                switch(var.getValue().getType()){
                    case NUMBER: instructions.set(i, new Instruction("__float " + var.getValue().getCurrentValue())); return true;
                    case STRING: instructions.set(i, new Instruction("putS " + var.getValue().getCurrentValue())); return true;
                    case JAVA_CLASS: instructions.set(i, new Instruction("__java " + ((Class<?>) var.getValue().getCurrentValue()).getName())); return true;
                    case PRINTLN_FUNCTION: instructions.set(i, new Instruction("__println_f")); return true;
                    default: return false;
                }
            }
        }
        stack.push(StaticValue.variable(new StaticVariable(name, StaticValue.unknown())));
        return false;
    }

    private void makeVar(String name) {
        variables.add(new StaticVariable(name, stack.pop()));
    }

    private boolean canChangeVariables(StaticValue val){
        switch (val.getType()){
            case STRING: return false;
            case VARIABLE: {
                StaticVariable var = (StaticVariable) val.getCurrentValue();
                if(GlobalSettings.builtinVariables.contains(var.getName()) && !GlobalSettings.changedVariables.contains(var.getName())){
                    return false;
                }
                return canChangeVariables(var.getValue());
            }
            case NUMBER: return false;
            case BOUND: return canChangeVariables(((ImmutablePair<StaticBindFunc, StaticValue[]>) val.getCurrentValue()).getA().origin);
            case PROPERTY_BIND: return false;
            case UNKNOWN: return true;
            case ERROR: return false;
            case UNDEFINED: return false;
            case PRINTLN_FUNCTION: return false;
            case JAVA_CLASS: return false;
            case PROPERTY_PROPERTY: return false;
            default: throw new RuntimeException("Unexpected value type: " + val.getType());
        }
    }

    private void handleVariableInvoke(StaticValue val){
        if(canChangeVariables(val)){
            resetVariables();
        }
    }

    private void setVar(String name) {
        StaticValue val = stack.pop();
        for(int k = 0; k < variables.size(); k++){
            StaticVariable var = variables.get(k);
            if(var.getName().equals(name)){
                variables.set(k, new StaticVariable(name, val));
            }
        }
    }

    private static boolean isConstant(StaticValue val){
        switch (val.getType()){
            case JAVA_CLASS: return true;
            case NUMBER: return true;
            case PROPERTY_BIND: return false;
            case STRING: return true;
            case UNKNOWN: return false;
            case VARIABLE: return isConstant(((StaticVariable) val.getCurrentValue()).getValue());
            case BOUND: return false;
            case PRINTLN_FUNCTION: return true;
            case ERROR: return false;
            case JAVA_OBJECT: return false;
            case UNDEFINED: return true;
            case ARRAY: return false;
            case PROPERTY_PROPERTY: return false;
            case NUMOP: return false;
            default: throw new RuntimeException("Unexpected type: " + val);
        }
    }

    private static boolean isPush(InstructionData dat){
        switch(dat){
            case putS: return true;
            case load: return false;
            case putM: return true;
            case __float: return true;
            case get: return false;
            case nnr2: return false;
            case putI: return true;
            case __java: return true;
            case putA: return true;
            case UNKNOWN: return true;
            case __invoke0: return false;
            case __invoke1: return false;
            case __invoke2: return false;
            case __println_f: return true;
            case invoke: return false;
            case __mkvar: return false;
            case __println_c: return false;
            case __setvar: return false;
            case invoke1: return false;
            case pop: return false;
            default: throw new RuntimeException("Unexpected type: " + dat);
        }
    }

    private static Instruction getConstant(StaticValue v) {
        switch(v.getType()){
            case JAVA_CLASS: return new Instruction("__java " + ((Class<?>) v.getCurrentValue()).getName());
            case NUMBER:{
                double d = ((Number) v.getCurrentValue()).doubleValue();
                if(d == (int) d){
                    return new Instruction("putI " + (int) d);
                }
                return new Instruction("__float " + d);
            }
            case STRING: return new Instruction("putS " + v.getCurrentValue().toString());
            case VARIABLE: return getConstant(((StaticVariable) v.getCurrentValue()).getValue());
            case PRINTLN_FUNCTION: return new Instruction("__println_f");
            case UNDEFINED: return new Instruction("__undefined");
            default: throw new RuntimeException("Unexpected type: " + v);
        }
    }

    public void addNumOp(String name, NumberOperator op){
        if(!GlobalSettings.changedVariables.contains(name)){
            variables.add(new StaticVariable(name, StaticValue.numOp(op)));
        }
    }

    public static StaticRuntime make() {
        StaticRuntime rt = new StaticRuntime();

        OptimizerSetup.initNumOps(rt);

        return rt;
    }
}
