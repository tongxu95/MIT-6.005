/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

	private static final Expression n1 = new Number(1);
	private static final Expression n2 = new Number(3.1415);
	private static final Expression n3 = new Number(1.0);
	private static final Expression var1 = new Variable("y");
	private static final Expression grouping1 = new Grouping(var1, 3);
	private static final Expression var2 = new Variable("Foo");
	private static final Expression add1 = new Add(n1, n2);	
	private static final Expression grouping2 = new Grouping(add1, 1);	
	private static final Expression multiply1 = new Multiply(grouping2, var1);
	
	private static final Expression grouping3 = new Grouping(var1, 1);
	private static final Expression grouping4 = new Grouping(n1, 1);
	
	private static final Expression sameExpr11 = new Add(n1, var1);	
	private static final Expression sameExpr12 = new Grouping (sameExpr11, 1);
	private static final Expression sameExpr13 = new Add (grouping4, grouping1);
	private static final Expression diffExpr1 = new Add (var1, n1);

	private static final Expression sameExpr21 = new Add(var1, add1);		
	private static final Expression sameExpr22 = new Grouping(sameExpr21, 1);
	
	private static final Expression sameExpr31 = new Multiply(n1, var1);	
	private static final Expression sameExpr32 = new Grouping (sameExpr31, 1);
	private static final Expression sameExpr33 = new Multiply(n3, var1);	
	private static final Expression diffExpr3 = new Multiply (var1, n3);	
	
	private static final Expression sameExpr4 = new Grouping(multiply1, 1);	
	
	Variable x = new Variable("x");
	private static final Number constDeriv = new Number(0);
	private static final Number varDeriv = new Number(1);
	private static final Grouping prodConstDeriv = new Grouping(constDeriv, 1);
	private static final Grouping prodVarDeriv = new Grouping(varDeriv, 1);
	
	private static final Expression var3 = new Variable("x");
	private static final Expression expr1 = new Multiply(var3, var3);
	private static final Expression expr2 = new Multiply(var3, expr1);
	private static final Expression rightDeriv2 = new Multiply(expr1, prodVarDeriv);
	private static final  Multiply derivLeftLeft = new Multiply(var3, prodVarDeriv);
	private static final Grouping derivRight = new Grouping(new Add(derivLeftLeft, 
			derivLeftLeft), 1);
	private static final Multiply leftDeriv2 = new Multiply(var3, derivRight);	
	private static final Add deriv2 = new Add(leftDeriv2, rightDeriv2);
	
	private static final Expression expr3 = new Multiply(var3, var1);
	private static final Expression leftDeriv3 = new Multiply(var3, prodConstDeriv);
	private static final Expression rightDeriv3 = new Multiply(var1, prodVarDeriv);	
	private static final Add deriv3 = new Add(leftDeriv3, rightDeriv3);
	
	private static final Expression expr4 = new Add(expr2, expr3);
	private static final Add deriv4 = new Add(deriv2, deriv3);
	
	private static final Expression expr5 = new Grouping(expr1, 1);
	private static final Expression expr6 = new Multiply(expr5, var3);
	private static final Multiply leftDeriv6 = new Multiply(expr5, prodVarDeriv);
	private static final Add deriv6 = new Add(leftDeriv6, leftDeriv2);
	
	private static final Number xValue = new Number(5);
	private static final Variable y = new Variable("y");
	private static final Number yValue = new Number(10.0);
	private static final Expression constant = new Number(125);
	
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // Testing strategy: toString():
	// 		Number: non-negative integer and double
	//		Variable: name length: 1, n (lower case, mixed cases, upper case)
	//		Add: num of parameters: 2 (Number/Variable), > 2 (Add/Multiply/Grouping)
	//		Multiply: num of parameters: 2 (Number/Variable), > 2 (Add/Multiply/Grouping)
    //		Grouping: parameter type: Number, Variable, Add, Multiply, Grouping
    //				  num of parentheses: 0, 1, n
    
    
    //toString: Number=integer
    @Test
    public void testIntegerNumberToString() {        
        int n = 1;
        assertEquals("valid string representation for Number", Integer.toString(n), n1.toString());
    }
    
    //toString: Number=integer
    @Test
    public void testDecimalNumberToString() {        
        double d = 3.1415;
        assertEquals("valid string representation for Number", Double.toString(d), n2.toString());
    }
    
    //toString: Variable: length = 1, lower cases
    @Test
    public void testVariableOneLetterToString() {
        String s = "y";
        assertEquals("valid string representation for Variable", s, var1.toString());
    }
    
    //toString: Grouping: parameter type: Variable; num of parentheses: n
    @Test
    public void testGroupingVariableToString() {
        String s = "(((y)))";
        assertEquals("valid string representation for Variable", s, grouping1.toString());
    }
    
    //toString: Variable: length = n, mixed cases
    @Test
    public void testVariableMultipleLettersToString() {
        String s = "Foo";
        assertEquals("valid string representation for Variable", s, var2.toString());
    }
    
    //toString: Add: num of parameters = 2 (Numbers）
    @Test
    public void testAddNumbersToString() {
        String s = "1 + 3.1415";
        assertEquals("valid string representation for Add", s, add1.toString());
    }
    
    //toString: Grouping: parameter type: Add; num of parentheses: 1
    @Test
    public void testGroupingSumToString() {
        String s = "(1 + 3.1415)";
        assertEquals("valid string representation for Variable", s, grouping2.toString());
    }
    
    //toString: Multiply: num of parameters > 2 (Grouping (Add), Variable)
    @Test
    public void testMultiplyGroupingAndVariableToString() {
        String s = "(1 + 3.1415)*y";
        assertEquals("valid string representation for Variable", s, multiply1.toString());
    } 

       
    // Testing strategy: equals() and hashCode():
	// 		Number: equality of integer and double with the same value
    //				equality of Number with and without parentheses
	//		Variable: equality of different instances of Variable with the same name (case sensitive)
    //				  equality of Variable with and without parentheses
	//		Add/Multipy: equality of expressions with the same mathematical meaning and grouping 
    //					 (with or without parentheses)
    
    
    // same hash code and equality for different instances of Number 1.0 (double)
    // same hash code and equality for 1.0 (double) and 1 (integer)
    @Test
    public void testNumbersEquality() {        
        assertTrue("The two numbers are equal", n3.equals(n1));
        assertFalse("The two numbers are not equal", n3.equals(n2));
        
        assertTrue("Same hashcode for two equal numbers", n3.hashCode() == n1.hashCode());
        assertTrue("Different hashcode for two unequal numbers", n3.hashCode() != n2.hashCode());
    }
    
    // same hash code and equality for different instance of Variable y
    @Test
    public void testVariableEquality() {
        Expression var3 = new Variable("y");
        Expression var4 = new Variable("Y");
        assertTrue("The two numbers are equal", var3.equals(var1));
        assertFalse("The two numbers are not equal", var4.equals(var1));
        
        assertTrue("Same hashcode for two equal numbers", var3.hashCode() == var1.hashCode());
        assertTrue("Different hashcode for two unequal numbers", var4.hashCode() != var1.hashCode());
    }
    
    // same hash code and equality for Variable y and Grouping (y) and (((y)))
    @Test
    public void testVariableEqualityWithAndWithoutParentheses() {
    	assertTrue("The two expression are equal", var1.equals(grouping1)); 
    	assertTrue("The two expression are equal", grouping1.equals(var1));
    	assertTrue("The two expression are equal", grouping1.equals(grouping3));
        assertTrue("Same hashcode for two equal expression", var1.hashCode() == grouping1.hashCode());
        assertTrue("Same hashcode for two equal expression", grouping3.hashCode() == grouping1.hashCode());
    }
    
    // same hash code and equality for Number 1 and Grouping (1) and (1.0)
    @Test
    public void testNumberEqualityWithAndWithoutParentheses() {
    	assertTrue("The two expression are equal", n1.equals(grouping4)); 
    	assertTrue("The two expression are equal", grouping4.equals(n1));
        assertTrue("Same hashcode for two equal expression", n1.hashCode() == grouping4.hashCode());
    }
    
    // same hash code and equality for Add 1 + y and Grouping (1 + y) and (1) + (((y))), 
    // different hash code and inequality to Add y + 1 
    @Test
    public void testSummationEqualityWithAndWithoutParentheses() {    	
    	assertTrue("The two expression are equal", sameExpr11.equals(sameExpr12)); 
    	assertTrue("The two expression are equal", sameExpr12.equals(sameExpr13));
    	assertFalse("The two expression are not equal", sameExpr13.equals(diffExpr1));
        assertTrue("Same hashcode for two equal expressions", 
        		sameExpr11.hashCode() == sameExpr12.hashCode());
        assertTrue("Same hashcode for two equal expressions", 
        		sameExpr12.hashCode() == sameExpr13.hashCode());
        assertFalse("Different hashcode for two unequal expressions", 
        		sameExpr13.hashCode() == diffExpr1.hashCode());
    }
    
    // same hash code and equality for Add y+ 1 + 3.1415 and Grouping (y + 1 + 3.1415) 
    @Test
    public void testMultipleSummationEqualityWithAndWithoutParentheses() {
    	assertTrue("The two expression are equal", sameExpr21.equals(sameExpr22));
    	assertTrue("Same hashcode for two equal expressions", 
        		sameExpr21.hashCode() == sameExpr22.hashCode());
    }
    
    // same hash code and equality for Multiply 1*y and Grouping (1*y) and 1.0*y, 
    // different hash code and inequality to Add y*1.0 
    @Test
    public void testMultiplicationEqualityWithAndWithoutParentheses() {    	
    	assertTrue("The two expression are equal", sameExpr31.equals(sameExpr32)); 
    	assertTrue("The two expression are equal", sameExpr32.equals(sameExpr33));
    	assertFalse("The two expression are not equal", sameExpr33.equals(diffExpr3));
        assertTrue("Same hashcode for two equal expressions", 
        		sameExpr11.hashCode() == sameExpr12.hashCode());
        assertTrue("Same hashcode for two equal expressions", 
        		sameExpr12.hashCode() == sameExpr13.hashCode());
        assertFalse("Different hashcode for two unequal expressions", 
        		sameExpr13.hashCode() == diffExpr1.hashCode());
    }
    
    // same hash code and equality for Multiply (1 + 3.1415)*y and
    // Grouping ((1 + 3.1415)*y)  
    @Test
    public void testMultipleProductEqualityWithAndWithoutParentheses() {
    	assertTrue("The two expression are equal", multiply1.equals(sameExpr4));
    	assertTrue("Same hashcode for two equal expressions", 
        		multiply1.hashCode() == sameExpr4.hashCode());
    }
    
    
    // Testing strategy: parse():
	// 		Number: num of digits: 1, n
    //				num of decimals: 0, 1, n
	//		Variable: num of letters:1, n (lower case, mixed cases, upper case)
	//		Add/Multipy: num of parameters: 2, >2
    //					 parameter type: Number, Variable, Sum/Product (with or
    //					 without parentheses)
    //		Parentheses: 0, 1, n (multiple parentheses/nested parentheses)
    //		Spaces: space, tab (0, 1, n)
    //		catches invalid expression: unbalanced parentheses, missing operator,
    //									missing parameter for operator, invalid
    //									operator (i.e. -, ^, /, %), invalid input
    //									(negative number, invalid representation)
    
    // Number: num of digit = n, num of decimals = 0
    @Test
    public void testParseInteger() {
    	String s = "20200219";
    	Expression expr = Expression.parse(s);
    	assertTrue("successfully parsed", s.equals(expr.toString()));
    }
    
    // Number: num of digit = 1, num of decimals = n
    @Test
    public void testParseDecimal() {
    	String s = "3.1415";
    	Expression expr = Expression.parse(s);
    	assertTrue("successfully parsed", s.equals(expr.toString()));
    }
    
    // Number: num of letters = n (mixed cases)
    @Test
    public void testParseVariable() {
    	String s = "Foo";
    	Expression expr = Expression.parse(s);
    	assertTrue("successfully parsed", s.equals(expr.toString()));
    }
    
    // Sum: num of parameters = 2; type of parameter: Multiply (with single parentheses); n spaces
    @Test
    public void testParseSumOfProduct() {
    	String s = "(2*x    )+    (    y*x    )";
    	Expression expr = Expression.parse(s);
    	String expression = "(2*x) + (y*x)";
    	assertTrue("successfully parsed", expression.equals(expr.toString()));
    }
    
    // Sum: num of parameters > 2; type of parameter; Add, Variables, (with nested parentheses); single-spaced
    @Test
    public void testParseProductOfSumAndVariables() {
    	String s = "((3 + 4) * x * x)";
    	Expression expr = Expression.parse(s);
    	String expression = "((3 + 4)*x*x)";
    	assertTrue("successfully parsed", expression.equals(expr.toString()));
    }
    
    // Sum: num of parameters > 2; type of parameter; Numbers, Variables; no space; multiple parentheses
    @Test
    public void testParseMultipleNumbersAndVariblesEquation() {
    	String s = "4+3*x+2*x*x+1*x*x*(((x)))";
    	Expression expr = Expression.parse(s);
    	String expression = "4 + 3*x + 2*x*x + 1*x*x*(((x)))";
    	assertTrue("successfully parsed", expression.equals(expr.toString()));
    }
    
    // Invalid expression: unbalanced parentheses
    @Test (expected=IllegalArgumentException.class)
    public void testParseUnbalancedParentheses() {
    	Expression.parse("( 3");    	
    }
    
    // Invalid expression: missing operator
    @Test (expected=IllegalArgumentException.class)
    public void testParseMissingOperator() {
    	Expression.parse("3x");    	
    }
    
    // Invalid expression: missing parameter for operator
    @Test (expected=IllegalArgumentException.class)
    public void testParseMissingParameterForOperator() {
    	Expression.parse("3*");    	
    }
    
    // Invalid expression: invalid operator
    @Test (expected=IllegalArgumentException.class)
    public void testParseInvalidOperator() {
    	Expression.parse("2 - 3");    	
    }
    
    // Invalid expression: invalid number representation
    @Test (expected=IllegalArgumentException.class)
    public void testParseInvalidParameter() {
    	Expression.parse("6.02e23");    	
    }
    
    // Testing strategy: differentiate():
    //		parameter type: variable, constant (number, a different variable),
    //		num of parameters: 1, 2, >2
    //		operator type: *, +, parentheses				  

    // num of parameter = 1, parameter type: constant (number)
    @Test
    public void testDifferentiateConstant() {
    	assertEquals("correct derivation for constant", n2.differentiate(x), constDeriv);
    }
    
    // num of parameter = 1, parameter type: variable
    @Test
    public void testDifferentiateVariableByItself() {
    	assertEquals("correct derivation for variable", var3.differentiate(x), varDeriv);
    }
    
    // num of parameter = 1, parameter type: constant (a different variable)
    @Test
    public void testDifferentiateVariableByDifferentVariable() {
    	assertEquals("correct derivation for variable", var1.differentiate(x), constDeriv);
    }
    
    // num of parameters > 2, parameter type: variable, operator type: *
    @Test
    public void testDifferentiateProductMultipleVariable() {
    	assertEquals("correct derivation for expression", expr2.differentiate(x), deriv2);
    }
    
    // num of parameters = 2, parameter type: variable, constant (a different variable)
    // operator type: *
    @Test
    public void testDifferentiateProductVariableAndConstant() {
    	assertEquals("correct derivation for expression", expr3.differentiate(x), deriv3);
    }
    
    // num of parameters = >2, parameter type: variable, constant (a different variable)
    // operator type: *, +
    @Test
    public void testDifferentiateAddVariableAndConstant() {
    	assertEquals("correct derivation for expression", expr4.differentiate(x), deriv4);
    }
    
    // num of parameters > 2, parameter type: variable, operator type: *, parentheses
    @Test
    public void testDifferentiateProductMultipleVariableDifferentGrouping() {
    	assertEquals("correct derivation for expression", expr6.differentiate(x), deriv6);
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
    	Map<Variable, Number> envir = new HashMap<>();
    	assertEquals("no simplification required", n2.simplify(envir), n2);
    }
    
    // num of parameters = 1, parameter type = Variable, simplifies to constant
    @Test
    public void testSimplifyVariableInEnvironment() {
    	Map<Variable, Number> envir = new HashMap<>();
    	envir.put(y, yValue);
    	assertEquals("simplifies to a constant", var1.simplify(envir), yValue);
    }
    
    // num of parameters = 1, parameter type = Variable, no simplification
    @Test
    public void testSimplifyVariableNotInEnvironment() {
    	Map<Variable, Number> envir = new HashMap<>();
    	envir.put(x, xValue);
    	assertEquals("simplifies to a constant", var1.simplify(envir), var1);
    }
    
    // num of parameters = 2， parameter type: numbers, operator: +, simplifies to constant
    @Test
    public void testSimplifySumToConstant() {
    	Map<Variable, Number> envir = new HashMap<>();
    	Number leftNum = new Number(1);
    	Number rightNum = new Number(3.1415);
    	Expression constant = new Number(leftNum.getValue() + rightNum.getValue());
    	assertEquals("simplifies to a constant", add1.simplify(envir), constant);
    }    
    
    // num of parameters > 2, parameter type: Variable, operator type: *, parentheses
    // simplifies to constant
    @Test
    public void testSimplifyProductToConstant() {
    	Map<Variable, Number> envir = new HashMap<>();
    	envir.put(x, xValue);
    	assertEquals("simplifies to a constant", expr6.simplify(envir), constant);
    }
    
    // num of parameters > 2, parameter type: variable, operator type: *, +, partial simplification
    @Test
    public void testSimplifyPartialSimplication() {
    	Map<Variable, Number> envir = new HashMap<>();
    	envir.put(x, xValue);
    	Expression simpExpr = new Add(constant, new Multiply(xValue, y));
    	assertEquals("simplifies to a constant", expr4.simplify(envir), simpExpr);
    }
    
}
