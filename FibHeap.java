package com.fib;

import java.util.*;

//FibonacciHeapNode
class FibHeapNode {
	int key, degree;
	boolean mark;
	FibHeapNode child, left, right, parent;

	public FibHeapNode(int element) {
		this.key = element;
		this.right = this;
		this.left = this;
	}
}

// FibonacciHeap
class FibonacciHeap {
	private FibHeapNode min;
	private int count;

	public FibonacciHeap() {
		setMin(null);
		count = 0;
	}

	public boolean isEmpty() {
		return getMin() == null;
	}

	public void clear() {
		setMin(null);
		count = 0;
	}

	public void insert(int val) {
		FibHeapNode fhNode = new FibHeapNode(val);
		if (getMin() == null)
			setMin(fhNode);
		else {
			fhNode.left = getMin();
			fhNode.right = getMin().right;
			getMin().right = fhNode;
			fhNode.right.left = fhNode;
			if (val < getMin().key)
				setMin(fhNode);
		}
		count++;
	}

	public FibonacciHeap union(FibonacciHeap fh) {
		FibonacciHeap unionHeap = new FibonacciHeap();
		unionHeap.setMin(this.getMin());
		if (unionHeap.getMin() != null) {
			if (fh.getMin() != null) {
				unionHeap.getMin().right.left = fh.getMin().left;
				fh.getMin().left.right = unionHeap.getMin().right;
				unionHeap.getMin().right = fh.getMin();
				fh.getMin().left = unionHeap.getMin();
			}
		}
		if ((this.getMin() == null) || (fh.getMin() != null && fh.getMin().key < this.getMin().key)) {
			unionHeap.setMin(fh.getMin());
		}
		unionHeap.count = this.count + fh.count;
		return unionHeap;

	}

	public FibHeapNode extractMin() {
		FibHeapNode z = this.getMin();
		if (z != null) {
			int zDeg = z.degree;
			FibHeapNode x = z.child;
			FibHeapNode xRight;

			// repeat for all children
			while (zDeg > 0) {
				xRight = x.right;

				// remove x from child list
				x.left.right = x.right;
				x.right.left = x.left;

				// add x to root list of heap
				x.left = this.getMin();
				x.right = this.getMin().right;
				this.getMin().right = x;
				x.right.left = x;

				x.parent = null;
				x = xRight;
				zDeg--;
			}

			// remove z from root list
			z.left.right = z.right;
			z.right.left = z.left;

			if (z == z.right) {
				this.setMin(null);
			} else {
				this.setMin(z.right);
				// consolidate
				this.consolidate();
			}

			this.count--;
		}
		return z;
	}

	public void consolidate() {
		double p = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);
		int len = ((int) Math.floor(Math.log(count) * p)) + 1;

		List<FibHeapNode> fibNodes = new ArrayList<FibHeapNode>(len);

		for (int i = 0; i < len; i++) {
			fibNodes.add(null);
		}

		// Iterate on right nodes of min node to find size of all root nodes.
		int rootNodeSize = 0;
		FibHeapNode x = this.getMin();

		if (x != null) {
			rootNodeSize++;
			x = x.right;

			while (x != this.getMin()) {
				rootNodeSize++;
				x = x.right;
			}
		}

		while (rootNodeSize > 0) {
			int d = x.degree;
			FibHeapNode w = x.right;

			for (;;) {
				FibHeapNode y = fibNodes.get(d);
				if (y == null) {
					break;
				}

				// one node becomes child of the other.
				if (x.key > y.key) {
					FibHeapNode temp = y;
					y = x;
					x = temp;
				}

				link(y, x);

				fibNodes.set(d, null);
				d++;
			}
			// set value for this degree
			fibNodes.set(d, x);

			// Move to next node in root list.
			x = w;
			rootNodeSize--;
		}

		// recalculate min
		this.setMin(null);

		for (int i = 0; i < len; i++) {
			FibHeapNode y = fibNodes.get(i);
			if (y == null) {
				continue;
			}

			if (this.getMin() != null) {
				y.left.right = y.right;
				y.right.left = y.left;

				y.left = this.getMin();
				y.right = this.getMin().right;
				this.getMin().right = y;
				y.right.left = y;

				if (y.key < this.getMin().key) {
					this.setMin(y);
				}
			} else {
				this.setMin(y);
			}
		}

	}

	private void link(FibHeapNode y, FibHeapNode x) {
		y.left.right = y.right;
		y.right.left = y.left;

		// make y a child of x
		y.parent = x;

		if (x.child == null) {
			x.child = y;
			y.right = y;
			y.left = y;
		} else {
			y.left = x.child;
			y.right = x.child.right;
			x.child.right = y;
			y.right.left = y;
		}

		x.degree++;

		y.mark = false;
	}

	public void decreaseKey(FibHeapNode x, int key) {
		if (key > x.key) {
			throw new IllegalArgumentException("new key is greater than current key");
		}
		x.key = key;
		FibHeapNode y = x.parent;
		if ((y != null) && (x.key < y.key)) {
			cut(x, y);
			cascadingCut(y);
		}
		if (x.key < this.getMin().key) {
			this.setMin(x);
		}
	}

	private void cascadingCut(FibHeapNode y) {
		FibHeapNode z = y.parent;
		if (z != null) {
			if (!y.mark) {
				y.mark = true;
			} else {
				cut(y, z);
				cascadingCut(z);
			}
		}
	}

	private void cut(FibHeapNode x, FibHeapNode y) {
		x.left.right = x.right;
		x.right.left = x.left;
		y.degree--;

		// reset y.child if necessary
		if (y.child == x) {
			y.child = x.right;
		}

		if (y.degree == 0) {
			y.child = null;
		}

		x.left = this.getMin();
		x.right = this.getMin().right;
		this.getMin().right = x;
		x.right.left = x;

		x.parent = null;
		x.mark = false;
	}

	public void delete(FibHeapNode x) {
		decreaseKey(x, (int) Double.NEGATIVE_INFINITY);
		extractMin();
	}

	public void printHeap(FibHeapNode fibHeapNode) {
		String par = "nil";
		System.out.print("\nHeap = ");
		FibHeapNode ptr = fibHeapNode;
		if (ptr == null) {
			System.out.print("Empty\n");
			return;
		}
		do {
			if (ptr.parent != null) {
				par = "" + ptr.parent.key;
			}
			System.out.print(ptr.key + "(Par : " + par + " Degree : " + ptr.degree + " Marked? : " + ptr.mark + ")\n");
			if(ptr.child != null)
			{
			printHeap(ptr.child);
			}
			ptr = ptr.right;
		} while (ptr != getMin() && ptr.right != null);
		System.out.println();
	}

	public void printChildren(FibHeapNode ptr) {
		System.out.print("\n Children of " + ptr.key + " : ");
		//ptr = ptr.child;
		if (ptr != null) {
			do {
				System.out.print(ptr.key + "(Marked? : " + ptr.mark + ")\n");
				if (ptr.child != null) {
					printChildren(ptr);
				}
				ptr = ptr.right;
			} while (ptr.right != null);
			System.out.println();
		} 

	}

	public FibHeapNode getMin() {
		return min;
	}

	public void setMin(FibHeapNode min) {
		this.min = min;
	}
}

public class FibHeap {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("FibonacciHeap \n\n");
		FibonacciHeap fh = new FibonacciHeap();

		char ch;
		do {
			System.out.println("\nOperations on FibonacciHeap\n");
			System.out.println("1. insert element ");
			System.out.println("2. extract min");
			System.out.println("3. union");
			System.out.println("4. consolidate");
			System.out.println("5. decrease key");
			System.out.println("6. delete");
			System.out.println("7. clear");

			int choice = sc.nextInt();
			FibHeapNode x;
			switch (choice) {
			case 1:
				System.out.println("Enter element");
				fh.insert(sc.nextInt());
				System.out.println("Result after insertion : ");
				fh.printHeap(fh.getMin());
				break;
			case 2:
				x = fh.extractMin();
				if (x != null) {
					System.out.println("Extracted min : " + x.key);
				} else {
					System.out.println("Extracted min unavailable");
				}
				break;
			case 3:
				System.out.println("Creating heap2 for union with heap1: ");
				FibonacciHeap fh2 = new FibonacciHeap();
				Scanner sc2 = new Scanner(System.in);
				System.out.println("Enter elements for heap2 in separate lines (type s to stop) :");
				while (sc2.hasNextInt()) {
					fh2.insert(sc2.nextInt());
				}
				System.out.println("Heap1 :");
				fh.printHeap(fh.getMin());
				System.out.println("Heap2 :");
				fh2.printHeap(fh2.getMin());
				FibonacciHeap unionFh = fh.union(fh2);
				System.out.println("Result after union : ");
				unionFh.printHeap(unionFh.getMin());
				break;
			case 4:
				System.out.println("Result after consolidation : ");
				fh.consolidate();
				fh.printHeap(fh.getMin());
				break;
			case 5:
				System.out.println("Enter node and value to be decreased :");
				if (sc.hasNextInt()) {
					x = new FibHeapNode(sc.nextInt());

					if (sc.hasNextInt()) {
						fh.decreaseKey(x, sc.nextInt());
						System.out.println("Result after decrease key : ");
						fh.printHeap(fh.getMin());
					} else {
						System.out.println("Value to be decreased isn't entered");
					}
				} else {
					System.out.println("Node to be decreased isn't entered");
				}
				break;
			case 6:
				System.out.println("Enter node to be deleted :");
				if (sc.hasNextInt()) {
					x = new FibHeapNode(sc.nextInt());
					System.out.println("Result after deletion = ");
					fh.delete(x);
					fh.printHeap(fh.getMin());
				} else {
					System.out.println("Node to be decreased isn't entered");
				}
				break;
			case 7:
				fh.clear();
				break;
			default:
				System.out.println("Wrong Entry \n ");
				break;
			}
			fh.printHeap(fh.getMin());

			System.out.println("\nDo you want to continue (Type y or n) \n");
			ch = sc.next().charAt(0);
		} while (ch == 'Y' || ch == 'y');

		sc.close();
	}
}