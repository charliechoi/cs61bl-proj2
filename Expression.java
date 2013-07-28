import java.util.*;

public class Expression {

    protected TreeNode myRoot;
    protected String myLine="";
    protected boolean somethin = false;
    public Expression(String line) throws IllegalLineException {
        myLine=line;
        validExpr(myLine);
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
    
    
    public String aString(){
        return myLine;
    }
    public boolean setBoolean(boolean a){
        somethin = a;
        return somethin;
    }
    public boolean checkBoolean(){
        return somethin;
    }
    protected static final String indent1 = " ";
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


    public TreeNode expr(){
        TreeNode a=null;
		try {
			a = exprTree(myLine);
			
		} catch (IllegalLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
    }
    // Return the tree corresponding to the given arithmetic expression.
    // The expression is legal, fully parenthesized, contains no blanks,
    // and involves two variables, =>,|, &.
    private TreeNode exprTree (String expr) throws IllegalLineException {
        if (expr.length() == 0)
            throw new IllegalLineException("bad expr: empty string");
        
        validExpr(expr);        // throws exception if expr is bad, otherwise continue
        
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
           // System.out.println ("expression = " + expr);
            //System.out.println ("operand 1 = " + opnd1);
            //System.out.println ("operator = " + op);
            //System.out.println ("operand 2 = " + opnd2);
            //System.out.println ( );
            return new TreeNode(op,exprTree(opnd1),exprTree(opnd2));
        }
    }

    public static void validExpr(String s) throws IllegalLineException {
        // this is a recursive method to check if a String is a valid expr
        Set<Character> ops = new HashSet<Character>();
        ops.add('=');
        ops.add('&');
        ops.add('|');

        // base cases
        if (s.length() == 0)
            return;

        // if first char is not '(', then it must be '~' or a variable
        if (s.charAt(0) != '(') {
            if (validOps(s) != 0)
                throw new IllegalLineException("bad expr: no valid opening parens");
            	
            if (s.charAt(0) != '~' && !Character.isLetter(s.charAt(0)))
                throw new IllegalLineException("bad expr: no valid opening parens");
            while (s.charAt(0) == '~') { // remove the 'not's since it doesn't affect expression validity
                s = s.substring(1, s.length());
            }
            if (s.length() == 0)
                throw new IllegalLineException("bad expr: '~' used incorrectly");
            if (s.charAt(0) == '(') {// after removing '~', recurse if more nesting
                validExpr(s);
                return;
            }
            if (!Character.isLetter(s.charAt(0)))
                throw new IllegalLineException("bad expr: '~' used incorrectly");
            if (s.length() != 1)
                throw new IllegalLineException("bad expr: invalid parens or variable");
            return;
        }

        // we just finished '~' and variable checking. move on to '(' nesting checking
        // following only runs if first char is '('
        
        if (s.charAt(s.length() - 1) != ')'){
            throw new IllegalLineException("bad expr: invalid outer nesting");
        }
        s = s.substring(1, s.length() - 1); // remove outer parens

        if (validOps(s) == 0){
            throw new IllegalLineException("bad expr: no valid operator found");
        }
        int opIndex = validOps(s); // throws error if there is no valid Operator, why else would we need parens
        String left = s.substring(0, opIndex);
        String right;
        if (s.charAt(opIndex) == '=') {
            right = s.substring(opIndex + 2, s.length());
        } else {
            right = s.substring(opIndex + 1, s.length());
        }
        validExpr(left);
        validExpr(right);
        // (~a=>(~b=>~(a|b)))
    }

    private static int validOps(String s) throws IllegalLineException {
        // merely checks to see if there exists ONE operator at nesting 0.
        // returns index of the operator.
        Set<Character> ops = new HashSet<Character>();
        ops.add('=');
        ops.add('&');
        ops.add('|');
        int nesting = 0;
        int validOpCount = 0;   // cannot have more than 1
        int opIndex = 0;

        if (s == ""){
            throw new IllegalLineException("bad expr: empty parens");
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '('){
                nesting++;
            }
            if (s.charAt(i) == ')'){
                nesting--;
            }
            if (nesting < 0){
                throw new IllegalLineException("bad expr: encountered early ')'");
            }
            if (ops.contains(s.charAt(i)) && nesting == 0) {
                if (i == 0) {    // operand found in beginning
                    throw new IllegalLineException("bad expr: no left operand");
                }
                if (s.length() == i + 1){ // end of iteration
                    throw new IllegalLineException("bad expr: no right operand");
                }
                if (s.charAt(i+1) != '>' && s.charAt(i) == '='){
                    throw new IllegalLineException("bad expr: invalid operator");
                }
                if (s.charAt(i) == '=' && s.length() == i + 2){
                    throw new IllegalLineException("bad expr: no right operand");
                }
                validOpCount++;
                opIndex = i;
            }
        }
        if (validOpCount > 1){
            throw new IllegalLineException("bad expr: too many operators");
        }
        return opIndex;
    }


    public boolean compare(Expression Check){
        TreeNode treeProof;
        boolean compareTruth=false;
		try {
			treeProof = exprTree(myLine);
			TreeNode treeCheck = exprTree(Check.aString());
			HashMap subTree = new HashMap();
	        compareTruth = inOrder(treeProof, treeCheck, subTree);
		} catch (IllegalLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return compareTruth;
    }
        
     
    @SuppressWarnings("unchecked")
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
        return true;
  
    }
   
}
