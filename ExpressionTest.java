import static org.junit.Assert.*;

import org.junit.Test;


public class ExpressionTest {

  @Test
	public void test() {
		
			Expression a = new Expression("(a&b)");
			Expression b = new Expression("(c&d)");
			a.expr();
			a.print();
			b.expr();
			b.print();
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


