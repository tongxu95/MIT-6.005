/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import expressivo.parser.ExpressionLexer;
import expressivo.parser.ExpressionParser;

import java.util.Map;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS3 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition
    //   Expression = Number (value: Double) + Variable (name: String)
	//				+ Add (left: Expression, right: Expression)
	// 				+ Multiply (left: Expression, right: Expression)
	//				+ Grouping (Expression)
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) throws IllegalArgumentException {
        try {
        	CharStream stream = new ANTLRInputStream(input);
            ExpressionLexer lexer = new ExpressionLexer(stream);
            lexer.reportErrorsAsExceptions();
            
            TokenStream tokens = new CommonTokenStream(lexer);
            ExpressionParser parser = new ExpressionParser(tokens);
            parser.reportErrorsAsExceptions();
            
            ParseTree tree = parser.root();
            ParseTreeWalker walker = new ParseTreeWalker();
            MakeExpression walkExpression = new MakeExpression();
            walker.walk(walkExpression, tree);
            return walkExpression.getExpression();
        } catch (Exception e) {
        	throw new IllegalArgumentException("ParseError: illegal expression");
        }
    }
    
    /**
     * @return a parsable representation of this expression, such that
     * 		   for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * 		   Expressions, as defined in the PS3 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * 		   equality, such that for all e1,e2:Expression,
     *     	   e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    /**
     * @param var a Variable 
     * @return a new Expression with the derivative of this Expression with respect to var.
     */
    public Expression differentiate (Variable var);
    
    /**
     * @param envir a Mapping of Variables to Numbers 
     * @return a new and simplified Expression that substitutes the values of those variables 
     * 		   into the expression. Any variables in the expression but not in envir remain as
     * 		   variables. Any variables in envir but not the expression are simply ignored.
     *  	   When the substituted Expression contain only Numbers, with no Variables remaining, 
     *  	   the substituted Expression reduces to a single Number.
     */
    public Expression simplify (Map <Variable, Number> envir);
    
    /**
     * @return true if this Expression is a Number, or a Grouping that contains a Number,
     * and false otherwise. 
     */
    public boolean isNumber ();
}
