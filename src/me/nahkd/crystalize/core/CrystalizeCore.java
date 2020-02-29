package me.nahkd.crystalize.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import me.nahkd.crystalize.core.expressions.FunctionExpression;
import me.nahkd.crystalize.core.expressions.MultipleExpressions;
import me.nahkd.crystalize.core.expressions.NumberExpression;
import me.nahkd.crystalize.core.expressions.StringExpression;
import me.nahkd.crystalize.core.expressions.VariableExpression;
import me.nahkd.crystalize.core.objects.ObjectFunction;
import me.nahkd.crystalize.core.objects.ObjectsCompound;
import me.nahkd.crystalize.core.statements.StatementExpressionEvaluate;
import me.nahkd.crystalize.core.statements.StatementFunctionReturn;
import me.nahkd.crystalize.lang.functions.InternalFunctionPrint;
import me.nahkd.crystalize.utils.NumberUtils;
import me.nahkd.crystalize.utils.StringUtils;

public class CrystalizeCore {
	
	public static final CrystalizeStatement[] compile(File file, ObjectsCompound instance) {
		try {
			String scr = "";
			for (String line : Files.readAllLines(file.toPath())) {
				scr += line + "\n";
			}
			return compile(scr, instance);
		} catch (IOException e) {
			e.printStackTrace();
			return new CrystalizeStatement[0];
		}
	}
	/**
	 * Compile script to list of statements. You can also turn it to bytecodes so
	 * you can save it as a binary file.
	 * @param script The plain script
	 * @param instance The instance to do stuffs
	 * @return List of statements
	 */
	public static final CrystalizeStatement[] compile(String script, ObjectsCompound instance) {
		ArrayList<CrystalizeStatement> statements = new ArrayList<CrystalizeStatement>();
		// 1. Normalize the script
		script = script
				.replaceAll("(  +)", " ")
				.replaceAll("(\\) +\\{)", "){")
				.replaceAll("\n", ";");
		if (!script.endsWith(";")) script += ";";
		
		// 2. Init
		LangMatcher matcher = new LangMatcher(script);
		
		// 3. Parsing loop
		while (!matcher.endOfString()) {
			if (matcher.next(" ")) {
				matcher.accept();
				continue;
			}
			if (matcher.next("function ")) {
				// Named function
				matcher.accept();
				String name = matcher.nextUntil("(");
				String[] params = matcher.nextUntil(")").replaceAll("(, +)", ",").split(",");
				matcher.nextUntil("{");
				matcher.curlyBracketsCounter = 0;
				String functionScript = matcher.nextWithBrackets();
				instance.objects.put(name, new ObjectFunction(functionScript, params, compile(functionScript, instance)));
			} else if (matcher.next("function(")) {
				// Anonymous function
			} else if (matcher.next("var ")) {
				// Define variable
				matcher.accept();
				String varName = StringUtils.trim(matcher.nextUntil("="));
				String exp = StringUtils.trim(matcher.nextUntil(";"));
				Expression expE = parseExpression(exp, instance);
				CrystalizeObject expObj;
				if (expE instanceof FunctionExpression) {
					FunctionExpression funcExp = (FunctionExpression) expE;
					CrystalizeObject[] objs = new CrystalizeObject[funcExp.exps.length];
					for (int i = 0; i < objs.length; i++) objs[i] = funcExp.exps[i].toObject(instance);
					expObj = funcExp.linkedFunction.validate(instance, objs).toObject(instance);
				}
				else expObj = expE.toObject(instance);
				instance.objects.put(varName, expObj);
			} else if (matcher.next("return ")) {
				matcher.accept();
				String exp = matcher.nextUntil(";");
				statements.add(new StatementFunctionReturn(parseExpression(exp, instance)));
			} else {
				// Expression
				String expStr = matcher.nextUntil(";");
				if (expStr == "") continue;
				statements.add(new StatementExpressionEvaluate(parseExpression(expStr, instance)));
			}
		}
		
		return statements.toArray(new CrystalizeStatement[0]);
	}
	
	public static final Expression parseExpression(String exp, ObjectsCompound instance) {
		// System.out.println(exp);
		if (exp.startsWith("\"")) {
			// Welcum to my old way to parse expressions
			String str = "";
			int pp = 0;
			char[] arr = exp.toCharArray();
			int i = 0;
			for (int j = 0; j < arr.length; j++) {
				i++;
				char ch = arr[i];
				if (pp == 0) {
					if (ch == '\\') pp = 1;
					else if (ch == '"') break;
					else str += ch;
				} else if (pp == 1) {
					str += ch;
					pp = 0;
				}
			}
			if (i >= exp.length() - 1) return new StringExpression(str);
			else {
				LangMatcher matcher = new LangMatcher(StringUtils.trim(exp.substring(i + 1)));
				matcher.nextUntil("+", "-", "*", "/");
				Expression left = new StringExpression(str);
				Expression right = parseExpression(StringUtils.trim(matcher.matchingString.substring(matcher.mainPointer)), instance);
				String sign = matcher.previousMatched;
				return new MultipleExpressions(left, sign, right);
			}
		}
		LangMatcher matcher = new LangMatcher(exp);
		// System.out.println(exp);
		String _temp = matcher.nextUntil("+", "-", "*", "/", "!!!eos");
		String a = StringUtils.trim(_temp);
		if (matcher.previousMatched == "!!!eos") {
			return parseExpression1(a, instance);
		} else {
			Expression left = parseExpression1(a, instance);
			Expression right = parseExpression(StringUtils.trim(StringUtils.trim(
					matcher.matchingString.substring(a.length()))
					.substring(matcher.previousMatched.length()))
			, instance);
			String sign = matcher.previousMatched;
			return new MultipleExpressions(left, sign, right);
		}
	}
	private static final Expression parseExpression1(String a, ObjectsCompound instance) {
		if (NumberUtils.isNumeric(a)) return new NumberExpression(Double.parseDouble(a));
		else if (a.contains("(") && a.endsWith(")")) {
			String functionName = a.split("\\(")[0];
			String[] params = a.substring(functionName.length() + 1, a.length() - 1).replaceAll(", ", ",").split(",");
			if (params[0].length() == 0) {
				return new FunctionExpression((ObjectFunction) instance.getObject(functionName));
			}
			Expression[] exps = new Expression[params.length];
			for (int i = 0; i < params.length; i++) exps[i] = parseExpression(params[i], instance);
			FunctionExpression expFunc = new FunctionExpression((ObjectFunction) instance.getObject(functionName), exps);
			return expFunc;
		} else {
			// Variable
			if (instance.hasObject(a)) return new VariableExpression(instance.getObject(a));
			else throw new Error("Variable " + a + " not found in current context!");
		}
	}
	
	public static final Expression eval(ObjectsCompound instance, CrystalizeStatement... statements) {
		for (int i = 0; i < statements.length; i++) {
			CrystalizeStatement statement = statements[i];
			if (statement instanceof StatementFunctionReturn) {
				return finalValue(((StatementFunctionReturn) statement).result, instance);
			}
			else if (statement instanceof StatementExpressionEvaluate) finalValue(((StatementExpressionEvaluate) statement).expression, instance);
		}
		return null;
	}
	private static final Expression finalValue(Expression exp, ObjectsCompound instance) {
		if (exp instanceof VariableExpression) return ((VariableExpression) exp).linked.toExpression();
		else if (exp instanceof FunctionExpression) {
			FunctionExpression e = (FunctionExpression) exp;
			CrystalizeObject[] objs = new CrystalizeObject[e.exps.length];
			for (int i = 0; i < e.exps.length; i++) objs[i] = e.exps[i].toObject(instance);
			return e.linkedFunction.validate(instance, objs);
		}
		else if (exp instanceof MultipleExpressions) return ((MultipleExpressions) exp).finalValue(instance);
		else return exp;
	}
	
	public static final CrystalizeObject expressionToObject(Expression exp) {
		return null;
	}
	
	public static final void setupCore(ObjectsCompound instance) {
		instance.objects.put("print", new InternalFunctionPrint());
	}
	
}
