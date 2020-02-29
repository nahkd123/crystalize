package me.nahkd.crystalize.core.expressions;

import me.nahkd.crystalize.core.CrystalizeObject;
import me.nahkd.crystalize.core.Expression;
import me.nahkd.crystalize.core.objects.ObjectsCompound;

public class MultipleExpressions extends Expression {
	
	public final Expression left;
	public final String sign;
	public final Expression right;
	
	public MultipleExpressions(Expression left, String sign, Expression right) {
		this.left = left;
		this.sign = sign;
		this.right = right;
	}
	
	public Expression finalValue(ObjectsCompound instance) {
		Expression left, right;
		if (this.left instanceof FunctionExpression) left = ((FunctionExpression) this.left).linkedFunction.validate(instance);
		else if (this.left instanceof MultipleExpressions) left = ((MultipleExpressions) this.left).finalValue(instance);
		else left = this.left;
		
		if (this.right instanceof FunctionExpression) right = ((FunctionExpression) this.right).linkedFunction.validate(instance);
		else if (this.right instanceof MultipleExpressions) right = ((MultipleExpressions) this.right).finalValue(instance);
		else right = this.right;
		
		if (left instanceof NumberExpression && right instanceof NumberExpression) {
			if (sign == "+") return new NumberExpression(((NumberExpression) left).number + ((NumberExpression) right).number);
			if (sign == "-") return new NumberExpression(((NumberExpression) left).number - ((NumberExpression) right).number);
			if (sign == "*") return new NumberExpression(((NumberExpression) left).number * ((NumberExpression) right).number);
			if (sign == "/") return new NumberExpression(((NumberExpression) left).number / ((NumberExpression) right).number);
		} else if (left instanceof StringExpression) {
			if (sign == "+") return new StringExpression(((StringExpression) left).str + right);
		} else if (right instanceof StringExpression) {
			if (sign == "+") return new StringExpression(left + ((StringExpression) right).str);
		}
		return null;
	}

	@Override
	public CrystalizeObject toObject(ObjectsCompound instance) {
		return finalValue(instance).toObject(instance);
	}
	
}
