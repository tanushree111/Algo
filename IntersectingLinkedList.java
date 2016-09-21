/**
 * 
 */
package com.adt;

/**
 * @author Tanushree
 *
 */

class Node{
	private int data;
	private Node next;

	/**
	 * @param data
	 * @param next
	 */
	public Node(int data) {
		this.data = data;
		this.next = null;
	}
	
	/**
	 * @return the data
	 */
	public int getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(int data) {
		this.data = data;
	}
	/**
	 * @return the next
	 */
	public Node getNext() {
		return next;
	}
	/**
	 * @param next the next to set
	 */
	public void setNext(Node next) {
		this.next = next;
	}
}

class LinkedList {
	
	private Node head = null;

	/**
	 * @return the head
	 */
	public Node getHead() {
		return head;
	}

	//insert a new node at start
	public void insert(Node newNode){
		newNode.setNext(this.head);
		this.head = newNode;
	}
	
	//remove a node from start
	public Node remove(){
		Node rmNode = this.head;
		head = head.getNext();
		return rmNode;
	}
	
	//traverse through the list and print values
	public void printData(){
		Node current = head; 
		while(current != null){
			System.out.println("node value = " + current.getData());
			current = current.getNext();
		}
	}
 
}
public class IntersectingLinkedList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Initialize lists
		LinkedList list1 = new LinkedList();
		LinkedList list2 = new LinkedList();
		
		//Initialize nodes
		Node node1 = new Node(5);
		Node node2 = new Node(3);
		Node node3 = new Node(13);
		Node node4 = new Node(2);
		Node node5 = new Node(9);
		Node node6 = new Node(20);
		
		//insert nodes
		list1.insert(node1);

		
		list2.insert(node1);
		
		
		//print lists after insertion
		System.out.println("The list1 is :");
		list1.printData();
		System.out.println("The list2 is :");
		list2.printData();
		
		Node intersectNode = findIntersection(list1.getHead(), list2.getHead());
		
		if(intersectNode != null){
        System.out.println("The intersecting node is " + intersectNode.getData());
		}else{
			System.out.println("Intersecting node not found");
		}
	}
	public static Node findIntersection(Node head1, Node head2) {
		int list1Count = 0;
        int list2Count = 0;
        Node node1 = head1;
        Node node2 = head2;
        
        while (node1 != null)
		{
        	list1Count++;
        	node1 = node1.getNext();
		}	
        
        while (node2 != null)
		{
        	list2Count++;
        	node2 = node2.getNext();
		}
        
        int diff;
 
        if (list1Count > list2Count){
            diff = list1Count - list2Count;
            node1 = head1;
            node2 = head2;
        }
        else{
        	diff = list2Count - list1Count;
        	node1 = head2;
        	node2 = head1;
        }
        
        // Loop till the difference node
        for(int i=0; i<diff; i++){
        	if(node1 != null )
        		node1 = node1.getNext();
        	else
        		return null;
        }
        
        //traverse simultaneously
        while(node1 != null && node2 != null){
        	if(node1.getData() == node2.getData())
        	{
        		return node1;
        	}
        	node1 = node1.getNext();
        	node2 = node2.getNext();
        }
        return null;
    }
}

