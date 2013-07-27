
import java.util.*;






public class Expression {
	
	private TreeNode myRoot;
	private String myLine="";
	public Expression(String line) {
		myLine=line;
		
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


	
	// Return the tree corresponding to the given arithmetic expression.
	// The expression is legal, fully parenthesized, contains no blanks, 
	// and involves two variables, =>,|, &.
	private TreeNode exprTree (String expr) {
	    if (expr.charAt (0) != '(' && expr.charAt(0)!='~') {
	        return new TreeNode(expr.charAt(0),null,null); 
	    }else if(expr.charAt(0)=='~'&&expr.length()==2){
	    	return new TreeNode(expr.charAt(0),new TreeNode(expr.charAt(1)),null); 
	    } else {
	        // expr is a parenthesized expression.
	        // Strip off the beginning and ending parentheses,
	        // find the main operator (an occurrence of + or * not nested
	        // in parentheses, and construct the two subtrees.
	        int nesting = 0;
	        String op="";
	        String opnd1=""; 
	        String opnd2="";
	        int opPos = 0;
	        int opPos2 =0; //deal specifically with =>
	        boolean addNot=false;
	        
	        for (int k=0; k<expr.length()-1; k++) {
	        	if(expr.charAt(k)=='('){
	        		nesting++;
	        	}else if(expr.charAt(k)==')'){
	        		nesting--;        	
	        	}else if(expr.charAt(k)=='~'&&nesting==1&& expr.charAt(k+1)=='('){
	        		addNot=true;
	        	}else if(expr.charAt(k)=='='&&nesting==1){
	        			opPos2=k;
	        			opnd1 = expr.substring (1, opPos2);
	        			opnd2 = expr.substring (opPos2+2, expr.length()-1);
	        			op = expr.substring (opPos2, opPos2+2);
	        			break;
	        	}else if((expr.charAt(k)=='|'||expr.charAt(k)=='&')&&(nesting==1)){	  
	        			opPos=k;
	        			opnd1 = expr.substring (1, opPos);
	        			opnd2 = expr.substring (opPos+1, expr.length()-1);
	        			op = expr.substring (opPos, opPos+1);
	        			break;
	        		
	        	}
	        }
	        if (addNot){
	        	op="~"+op;
	        	addNot=false;
	        	opnd1=opnd1.substring(1);
	        }
	        	System.out.println ("expression = " + expr);
	        	System.out.println ("operand 1  = " + opnd1);
	        	System.out.println ("operator   = " + op);
	        	System.out.println ("operand 2  = " + opnd2);
	        	System.out.println ( );
	        	return new TreeNode(op,exprTree(opnd1),exprTree(opnd2));
	    }
	    }
	    
	    
	public boolean compare(TreeNode treeProof, TreeNode treeCheck){
        HashMap subTree = new HashMap();
        boolean compareTruth = inOrder(treeProof, treeCheck, subTree);
        return compareTruth;
        }
        
     
	public boolean inOrder(TreeNode Proof, TreeNode Check,HashMap subTree){
    if (!Proof.myItem.equals(Check.myItem)){
        if (Proof.myItem.equals("|")||Proof.myItem.equals("&")||Proof.myItem.equals("~")||Proof.myItem.equals("=>")){
            return false;
        }else{
            if (subTree.get(Proof.myItem)==null){
                subTree.put(Proof.myItem,Check);
            }else{
                if (!inOrder(Check,(TreeNode)subTree.get(Proof.myItem),subTree)){
                    return false;
                }
            }
        }
    }
        if (Proof.myLeft!=null){
            Proof = Proof.myLeft;
            if (Check.myLeft == null){
                return false;
            }else{
                Check = Check.myLeft;
            }
            if (!inOrder(Proof,Check,subTree)){
                return false;
            }
        }
    if (Proof.myRight!=null){
        Proof = Proof.myRight;
        if(Check.myRight == null){
            return false;
        }else{
            Check=Check.myRight;
        }
        if (!inOrder(Proof,Check,subTree)){
            return false;
        }
        }
    if (Proof.myItem.equals(Check.myItem)){
        return true;
    }else{
        return false;
}
}

}
