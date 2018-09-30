// Starter code for lp1.
// Version 1.0 (8:00 PM, Wed, Sep 5).

// Change following line to your NetId
package LongProject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class Num  implements Comparable<Num> {

    static long defaultBase = 1000000000;  // Change as needed
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]
	static Comparator<Integer> cmp = Comparator.naturalOrder();

	public void setNumLen(){
		for(int i=arr.length-1;i>=0;i--){
			if(arr[i] > 0){
				this.len = i+1;
				break;
			}
		}
	}

	public Num(){
		//create object
	}

    public Num(String s) {
    	this.isNegative = s.charAt(0) == '-';
    	if(this.isNegative){
    		s = s.substring(1);
		}
    	int digitLength = s.length();
		this.base = 10;
		int index = 0;
		this.arr = new long[(int) (((digitLength+1)/Math.log10(this.base))+1)];
		for(int i = digitLength-1;i>=0;i--){
			this.arr[index++] = s.charAt(i)-'0';
		}
		Num tempConvertedBase = convertBase((int) defaultBase);
		this.arr = tempConvertedBase.arr;
		this.base = tempConvertedBase.base;
		this.setNumLen();
    }

    public Num(long x) {
		this.isNegative = x<0;
		if(this.isNegative){
			x = Math.abs(x);
		}
    	int d = 0;
    	long x1 = x;
    	while(x1>0){
    		x1 = x1/defaultBase;
    		d++;
    	}
    	this.arr = new long[(int) (((d+1)/Math.log10(this.base))+1)];
    	//added by richa st
    	int i = 0;
    	while(x>0){
    		this.arr[i] = x%this.base;
    		x = x/this.base;
    		i++;
    	}
    	this.setNumLen();
    	//this.convertBase((int) this.base);
    }

    public static Num getSum(Num a,Num b,Num z) {
		int lenMax ,lenMin;
		long max[] = null;
		max = (a.len > b.len)?a.arr:b.arr;
		lenMax = (a.len > b.len)?a.len:b.len;
		lenMin = (a.len > b.len)?b.len:a.len;

		z.arr = new long[lenMax+1];
		int i,carry=0;
		for(i = 0;i<lenMin;i++){
			z.arr[i] = a.arr[i] + b.arr[i] + carry;
			if(z.arr[i]>=a.base){
				carry = (int) (z.arr[i]/a.base);
				z.arr[i] = z.arr[i]%a.base;
			}
			else
				carry = 0;
		}
		for(int j = i;j<lenMax;j++){
			z.arr[j] = max[j] + carry;
			if(z.arr[i]>=a.base){
				carry = (int) (z.arr[i]/a.base);
				z.arr[i] = z.arr[i]%a.base;
			}
			else
				carry = 0;
		}
		if(carry > 0){
			z.arr[i++] = carry;
			carry = 0;
		}
		z.setNumLen();

		return z;
	}

	public static Num add(Num a, Num b) {
		Num z = new Num();	//summ will be stored in this object
		z.base = a.base;
		if(a.isNegative && b.isNegative){
			z.isNegative = true;
			z = getSum(a,b,z);
			return z;
		}else if(!a.isNegative && !b.isNegative){
			z.isNegative = false;
			z = getSum(a,b,z);
			return z;
		}else if(!a.isNegative && b.isNegative){
			int comp = a.compareTo(b);
			System.out.println("here adding +ve and -ve "+comp);
			if(comp == 1){
				z.isNegative = false;
				z = getDifference(a,b,z,a.arr,b.arr);
				return z;
			}else if(comp == 0){
				z = new Num((long)0);
				z.isNegative =  false;
				z.setNumLen();
				return z;
			}else{
				z.isNegative = true;
				z = getDifference(a,b,z,b.arr,a.arr);
				return z;
			}
		}else{
			int comp = a.compareTo(b);
			System.out.println("here adding -ve and +ve "+comp);
			if(comp == 1){
				z.isNegative = true;
				z = getDifference(a,b,z,a.arr,b.arr);
				return z;
			}else if(comp == 0){
				z = new Num((long)0);
				z.isNegative =  false;
				z.setNumLen();
				return z;
			}else{
				z.isNegative = false;
				z = getDifference(a,b,z,b.arr,a.arr);
				return z;
			}
		}
    }

    public static Num getDifference(Num a,Num b,Num z,long[] gt,long[] lt){
		z.arr = new long[gt.length];
		int i;
		for(i=0;i<lt.length;i++){
			z.arr[i] = gt[i] - lt[i];
			if(z.arr[i]<0){
				z.arr[i]+= z.base;
				gt[i+1] = gt[i+1]-1;
			}
		}
		for(int j=i+1;j<gt.length;j++)
			z.arr[j] = gt[j];
		z.setNumLen();
		return z;
	}

    public static Num subtract(Num a, Num b) {
    	Num z = new Num();
		long gt[] = null;
		long lt[] = null;
		int comp = a.compareTo(b);
		System.out.println("signs are "+a.isNegative+" "+b.isNegative+" and comp is "+comp);
		//	(a-b)
		if(!a.isNegative && !b.isNegative){
			//if a and b are positive
			if(comp == 1){
				// a > b and hence difference is +ve
				gt = a.arr;
				lt = b.arr;
				z.isNegative = false;
			}else if(comp == 0){
				System.out.println("here as they are same");
				z = new Num((long)0);
				z.isNegative =  false;
				z.setNumLen();
				return z;
			}else{
				// a < b and hence difference is -ve
				gt = b.arr;
				lt = a.arr;
				z.isNegative = true;
			}
		}else if(a.isNegative && b.isNegative){
			// both a and b are negative
			if(comp == 1){
				// |a| > |b| and hence difference is -ve
				gt = a.arr;
				lt = b.arr;
				z.isNegative = true;
			}else if(comp == 0){
				System.out.println("here as they are same and -ve");
				z = new Num((long)0);
				z.setNumLen();
				z.isNegative =  false;
				return z;
			}else{
				// |a| < |b| and hence difference is +ve
				gt = b.arr;
				lt = a.arr;
				z.isNegative = false;
			}
		}else if(a.isNegative && !b.isNegative){
			// since a is -ve and b is +ve the difference is -ve. i.e -(|a|+|b|)
			//irrespective of a > b or b > a
			z.isNegative = true;
			z = getSum(a,b,z);
			return z;
		}else{
			// since a is +ve and b is -ve the difference is +ve. i.e (a+b)
			//irrespective of a > b or b > a
			System.out.println("here is the +ve and -ve case");
			z.isNegative = false;
			z = getSum(a,b,z);
			return z;
		}
		z = getDifference(a,b,z,gt,lt);
		return z;
//		if(comp == 1){
//			gt = a.arr;
//			lt = b.arr;
//
//			//if both numbers are -ve then the resulting difference will have sign of greater of the two
//			z.isNegative = (a.isNegative && b.isNegative);
////			if(a.isNegative && b.isNegative)
////				z.isNegative = true;
//		}else if(comp == -1){
//			gt = b.arr;
//			lt = a.arr;
//
//
//			if(!a.isNegative && !b.isNegative)
//				z.isNegative = true;
//		}else{
//			if((!a.isNegative && !b.isNegative) ||(a.isNegative && b.isNegative)){
//				z = new Num((long)0);
//				z.setNumLen();
//				return z;
//			}
//		}

//		if((a.isNegative && !b.isNegative )||( !a.isNegative && b.isNegative)){
//			z = add(a, b);
//			if(a.isNegative && !b.isNegative)
//				z.isNegative = true;
//			z.setNumLen();
//			return z;
//		}

    }

    public static Num product(Num a, Num b) {
    	Num z = new Num();
    	z.base = a.base;
    	z.arr = new long[a.arr.length + b.arr.length];
    	int carry = 0;
    	long val = 0;
    	long bs = a.base;
    	for(int i = 0; i<a.arr.length; i++){
    		for(int j = 0; j<b.arr.length; j++){
    			val =z.arr[j+i]+ (a.arr[i] * b.arr[j])+carry;
    			if(val>=bs){
        			carry = (int) (val/bs);
        			val = val%bs;
        		}
        		else
        			carry = 0;
    			z.arr[j+i] = val;
    		}
    	}
		z.setNumLen();
    	return z;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
    	
    	Num temp = null;
    	boolean negResult = false;
    	if(n<0){
    		temp = new Num((long)0);
    		return temp;
    	}
    	
    	if(a.isNegative && n%2!=0)
    		negResult = true;
    	
    	if(n == 0){
    		temp = new Num((long)1);
    		if(negResult)
    			temp.isNegative = true;
    		return temp;
    	}
    	temp = power(a, n/2);
    	if(n%2==0)
    		return product(temp, temp);
    	else
    		return product(product (temp,temp),a);
    	
    }

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {
    	int comp = a.compareTo(b);
    	Num gt = null;
    	Num lt = null;
    	if(comp == 1){
    		gt = a;
    		lt = b;
    	}
    	else if(comp == -1){
    		gt = b;
    		lt = a;
    	}
    	else{
    		Num z = new Num();
        	z.arr = new long[]{1};
        	return z;
    	}
    	Num z = new Num();
    	Num high = new Num(10000);
    	Num low = new Num (0);
    	Num mid = add(high, low).by2();
    	Num prod = product(mid, lt);
    	int prodComp = prod.compareTo(gt);
    	while(prodComp!=0){
    		if(prodComp==1)
    			high = mid;
    		else if(prodComp==-1)
    			low = mid;
    		else
    			return mid;
    		mid = (add(high, low)).by2();
    		prod = product(mid, lt);
    		prodComp = prod.compareTo(gt);
    	}
		mid.setNumLen();
	return mid;
    }

    // return a%b
    public static Num mod(Num a, Num b) {
    	Num remainder = new Num();
    	int comp = a.compareTo(b);
    	Num gt = null;
    	Num lt = null;
    	if(comp == 1){
    		gt = a;
    		lt = b;
    	}
    	else if(comp == -1){
    		gt = b;
    		lt = a;
    	} 
    	remainder = subtract(gt, product(divide(gt, lt), lt));
    	remainder.setNumLen();
	return remainder;
    }

    // Use binary search
    public static Num squareRoot(Num a) {
    	Num high = a.by2();
    	Num low = new Num (0);
    	Num mid = add(high, low).by2();
		Num squarednumber = power(mid, 2);
		int compare = squarednumber.compareTo(a);
		while(compare != 0){
				if(compare > 1){
					high = mid;
				}
				else if(compare < 1){
					low = mid;
				}
				else
					return mid;
				mid = add(high, low).by2();
				squarednumber = power(mid, 2);
				compare = squarednumber.compareTo(a);
			}
			mid.setNumLen();
	return mid;
    }


    // Utility functions
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1 otherwise
    public int compareTo(Num other) {
    	int i = this.arr.length-1;
    	if(this.arr.length > other.arr.length){
    		boolean nonZero = false;
    		for(int j = other.arr.length;j<this.arr.length;j++){
    			if(this.arr[j]!=0){
    				nonZero = true;
    				return 1;
    			}
    		}
    		if(!nonZero){
    			i = other.arr.length-1;
    		}
    	}
    	else if(this.arr.length < other.arr.length){
    		boolean nonZero = false;
    		for(int j = this.arr.length;j<other.arr.length;j++){
    			if(other.arr[j]!=0){
    				nonZero = true;
    				return -1;
    			}
    		}
    		if(!nonZero){
    			i = this.arr.length-1;
    		}
    	}
    	
		long diff = 0;
		while(i>=0){
			diff = this.arr[i] - other.arr[i];
			if(diff>0){
				return 1;
			}
			else if(diff<0){
				return -1;
			}
			i--;
		}
		
		return 0;
    }
    
    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"
    public void printList() {
		System.out.print(this.base+":");
    	if(this.isNegative)
        	System.out.print(" -");

    	for(int i = 0; i<this.len;i++)
    		System.out.print(" "+this.arr[i]);
    	System.out.println("");
    }
    
    // Return number to a string in base 10
    public String toString() {
    	Num base10Num = this.convertBase(10);
    	String str = "";
    	if(this.isNegative){
			str+="-";
		}
    	for(int i=base10Num.len-1; i>=0; i--){
			str += base10Num.arr[i];
		}
		base10Num.len = str.length();
    	if(str.length() == 0){
			System.out.println(str.length());
		}else{
			System.out.println(str);
		}
    	return str;
    }

    public long base() { return base; }

    public Num convertBase(int base){
    	
    	Num exbase = convertDigit(this.base,base);
    	exbase.setNumLen();
    	Num newNum = new Num(0);
    	newNum.base = base;
    	newNum.isNegative=this.isNegative;
    	Num convDig = new Num();
    	for(int i = this.arr.length-1; i>=0;i--){
    		newNum = product(newNum, exbase);
    		convDig = convertDigit(this.arr[i],base);
    		newNum = add(newNum,convDig);
    	}
    	return newNum;
    }
    
    public Num convertDigit(long oldbase,int newBase) {
    	Num bs = new Num();
    	int d = 0;
    	bs.base = newBase;
    	long x = oldbase;
    	long x1 = x;
    	while(x1>0){
    		x1 = x1/10;
    		d++;
    	}
    	bs.arr = new long[(int) (((d+1)/Math.log10(newBase))+1)];
    	int i = 0;
    	while(x>0){
    		bs.arr[i] = x%newBase;
    		i++;
    		x = x/newBase;
    	}
    	bs.setNumLen();
    	return bs;
    }
    
    // Divide by 2, for using in binary search
    public Num by2() {
    	int i=this.arr.length-1;
    	long v = 0,rem = 0;
    	Num z = new Num();
    	z.arr = new long[this.arr.length];
    	while(i>=0){
    		v = this.arr[i];
    		if(rem == 1)
    			v = base + v;
    		z.arr[i] = v/2;
    		rem = v%2;
    		i--;
    	}
    	z.setNumLen();
	return z;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
    	Stack<Num> st = new Stack<Num>();
    	
    	HashSet<String> optr = new HashSet<String>();
    	optr.add("^");
    	optr.add("*");
    	optr.add("/");
    	optr.add("%");
    	optr.add("+");
    	optr.add("-");
    	
    	for(int i = 0; i<expr.length; i++){
    		if(!optr.contains(expr[i]))
    			st.push(new Num(expr[i]));
    		else{
    			st.push(eval(st.pop(),st.pop(),expr[i]));
    		}
    	}
    	
    	return st.pop();
    }
    
    public static Num eval(Num a, Num b, String x){
    	Num z = null;
    	switch(x){
	    	case "+" : z = add(a,b);
	    		break;
	    	case "-" : z = subtract(a, b);
	    		break;
	    	case "*" : z = product(a, b);
	    		break;
	    	case "/" : z = divide(a, b);
	    		break;
	    	case "%" : z = mod(a, b);
	    		break;
    	}
    	return z;
    }

    // Evaluate an expression in infix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluateInfix(String[] expr) {
    	Num postfixsolution = new Num();
    	String postfixstring = new String("");
    	String[] postfixarrayoutput;
    	HashMap<String, Integer> priority = new HashMap<String, Integer>();
    	priority.put("+", 1);
    	priority.put("-", 1);
    	priority.put("/", 2);
    	priority.put("*", 2);
    	priority.put("^", 3);
    	
    	Stack<Character> stack = new Stack<>();
    	
    	for(int i = 0; i < expr.length; i++){
    		String val = expr[i];
    		char charval = val.charAt(0);
    		if(Character.isLetter(charval))
    		{
    			postfixstring += charval;
    		}
    		else if(charval == '('){
    			stack.push(charval);
    		}
    		else if(charval == ')'){
    			while(! stack.isEmpty() && stack.peek()!= '('){
    				postfixstring += stack.pop();
    			}
    			stack.pop();
    		}
    		else 
    		{
    			while(!stack.isEmpty() && priority.get(charval) <= priority.get(stack.peek()) ){
    				postfixstring += stack.pop();
    			}
    			stack.push(charval);
    		}
    	}
    	
    	while(!stack.isEmpty()){
    		postfixstring += stack.pop();
    	}
    	postfixarrayoutput = postfixstring.split("");
    	postfixsolution = evaluatePostfix(postfixarrayoutput);
	return postfixsolution;
    }


    public static void main(String[] args) {
//	Num x = new Num("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
//	x.printList();
//	Num y = new Num("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
//	y.printList();
//	subtract(x,y).toString();

		Num x = new Num("-999999999");
		x.printList();
		Num z = new Num("222222222");
		z.printList();
		add(x,z).toString();
//	Num z1 = z.convertBase(10);
//	System.out.println("Print tostring");
//	z1.toString();
//	System.out.println("Printlist");
//	z1.printList();
    }
}
