package io.github.coalangsoft.cclproject.opt.systems;

import io.github.coalangsoft.cclproject.opt.Instruction;
import io.github.coalangsoft.cclproject.opt.InstructionCategory;
import io.github.coalangsoft.cclproject.opt.InstructionData;
import io.github.coalangsoft.cclproject.opt.SystemChange;
import io.github.coalangsoft.cclproject.opt.module.KnownSizeOptimizeRule;

import java.util.List;

/**
 * Created by survari on 09.07.17.
 */
public class WhileOPT extends KnownSizeOptimizeRule
{
	public WhileOPT()
	{
		super(6);
	}

	@Override
	protected Instruction[] act(SystemChange profile, List<Instruction> instructions)
	{
		Instruction i1, i2, i3, i4, i5, i6;
		i1 = instructions.get(0); // !
		i2 = instructions.get(1); // !
		i3 = instructions.get(2);
		i4 = instructions.get(3); // !
		i5 = instructions.get(4);
		i6 = instructions.get(5);

		if (i1.getData().getCategory() == InstructionCategory.LOAD &&
				i1.getParameter().equals("while") &&
				!profile.isWhileChanged() &&
				i4.getData() == InstructionData.putM)
		{
			if (i2.getData() == InstructionData.putI &&
					i2.getParameter().equals("1"))
			{
				return new Instruction[]{i4, new Instruction("__whiletrue")};
			}
		}

		return null;
	}
}
