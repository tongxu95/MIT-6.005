package expressivo;

import java.util.Map;

/**
* An immutable data type representing nonnegative integers and floating-point numbers in a
* polynomial expression.
*/
class Number implements Expression {
	private final boolean integer;
	private final Double value;
	
	// Abstraction function:
    //   represent nonnegative integers and floating-point numbers 
    // Representation invariant:
    //   value >= 0;
    // Safety from rep exposure:
    //   All fields are private and final
    //	 Double guarantees immutability
	
    /**
     * Create a Number
     * @param value a non-negative integer
     */
	public Number (Integer value) {
		this.value = (double) value;
		integer = true;
		checkRep();
	}
	
    /**
     * Create a Number
     * @param value a non-negative floating point number
     */
	public Number (Double value) {
		this.value = value;
		integer = false;
		checkRep();
	}
	
	private void checkRep() {
		assert value >= 0;
	}
	
    /**
     * @return the floating point value of this number, used for comparing equality of two Numbers
     */
	public Double getValue() {
		return value;
	}
	
    /**
     * @return true if this Number is an integer, and false otherwise 
     */
	public boolean isInteger() {
		return integer;
	}
	
    /**
     * @return the string representation of a non-negative integers or floating-point numbers
     * 		   number in a polynomial expression
     */
	@Override
	public String toString() {
		if (integer) {
			return String.valueOf(value.intValue());
		} else {
			return String.valueOf(value);
		}		
	}
	
    /**
     * @param thatObject any object
     * @return true if this and thatObject are Numbers with the same value (integer or double);
     * 		   or if thatObject is an instance of Grouping that contain an Expression that is
     * 		   an instance of Number with the same value (integer or double)
     */
	@Override
	public boolean equals (Object thatObject) {
		if (thatObject instanceof Grouping) {
			Grouping thatGrouping = (Grouping) thatObject;
			return this.equals(thatGrouping.getExpr());
		} else if (! (thatObject instanceof Number)) return false;
		Number thatNumber = (Number) thatObject;
		return value.equals(thatNumber.getValue());
	}
	
    /**
     * @return a hash code for this Number, using the hash code of its value 
     */
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
    /**
     * @param var a Variable 
     * @return a new Expression (type: Number) with the derivative of this Number with respect to
     * 		   var; evaluates to 0.  
     */
	@Override
	public Expression differentiate (Variable var) {
		return new Number(0);
	}
	
    /**
     * @param envir a Mapping of Variables to Numbers 
     * @return this Number. No simplification required for a Number.
     */
	@Override
	public Expression simplify (Map<Variable, Number> envir) {
		return this;
	}
	
	@Override
	public boolean isNumber() {
		return true;
	}
	
	
}
