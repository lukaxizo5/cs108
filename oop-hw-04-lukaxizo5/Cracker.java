// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	public static final int MAX_THREADS = CHARS.length;
	private CountDownLatch countDownLatch;
	private String password;
	private AtomicBoolean cracked = new AtomicBoolean(false);
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}

	public static byte[] generateHashValue(String toHash) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		messageDigest.update(toHash.getBytes());
		return messageDigest.digest();
	}

	private class Worker implements Runnable {
		private int startIndex;
		private int endIndex;
		private int maxLength;
		private byte[] target;
		private CountDownLatch countDownLatch;

		public Worker(int startIndex, int endIndex, int maxLength, byte[] target, CountDownLatch countDownLatch) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.maxLength = maxLength;
			this.target = target;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() throws RuntimeException {
			for (int i = startIndex; i <= endIndex; i++) {
				if (cracked.get()) {
					break;
				}
				String curr = String.valueOf(CHARS[i]);
                try {
                    crack(curr);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
			countDownLatch.countDown();
		}

		private void crack(String curr) throws NoSuchAlgorithmException {
			if (cracked.get()) {
				return;
			}
			if (curr.length() > maxLength) {
				return;
			}
			if (Arrays.equals(generateHashValue(curr), target)) {
				cracked.set(true);
				password = curr;
				System.out.println(curr);
				return;
			}
			for (int i = 0; i < CHARS.length; i++) {
				crack(curr + CHARS[i]);
			}
		}
	}

	public void crackPassword(String targ, int len, int num) throws InterruptedException {
		countDownLatch = new CountDownLatch(num);
		byte[] target = hexToArray(targ);
		int size = Math.floorDiv(CHARS.length, len);
		for (int i = 0; i < Math.min(num, MAX_THREADS); i++) {
			int startIndex = i * size;
			int endIndex = Math.min(startIndex + size, CHARS.length) - 1;
			new Thread(this.new Worker(startIndex, endIndex, len, target, countDownLatch)).start();
		}
		countDownLatch.await();
		System.out.println("all done");
	}

	/*
	 * return the cracked password (for testing)
	 */
	public String getPassword() {
		return password;
	}

	/*
	 * return true if the password is cracked, false otherwise (for testing)
	 */
	public boolean getCracked() {
		return cracked.get();
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
		if (args.length == 1) {
			System.out.println(hexToString(generateHashValue(args[0])));
			return;
		}
		else if (args.length < 2) {
			System.out.println("Args: target length [workers]");
			System.exit(1);
		}
		// args: targ len [num]
		String targ = args[0];
		int len = Integer.parseInt(args[1]);
		int num = 1;
		if (args.length > 2) {
			num = Integer.parseInt(args[2]);
		}
		// a! 34800e15707fae815d7c90d49de44aca97e2d759
		// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
		Cracker cracker =  new Cracker();
		cracker.crackPassword(targ, len, num);
	}
}
