package expressivo;

import java.util.Map;

/**
* An immutable data type representing a variable (case-sensitive nonempty strings of letters) in a
* polynomial expression.
*/
class Variable implements Expression{
	private final String name;
	
	// Abstraction function:
    //   represent variable names 
    // Representation invariant:
    //   contain letters only
	// 	 cannot be empty or contain spaces and other special characters
    // Safety from rep exposure:
    //   All fields are private and final
    //	 String guarantees immutability
	
    /**
     * Create a Variable
     * @param name nonempty string of letters
     */
	public Variable (String name) {
		this.name = name;
		checkRep();
	}
	
	private void checkRep() {
		assert name.matches("[a-zA-Z]+");
	}
	
    /**
     * @return the string name of this variable 
     */
	public String getName() {
		return name;
	}
	
    /**
     * @return the string representation a variable in a polynomial expression
     */
	@Override
	public String toString() {
		return name;
	}
	
    /**
     * @param thatObject any object
     * @return true if this and thatObject are Variable with the same string names;
     * 		   or if thatObject is an instance of Grouping that contain an Expression that is
     * 		   an instance of Variable with the same string name
     */
	@Override
	public boolean equals (Object thatObject) {
		if (thatObject instanceof Grouping) {
			Grouping thatGrouping = (Grouping) thatObject;
			return this.equals(thatGrouping.getExpr());
		} else if (!(thatObject instanceof Variable)) return false;
		Variable thatVariable = (Variable) thatObject;
		return name.equals(thatVariable.getName());
	}
	
    /**
     * @return a hash code for this Variable, using the hash code of its string name
     */
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
    /**
     * @param var a Variable 
     * @return a new Expression (type: Number) with the derivative this Variable with respect to 
     * 		   var; evaluates to 1 if this Variable and var are the same variable, and 0 otherwise.  
     */
	@Override
	public Expression differentiate (Variable var) {
		if (this.equals(var)) return new Number(1);
		else return new Number(0);
	}
	
    /**
     * @param envir a Mapping of Variables to Numbers 
     * @return A Number if envir contains value for this Variable; otherwise, return this Variable  
     */
	@Override
	public Expression simplify (Map<Variable, Number> envir) {
		if (envir.containsKey(this)) return envir.get(this);
		else return this;
	}
	
	@Override
	public boolean isNumber() {
		return false;
	}
	
	
}
