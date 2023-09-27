package adt.hashtable.closed;

import java.util.ArrayList;
import util.Util;

import adt.hashtable.hashfunction.HashFunction;
import adt.hashtable.hashfunction.HashFunctionClosedAddress;
import adt.hashtable.hashfunction.HashFunctionClosedAddressMethod;
import adt.hashtable.hashfunction.HashFunctionFactory;

public class HashtableClosedAddressImpl<T> extends
		AbstractHashtableClosedAddress<T> {

	/**
	 * A hash table with closed address works with a hash function with closed
	 * address. Such a function can follow one of these methods: DIVISION or
	 * MULTIPLICATION. In the DIVISION method, it is useful to change the size
	 * of the table to an integer that is prime. This can be achieved by
	 * producing such a prime number that is bigger and close to the desired
	 * size.
	 * 
	 * For doing that, you have auxiliary methods: Util.isPrime and
	 * getPrimeAbove as documented bellow.
	 * 
	 * The length of the internal table must be the immediate prime number
	 * greater than the given size (or the given size, if it is already prime). 
	 * For example, if size=10 then the length must
	 * be 11. If size=20, the length must be 23. You must implement this idea in
	 * the auxiliary method getPrimeAbove(int size) and use it.
	 * 
	 * @param desiredSize
	 * @param method
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashtableClosedAddressImpl(int desiredSize,
			HashFunctionClosedAddressMethod method) {
		int realSize = desiredSize;

		if (method == HashFunctionClosedAddressMethod.DIVISION) {
			realSize = this.getPrimeAbove(desiredSize); // real size must the
														// the immediate prime
														// above
		}
		initiateInternalTable(realSize);
		HashFunction function = HashFunctionFactory.createHashFunction(method,
				realSize);
		this.hashFunction = function;
	}

	// AUXILIARY
	/**
	 * It returns the prime number that is closest (and greater) to the given
	 * number.
	 * If the given number is prime, it is returned. 
	 * You can use the method Util.isPrime to check if a number is
	 * prime.
	 */
	int getPrimeAbove(int number) {
		int prime = number + 1;
		while (!Util.isPrime(prime)) {
			prime += 1;
		} 
		return prime;
	}

	@Override
	public void insert(T element) {
		int hash = ((HashFunctionClosedAddress<T>) this.hashFunction).hash(element);
		ArrayList<T> list = (ArrayList<T>) this.table[hash];
		
		if (list == null) {
			list = new ArrayList<T>();
			list.add(element);
			this.table[hash] = list;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).hashCode() == element.hashCode()) {
					list.set(i, element);
					return;}
			}
			this.COLLISIONS += 1;
			list.add(element);
		}
		this.elements += 1;
	}

	@Override
	public void remove(T element) {
		int hash = ((HashFunctionClosedAddress<T>) this.hashFunction).hash(element);
		ArrayList<T> list = (ArrayList<T>) this.table[hash];

		if (list != null) {
			if (list.size() > 1) {this.COLLISIONS -=1;}
			for (T e: list) {
				if (e.equals(element)) {
					list.remove(element);
				}
			}
			this.elements -= 1;	
		}
	}

	@Override
	public T search(T element) {
		int hash = ((HashFunctionClosedAddress<T>) this.hashFunction).hash(element);
		ArrayList<T> list = (ArrayList<T>) this.table[hash];
		T out = null;

		if (list != null) {
			for (T e: list) {
				if (e.equals(element)) {
					out = e;
				}
			}
		}
		return out;
	}

	@Override
	public int indexOf(T element) {
		int hash = ((HashFunctionClosedAddress<T>) this.hashFunction).hash(element);
		ArrayList<T> list = (ArrayList<T>) this.table[hash];
		int index = -1;

		if (list != null) {
			for (int i = 0; i < list.size(); i++){
				if (list.get(i) == element) {
					index = i;
				}
			}
		}
		
		return index;
	} 

}
