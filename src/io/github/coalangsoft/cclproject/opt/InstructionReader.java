package io.github.coalangsoft.cclproject.opt;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Matthias on 06.07.2017.
 */
public class InstructionReader {

    public static ArrayList<Instruction> read(InputStream stream){
        Scanner s = new Scanner(stream);
        ArrayList<Instruction> res = new ArrayList<>();
        while(s.hasNextLine()){
            res.add(new Instruction(s.nextLine()));
        }
        s.close();
        return res;
    }

}
