import java.util.*;
import java.io.*;

public class Proof {
	
	private Queue<String> proofQueue = new LinkedList<String>();
	private Queue<String> LineQueue = new LinkedList<String>();
	private ArrayList<String> LineNumCollection = new ArrayList<String>();
	private LineNumber number = new LineNumber();
	private ArrayList<Expression> expressionList = new ArrayList<Expression>();
	private Stack<Expression> showStack = new Stack<Expression>();
	private TheoremSet theorem = new TheoremSet();
	
	public Proof (TheoremSet theorems) {
		theorem = theorems;
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
		if (this.isOK()){
			if (x.equals("print")){
				System.out.print(toString());
			} else {
				//update line repository
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
				temp.replace("~","");
				notCount = split[split.length-1].length() - temp.length();
				
				//mp
				if (split[0].equals("mp") && split.length == 4){
					int indexOne = LineNumCollection.indexOf(split[1]);
					int indexTwo = LineNumCollection.indexOf(split[2]);
					//get the corresponding expressions
					Expression first = expressionList.get(indexOne);
					Expression second = expressionList.get(indexTwo);
					
					if (!this.checkingMP(proofExpression, split)){
						throw new IllegalInferenceException("mp checking error");
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
								throw new IllegalInferenceException("mp error");
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
								throw new IllegalInferenceException("mp error");
							}	
						} else{
							throw new IllegalInferenceException("mp error");
						}
					}
					
				//show	
				} else if (split[0].equals("show")&& split.length==2){
					if(showStack.isEmpty()){
						number.NewLine();
					} else {
						number.NewSubLine();
					}
					expressionList.add(proofExpression);
					showStack.push(proofExpression);
					
				//assume	
				} else if (split[0].equals("assume") && split.length==2){
					if (notCount%2==1){
						proofExpression.setBoolean(false);
					}else{
						proofExpression.setBoolean(true);
					}
					expressionList.add(proofExpression);
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
					
					if(this.checkingCO(proofExpression)){
						if(first.checkBoolean()!=second.checkBoolean()){
							proofExpression.setBoolean(true);
							expressionList.add(proofExpression);
							this.update(proofExpression);
						} else {
							throw new IllegalInferenceException("co error");
						} 
					} else {
						throw new IllegalInferenceException("co error");
					}
				
				//ic	
				}else if (split[0].equals("ic")&&split.length==3){
					if(this.ic(split[2], split)){
						expressionList.add(proofExpression);
						this.update(proofExpression);
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
		
			if(this.getLeft(split).equals(this.getShorter(split).myLine) && this.getRight(split).equals(proofExpression.myLine)){
				return true;
			} else{
				return false;
			}
		
		}
	
	public boolean checkingMT(Expression proofExpression, String[] split){
		Expression Shorter= this.getShorter(split);
		Expression Longer = this.getLonger(split);
		String negRight = Shorter.myLine.replaceFirst("~", "");
		String negLeft = proofExpression.myLine.replaceFirst("~", "");
		String compare = "("+negLeft+ "=>" + negRight+")";
		System.out.println(compare);
		//also check if right side of longer expression is the expression we want to set boolean to.
		if(compare.equals(Longer)){
			return true;
		} else{
			return false;
		}
	}
	
	public String getLeft(String [] split){

		Expression Shorter= this.getShorter(split);
		Expression Longer = this.getLonger(split);
		int firstIndex= Longer.myLine.indexOf(Shorter.myLine);
		int shorterLength = Shorter.myLine.length();
		return Longer.myLine.substring(1, firstIndex+shorterLength-1);
	}
	
	public String getRight(String [] split){
	
		Expression Shorter = this.getShorter(split);
		Expression Longer = this.getLonger(split);
		int firstIndex= Longer.myLine.indexOf(Shorter.myLine);
		int shorterLength = Shorter.myLine.length();
		return Longer.myLine.substring(shorterLength+2, Longer.myLine.length()-1);
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
		if(proofExpression.equals(showStack.peek())){
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
	
	public boolean ic(String s,String [] split){
		int indexOne = LineNumCollection.indexOf(split[1]);
		String sub = expressionList.get(indexOne).aString();
		String temper = s.replaceFirst(sub, "");
		String first ="";
		String operand ="";
		String second ="";
		if (temper.substring(0,1).equals("(")){
			first = sub;
			if (temper.substring(1,2).equals("=")){
				operand = "=>";
			}else{
				operand = temper.substring(1,2);
			}
			String temp2 = sub.replaceFirst(operand, "");
			second = temp2.substring(1,temp2.length()-2);


		}else{
			second = sub;
			if (temper.substring(temper.length()-2,temper.length()-1).equals("=")){
				operand = "=>";
			}else{
				operand = temper.substring(temper.length()-2,temper.length()-1);
			}
			String temp2 = operand + sub;
			String temp3 = s.replaceFirst(temp2, "");
			first = temp3.replaceFirst("(", "");

		}
		int indexLeft = LineNumCollection.indexOf(second);
		int indexRight = LineNumCollection.indexOf(first);
		Expression lefty = expressionList.get(indexLeft);
		Expression righty = expressionList.get(indexRight);
		if (operand.equals("=>")||operand.equals("|")){
			if (lefty.checkBoolean()==false && righty.checkBoolean()==false){
				return false;
			}
		}else{
			if (lefty.checkBoolean() == false || righty.checkBoolean() ==false){
				return false;
			}
		}
		return true;
	}
	
	public boolean checkingCO(Expression proofExpression){
		if(proofExpression.equals(showStack.peek())){
			return true;
		} else{
			return false;
		}
	}
	
	// check if index of expression corresponds to correct line number. will be called everytime in extendproof.
	public boolean isOK(){
		if (expressionList.size()+1==LineNumCollection.size()){
			return true;
		}else{
			return false;
		}
	}
}
	
	
	

