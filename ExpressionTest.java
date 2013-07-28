
import junit.framework.TestCase;

import org.junit.Test;


public class ExpressionTest extends TestCase{

	@Test
	
	public static void test2(){
		System.out.println("Start Testing");
	}
	public static void testisOOK(){
		boolean result=false;
		Expression a = new Expression("a&d");
		try{
			a.isOOK();		
		}catch (IllegalLineException e){
			System.out.println(e.getMessage());
			result=true;
		}assertTrue(result);
	}
	public static void testisOOK2(){
		boolean result=false;
		Expression a = new Expression("a&d))");
		try{
			a.isOOK();		
		}catch (IllegalLineException e){
			System.out.println(e.getMessage());
			result=true;
		}assertTrue(result);
	}
	
	
	public static void test3(){
		Expression a = new Expression("(a&b)");
		Expression b = new Expression("(c&d)");
		a.expr();
		a.print();
		b.expr();
		b.print();
		
	}
	public void aStringTest(){
		System.out.println("abbb");
		Expression a = new Expression("(a&b)");
		Expression b = new Expression("(c&d)");
		assertTrue(a.aString().equals("(a&b)"));
		assertFalse(a.aString().equals("a"));
		assertTrue(b.aString().equals("(c&d)"));
		assertFalse(a.aString().equals(b.aString()));
		assertTrue(a.aString().equals(a.aString()));
	}
	public void compareTest(){
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
			
			}
	
	
	
	}

