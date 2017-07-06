package test.coaopt;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionOptimizer;
import io.github.coalangsoft.cclproject.opt.InstructionReader;
import io.github.coalangsoft.cclproject.opt.SystemChange;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Matthias on 06.07.2017.
 */
public class CoaOptTest {

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Instruction> is = InstructionReader.read(CoaOptTest.class.getResourceAsStream("./test.txt"));

        SystemChange c = new SystemChange();
//        c.setForChanged(true);
//        c.setIfChanged(true);
//        c.setJavaChanged(true);
//        c.setWhileChanged(true);
//        c.setPrintlnChanged(true);

        InstructionOptimizer.optimize(c, is);
        System.out.println(is);
        for(int i = 0; i < is.size(); i++){
            System.out.println(is.get(i).getRaw());
        }
    }

}
