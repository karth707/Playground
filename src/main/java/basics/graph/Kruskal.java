package basics.graph;

import basics.UnionFind;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Kruskal<T> {

  private final List<GraphEdge<T>> edges;
  private final Comparator<T> weightComparator;
  private final UnionFind<GraphNode<T>> unionFind;

  public Kruskal(List<GraphEdge<T>> edges, Comparator<T> weightComparator) {
    this.edges = edges;
    this.weightComparator = weightComparator;
    this.unionFind = new UnionFind<>();
  }

  public Set<GraphEdge<T>> buildMST() {

    PriorityQueue<GraphEdge<T>> edgeQueue =
        new PriorityQueue<>((e1, e2) -> weightComparator.compare(e1.weight(), e2.weight()));

    Set<GraphNode<T>> nodes = new HashSet<>();
    for (GraphEdge<T> e : edges) {
      nodes.add(e.from());
      nodes.add(e.to());
      edgeQueue.add(e);
    }

    // Make the disjoint sets
    nodes.forEach(unionFind::makeSet);

    Set<GraphEdge<T>> mst = new HashSet<>();
    while (!edgeQueue.isEmpty()) {
      GraphEdge<T> top = edgeQueue.poll();
      GraphNode<T> repFrom = unionFind.findSet(top.from());
      GraphNode<T> repTo = unionFind.findSet(top.to());

      // Nodes in disjoint sets, this edge should be in mst
      if (repFrom != repTo) {
        unionFind.union(repFrom, repTo);
        mst.add(top);
      }
    }

    if (mst.size() != nodes.size() - 1) {
      throw new IllegalStateException("Could not build a Minimum Spanning Tree");
    }

    return mst;
  }
}
