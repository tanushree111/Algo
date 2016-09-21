package hash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HashEntry {
	String key;
	int value;
	HashEntry next;

	HashEntry(String key, int value) {
		this.key = key;
		this.value = value;
		this.next = null;
	}

	public String getKey() {
		return key;
	}

	public int getValue() {
		return value;
	}
}

class HashTable {
	private final static int TABLE_SIZE = 100;
	private int size;
	private HashEntry[] table;

	public HashTable() {
		size = 0;
		table = new HashEntry[TABLE_SIZE];
		for (int i = 0; i < TABLE_SIZE; i++)
			table[i] = null;
	}

	public void clearTable() {
		for (int i = 0; i < TABLE_SIZE; i++)
			table[i] = null;
	}

	public HashEntry find(String key) {
		int hash = (stringHash(key) % TABLE_SIZE);
		if (table[hash] == null)
			return null;
		else {
			HashEntry entry = table[hash];
			while (entry != null && !entry.key.equals(key))
				entry = entry.next;

			return entry;
		}
	}

	public void increase(String key) {
		HashEntry entry = find(key);
		if (entry != null) {
			entry.value++;
		} else {
			insert(key, 1);
		}

	}

	public void insert(String key, int value) {
		HashEntry entryPresent = find(key);
		if (entryPresent != null) {
			entryPresent.value += value;
		} else {
			int hash = (stringHash(key) % TABLE_SIZE);
			if (table[hash] == null)
				table[hash] = new HashEntry(key, value);
			else {
				HashEntry entry = table[hash];
				while (entry.next != null && !entry.key.equals(key))
					entry = entry.next;
				if (entry.key.equals(key))
					entry.value = value;
				else
					entry.next = new HashEntry(key, value);
			}
			size++;
		}
	}

	public void delete(String key) {
		int hash = (stringHash(key) % TABLE_SIZE);
		if (table[hash] != null) {
			HashEntry prevEntry = null;
			HashEntry entry = table[hash];
			while (entry.next != null && !entry.key.equals(key)) {
				prevEntry = entry;
				entry = entry.next;
			}
			if (entry.key.equals(key)) {
				if (prevEntry == null)
					table[hash] = entry.next;
				else
					prevEntry.next = entry.next;
				size--;
			}
		}
	}

	private int stringHash(String str) {

		int len = str.length();
		int hash = 0, n = 0;

		for (int i = 0; i < len; i++) {
			n = (int) str.charAt(i);
			hash += i * n % 31;
		}
		return hash % TABLE_SIZE;
	}

	public void listAll() {
		for (int i = 0; i < TABLE_SIZE; i++) {
			System.out.print("\nIndex " + (i + 1) + " : ");
			HashEntry entry = table[i];
			while (entry != null) {
				System.out.print(entry.key + " : " + entry.value + "\t");
				entry = entry.next;
			}
		}
	}

}

public class StringHashing {
	public static void main(String[] args) {
		String content = null;
		/* HashTable object */
		HashTable ht = new HashTable();
		try {
			URL website = new URL(
					"http://www.ccs.neu.edu/home/vip/teach/Algorithms/7_hash_RBtree_simpleDS/hw_hash_RBtree/alice_in_wonderland.txt");
			URLConnection connection = website.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuilder response = new StringBuilder();
			String inputLine;

			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);

			in.close();

			content = response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Pattern p = Pattern.compile("\\w+");
		Matcher m = p.matcher(content);

		while (m.find()) {
			ht.insert(m.group(), 1);
		}

		Scanner scan = new Scanner(System.in);

		char ch;
		/* Perform HashTable operations */
		do {
			System.out.println("\nHash Table Operations\n");
			System.out.println("1. insert ");
			System.out.println("2. delete");
			System.out.println("3. increase");
			System.out.println("4. find");
			System.out.println("5. list all keys");
			System.out.println("6. clear");

			int choice = scan.nextInt();
			switch (choice) {
			case 1:
				System.out.println("Enter key and value");
				ht.insert(scan.next(), scan.nextInt());
				break;
			case 2:
				System.out.println("Enter key");
				ht.delete(scan.next());
				break;
			case 3:
				System.out.println("Enter key");
				ht.increase(scan.next());
				System.out.println("Value for entered key increased");
				break;
			case 4:
				System.out.println("Enter key");
				System.out.println("Value = " + (ht.find(scan.next())).value);
				break;
			case 5:
				System.out.println("All keys : ");
				ht.listAll();
				break;
			case 6:
				ht.clearTable();
				System.out.println("Hash Table Cleared\n");
				break;
			default:
				System.out.println("Please enter a valid input \n ");
				break;
			}

			System.out.println("\nDo you want to continue (Type y or n) \n");
			ch = scan.next().charAt(0);
		} while (ch == 'Y' || ch == 'y');

		scan.close();
	}
}
