import java.util.*;


public class Expression {
  protected TreeNode myRoot;

	public Expression ( ) {
		myRoot = null;
	}

	public Expression (String s) {
		myRoot = exprTree(s).myRoot;
	}

	protected static class TreeNode {
		
		public Object myItem;
		public TreeNode myLeft;
		public TreeNode myRight;
		
		public TreeNode (Object obj) {
			myItem = obj;
			myLeft = myRight = null;
		}
		
		public TreeNode (Object obj, TreeNode left, TreeNode right) {
			myItem = obj;
			myLeft = left;
			myRight = right;
		}
	}
	protected static final String indent1 = "    ";
	public void print ( ) {
	    if (myRoot != null) {
	        printHelper (myRoot, 0);
	    }
	}
	protected static void printHelper (TreeNode root, int indent){
 		
		if (root.myRight !=null){
 			indent++;
 			TreeNode temp = root;
 			temp = root.myRight;
 			printHelper (temp, indent);
 			indent--;
 		}
 		println(root.myItem, indent-1);
 
 		if (root.myLeft!=null){
 			indent++;
 			TreeNode temp = root.myLeft;
 			printHelper(temp,indent);
 			indent--;
 		} 
	}
	protected static void println (Object obj, int indent) {
	    for (int k=0; k<=indent; k++) {
	        System.out.print (indent1);
	    }
	    System.out.println (obj);
	}

	public static Expression exprTree (String s) {
		Expression result = new Expression( );
	    result.myRoot = result.exprTreeHelper (s);
	    return result;
	}

	private TreeNode exprTreeHelper (String expr) {
		String myNot = "";
	    if (expr.charAt (0) != '(') {
	        return new TreeNode(expr.charAt(0),null,null); 
	    } else {

	        int nesting = 0;
	        int opPos = 0;
	        
	        for (int k=0; k<expr.length()-1; k++) {
	        	if(expr.charAt(k)=='('){
	        		nesting++;
	        	}if((expr.charAt(k)=='|'||expr.charAt(k)=='&')&&(nesting==1)){	  
	        	   opPos=k;
	        	   break;
	        	}else if ((expr.charAt(k)=='=')&&(nesting==1)){
	        		opPos=k+1;
	        		String opnd1 = expr.substring (1, opPos);
	        		opnd1.replaceFirst("=","");
	     	        String opnd2 = expr.substring (opPos+1, expr.length()-1);
	     	        String op = "=>";
	     	        System.out.println ("expression = " + expr);
	     	        System.out.println ("operand 1  = " + opnd1);
	     	        System.out.println ("operator   = " + op);
	     	        System.out.println ("operand 2  = " + opnd2);
	     	        System.out.println ( );
	     	        return new TreeNode(myNot+op,exprTreeHelper(opnd1),exprTreeHelper(opnd2));
	        	}else if ((expr.charAt(k)=='~')&&(nesting==1)){
	        		myNot ="~";
	        		break;
	        		
	           }if(expr.charAt(k)==')'){
	        		nesting--;
	        	}	           
	        }
	        String opnd1 = expr.substring (1, opPos);
	        String opnd2 = expr.substring (opPos+1, expr.length()-1);
	        String op = expr.substring (opPos, opPos+1);
	        
	        System.out.println ("expression = " + expr);
	        System.out.println ("operand 1  = " + opnd1);
	        System.out.println ("operator   = " + op);
	        System.out.println ("operand 2  = " + opnd2);
	        System.out.println ( );
	        return new TreeNode(myNot+op,exprTreeHelper(opnd1),exprTreeHelper(opnd2));
	      
	    }
	}
	public static void main(String []args){
		String proof = "(a|b)";
		Expression t =  Expression.exprTree(proof);
		t.print();
		String roof ="((a&b)=>c)";
		Expression p = Expression.exprTree(roof);
		p.print();
		String goof = "(a=>(c=>(a|b))&(a=>)";
		Expression g = Expression.exprTree(goof);
		g.print();
		
		
	}
	

}
