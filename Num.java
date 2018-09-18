
// Starter code for lp1.
// Version 1.0 (8:00 PM, Wed, Sep 5).

// Change following line to your NetId
package RXA170033;

import java.util.Arrays;

public class Num  implements Comparable<Num> {

    static long defaultBase = 100;  // Change as needed
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    public Num(String s) {
    	int i = s.length();
    	long b = base;
    	int pow=0;
    	int k = 0;
    	while(b>1){
    		b=b/10;
    		pow++;
    	}
    	this.arr = new long[(i/pow)+1];
    	while(i>=pow){
    		this.arr[k] = Long.parseLong(s.substring(i-pow,i));
    		i = i-pow;
    		k++;
    	}
    	if (i>0)
    		this.arr[k] = Long.parseLong(s.substring(0, i));
    }

    public Num(long x) {
    	int d = 0;
    	long x1 = x;
    	while(x1>0){
    		x1 = x1/10;
    		d++;
    	}
    	this.arr = new long[(int) (((d+1)/Math.log10(base))+1)];
    	this.arr[0] = x;
    	this.convertBase((int) this.base);
    }

    public Num() {
	}

	public static Num add(Num a, Num b) {
    	
    	int lenMax ,lenMin;
    	long max[] = null;
    	if(a.arr.length > b.arr.length){
    		max = a.arr;
    		lenMax = a.arr.length;
    		lenMin = b.arr.length;
    	}
    	else{
    		max = b.arr;
    		lenMax = b.arr.length;
    		lenMin = a.arr.length;
    	}
    	Num z = new Num();
    	z.arr = new long[lenMax+1];
    	int i,carry=0;
    	for(i = 0;i<lenMin;i++){
    		z.arr[i] = a.arr[i] + b.arr[i] + carry;
    		if(z.arr[i]>=Num.defaultBase){
    			carry = (int) (z.arr[i]/Num.defaultBase);
    			z.arr[i] = z.arr[i]%Num.defaultBase;
    		}
    		else
    			carry = 0;
    	}
    	
    	for(int j = i;j<lenMax;j++){
    		z.arr[j] = max[j] + carry;
    		if(z.arr[i]>=Num.defaultBase){
    			carry = (int) (z.arr[i]/Num.defaultBase);
    			z.arr[i] = z.arr[i]%Num.defaultBase;
    		}
    		else
    			carry = 0;
    	}
	return z;
    }

    public static Num subtract(Num a, Num b) {
    	long gt[] = null;
    	long lt[] = null;
    	
    	int comp = a.compareTo(b);
    	
    	if(comp == 1){
    		gt = a.arr;
    		lt = b.arr;
    	}
    	else if(comp == -1){
    		gt = b.arr;
    		lt = a.arr;
    	}
    	else
    	{
    		Num z = new Num();
        	z.arr = new long[]{0};
        	return z;
    	}
    	Num z = new Num();
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
    	
    	return z;
    }

    public static Num product(Num a, Num b) {
    	Num z = new Num();
    	z.arr = new long[a.arr.length + b.arr.length];
    	int carry = 0;
    	long val = 0;
    	for(int i = 0; i<a.arr.length; i++){
    		for(int j = 0; j<b.arr.length; j++){
    			val =z.arr[j+i]+ (a.arr[i] * b.arr[j])+carry;
    			if(val>=Num.defaultBase){
        			carry = (int) (val/Num.defaultBase);
        			val = val%Num.defaultBase;
        		}
        		else
        			carry = 0;
    			z.arr[j+i] = val;
    		}
    	}
    	return z;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
	return null;
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
	return mid;
    }

    // return a%b
    public static Num mod(Num a, Num b) {
	return null;
    }

    // Use binary search
    public static Num squareRoot(Num a) {
	return null;
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
    	for(long numDigit : this.arr){
    		System.out.print(" "+numDigit);
    	}
    	System.out.println("");
    }
    
    // Return number to a string in base 10
    public String toString() {
    	
	return null;
    }

    public long base() { return base; }

    // Return number equal to "this" number, in base=newBase
    public Num convertBase(int newBase) {
    	long x = this.arr[0];
    	int i = 0;
    	while(x>0){
    		this.arr[i] = x%newBase;
    		i++;
    		x = x/newBase;
    	}
    	return this;
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
	return z;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
	return null;
    }

    // Evaluate an expression in infix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluateInfix(String[] expr) {
	return null;
    }


    public static void main(String[] args) {
	Num x = new Num(4);
	x.printList();
	Num y = new Num("4000");
	y.printList();
	/*Num z = Num.add(x, y);
	z.printList();
	Num s = Num.subtract(x, y);
	s.printList();
	Num p = Num.product(x, y);
	p.printList();*/
	Num d = Num.divide(x, y);
	d.printList();
	/*System.out.println("by 2:");
	Num b = y.by2();
	b.printList();*/
	/*Num a = Num.power(x, 8);
	System.out.println(a);
	if(z != null) z.printList();*/
    }
}