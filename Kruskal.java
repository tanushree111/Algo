package com.bin;

class Vertex {
	int rank;
	Vertex rep;
	int name;
	NeighbourLinkedList adj;

	Vertex(int name) {
		this.name = name;
		rep = this; 
	}
}

class NeighbourLinkedList {
	int index;
	NeighbourLinkedList next;
	int weight;

	NeighbourLinkedList(int index, int weight, NeighbourLinkedList next) {
		this.index = index;
		this.weight = weight;
		this.next = next;
	}
}

class EdgeLinkedList {
	Vertex src;
	Vertex desti;
	EdgeLinkedList next;
	int weight;

	EdgeLinkedList(Vertex src, Vertex desti, int weight, EdgeLinkedList next) {
		this.src = src;
		this.desti = desti;
		this.weight = weight;
		this.next = next;
	}
}

class Graph {
	Vertex[] vertices;
	EdgeLinkedList edgeList;
	int maxSize;
	int size;
	int edgeNum;

	public Graph(int maxSize) {
		this.maxSize = maxSize;
		vertices = new Vertex[maxSize];
	}

	public void addVertex(int name) {
		vertices[size++] = new Vertex(name);
	}

	public void addEdge(int src, int dest, int weight) {
		vertices[src - 1].adj = new NeighbourLinkedList(dest - 1, weight, vertices[src - 1].adj);
		edgeList = new EdgeLinkedList(vertices[src - 1], vertices[dest - 1], weight, edgeList);
		edgeNum++;
	}

	public void krushkalMST() {
		EdgeLinkedList[] edges = new EdgeLinkedList[edgeNum];
		int i = 0;
		while (edgeList != null) {
			edges[i] = edgeList;
			i++;
			edgeList = edgeList.next;
		}
		quicksort(edges, 0, edgeNum - 1);
		for (i = 0; i < edgeNum; i++) {
			Vertex u = findSet(edges[i].src);
			Vertex v = findSet(edges[i].desti);
			if (u != v) {
				System.out.println(edges[i].src.name + " - " + edges[i].desti.name + " weight " + edges[i].weight);
				setUnion(u, v);
			}
		}
	}

	public Vertex findSet(Vertex u) {
		if (u.rep != u) {
			u.rep = findSet(u.rep); 
		}
		return u.rep;
	}

	public void setUnion(Vertex u, Vertex v) {
		if (u.rank == v.rank) {
			v.rep = u;
			u.rank++;
		} else if (u.rank < v.rank) {
			u.rep = v;
		} else {
			v.rep = u;
		}
	}

	public void quicksort(EdgeLinkedList[] edges, int start, int end) {
		if (start < end) {
			switchEdges(edges, end, start + (end - start) / 2);
			int pIndex = pivot(edges, start, end);
			quicksort(edges, start, pIndex - 1);
			quicksort(edges, pIndex + 1, end);
		}
	}

	public int pivot(EdgeLinkedList[] edges, int start, int end) {
		int pIndex = start;
		EdgeLinkedList pivot = edges[end];
		for (int i = start; i < end; i++) {
			if (edges[i].weight < pivot.weight) {
				switchEdges(edges, i, pIndex);
				pIndex++;
			}
		}
		switchEdges(edges, end, pIndex);
		return pIndex;
	}

	public void switchEdges(EdgeLinkedList[] edges, int index1, int index2) {
		EdgeLinkedList temp = edges[index1];
		edges[index1] = edges[index2];
		edges[index2] = temp;
	}
}

public class Kruskal {

	public static void main(String[] args) {

		Graph g = new Graph(4);
		g.addVertex(1);
		g.addVertex(2);
		g.addVertex(3);
		g.addVertex(4);
		g.addEdge(1, 2, 10);
		g.addEdge(1, 3, 6);
		g.addEdge(1, 4, 5);
		g.addEdge(3, 4, 4);
		g.addEdge(2, 4, 15);
		g.krushkalMST();
	}

}
