package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String str = "C:12334:CONTROL DEVICE 1 1 2 6 0 ''";
//		Pattern p = Pattern.compile(":{1}\\d+:{1}");
//		String pattern = p.pattern();
//		System.out.println("正则=   " + pattern);
//		
//		Matcher m = p.matcher(str);
//		if(m.find())
//		{
//			String temp = m.group();
//			Pattern p1 = Pattern.compile("\\d+");
//			Matcher m1 = p1.matcher(temp);
//			if(m1.find())
//			{
//				System.out.println(m1.group());
//			}
//		}
//		
		for(int i=1;i<=7;i++){
			if((i%2)!=0){
				for(int j=0;j<i;j++){
					System.out.print("*");
				}
				System.out.println("");
			}
		}
	}

}
