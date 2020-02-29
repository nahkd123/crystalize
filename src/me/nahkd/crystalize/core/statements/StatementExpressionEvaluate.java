package me.nahkd.crystalize.core.statements;

import me.nahkd.crystalize.core.CrystalizeStatement;
import me.nahkd.crystalize.core.Expression;

public class StatementExpressionEvaluate extends CrystalizeStatement {
	
	public final Expression expression;
	
	public StatementExpressionEvaluate(Expression exp) {
		expression = exp;
	}
	
}
