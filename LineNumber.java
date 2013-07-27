import java.util.*;

public class LineNumber {
	
	private String LineNum;
	
	public  LineNumber(){
		
		LineNum = "1";
	}
	public String NewLine (){

		int temp = Integer.parseInt(LineNum.substring(LineNum.length()-1));
		temp++;
		LineNum = LineNum.substring(0,LineNum.length()-1) + Integer.toString(temp);
		
		return LineNum;
	}
	public String NewSubLine(){
		LineNum = LineNum +".1";
		
		return LineNum;
	}
	public String DeleteSub(){
		LineNum = LineNum.substring(0,LineNum.length()-2);
		
		return LineNum;
	}
	// sets LineNum to correct next Line Number
	
	
	public String get(){
		return LineNum;
	}
	
}

