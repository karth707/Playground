package basics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UnionFind<T> {

  private final Map<T, Node<T>> nodeMap;
  private final Set<Node<T>> representatives;

  public UnionFind() {
    this.nodeMap = new HashMap<>();
    this.representatives = new HashSet<>();
  }

  /** Makes a new disjoint set */
  public void makeSet(T data) {
    Node<T> node = new Node<>(data);
    nodeMap.put(data, node);
    representatives.add(node);
  }

  /** Merges the sets representing the incoming data */
  public void union(T data1, T data2) {
    Node<T> rep1 = find(nodeMap.get(data1));
    Node<T> rep2 = find(nodeMap.get(data2));

    if (rep1.getRank() == rep2.getRank()) {
      rep1.setRank(rep1.getRank() + 1);
      rep2.setParent(rep1);
      representatives.remove(rep2);
    } else if (rep1.getRank() > rep2.getRank()) {
      rep2.setParent(rep1);
      representatives.remove(rep2);
    } else {
      rep1.setParent(rep2);
      representatives.remove(rep1);
    }
  }

  /**
   * Finds the representative set for the data Also does path compression for the children to refer
   * the parent
   */
  private Node<T> find(Node<T> node) {
    Node<T> parent = node.getParent();
    if (parent.equals(node)) {
      return parent;
    }
    // Path compression
    node.parent = find(parent);
    return node.getParent();
  }

  public T findSet(T data) {
    return find(nodeMap.get(data)).getData();
  }

  public int getSize() {
    return representatives.size();
  }

  public Set<T> getRepresentatives() {
    return representatives.stream().map(Node::getData).collect(Collectors.toSet());
  }

  private static class Node<T> {

    private T data;
    private Node<T> parent;
    private int rank;

    public Node(T data) {
      this.data = data;
      this.parent = this;
      this.rank = 0;
    }

    public T getData() {
      return data;
    }

    public void setData(T data) {
      this.data = data;
    }

    public Node<T> getParent() {
      return parent;
    }

    public void setParent(Node<T> parent) {
      this.parent = parent;
    }

    public int getRank() {
      return rank;
    }

    public void setRank(int rank) {
      this.rank = rank;
    }
  }

  public static void main(String[] args) {
    UnionFind<Integer> unionFind = new UnionFind<>();
    for (int data = 1; data < 8; data++) {
      unionFind.makeSet(data);
    }
    unionFind.union(1, 5);
    unionFind.union(4, 6);
    unionFind.union(5, 7);
    unionFind.union(2, 3);

    System.out.println("Total Sets: " + unionFind.getSize());
    System.out.println("Representatives: " + unionFind.getRepresentatives());
  }
}
