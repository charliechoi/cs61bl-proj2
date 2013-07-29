import junit.framework.TestCase;

import org.junit.Test;


public class ExpressionTest extends TestCase{

	@Test

	public static void test2(){
		System.out.println("Start Testing");

	}
	public static void testvalidExpr1(){          // testing misconstruction with the parenthesis
		boolean result1=false;
		Expression a;
		
		try{
			a= new Expression("a&d");
			a.validExpr("a&d");		
		}catch (IllegalLineException e){

			result1=true;
		}assertTrue(result1);

		boolean result2=false;
		try{
			Expression b = new Expression("a&d))");
			b.validExpr("a&d))");		
		}catch (IllegalLineException e){
			result2=true;
		}assertTrue(result2);
	}

	public static void testvalidExpr2(){    // testing misconstructin with the operands
		boolean result1=false;
		try{
			Expression a = new Expression("(a&&d)");
			a.validExpr("(a&&d)");		
		}catch (IllegalLineException e){
			result1=true;
		}assertTrue(result1);


		boolean result2=false;
		try{
			Expression b = new Expression("(((a|d)=>a)%d)");
			b.validExpr("(((a|d)=>a)%d)");		
		}catch (IllegalLineException e){
			result2=true;
		}assertTrue(result2);

		boolean result3=false;
		try{
		Expression c = new Expression("(((a|d)=>a)%d)");
			
		}catch (IllegalLineException e){
			result3=true;
		}assertTrue(result3);
	}

	public static void testvalidExpr3(){  
		boolean result1=false;
		try{
		Expression a = new Expression("((aa=>d)|a)");
				
		}catch (IllegalLineException e){
			result1=true;
		}assertTrue(result1);


		
	}

	public static void testString(){
		try{
		Expression a = new Expression("(a&b)");
		Expression b = new Expression("(c&d)");
		assertTrue(a.aString().equals("(a&b)"));
		assertFalse(a.aString().equals("a"));
		assertTrue(b.aString().equals("(c&d)"));
		assertFalse(a.aString().equals(b.aString()));
		assertTrue(a.aString().equals(a.aString()));

	}catch (IllegalLineException e){
		assertTrue(false);
	}
	}

	public static void testPrint(){

		try{
		Expression a = new Expression("(a&b)");
		Expression b = new Expression("(c&d)");
		a.expr();
		a.print();
		b.expr();
		b.print();

	}catch (IllegalLineException e){
		assertTrue(false);
	}
	}
	public static void testCompare(){
		try{
		Expression a = new Expression("(a&b)");
		Expression b = new Expression("(c&d)");
		assertTrue(a.compare(b));
		Expression c = new Expression("(c|d)");
		assertFalse(a.compare(c));
		Expression d = new Expression("(~a|(b&c))");
		assertFalse(a.compare(d));
		Expression e = new Expression("(~a&(b&c))");
		assertTrue(a.compare(e));
		Expression f = new Expression("(a&(b&(a|b)))");
		assertTrue(a.compare(f));
		Expression g = new Expression("(a|(b&(a|b)))");
		assertFalse(a.compare(g));
		Expression h = new Expression("(a|(b=>(a|b)))");
		Expression k = new Expression("((x|y)|((x&b)=>((x|y)|(x&b))))");
		assertTrue(h.compare(k));

	}catch (IllegalLineException e){
		assertTrue(false);
	}
	}
}
