/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {
	
	private static final String n1 = "3.1415";
	private static final String var1 = "x";
	private static final String var2 = "y";
	private static final String expr1 = "x*x*x";
	private static final String expr2 = "x*y";
	private static final String expr3 = expr1 + " + " + expr2;
	private static final String expr4 = "(x*x)*x";
	
	private static final String constDeriv = "0";
	private static final String varDeriv = "1";
	private static final String expr1Deriv = "x*(x*(1) + x*(1)) + x*x*(1)";
	private static final String expr2Deriv = "x*(0) + y*(1)";
	private static final String expr3Deriv = expr1Deriv + " + " + expr2Deriv;
	private static final String expr4Deriv = "(x*x)*(1) + x*(x*(1) + x*(1))";
	
	private static final String valueVar1 = "5";
	private static final String valueVar2 = "10.0";
	private static final String expr5 = "1 + 3.1415";

	private static final String simpExpr5 = String.valueOf(1 + 3.1415);
	private static final String simpExpr4 = String.valueOf(125);
	private static final String simpExpr3 = String.valueOf(125) + " + " + "5*y";
	
	
//	private static final Expression expr1 = new Multiply(var3, var3);

//	private static final Expression expr3 = new Multiply(var3, var1);
//

//	private static final Grouping prodConstDeriv = new Grouping(constDeriv, 1);
//	private static final Grouping prodVarDeriv = new Grouping(varDeriv, 1);
//	
//	private static final Expression leftDeriv2 = new Multiply(expr1, prodVarDeriv);
//	private static final  Multiply derivRightLeft = new Multiply(var3, prodVarDeriv);
//	private static final Grouping derivRight = new Grouping(new Add(derivRightLeft, 
//			derivRightLeft), 1);
//	private static final Multiply rightDeriv2 = new Multiply(var3, derivRight);	
//	private static final Add deriv2 = new Add(leftDeriv2, rightDeriv2);
//	
//	private static final Expression leftDeriv3 = new Multiply(var3, prodConstDeriv);
//	private static final Expression rightDeriv3 = new Multiply(var1, prodVarDeriv);	
//	private static final Add deriv3 = new Add(leftDeriv3, rightDeriv3);
//	
//	private static final Expression expr4 = new Add(expr2, expr3);
//	private static final Add deriv4 = new Add(deriv2, deriv3);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // Testing strategy: Commands.differentiate():
    //		parameter type: variable, constant (number, a different variable),
    //		num of parameters: 1, 2, >2
    //		operator type: *, +, parentheses			  

    // num of parameter = 1, parameter type: constant (number)
    @Test
    public void testDifferentiateConstant() {
    	assertEquals("correct derivation for constant", Commands.differentiate(n1, var1), constDeriv);
    }
    
    // num of parameter = 1, parameter type: variable
    @Test
    public void testDifferentiateVariableByItself() {
    	assertEquals("correct derivation for variable", Commands.differentiate(var1, var1), varDeriv);
    }
    
    // num of parameter = 1, parameter type: constant (a different variable)
    @Test
    public void testDifferentiateVariableByDifferentVariable() {
    	assertEquals("correct derivation for variable", Commands.differentiate(var2, var1), constDeriv);
    }
    
    // num of parameters > 2, parameter type: variable, operator type: *
    @Test
    public void testDifferentiateProductMultipleVariable() {
    	assertEquals("correct derivation for expression", Commands.differentiate(expr1, var1), expr1Deriv);
    }
    
    // num of parameters = 2, parameter type: variable, constant (a different variable)
    // operator type: *
    @Test
    public void testDifferentiateProductVariableAndConstant() {
    	assertEquals("correct derivation for expression", Commands.differentiate(expr2, var1), expr2Deriv);
    }
    
    // num of parameters = >2, parameter type: variable, constant (a different variable)
    // operator type: *, +
    @Test
    public void testDifferentiateAddVariableAndConstant() {
    	assertEquals("correct derivation for expression", Commands.differentiate(expr3, var1), expr3Deriv);
    }
    
    // num of parameters > 2, parameter type: variable, operator type: *, parentheses
    @Test
    public void testDifferentiateProductMultipleVariableWithDifferentGrouping() {
    	assertEquals("correct derivation for expression", Commands.differentiate(expr4, var1), expr4Deriv);
    }
 
    // Testing strategy: differentiate():
    //		Number: one test case
    //		Variable: reduce to a constant, no simplification
    //		Add/Multiply: reduce to constant, partial simplification, no simplification
    //					  num of parameters: 2, >2, parameter type: variable
    //		Environment: empty, variables not in expression, variables in expression
    //		remove parentheses
    
    // num of parameters = 1, parameter type = Number (decimal)
    @Test
    public void testSimplifyNumber() {
    	Map<String, String> envir = new HashMap<>();
    	assertEquals("no simplification required", Commands.simplify(n1, envir), n1);
    }
    
    // num of parameters = 1, parameter type = Variable, simplifies to constant
    @Test
    public void testSimplifyVariableInEnvironment() {
    	Map<String, String> envir = new HashMap<>();
    	envir.put(var2, valueVar2);
    	assertEquals("simplifies to a constant", Commands.simplify(var2, envir), valueVar2);
    }
    
    // num of parameters = 1, parameter type = Variable, no simplification
    @Test
    public void testSimplifyVariableNotInEnvironment() {
    	Map<String, String> envir = new HashMap<>();
    	envir.put(var2, valueVar2);
    	assertEquals("simplifies to a constant", Commands.simplify(var1, envir), var1);
    }
    
    // num of parameters = 2， parameter type: numbers, operator: +, simplifies to constant
    @Test
    public void testSimplifySumToConstant() {
    	Map<String, String> envir = new HashMap<>();
    	assertEquals("simplifies to a constant", Commands.simplify(expr5, envir), simpExpr5);
    }    
    
    // num of parameters > 2, parameter type: Variable, operator type: *, parentheses
    // simplifies to constant
    @Test
    public void testSimplifyProductToConstant() {
    	Map<String, String> envir = new HashMap<>();
    	envir.put(var1, valueVar1);
    	assertEquals("simplifies to a constant", Commands.simplify(expr4, envir), simpExpr4);
    }
    
    // num of parameters > 2, parameter type: variable, operator type: *, +, partial simplification
    @Test
    public void testSimplifyPartialSimplication() {
    	Map<String, String> envir = new HashMap<>();
    	envir.put(var1, valueVar1);
    	assertEquals("simplifies to a constant", Commands.simplify(expr3, envir), simpExpr3);
    }
    
    
}
