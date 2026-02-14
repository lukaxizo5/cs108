
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	List<T> rules;
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		this.rules = rules;
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		Set<T> set = new HashSet<T>();
		for (int i = 0; i < rules.size() - 1; i++) {
			if (rules.get(i) == null) {
				continue;
			}
			if (rules.get(i).equals(elem) && rules.get(i + 1) != null) {
				set.add(rules.get(i + 1));
			}
		}
		return set;
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		boolean madeChanges = true;
		while (madeChanges) {
			madeChanges = false;
			ListIterator<T> it = list.listIterator();
			if (!it.hasNext()) {
				break;
			}
			T curr = it.next();
			while (it.hasNext()) {
				T elem = it.next();
				Set<T> set = noFollow(curr);
				if (set.contains(elem)) {
					it.remove();
					madeChanges = true;
				}
				else {
					curr = elem;
				}
			}
		}
	}
}
