package me.nahkd.crystalize.core.statements;

import me.nahkd.crystalize.core.CrystalizeStatement;
import me.nahkd.crystalize.core.Expression;

public class StatementFunctionReturn extends CrystalizeStatement {
	
	public final Expression result;
	
	public StatementFunctionReturn(Expression result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "return " + result;
	}
	
}
