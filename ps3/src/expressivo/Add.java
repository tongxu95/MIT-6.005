package expressivo;

import java.util.Map;

/** 
 * An immutable data type representing an addition expression.
 */
class Add implements Expression {
	private final Expression left, right;
	
	// Abstraction function:
    //   represent an addition expression 
    // Representation invariant:
    //   left != null, right != null
    // Safety from rep exposure:
    //   All fields are private and final
    //	 Expressions are immutable
	
	public Add(Expression left, Expression right) {
		this.left = left;
		this.right = right;
		checkRep();
	}
	
	private void checkRep() {
		assert left != null;
		assert right != null;
	}
	
    /**
     * @return the Expression to the left of the add operator
     */
	public Expression getLeft() {
		return left;
	}
	
    /**
     * @return the Expression to the right of the add operator
     */
	public Expression getRight() {
		return right;
	}
	
    /**
     * @return a string representation of an addition operation. The string consists of the 
     * 		   string representation for the left Expression and the right Expression, 
     * 		   concatenated together with " + " characters. 
     * 		   In other words, this method returns a string equal to the value of:
     * 		   left.toString() + right.toString()
     */
	@Override
	public String toString() {
		return left.toString() + " + " + right.toString();
	}
	
    /**
     * @param thatObject any object
     * @return true if this and that Object are instances of Add (or thatOject is an instance of
     * 		   Grouping containing an Add expression), and left Expression of this and thatObject
     * 		   are equal, and the right Expression of this and thatObjct are equal.
     * 		   Two expressions are equal when they have the same mathematical meaning (contain the  
     * 		   same variables, numbers, and operators, in the same order) and grouping
     */
	@Override
	public boolean equals (Object thatObject) {
		if (thatObject instanceof Grouping) {
			Grouping thatGrouping = (Grouping) thatObject;
			return this.equals(thatGrouping.getExpr());
		} else if (!(thatObject instanceof Add)) return false;
		Add thatAdd = (Add) thatObject;
		return (left.equals(thatAdd.getLeft())) && (right.equals(thatAdd.getRight()));
	}
	
    /**
     * @return a hash code for this addition operation by adding the hash code for its left and 
     * 		   right expressions.
     */
	@Override
	public int hashCode() {
		return left.hashCode()*2 + right.hashCode();
	}
	
    /**
     * @param var a Variable 
     * @return a new Expression (type: Add) in which the left Expression is the derivative of the 
     * 		   left Expression in this Expression with respect to var, and the right Expression is
     * 		   the derivation of the right Expression in this Expression with respect to var.
     * 		   Derivative in the form
     *		   		d(u*v)/dx = u*(dv/dx) + x*(du/dx)
     */
	@Override
	public Expression differentiate (Variable var) {
		return new Add(this.getLeft().differentiate(var), 
				this.getRight().differentiate(var));
	}
	
    /**
     * @param envir a Mapping of Variables to Numbers 
     * @return A new Add Expression with substituted left and right expressions, or if both
     * 		   left and right expressions are Numbers, return a single Number that is the
     * 		   sum of the left and right Numbers.   
     */
	@Override
	public Expression simplify (Map<Variable, Number> envir) {
		Add simplified = new Add(this.getLeft().simplify(envir),
				this.getRight().simplify(envir));
		if (simplified.getLeft().isNumber() && simplified.getRight().isNumber()) {
			Number leftConst = (Number) simplified.getLeft();
			Number rightConst = (Number) simplified.getRight();
			if (leftConst.isInteger() && rightConst.isInteger()) {
				return new Number(leftConst.getValue().intValue() +
						rightConst.getValue().intValue());
			} else {
				return new Number(leftConst.getValue() + rightConst.getValue());
			}
		}
		return simplified;		
	}
	
	@Override
	public boolean isNumber() {
		return false;
	}
	
	
}
