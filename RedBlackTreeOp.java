/**
 * 
 */
package com.rbt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * @author Tanushree
 *
 */

/* Class Node */
class RedBlackNode {
	int key;
	boolean color; // true black and false for red
	RedBlackNode left, right;
	RedBlackNode parent;

	/* Constructor */
	public RedBlackNode(int val) {
		this(val, null, null);
	}

	/* Constructor */
	public RedBlackNode(int val, RedBlackNode left, RedBlackNode right) {
		this.key = val;
		this.color = true;
		this.left = left;
		this.right = right;
	}
}

class RedBlackTree {

	private RedBlackNode root;

	/* Constructor */
	public RedBlackTree() {
		setRoot(null);
	}

	/* Function to insert item */
	public void insert(int val) {
		RedBlackNode newNode = new RedBlackNode(val, null, null);
		RedBlackNode x = getRoot();
		RedBlackNode y = null;
		//loop till u reach where x has to be inserted, with another trailing pointer y
		while (x != null) {
			y = x;
			if (val < x.key)
				x = x.left;
			else
				x = x.right;
		}

		newNode.parent = y;
		// check against y
		if (y == null) {
			setRoot(newNode);
		} else if (val < y.key) {
			y.left = newNode;
		} else {
			y.right = newNode;
		}
		//color it Red
		newNode.color = false;

		insertFixup(newNode);
	}

	private void insertFixup(RedBlackNode node) {
		RedBlackNode uncle;
		while (node.parent != null && !node.parent.color) {
			if (node.parent == node.parent.parent.left) {
				uncle = node.parent.parent.right;
				//z’s uncle y is red
				if (uncle != null && !uncle.color) {
					node.parent.color = true;
					uncle.color = true;
					node.parent.parent.color = false;
					node = node.parent.parent;
				}
				//z's uncle y is black and  z is a right child
				else {
					if (node == node.parent.right) {
						node = node.parent;
						leftRotate(node);
					}
					//z’s uncle y is black and ź is a left child
					node.parent.color = true;
					node.parent.parent.color = false;
					rightRotate(node.parent.parent);
				}
			} else {
				uncle = node.parent.parent.left;
				if (uncle != null && !uncle.color) {
					node.parent.color = true;
					uncle.color = true;
					node.parent.parent.color = false;
					node = node.parent.parent;
				} else {
					if (node == node.parent.left) {
						node = node.parent;
						rightRotate(node);
					}
					node.parent.color = true;
					node.parent.parent.color = false;
					leftRotate(node.parent.parent);
				}
			}
		}
		getRoot().color = true;
	}

	private void leftRotate(RedBlackNode node) {
		//set nodeRight(book's y)
		RedBlackNode nodeRight = node.right;
		//turn y’s left subtree into x’s right subtree
		node.right = nodeRight.left;
		if (nodeRight.left != null) {
			nodeRight.left.parent = node;
		}
		//linkx’s parent to y
		nodeRight.parent = node.parent;
		if (node.parent == null) {
			setRoot(nodeRight);
		} else if (node == node.parent.left) {
			node.parent.left = nodeRight;
		} else {
			node.parent.right = nodeRight;
		}
		//put x on y’sleft
		nodeRight.left = node;
		node.parent = nodeRight;
	}

	private void rightRotate(RedBlackNode node) {
		//set nodeLeft(book's y)
		RedBlackNode nodeLeft = node.left;
		//turn y’s right subtree into x’s left subtree
		node.left = nodeLeft.right;
		if (nodeLeft.right != null) {
			nodeLeft.right.parent = node;
		}
		//linkx’s parent to y
		nodeLeft.parent = node.parent;
		if (node.parent == null) {
			setRoot(nodeLeft);
		} else if (node == node.parent.right) {
			node.parent.right = nodeLeft;
		} else {
			node.parent.left = nodeLeft;
		}
		//put x on y’s right
		nodeLeft.right = node;
		node.parent = nodeLeft;
	}

	/* Functions to search for an element */
	public RedBlackNode search(RedBlackNode root, int val) {
		while ((root != null) && root.key != val) {
			if (val < root.key)
				root = root.left;
			else if (val > root.key)
				root = root.right;
		}
		return root;
	}

	/* Function for inorder traversal */
	public void inorder(RedBlackNode root) {
		String par = "null";
		String lc = "null";
		String rc = "null";
		if (root != null) {
			inorder(root.left);
			char c = 'B';
			if (!root.color)
				c = 'R';
			if (root.parent != null) {
				par = "" + root.parent.key;
				
			} if (root.left != null) {
				lc = "" + root.left.key;
				
			} if (root.right != null) {
				rc = "" + root.right.key;
				
			}
			
			System.out.print(root.key + "" + c + "(Par-" + par  + " Left-" + lc + " Right-" + rc + ")\n");
			inorder(root.right);
		}
	}

	public RedBlackNode minimum(RedBlackNode root) {
		while (root.left != null) {
			root = root.left;
		}
		return root;
	}

	public RedBlackNode maximum(RedBlackNode root) {
		while (root.right != null) {
			root = root.right;
		}
		return root;
	}

	public RedBlackNode successor(RedBlackNode node) {
		if (node.right != null) {
			return minimum(node.right);
		}
		RedBlackNode par = node.parent;
		while (par != null && node == par.right) {
			node = par;
			par = par.parent;
		}
		return par;
	}

	public RedBlackNode predecessor(RedBlackNode node) {
		if (node.left != null) {
			return maximum(node.left);
		}
		RedBlackNode par = node.parent;
		while (par != null && node == par.left) {
			node = par;
			par = par.parent;
		}
		return par;
	}

	public RedBlackNode getRoot() {
		return root;
	}

	public void setRoot(RedBlackNode root) {
		this.root = root;
	}

	public void clear() {
		this.root = null;
	}
}

public class RedBlackTreeOp {
	public static void main(String[] args) {
		// Read data from file
		String path = new java.io.File("").getAbsolutePath();
		String file = path + "/src/com/rbt/rbtinput.txt";
		RedBlackTree rbt = new RedBlackTree();
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line;
			while ((line = br.readLine()) != null) {
				try {
					int val = Integer.parseInt(line);
					rbt.insert(val);
				} catch (NumberFormatException e) {
					System.out.println("ERROR: " + line + " could not be parsed to an integer value");
				}
			}
			br.close();
			rbt.inorder(rbt.getRoot());

			Scanner scan = new Scanner(System.in);
			char ch;
			/* Perform tree operations */
			do {
				System.out.println("\nRed Black Tree Operations\n");
				System.out.println("1. insert ");
				System.out.println("2. search");
				System.out.println("3. inorder traversal");
				System.out.println("4. minimum");
				System.out.println("5. maximum");
				System.out.println("6. successor");
				System.out.println("7. predecessor");
				System.out.println("8. clear tree");

				int choice = scan.nextInt();
				switch (choice) {
				case 1:
					System.out.println("Enter integer element to insert");
					rbt.insert(scan.nextInt());
					System.out.println("Inorder traversal after insert: ");
					rbt.inorder(rbt.getRoot());
					break;
				case 2:
					System.out.println("Enter integer element to search");
					RedBlackNode node = rbt.search(rbt.getRoot(), scan.nextInt());
					System.out.println("Search result : \n");
					if (node != null) {
						System.out.println("Node key : " + node.key
								+ "\nNode color (true for black and false for red) : " + node.color);
					} else {
						System.out.println("Element not found");
					}
					break;
				case 3:
					System.out.println("Inorder traversal : ");
					rbt.inorder(rbt.getRoot());
					break;
				case 4:
					System.out.println("Minimum value : " + rbt.minimum(rbt.getRoot()).key);
					break;
				case 5:
					System.out.println("Maximum value : " + rbt.maximum(rbt.getRoot()).key);
					break;
				case 6:
					System.out.println("Enter integer element whose successor is to be found");
					RedBlackNode nodeS = rbt.search(rbt.getRoot(), scan.nextInt());
					if(nodeS != null){
						RedBlackNode succ = (rbt.successor(nodeS));
						if(succ != null){
							System.out.println("Successor : " + (rbt.successor(nodeS)).key);
						}else{
							System.out.println("Successor not found");
						}
					}else{
						System.out.println("Element not found");
					}
					
					break;
				case 7:
					System.out.println("Enter integer element whose predecessor is to be found");
					RedBlackNode nodeP = rbt.search(rbt.getRoot(), scan.nextInt());
					if(nodeP != null){
						RedBlackNode pred = (rbt.successor(nodeP));
						if(pred != null){
							System.out.println("Predecessor : " + (rbt.predecessor(nodeP)).key);
						}else{
							System.out.println("Predecessor not found");
						}
					}
					else{
						System.out.println("Element not found");
					}
					
					break;
				case 8:
					System.out.println("\nTree Cleared");
					rbt.clear();
					break;
				default:
					System.out.println("Wrong Entry \n ");
					break;
				}

				System.out.println("\nDo you want to continue (Type y or n) \n");
				ch = scan.next().charAt(0);
			} while (ch == 'Y' || ch == 'y');
		} catch (IOException e) {
			System.out.println("ERROR: unable to read file " + file);
			e.printStackTrace();
		}

	}
}