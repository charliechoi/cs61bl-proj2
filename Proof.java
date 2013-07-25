import java.util.*;
import java.io.*;

public class Proof {
	
	private Queue<String> proofQueue = new LinkedList<String>();
	private Queue<LineNumber> LineQueue = new LinkedList<LineNumber>();
	private ArrayList<String> LineNumCollection = new ArrayList<String>();
	private LineNumber number = new LineNumber();
	private LineNumber forPrint = new LineNumber();
	private ArrayList<Expression> expressionList = new ArrayList<Expression>();
	
	public Proof (TheoremSet theorems) {
		
	}

	public String nextLineNumber ( ) {
		String current = number.get();
		LineNumCollection.add(current);
		return current;
	}

	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {
		//check if the first word after the line number is print or one of the reasons.
		// if the reason is nth show, check if the string matches the nth subproof of the whole proof.
		// if the reason is assume, check if the string is on the left side of the subproof.
		// if the reason is mt, mp, or co make sure the string starts with line numbers of statements that support the reason.
		// then access the statement of the corresponding line number, and see if the expression that follows is in fact true.
		// if the reason is the name of the theorem, access the hashmap of inputted theorem names and the corresponding expression.
		// then check if the input expression MATCHES (create another method or class) the theorem expression.
		
		//add to proof collection for accessing later.
		
		Queue<LineNumber> LineQueueCopy = LineQueue;
		Queue<String> proofQueueCopy = proofQueue;
		// update LineNum for not this line of proof but next line.
		
		if (x.equals("print")){
			while(!LineQueueCopy.isEmpty()){
				
				System.out.println(LineQueueCopy.remove().get()+" ");
			}
			while(!proofQueue.isEmpty()){
				
				System.out.print(proofQueueCopy.remove());
			}
		} else {
			LineQueue.add(number);
			proofQueue.add(x);
			String[] split = x.split(" ",0);
			Expression proofExpression = new Expression(split[split.length-1]);
			expressionList.add(proofExpression);
			if (split[0].equals("mt") && split.length == 4){
				int indexOne = LineNumCollection.indexOf(split[1]);
				int indexTwo = LineNumCollection.indexOf(split[2]);
				if (expressionList.get(indexOne).checkBoolean()){
					if(expressionList.get(indexTwo).checkBoolean()){
						proofExpression.setBoolean(true);
					}
				}
			}
		}
	}

	public String toString ( ) {
		return "";
	}

	public boolean isComplete ( ) {
		return true;
	}
	
	
}
