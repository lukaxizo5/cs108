// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Bank {
	public static final int ACCOUNTS = 20;	 // number of accounts
	public static final int INITIAL_BALANCE = 1000;  // initial balance of accounts
	private final Transaction nullTrans = new Transaction(-1, 0, 0);
	private BlockingQueue<Transaction> transactions;
	private CountDownLatch countDownLatch;
	private int numWorkers;
	private ArrayList<Account> accounts;
	private AtomicInteger threadsStarted;
	private AtomicInteger threadsFinished;
	private int transactionsMade = 0;


	public Bank(int numWorkers) {
		this.numWorkers = numWorkers;
		this.accounts = new ArrayList<>();
		for (int i = 0; i < ACCOUNTS; i++) {
			this.accounts.add(new Account(this, i, INITIAL_BALANCE));
		}
		this.transactions = new LinkedBlockingQueue<>();
		this.threadsStarted = new AtomicInteger(0);
		this.threadsFinished = new AtomicInteger(0);
	}

	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	public void readFile(String file) throws IOException, InterruptedException {
		BufferedReader reader = new BufferedReader(new FileReader(file));

		// Use stream tokenizer to get successive words from file
		StreamTokenizer tokenizer = new StreamTokenizer(reader);

		while (true) {
			int read = tokenizer.nextToken();
			if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
			int from = (int)tokenizer.nval;

			tokenizer.nextToken();
			int to = (int)tokenizer.nval;

			tokenizer.nextToken();
			int amount = (int)tokenizer.nval;

			transactions.put(new Transaction(from, to, amount));
			transactionsMade++;
		}
		for (int  i = 0; i < numWorkers; i++) {
			transactions.put(nullTrans);
		}
		reader.close();
	}

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file, int numWorkers) throws InterruptedException, IOException {
		countDownLatch = new CountDownLatch(numWorkers);
		for (int i = 0; i < numWorkers; i++) {
			new Thread(new Worker()).start();
			threadsStarted.incrementAndGet();
		}
		readFile(file);
		countDownLatch.await();
	}

	private class Worker implements Runnable {
		@Override
		public void run() throws RuntimeException {
			Transaction transaction;
			while (true) {
                try {
                    transaction = transactions.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (transaction.equals(nullTrans)) {
						threadsFinished.incrementAndGet();
						break;
					}
					int from = (int)transaction.getFrom();
					int to = (int)transaction.getTo();
					int amount = transaction.getAmount();
					accounts.get(from).makeTransaction(-amount);
					accounts.get(to).makeTransaction(amount);
            }
			countDownLatch.countDown();
		}
	}

	/*
	 * prints the accounts
	 */
	private void printAccounts() {
		for (Account account : accounts) {
			System.out.println(account);
		}
	}

	/*
	 * return the accounts arraylist (for testing)
	 */
	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	/*
	 * returns the number of transactions read from the file (for testing)
	 */
	public int getTransactionsMade() {
		return transactionsMade;
	}

	/*
	 * returns true if all transactions were processed (BlockingQueue is empty), false otherwise (for testing)
	 */
	public boolean allTransactionsWereMade() {
		return transactions.isEmpty();
	}

	/*
	 * return the number of started threads (for testing)
	 */
	public int getThreadsStarted() {
		return threadsStarted.get();
	}

	/*
	 * return the number of finished threads (for testing)
	 */
	public int getThreadsFinished() {
		return threadsFinished.get();
	}
	
	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) throws InterruptedException, IOException {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			System.exit(1);
		}
		
		String file = args[0];
		
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}

		Bank bank = new Bank(numWorkers);
		bank.processFile(file, numWorkers);
		bank.printAccounts();
	}
}

