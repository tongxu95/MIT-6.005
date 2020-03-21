package expressivo;

import java.util.Map;

/**
 * An immutable data type representing a multiplication expression.
 */
class Multiply implements Expression {
	private final Expression left, right;
	
	// Abstraction function:
    //   represent an addition expression 
    // Representation invariant:
    //   left != null, right != null
    // Safety from rep exposure:
    //   All fields are private and final
    //	 Expressions are immutable
	
	public Multiply (Expression left, Expression right) {
		this.left = left;
		this.right = right;
		checkRep();
	}
	
	private void checkRep() {
		assert left != null;
		assert right != null;
	}
	
    /**
     * @return the Expression to the left of the multiplication operator
     */
	public Expression getLeft() {
		return left;
	}
	
    /**
     * @return the Expression to the right of the multiplication operator
     */
	public Expression getRight() {
		return right;
	}	
	
    /**
     * @return a string representation of a multiplication operation. The string consists of the 
     * 		   string representation for the left Expression and the right Expression, 
     * 		   concatenated together with "*" characters. 
     * 		   In other words, this method returns a string equal to the value of:
     * 		   left.toString()*right.toString()
     */
	@Override
	public String toString() {
		return left.toString() + "*" + right.toString();
	}
	
    /**
     * @param thatObject any object
     * @return true if this and that Object are instances of Multiply, (or thatOject is an 
     * 		   instance of Grouping containing an Multiply expression), and the left Expression of
     * 		   this and thatObject are equal, and the right Expression of this and thatObjct are 
     * 		   equal. Two expressions are equal when they have the same mathematical meaning 
     * 		   (contain the same variables, numbers, and operators, in the same order) and grouping
     */
	@Override
	public boolean equals (Object thatObject) {
		if (thatObject instanceof Grouping) {
			Grouping thatGrouping = (Grouping) thatObject;
			return this.equals(thatGrouping.getExpr());
		} else if (!(thatObject instanceof Multiply)) return false;
		Multiply thatMultiply = (Multiply) thatObject;
		return (left.equals(thatMultiply.getLeft())) && (right.equals(thatMultiply.getRight()));
	}
	
    /**
     * @return a hash code for this multiplication operation by adding the hash code for its left
     * 		   and right expressions.
     */
	@Override
	public int hashCode() {
		return left.hashCode()*2 + right.hashCode();
	}
	
    /**
     * @param var a Variable 
     * @return a new Expression (type: Add) with the derivative of this expression with respect to 
     * 		   var. Derivative in the form:
     * 		   		d(u*v)/dx = u*(dv/dx) + x*(du/dx)
     * 		   Expression is not simplified.
     */
	@Override
	public Expression differentiate (Variable var) {
		Grouping derivativeOfLeft = new Grouping(this.getLeft().differentiate(var), 1);
		Grouping derivativeOfRight = new Grouping(this.getRight().differentiate(var), 1);
		Multiply leftDerivative = new Multiply (this.getLeft(), 
				derivativeOfRight);
		Multiply rightDerivative = new Multiply (this.getRight(), 
				derivativeOfLeft);
		return new Add(leftDerivative, rightDerivative);
	}
	
    /**
     * @param envir a Mapping of Variables to Numbers 
     * @return A new Multiply Expression with substituted left and right expressions.
     * 		   If both left and right expressions are Numbers, return a single Number that is
     * 		   the product of the left and right Numbers.   
     */
	@Override
	public Expression simplify (Map<Variable, Number> envir) {
		Multiply simplified = new Multiply(this.getLeft().simplify(envir),
				this.getRight().simplify(envir));
		if (simplified.getLeft().isNumber() && simplified.getRight().isNumber()) {
			Number leftConst = (Number) simplified.getLeft();
			Number rightConst = (Number) simplified.getRight();
			if (leftConst.isInteger() && rightConst.isInteger()) {
				return new Number(leftConst.getValue().intValue() *
						rightConst.getValue().intValue());
			} else {
				return new Number(leftConst.getValue() * rightConst.getValue());
			}
		}
		return simplified;
	}
	
	@Override
	public boolean isNumber() {
		return false;
	}
	
}
