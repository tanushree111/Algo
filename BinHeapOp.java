package com.bin;

import java.util.Scanner;

class BinNode {
	int key;
	int degree;
	BinNode parent;
	BinNode child;
	BinNode sibling;

	public BinNode(int k) {
		key = k;
		degree = 0;
		parent = null;
		sibling = null;
		child = null;
	}

	public BinNode reverse(BinNode sib) {
		BinNode ret;
		if (sibling != null)
			ret = sibling.reverse(this);
		else
			ret = this;
		sibling = sib;
		return ret;
	}

	public BinNode findMinNode() {
		BinNode x = this, y = this;
		int min = x.key;

		while (x != null) {
			if (x.key < min) {
				y = x;
				min = x.key;
			}
			x = x.sibling;
		}

		return y;
	}

	public BinNode findANodeWithKey(int value) {
		BinNode temp = this, node = null;

		while (temp != null) {
			if (temp.key == value) {
				node = temp;
				break;
			}
			if (temp.child == null)
				temp = temp.sibling;
			else {
				node = temp.child.findANodeWithKey(value);
				if (node == null)
					temp = temp.sibling;
				else
					break;
			}
		}

		return node;
	}

	public int getSize() {
		return (1 + ((child == null) ? 0 : child.getSize()) + ((sibling == null) ? 0 : sibling.getSize()));
	}
}

class BinomialHeap {
	private BinNode head;
	private int size;

	/**
	 * @return the head
	 */
	public BinNode getHead() {
		return head;
	}

	/**
	 * @param head
	 *            the head to set
	 */
	public void setHead(BinNode head) {
		this.head = head;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	public BinomialHeap() {
		head = null;
		size = 0;
	}

	public boolean isEmpty() {
		return head == null;
	}

	public int getSize() {
		return size;
	}

	// make heap - creates an empty heap
	public void clear() {
		head = null;
		size = 0;
	}

	// insert - inserts a value
	public void insert(int value) {
		if (value > 0) {
			BinNode temp = new BinNode(value);
			if (head == null) {
				head = temp;
				size = 1;
			} else {
				unionNodes(temp);
				size++;
			}
		}
	}

	public void merge(BinNode binHeap) {
		BinNode temp1 = head, temp2 = binHeap;

		while ((temp1 != null) && (temp2 != null)) {
			if (temp1.degree == temp2.degree) {
				BinNode tmp = temp2;
				temp2 = temp2.sibling;
				tmp.sibling = temp1.sibling;
				temp1.sibling = tmp;
				temp1 = tmp.sibling;
			} else {
				if (temp1.degree < temp2.degree) {
					if ((temp1.sibling == null) || (temp1.sibling.degree > temp2.degree)) {
						BinNode tmp = temp2;
						temp2 = temp2.sibling;
						tmp.sibling = temp1.sibling;
						temp1.sibling = tmp;
						temp1 = tmp.sibling;
					} else {
						temp1 = temp1.sibling;
					}
				} else {
					BinNode tmp = temp1;
					temp1 = temp2;
					temp2 = temp2.sibling;
					temp1.sibling = tmp;
					if (tmp == head) {
						head = temp1;
					} else {

					}
				}
			}
		}
		if (temp1 == null) {
			temp1 = head;
			while (temp1.sibling != null) {
				temp1 = temp1.sibling;
			}
			temp1.sibling = temp2;
		}
	}

	/* Function for union of nodes */
	void unionNodes(BinNode binHeap) {
		merge(binHeap);

		BinNode prevTemp = null, temp = head, nextTemp = head.sibling;

		while (nextTemp != null) {
			if ((temp.degree != nextTemp.degree)
					|| ((nextTemp.sibling != null) && (nextTemp.sibling.degree == temp.degree))) {
				prevTemp = temp;
				temp = nextTemp;
			} else {
				if (temp.key <= nextTemp.key) {
					temp.sibling = nextTemp.sibling;
					nextTemp.parent = temp;
					nextTemp.sibling = temp.child;
					temp.child = nextTemp;
					temp.degree++;
				} else {
					if (prevTemp == null) {
						head = nextTemp;
					} else {
						prevTemp.sibling = nextTemp;
					}
					temp.parent = nextTemp;
					temp.sibling = nextTemp.child;
					nextTemp.child = temp;
					nextTemp.degree++;
					temp = nextTemp;
				}
			}
			nextTemp = temp.sibling;
		}
	}

	/* Function to return minimum key */
	public int minimum() {
		return head.findMinNode().key;
	}

	/* Function to delete a particular element */
	public void delete(int value) {
		if ((head != null) && (head.findANodeWithKey(value) != null)) {
			decreaseKeyValue(value, minimum() - 1);
			extractMin();
		}
	}

	/* Function to decrease key with a given value */
	public void decreaseKeyValue(int old_value, int new_value) {
		BinNode temp = head.findANodeWithKey(old_value);
		if (temp == null)
			return;
		temp.key = new_value;
		BinNode tempParent = temp.parent;

		while ((tempParent != null) && (temp.key < tempParent.key)) {
			int z = temp.key;
			temp.key = tempParent.key;
			tempParent.key = z;

			temp = tempParent;
			tempParent = tempParent.parent;
		}
	}

	/* Function to extract the node with the minimum key */
	public int extractMin() {
		if (head == null)
			return -1;

		BinNode temp = head, prevTemp = null;
		BinNode minNode = head.findMinNode();

		while (temp.key != minNode.key) {
			prevTemp = temp;
			temp = temp.sibling;
		}

		if (prevTemp == null) {
			head = temp.sibling;
		} else {
			prevTemp.sibling = temp.sibling;
		}

		temp = temp.child;
		BinNode fakeNode = temp;

		while (temp != null) {
			temp.parent = null;
			temp = temp.sibling;
		}

		if ((head == null) && (fakeNode == null)) {
			size = 0;
		} else {
			if ((head == null) && (fakeNode != null)) {
				head = fakeNode.reverse(null);
				size = head.getSize();
			} else {
				if ((head != null) && (fakeNode == null)) {
					size = head.getSize();
				} else {
					unionNodes(fakeNode.reverse(null));
					size = head.getSize();
				}
			}
		}

		return minNode.key;
	}

	/* Function to display heap */
	public void displayHeap() {
		System.out.print("\nHeap : ");
		displayHeap(head);
		System.out.println("\n");
	}

	private void displayHeap(BinNode r) {
		String par = "nil";
		String child = "nil";
		String sib = "nil";
		if (r != null) {
			displayHeap(r.child);
			if(r.parent != null){
				par = "" + r.parent.key;
			}
			if(r.child != null){
				child = "" + r.child.key;
			}
			if(r.sibling != null){
				sib = "" + r.sibling.key;
			}
			System.out.print("Node value : " + r.key + "(Par : " + par + " Child : " + child + " Sib : "+ sib + " )\n");
			displayHeap(r.sibling);
		}
	}
}

/* Class BinomialHeapTest */
public class BinHeapOp {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		BinomialHeap binHeap = new BinomialHeap();

		char ch;
		do {
			System.out.println("\n Operations for Binomial Heap\n");
			System.out.println("1. make-heap");
			System.out.println("2. insert ");
			System.out.println("3. minimum ");
			System.out.println("4. extract-min ");
			System.out.println("5. union ");
			System.out.println("6. decrease-key ");
			System.out.println("7. delete ");
			System.out.println("8. clear");

			int choice = sc.nextInt();
			switch (choice) {
			case 1:
				System.out.println("Make-heap");
				binHeap = makeHeap();
				break;
			case 2:
				System.out.println("Enter integer element to insert");
				binHeap.insert(sc.nextInt());
				System.out.println("Result after insertion :");
				binHeap.displayHeap();
				break;
			case 3:
				System.out.println("Minimum :" + binHeap.minimum());
				System.out.println("Result after minimum :");
				binHeap.displayHeap();
				break;
			case 4:
				System.out.println("Extracted min :" + binHeap.extractMin());
				System.out.println("Result after extract min :");
				binHeap.displayHeap();
				break;
			case 5:
				System.out.println("Union");
				BinomialHeap binHeap2 = makeHeap();
				Scanner sc2 = new Scanner(System.in);
				System.out.println("Enter elements for heap2 in separate lines (type s to stop) :");
				while (sc2.hasNextInt()) {
					binHeap2.insert(sc2.nextInt());
				}
				System.out.println("Heap1 :");
				binHeap.displayHeap();
				System.out.println("Heap2 :");
				binHeap2.displayHeap();
				binHeap.unionNodes(binHeap2.getHead());
				System.out.println("Result after union :");
				binHeap.displayHeap();
				break;
			case 6:
				System.out.println("Enter old and new value of node to be decreased : ");
				if (sc.hasNextInt()) {
					int old_value = sc.nextInt();
					if (sc.hasNextInt()) {
						int new_value = sc.nextInt();
						binHeap.decreaseKeyValue(old_value, new_value);
						System.out.println("Result after decrease key :");
						binHeap.displayHeap();
					} else {
						System.out.println("New value not entered");
					}
				} else {
					System.out.println("Old value not entered");
				}

				break;
			case 7:
				System.out.println("Enter element to be deleted ");
				binHeap.delete(sc.nextInt());
				System.out.println("Result after deletion :");
				binHeap.displayHeap();
				break;
			case 8:
				binHeap.clear();
				System.out.println("Heap Cleared\n");
				break;
			default:
				System.out.println("Wrong Entry \n ");
				break;
			}
			

			System.out.println("\nDo you want to continue (Type y or n) \n");
			ch = sc.next().charAt(0);
		} while (ch == 'Y' || ch == 'y');
		sc.close();
	}

	private static BinomialHeap makeHeap() {
		return new BinomialHeap();
	}
}