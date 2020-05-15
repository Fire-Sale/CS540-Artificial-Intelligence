public class Helper {

	/**
	 * Class constructor.
	 */
	private Helper() {
	}

	/**
	 * This method is used to check if a number is prime or not
	 * 
	 * @param x A positive integer number
	 * @return boolean True if x is prime; Otherwise, false
	 */
	public static boolean isPrime(int x) {
		if (x == 1 || x == 2 || x == 3)
			return true;
		boolean ok = true;
		for (int i = 2; i * i <= x; i++) {
			if (x % i == 0) {
				ok = false;
				break;
			}
		}
		return ok;
	}

	/**
	 * This method is used to get the largest prime factor
	 * 
	 * @param x A positive integer number
	 * @return int The largest prime factor of x
	 */
	public static int getLargestPrimeFactor(int x) {
		for(int i = x; i >= 1; i--){
			if( x % i == 0 && isPrime(i)){
				return i;
			}
		}
		return 0;
	}
}