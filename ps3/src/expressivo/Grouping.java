package expressivo;

import java.util.Map;

public class Grouping implements Expression{
	private final int numParen;
	private final Expression expr;
	
	
	// Abstraction function:
    //   represent grouping of expression 
    // Representation invariant:
    //   numParen >= 0; expr != null
    // Safety from rep exposure:
    //   All fields are private and final
    //	 Expressions are immutable
	
	public Grouping(Expression expr, int numParen) {
		this.numParen = numParen;
		this.expr = expr;
		checkRep();
	}
	
	private void checkRep() {
		assert numParen >= 0;
		assert expr != null;
	}

    /**
     * @return the number of parentheses around the expression
     */
	public int getNumParen() {
		return numParen;
	}
	
    /**
     * @return the expression in the grouping
     */
	public Expression getExpr() {
		return expr;
	}
	
    /**
     * @return a string representation of a grouped expression. The string consists of the 
     * 		   string representation of the expression, enclosed in the number of parentheses
     *  	   specified by the users
     */
	@Override
	public String toString() {
		if (numParen == 0) {
			return expr.toString();
		} else {
			return "(" + new Grouping(expr, (numParen - 1)).toString() + ")";
		}
	}
	
    /**
     * @param thatObject any object
     * @return true if and only if the left Expression of this and thatObject are equal and the 
     * 		   right Expression of this and thatObjct are equal.
     */
	@Override
	public boolean equals (Object thatObject) {
		if ((thatObject instanceof Expression) && ! (thatObject instanceof Grouping)) {
			return thatObject.equals(expr);
		}
		Grouping thatGrouping = (Grouping) thatObject;
		return expr.equals(thatGrouping.getExpr());
	}
	
    /**
     * @return a hash code for the expression contained in this Grouping, depend on the type  
     * 		   of expression contained in Grouping.
     */
	@Override
	public int hashCode() {
		return expr.hashCode();
	}	

    /**
     * @param var a Variable 
     * @return an Expression with the derivative of the Expression contained
     * 		   in this grouping with respect to var.
     */
	@Override
	public Expression differentiate (Variable var) {
		return this.getExpr().differentiate (var);
	}
	
    /**
     * @param envir a Mapping of Variables to Numbers 
     * @return A substituted and simplified Expression of the Expression contained in the
     * 		   grouping.
     */
	@Override
	public Expression simplify (Map<Variable, Number> envir) {
		return this.getExpr().simplify(envir);
	}
	
	@Override
	public boolean isNumber() {
		if (expr.isNumber()) return true;
		return false;
	}
	
	
 }
