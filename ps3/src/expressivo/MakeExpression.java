package expressivo;

import java.util.Stack;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;

import expressivo.parser.ExpressionListener;
import expressivo.parser.ExpressionParser;

/** Make an Expression from a parse tree. */
public class MakeExpression implements ExpressionListener{
	private Stack<Expression> stack = new Stack<>();
    // Invariant: stack contains the Expression value of each parse subtree that has been fully
    // -walked so far, but whose parent has not yet been exited by the walk. The stack is ordered
    // by recency of visit, so that the top of the stack is the Expression for the most recently
    // walked subtree.
    //
    // At the start of the walk, the stack is empty, because no subtrees have been fully walked.
    // 
    // Whenever a node is exited by the walk, the Expression values of its children are on top
    // of the stack, in order with the last child on top. To preserve the invariant, we must
    // pop those child Expression values from the stack, combine them with the appropriate 
    // Expression producer, and push back an Expression value representing the entire subtree
    // under the node.
    //
    // At the end of the walk, after all subtrees have been walked and the the root has been 
    // exited, only the entire tree satisfies the invariant's "fully walked but parent not yet
    // exited" property, so the top of the stack is the Expression of the entire parse tree.
	
    /**
     * Returns the expression constructed by this listener object.
     * Requires that this listener has completely walked over an Expression parse tree using 
     * ParseTreeWalker.
     * @return Expression for the parse tree that was walked
     */
    public Expression getExpression() {
        return stack.get(0);
    }
    
    @Override public void exitRoot(ExpressionParser.RootContext context) {
        // do nothing, root has only one child so its value is already on top of the stack
    }
    
    @Override public void exitExpr(ExpressionParser.ExprContext context) throws IllegalArgumentException { 
    	int numLeftParen = context.LPAREN().size();
    	int numRightParen = context.RPAREN().size();
    	if (numLeftParen != numRightParen) {
    		throw new IllegalArgumentException();
    	}
    	Expression expr = stack.pop();
    	Expression grouping = new Grouping (expr, numLeftParen);
    	stack.push(grouping);	
    }
    
    @Override public void exitSum(ExpressionParser.SumContext context) {  
        List<ExpressionParser.ProductContext> addEnds = context.product();
        assert stack.size() >= addEnds.size();

        // the pattern above always has at least 1 child;
        // pop the last child
        assert addEnds.size() > 0;
        Expression sum = stack.pop();

        // pop the older children, one by one, and add them on
        for (int i = 1; i < addEnds.size(); ++i) {
            sum = new Add(stack.pop(), sum);
        }

        // the result is this subtree's Expression
        stack.push(sum);
    }

    @Override public void exitProduct(ExpressionParser.ProductContext context) {

        List<ExpressionParser.PrimitiveContext> multiplyEnds = context.primitive();
        assert stack.size() >= multiplyEnds.size();

        // the pattern above always has at least 1 child;
        // pop the last child
        assert multiplyEnds.size() > 0;
        Expression multiply = stack.pop();

        // pop the older children, one by one, and add them on
        for (int i = 1; i < multiplyEnds.size(); ++i) {
            multiply = new Multiply(stack.pop(), multiply);
        }

        // the result is this subtree's IntegerExpression
        stack.push(multiply);
    }
    
    @Override public void exitPrimitive(ExpressionParser.PrimitiveContext context) {
        if (context.NUMBER() != null) {
            // matched the NUMBER alternative
        	Expression number;
        	if (context.NUMBER().getText().matches("[0-9]+")) {
        		// integer
        		int n = Integer.parseInt(context.NUMBER().getText());
                number = new Number(n);
        	} else {
        		//double
                double d = Double.parseDouble(context.NUMBER().getText());
                number = new Number(d);
        	}
            stack.push(number);
        } else if (context.VARIABLE() != null){
        	// matched the VARIABLE alternative
        	Expression variable = new Variable(context.VARIABLE().getText());
        	stack.push(variable);
        } else {
        	// matched the '(' sum ')' alternative
            // do nothing, because sum's value is already on the stack
        }
    }

    // don't need these here, so just make them empty implementations
    @Override public void enterRoot(ExpressionParser.RootContext context) { }
    @Override public void enterExpr(ExpressionParser.ExprContext context) { }
    @Override public void enterSum(ExpressionParser.SumContext context) { }
    @Override public void enterProduct(ExpressionParser.ProductContext context) { }
    @Override public void enterPrimitive(ExpressionParser.PrimitiveContext context) { }

    @Override public void visitTerminal(TerminalNode terminal) { }
    @Override public void enterEveryRule(ParserRuleContext context) { }
    @Override public void exitEveryRule(ParserRuleContext context) { }
    @Override public void visitErrorNode(ErrorNode node) { }  
}
