
// Starter code for lp1.
// Version 1.0 (8:00 PM, Wed, Sep 5).

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class Num  implements Comparable<Num> {

    static long defaultBase = 1000000000;  // Default base for all the operations
    long base = defaultBase;  // base of the Num object that it is represented in
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]
	static int log10DefaultBase = 9; // log of defaultBase to the base 10 which is used for calculation purposes


	/**
	 * This function sets the length of the array as to how much of the array
	 * indices we have used. The array can be of size N but the first M blocks
	 * where 0>=M<=N could only be used. This helps save time during looping.
	 * */
	private void setNumLen(){
		for(int i = arr.length-1; i >= 0; i--){
			if(arr[i]==0 && i==0)
				this.len=1;
			if(arr[i] > 0){
				this.len = i+1;
				break;
			}
		}
	}

	/**
	 * This is a Constructor which takes a arbitrarily long number as a String
	 * and converts it into Num object in the default base assumed.
	 * @param s is the string of arbitrarily large number
	 * */
	public Num(String s) {
		this.isNegative = s.charAt(0) == '-';
		if(this.isNegative){
			s = s.substring(1);
		}
		int length = s.length();

		int c = 0;
		this.arr = new long[(int) (((length+1)/Math.log(this.base))+1)];
		this.arr = new long[(int) (length)];
		for(int i = length-1;i>=0;i--){
			this.arr[c++] = s.charAt(i)-'0';
		}
		this.base = 10;
		this.len = c;
		Num aa = fastConvertBase(s);
		this.arr = aa.arr;
		this.base = aa.base;
		this.setNumLen();
	}

	/**
	 * This is a Constructor which takes a long number
	 * and converts it into Num object in the default
	 * base assumed.
	 * @param x is the long number number
	 * */
	public Num(long x) {
		if(x==0){
			this.arr= new long[1];
			this.arr[0]=0;
		}
		else{
			this.isNegative = x<0;
			if(this.isNegative){
				x = Math.abs(x);
			}
			int d = 0;
			long temp = x;
			while( temp > 0 ){
				temp /= this.base;
				d++;
			}
			this.arr = new long[d];
			int i = 0;
			while(x > 0){
				this.arr[i++] = x % this.base;
				x = x / this.base;
			}
		}
		this.setNumLen();
	}

	/**
	 * Empty constructor inorder to initialize an object of this class
	 * */
	public Num() { }

	/**
	 * This method is used to add 2 arbitrarily large numbers present in
	 * the 2 objects that it takes as parameters and returns a Num object
	 * with the sum.
	 * @param a Num object containing the number to be added on the L.H.S of the addition
	 * @param b Num object containing the number to be added on the R.H.S of the addition
	 * @return a Num object with sum of both a and b
	 * */
	public static Num add(Num a, Num b) {
		boolean x=a.isNegative;
		boolean y=b.isNegative;
		Num z = new Num();
		if( x && !y ){
			a.isNegative = !a.isNegative;
			z = subtract(b, a);
			a.isNegative=x;
			return z;
		}
		else if( !x && y ){
			b.isNegative=!b.isNegative;
			z=subtract(a, b);
			b.isNegative=y;
			return z;
		}

		z.isNegative = a.isNegative;
		int lenMax ,lenMin,j;
    	long max[] = null;
    	if(a.len > b.len){
    		max = a.arr;
    		lenMax = a.len;
    		lenMin = b.len;
    	}
    	else{
    		max = b.arr;
    		lenMax = b.len;
    		lenMin = a.len;
    	}
    	z.arr = new long[lenMax+1];
    	z.base=a.base;
    	int i,carry=0;
    	for(i = 0; i<lenMin; i++){
    		z.arr[i] = a.arr[i] + b.arr[i] + carry;

    		if(z.arr[i] >= a.base){
    			carry = (int) (z.arr[i] / a.base);
    			z.arr[i] = z.arr[i] % a.base;
    		}
    		else
    			carry = 0;
    	}

    	for(j = i; j < lenMax; j++){
    		z.arr[j] = max[j] + carry;
    		if(z.arr[j] >= a.base){
    			carry = (int) (z.arr[j] / a.base);
    			z.arr[j] = z.arr[j] % a.base;
    		}
    		else
    			carry = 0;
    	}
		if (carry != 0)
			z.arr[j++] = carry;
			z.len = j;
		z.setNumLen();
	return z;
    }

	/**
	 * This method is used to subtract 2 arbitrarily large numbers present in
	 * the 2 objects that it takes as parameters and returns a Num object
	 * with the difference.
	 * @param a Num object containing the number to be subtracted on the L.H.S of the minus
	 * @param b Num object containing the number to be subtracted on the R.H.S of the minus
	 * @return a Num object with difference of a from b
	 * */
    public static Num subtract(Num a, Num b) {
    	Num z = new Num();
    	z.base=a.base;
    	boolean x=a.isNegative;
    	boolean y=b.isNegative;
		int i;
		long gt[] = null;
		long lt[] = null;
		int gtLen = 0, ltlen = 0;

		if( x != y ) {
			b.isNegative = !b.isNegative;
			z = add(a, b);
			b.isNegative=y;
			return z;
		}
		a.isNegative=false;
		b.isNegative=false;
		int comp = a.compareTo(b);
		if(comp == 1){
			gt = a.arr;
			lt = b.arr;
			z.isNegative = x;
			gtLen = a.len;
			ltlen = b.len;
		}
		else if(comp == -1){
			gt = b.arr;
			lt = a.arr;
			z.isNegative = !y;
			gtLen = b.len;
			ltlen = a.len;
		}

		else {
			z = new Num(0);
			z.isNegative = false;
			z.setNumLen();
			a.isNegative=x;
			b.isNegative=y;
			return z;
		}

		z.arr= new long[Math.max(a.len+1,b.len+1)];
    	for(i=0; i < ltlen; i++){
    		z.arr[i] = gt[i] - lt[i];
    		if(z.arr[i] < 0){
    			z.arr[i]+= z.base;
    			gt[i+1] -= 1;
    		}
    	}
    	while(i < gtLen){
				z.arr[i] = gt[i];
				i++;
		}
		z.setNumLen();
    	a.isNegative=x;
		b.isNegative=y;
    	return z;
    }

	/**
	 * This method is used to multiply 2 arbitrarily large numbers present in
	 * the 2 objects that it takes as parameters and returns a Num object
	 * with the product.
	 * @param a Num object containing the number to be multiplied on the L.H.S of the multiplication
	 * @param b Num object containing the number to be multiplied on the R.H.S of the multiplication
	 * @return a Num object with product of a and b
	 * */
    public static Num product(Num a, Num b) {
		Num z = new Num();
		int i,j;
		if(a.isNegative == b.isNegative)
			z.isNegative = false;
		else
			z.isNegative = true;
		z.base = a.base;
		z.arr = new long[a.len+b.len+1];
		int carry = 0;
		long val = 0;

		for(i = 0; i < a.len; i++){
			for(j = 0; j < b.len; j++){
				val = z.arr[i+j]+ (a.arr[i] * b.arr[j])+carry;
				if(val >= z.base){
					carry = (int) (val/z.base);
					val = val % z.base;
				}
				else
					carry = 0;

				z.arr[j+i] = val;
			}
			if(carry!=0){
				z.arr[j+i] = carry;
				carry=0;
			}

		}

		z.setNumLen();
		return z;
    }

	/**
	 * This method is used to calculate power of number to specific power using divide and
	 * conquer algorithm and returns the answer as a Num object.
	 * @param a Num object which will be raised to a power
	 * @param n long number which is the power of the Num object to be calculated
	 * @return a Num with a^n value
	 * */
	public static Num power(Num a, long n) {
		Num z = new Num();
		z.base = a.base;
		if(a.isNegative && n%2!=0)
			z.isNegative = true;
		else
			z.isNegative = false;
		if(n < 0)
			return new Num(0);
		else if(n == 0)
			return new Num((long)1);
		if(n%2==0)
			return product(power(a, n/2), power(a, n/2));
		else
			return product(product (power(a, n/2),power(a, n/2)),a);
	}

	/**
	 * This method is used to divide 2 arbitrarily large numbers present in
	 * the 2 objects that it takes as parameters using binary search and returns a Num object
	 * with the quotient.
	 * @param a Num object containing the number to be divided on the L.H.S of the division
	 * @param b Num object containing the number to be divided on the R.H.S of the division
	 * @return a Num object with quotient of a divided b
	 * */
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
			z.setNumLen();
        	return z;
    	}
    	Num z = new Num();
    	Num high = gt;
    	Num low = lt;
    	Num temp = new Num(0);
    	Num mid = add(high, low).by2();
    	Num prod = product(mid, lt);
    	int prodComp = prod.compareTo(gt);
    	while(prodComp!=0){
    		if(prodComp==1)
    			high = mid;
    		else if(prodComp==-1)
    			low = mid;
    		temp = mid;
    		mid = (add(high, low)).by2();
    		if(temp.compareTo(mid) == 0)
				return mid;
    		prod = product(mid, lt);
    		prodComp = prod.compareTo(gt);
    	}
		mid.setNumLen();
	return mid;
	}

	/**
	 * This method is used to calculate modulus of 2 arbitrarily large numbers present in
	 * the 2 objects that it takes as parameters and returns a Num object
	 * with the modulus.
	 * @param a Num object containing the number on the L.H.S of the modulus
	 * @param b Num object containing the number on the R.H.S of the modulus
	 * @return a Num object a modulus b
	 * */
	public static Num mod(Num a, Num b) {
		Num remainder = new Num();
		int comp = a.compareTo(b);
		if(comp == -1){
			return a;
		}
		else if(comp == 0){
			return new Num(0);
		}
		remainder = subtract(a, product(divide(a, b), b));
		remainder.setNumLen();
		return remainder;
	}

	/**
	 * This method is used to calculate the square root of a number that it accepts as a parameter
	 * using binary search and returns a Num object with the square root value
	 * @param a Num object whose square root has to be calculated
	 * @return a Num object with square root of Num object a
	 * */
    public static Num squareRoot(Num a) {
    	if(a.isNegative == true) {
		throw new ArithmeticException("cannot find squareroot of negative numbers");
		}
		else {
			Num high = a;
			Num low = new Num (0);
			Num mid = add(add(high, low).by2(), new Num (1));
			Num temp = new Num(0);
			Num squarednumber = power(mid, 2);
			int compare = squarednumber.compareTo(a);
			while(compare != 0){
					if(compare == 1){
						high = mid;
					}
					else if(compare == -1){
						low = mid;
					}
					temp = mid;
					mid = add(high, low).by2();
					if(temp.compareTo(mid) == 0)
						return mid;
					squarednumber = power(mid, 2);
					compare = squarednumber.compareTo(a);
				}
				mid.setNumLen();
				return mid;
		}
    }


	/**
	 * this function is used to compare the values of 2 Num objects where it takes 1 object other
	 * as a parameter and references this to the implicit object calling this method and returns +1
	 * if this is greater, 0 if equal, -1 otherwise.
	 * @param other Num object which is compared to this implicit object calling this method
	 * @return  +1 if this is greater, 0 if equal, -1 otherwise
	 * */
    public int compareTo(Num other) {
		if(this.isNegative != other.isNegative)
		{
			if(this.isNegative == true)
				return -1;
			else
				return  1;
		}
		else
		{
			if(this.len > other.len)
			{
				if(this.isNegative == true)
					return -1;
				else
					return 1;
			}
			else if(this.len < other.len)
			{
				if(this.isNegative == true)
					return 1;
				else
					return -1;
			}
			for(int i = this.len-1; i >= 0; i--)
			{
				if(this.arr[i] > other.arr[i])
					return 1;
				else if(this.arr[i] < other.arr[i])
					return -1;
			}
		}
		return 0;
    }

	/**
	 * This method is used to print the elements of the list in the Num object int the
	 * following pattern "base: elements of list ..."
	 * eg : "100: 65 9 1" for a number 10965 in base 100
	 * */
    public void printList() {
    	System.out.print(this.base+":");
    	if(this.isNegative)
        	System.out.print(" -");
    	for(int i=0; i < this.len; i++){
    		System.out.print(" "+this.arr[i]);
    	}
    	System.out.println("");
    }

	/**
	 * This method is used to convert the Num object to String in base 10
	 * @return a String of number in base 10
	 * */
	public String toString() {
		if(this.base == defaultBase){
			return fastConvertBase10(this);
		}
		StringBuilder sb = new StringBuilder();
		Num s=this.convertBase(10);
		if(s.isNegative)
			sb.append('-');
		for(int i=s.len-1; i>=0; i--)
			sb.append(s.arr[i]);
		s.len=sb.length();
		return sb.toString();
	}

    public long base() { return base; }

    /**
	 * This method is used to quickly convert the Num object to base 10 if and only
	 * if the Num object is in the default base.
	 * @param num Num object in default base
	 * @return a String of number in base 10
	 * */
	public static String fastConvertBase10(Num num){
		StringBuilder sb = new StringBuilder();
		if(num.isNegative){
			sb.append('-');
		}
		for(int i=num.len-1;i>=0;i--){
			if(i == num.len-1){
				sb.append(num.arr[i]);
			}else{
				sb.append(String.format("%"+String.valueOf(log10DefaultBase)+"s",String.valueOf(num.arr[i])).replace(' ','0'));
			}
		}
		return sb.toString();
	}

	/**
	 * This method is used to quickly convert the String of arbitrary long number to defaultbase
	 * @param s String of arbitrary long number
	 * @return a Num object with Number in default base
	 * */
    public static Num fastConvertBase(String s){
		Num tempNum = new Num();
		int strLength = s.length();
		int k = 0;
		tempNum.base = defaultBase;
		tempNum.arr = new long[(s.length()/log10DefaultBase)+1];

		while(strLength >=log10DefaultBase){
			tempNum.arr[k] = Long.parseLong(s.substring(strLength-log10DefaultBase,strLength));
			strLength = strLength-log10DefaultBase;
			k++;
		}
		if (strLength>0)
			tempNum.arr[k] = Long.parseLong(s.substring(0, strLength));
		return tempNum;
	}

	/**
	 * This method is used to convert Num object from current base to specified base that is
	 * taken as a parameter using Hornors method
	 * @param base the base integer to be converted
	 * @return a Num object with specified number as base
	 * */
	public Num convertBase(int base){
		Num exbase = convertDigit(this.base,base);
		exbase.setNumLen();
		Num newNum = new Num(0);
		newNum.base = base;
		Num convDigByDig = new Num();
		for(int i = this.len-1; i>=0;i--){
			newNum = product(newNum, exbase);
			convDigByDig = convertDigit(this.arr[i],base);
			newNum = add(newNum,convDigByDig);
		}
		newNum.isNegative =this.isNegative;

		return newNum;
	}


	/**
	 * This method is used to convert a number into the newBase using the primary base conversion method
	 * using the modulus
	 * taken as a parameter using Hornors method
	 * @param val the number to be converted into new base
	 * @param newBase the base number
	 * @return a Num object with newBase number as the new base
	 * */
	public Num convertDigit(long val,int newBase) {
		Num newVal = new Num();
		int d = 0;
		newVal.base = newBase;
		long temp = val;
		long x1 = temp;
		while(x1 > 0){
			x1 /= 10;
			d++;
		}
		//newVal.arr = new long[(int) (d+1)/(Math.log10(newBase)+1)];
		newVal.arr = new long[d];
		int i = 0;
		while(temp > 0){
			newVal.arr[i] = temp % newBase;
			i++;
			temp = temp/newBase;
		}
		newVal.setNumLen();
		return newVal;
	}

	/**
	 * This method is used to divide the number by 2 using Binary search
	 * @return Num with value divided by 2
	 * */
    public Num by2() {
    	int i=this.len-1;
    	long v = 0, rem = 0;
    	Num z = new Num();
    	z.arr = new long[this.len];
    	while(i >= 0){
    		v = this.arr[i];
    		if(rem == 1)
    			v += base;
    		z.arr[i--] = v/2;
    		rem = v%2;
    	}
		z.setNumLen();
	return z;
    }

	/**
	 * This method is used to evaluate a postfix expression and return the resulting number
	 * as a Num Object
	 * @param expr array of String containing the postfix expression
	 * @return Num with value of evaluated postfix expression
	 * */
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

	/**
	 * This method is used as a helper method for evaluate Postfix expression to call the
	 * proper operations on two Num objects and return the values as string
	 * @param a Num object present in the operation
	 * @param b Num object present in the operation
	 * @param x Helps in deciding which operation to choose
	 * @return Num object with evaluated numeric operations
	 * */
	public static Num eval(Num a, Num b, String x){
		Num z = null;
		switch(x){
			case "+" : z = add(a,b);
				break;
			case "-" : z = subtract(b, a);
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

	/**
	 * This method is used to evaluate a Infix expression and return the resulting number
	 * as a Num Object
	 * @param expr array of String containing the Infix expression
	 * @return Num with value of evaluated Infix expression
	 * */
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
		priority.put("(", 0);
		priority.put(")", 0);

		Stack<String> stack = new Stack<>();
		for(int i = 0; i < expr.length; i++){
			String val = expr[i];
			if(!priority.containsKey(val))
			{
				postfixstring += val + " ";
			}
			else if(val == "("){
				stack.push(val);
			}
			else if(val == ")"){
				while(! stack.isEmpty() && stack.peek()!= "("){
					postfixstring += stack.pop() + " ";
				}
				if (!stack.isEmpty() && stack.peek() != "(")
					return null;
				else
					stack.pop();
			}
			else
			{
				while(!stack.isEmpty() && priority.get(val) <= priority.get(stack.peek()) )
					postfixstring += stack.pop() + " ";
				stack.push(val);
			}
		}

		while(!stack.isEmpty()){
			postfixstring += stack.pop() + " ";
		}
		postfixarrayoutput = postfixstring.split(" ");
		postfixsolution = evaluatePostfix(postfixarrayoutput);
		return postfixsolution;
	}


	public static void main(String[] args) {
		Num x = new Num("12345678999");
		x.printList();
		Num y = new Num("12345678999");
		y.printList();
		add(x,y).printList();
		add(x,y).toString();
	}


}


/*
	Sample Input :
	//Evaluate Postfix expo : ["13", "12", "*", "48", "3", "/", "-", "66", "+"]
	Output:
	206
*/
