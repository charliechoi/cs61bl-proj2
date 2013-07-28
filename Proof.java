import java.util.*;
import java.io.*;

public class Proof {
	
	private Queue<String> proofQueue = new LinkedList<String>();
	private Queue<String> LineQueue = new LinkedList<String>();
	private ArrayList<String> LineNumCollection = new ArrayList<String>();
	private LineNumber number = new LineNumber();
	private ArrayList<Expression> expressionList = new ArrayList<Expression>();
	private Stack<Expression> showStack = new Stack<Expression>();
	private TheoremSet theo = new TheoremSet();
	private ArrayList<String> assumeexpressionStringList = new ArrayList<String>();
	
	public Proof (TheoremSet theorems) {
		theo = theorems;
	}

	public String nextLineNumber ( ) {
		String current = number.get();
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
		if (this.isOK()){
			
			if (x.equals("print")){
				System.out.print(toString());
			} else {
				//update line repository
				LineNumCollection.add(number.get());
				LineQueue.add(number.get());
				//update proof repository
				proofQueue.add(x);
				//split reason from line numbers from proof.
				String[] split = x.split(" ",0);
				//obtain proof expression
				Expression proofExpression = new Expression(split[split.length-1]);
				//an attempt to figure out if expression requires false or true when setting boolean at the end.
				int notCount=0;
				String temp = split[split.length-1];
				//System.out.println(temp);
				while (temp.contains("~")){
					int index = temp.indexOf("~");
					
					if (index==0){
						temp = temp.substring(1);
					} else {
						temp = temp.substring(0,index) + temp.substring(index+1);
					}
					
					if (temp.substring(index+1,index+1)=="("){
						return;
					}
				}
				
				//System.out.println(temp);
				notCount = split[split.length-1].length() - temp.length();
				//System.out.println(notCount);
				//mp
				if (split[0].equals("mp")){
					if (split.length == 4){
						int indexOne = LineNumCollection.indexOf(split[1]);
						int indexTwo = LineNumCollection.indexOf(split[2]);
						//get the corresponding expressions
						Expression first = expressionList.get(indexOne);
						Expression second = expressionList.get(indexTwo);
						
						if (!this.checkingMP(proofExpression, split)){
							throw new IllegalInferenceException("mp error: two expressions are not related to each other");
						} else {
							if (first.checkBoolean()){
								if(second.checkBoolean()){
									if (notCount%2==1){
										proofExpression.setBoolean(false);
									}else{
										proofExpression.setBoolean(true);
									}
									//update expressionList
									expressionList.add(proofExpression);
									//update line number and add boolean to show if pertinent.
									this.update(proofExpression);
								} else {
									throw new IllegalInferenceException("mp error: illegal inference");
								}
							} else if (!first.checkBoolean()){
								if (!second.checkBoolean()){
									if (notCount%2==1){
										proofExpression.setBoolean(false);
									}else{
										proofExpression.setBoolean(true);
									}
									//update expression
									expressionList.add(proofExpression);
									this.update(proofExpression);
								} else{
									throw new IllegalInferenceException("mp error: illegal inference");
								}	
							} else{
								throw new IllegalInferenceException("mp error: expression is not assigned boolean");
							}
						}
					} else {
						throw new IllegalLineException("mp error: not the correct number of arguments, need four");
					}
					
				//show	
				} else if (split[0].equals("show")){
					if(split.length==2){
						if(showStack.isEmpty()){
							number.NewLine();
						} else {
							number.NewSubLine();
						}
						expressionList.add(proofExpression);
						showStack.push(proofExpression);
					} else {
						throw new IllegalLineException("incorrect number of arguments, need 2");
					}
					
					
				//assume	
				} else if (split[0].equals("assume") && split.length==2){
					//System.out.println(notCount);
					if (notCount%2==1){
						proofExpression.setBoolean(false);
					}else{
						proofExpression.setBoolean(true);
					}
					//System.out.println(proofExpression.checkBoolean());
					expressionList.add(proofExpression);
					assumeexpressionStringList.add(proofExpression.myLine);
					number.NewLine();
					
				//mt	
				}else if (split[0].equals("mt") && split.length==4){
				
					if (!this.checkingMT(proofExpression, split)){
						throw new IllegalInferenceException("mt check error");
					} else {
						if (this.getLonger(split).checkBoolean()==true){
							if (notCount%2==1){
								proofExpression.setBoolean(false);
							}else{
								proofExpression.setBoolean(true);
							}
							expressionList.add(proofExpression);
							this.update(proofExpression);
						} else {
							throw new IllegalInferenceException("mt error");
						}
					}
					
				//co
				} else if (split[0].equals("co") && split.length==4){
					int indexOne = LineNumCollection.indexOf(split[1]);
					int indexTwo = LineNumCollection.indexOf(split[2]);
					//get the corresponding expressions
					Expression first = expressionList.get(indexOne);
					Expression second = expressionList.get(indexTwo);
					//System.out.println(first.myLine);
					//System.out.println(second.myLine);
					//System.out.println(first.checkBoolean());
					//System.out.println(second.checkBoolean());
					if(this.checkingCO(proofExpression)){
						if(first.checkBoolean()!=second.checkBoolean()){
							proofExpression.setBoolean(true);
							expressionList.add(proofExpression);
							this.update(proofExpression);
						} else {
							throw new IllegalInferenceException("co error");
						} 
					} else {
						throw new IllegalInferenceException("co check error");
					}
					
				
				//ic	
				}else if (split[0].equals("ic")&&split.length==3){
					if(!this.checkingIC(proofExpression, split)){
						throw new IllegalInferenceException("Invalid inference");
					} else{
						if (notCount%2==1){
							proofExpression.setBoolean(false);
						}else{
							proofExpression.setBoolean(true);
						}
						expressionList.add(proofExpression);
						this.update(proofExpression);
					}
				
					
				} else if (theo.Theorem.containsKey(split[0])){
					if (((Expression) theo.Theorem.get(split[0])).compare(proofExpression)){
						proofExpression.setBoolean(true);
					} else {
						throw new IllegalInferenceException("Theorem match error");
					}
				} 
					
				} 
			}
		}
		
	

	public String toString ( ) {
		Queue<String> LineQueueCopy = LineQueue;
		Queue<String> proofQueueCopy = proofQueue;
		String print = "" +"\n";
	// update LineNum for not this line of proof but next line.
		while(!proofQueue.isEmpty()){
			print = print + LineQueueCopy.remove()+"	"+ proofQueueCopy.remove() +"\n";		
		}
		return print;
	}

	public boolean isComplete ( ) {
		if(showStack.empty()==true){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean checkingMP(Expression proofExpression, String[] split){
			//System.out.println(this.getLeft(split));
			//System.out.println(this.getShorter(split).myLine);
			System.out.println(this.getRight(split));
			System.out.println(proofExpression.myLine);
			if(this.getLeft(split).equals(this.getShorter(split).myLine) && this.getRight(split).equals(proofExpression.myLine)){
				return true;
			} else{
				return false;
			}
		
		}
	
	public boolean checkingMT(Expression proofExpression, String[] split){
		Expression Shorter= this.getShorter(split);
		//System.out.println(Shorter.myLine);
		Expression Longer = this.getLonger(split);
		//System.out.println(Longer.myLine);
		String negRight = Shorter.myLine.replaceFirst("~", "");
		//System.out.println(negRight);
		String negLeft = proofExpression.myLine.replaceFirst("~", "");
		//System.out.println(negLeft);
		String compare = "("+negLeft+ "=>" + negRight+")";
	
		//System.out.println(compare);
		//System.out.println(compare.equals(Longer.myLine));
		//also check if right side of longer expression is the expression we want to set boolean to.
		if(compare.equals(Longer.myLine)){
			return true;
		} else{
			return false;
		}
	}
	
	public boolean checkingIC(Expression proofExpression, String[] split){
		String Right = this.getRight(split);
		String Left = this.getLeft(split);
		int indexOne = LineNumCollection.indexOf(split[1]);
		Expression first = expressionList.get(indexOne);
		int tempindex = expressionList.indexOf(Left);
		if (first.myLine.equals(Right)){
			if(assumeexpressionStringList.contains(Left)){
				return true;
			} else{
				return false;
			}
		} else{ 
			return false;
		}
	}
	
	public String getLeft(String [] split){

		Expression Shorter= this.getShorter(split);
		//System.out.println(Shorter.myLine);
		Expression Longer = this.getLonger(split);
		//System.out.println(Longer.myLine);
		int firstIndex= Longer.myLine.indexOf(Shorter.myLine);
		//System.out.println(firstIndex);
		int shorterLength = Shorter.myLine.length();
		//System.out.println(shorterLength);
		return Longer.myLine.substring(1, firstIndex+shorterLength);
	}
	
	public String getRight(String [] split){
	
		Expression Shorter = this.getShorter(split);
		Expression Longer = this.getLonger(split);
		int firstIndex= Longer.myLine.indexOf(Shorter.myLine);
		int shorterLength = Shorter.myLine.length();
		return Longer.myLine.substring(firstIndex+shorterLength+2, Longer.myLine.length()-1);
	}
	
	public Expression getShorter(String [] split){
		int indexOne = LineNumCollection.indexOf(split[1]);
		int indexTwo = LineNumCollection.indexOf(split[2]);
		//get the corresponding expressions
		Expression first = expressionList.get(indexOne);
		Expression second = expressionList.get(indexTwo);
		// a boolean to determine if the expressions above are related to each other, as in if one object is a sub-expression of the other.

		//find out which expression is larger, and see if the shorter one is inside the longer one.
		if (first.myLine.length()>second.myLine.length()){
			Expression Shorter = second;
			return Shorter;
		} else if (first.myLine.length()<second.myLine.length()){
			Expression Shorter = first;
			return Shorter;
		} else{
			return null;
		}
	}
	
	public Expression getLonger(String [] split){
		int indexOne = LineNumCollection.indexOf(split[1]);
		int indexTwo = LineNumCollection.indexOf(split[2]);
		//get the corresponding expressions
		Expression first = expressionList.get(indexOne);
		Expression second = expressionList.get(indexTwo);
		// a boolean to determine if the expressions above are related to each other, as in if one object is a sub-expression of the other.

		//find out which expression is larger, and see if the shorter one is inside the longer one.
		if (first.myLine.length()>second.myLine.length()){
			Expression Longer = first;
			return Longer;
		} else if (first.myLine.length()<second.myLine.length()){
			Expression Longer = second;
			return Longer;
		}else{
			return null;
		}
		
	}
	
	public void update(Expression proofExpression){
		//System.out.println(showStack.peek().myLine);
		if(proofExpression.myLine.equals(showStack.peek().myLine)){
			number.DeleteSub();
			// assign the most recent show object to true or false depending on number of ~.
			Expression recentShow = showStack.pop();
			Expression show = expressionList.get(expressionList.indexOf(recentShow));
			Expression showcopy = show;
			showcopy.myLine.replace("~","");
			int shownotcount = show.myLine.length()-showcopy.myLine.length();
			
			if (shownotcount%2==1){
				expressionList.get(expressionList.indexOf(recentShow)).setBoolean(false);
			}else{
				expressionList.get(expressionList.indexOf(recentShow)).setBoolean(true);
			}	
		} else {
			number.NewLine();
		}
	}
	
	public boolean checkingCO(Expression proofExpression){
		//System.out.println(proofExpression.myLine);
		//System.out.println(showStack.peek().myLine);
		if(proofExpression.myLine.equals(showStack.peek().myLine)){
			return true;
		} else{
			return false;
		}
	}
	
	// check if index of expression corresponds to correct line number. will be called everytime in extendproof.
	public boolean isOK(){
		if (expressionList.size()==LineNumCollection.size()){
			return true;
		}else{
			return false;
		}
	}
}
	
	
	

