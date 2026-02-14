import java.util.Objects;

// Transaction.java
/*
 (provided code)
 Transaction is just a dumb struct to hold
 one transaction. Supports toString.
*/
public class Transaction {
	public int from;
	public int to;
	public int amount;
	
   	public Transaction(int from, int to, int amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public int getAmount() {
		return amount;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Transaction that = (Transaction) o;
		return from == that.from && to == that.to && amount == that.amount;
	}

	public String toString() {
		   return("from:" + from + " to:" + to + " amt:" + amount);
	}
}
