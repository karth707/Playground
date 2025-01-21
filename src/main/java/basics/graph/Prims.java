package basics.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Prims<T> {

  private final Map<GraphNode<Integer>, List<AdjNode>> graph;

  public Prims(List<GraphEdge<Integer>> edges) {
    this.graph = buildGraph(edges);
  }

  public Set<GraphEdge<Integer>> buildMST() {
    Map<GraphNode<Integer>, Integer> distanceMap = new HashMap<>();
    PriorityQueue<QueueRecord> queue =
        new PriorityQueue<>(Comparator.comparingInt(QueueRecord::distance));

    // init initial state and pick the first node
    boolean startingNode = false;
    for (GraphNode<Integer> node : graph.keySet()) {
      int distance = Integer.MAX_VALUE;
      distanceMap.putIfAbsent(node, distance);
      if (!startingNode) {
        distance = 0;
        distanceMap.put(node, distance);
        startingNode = true;
      }
      queue.offer(new QueueRecord(null, node, distance));
    }

    // Process nodes greedily based on curr distance from starting node
    Set<GraphNode<Integer>> mstNodes = new HashSet<>();
    Set<GraphEdge<Integer>> mst = new HashSet<>();
    while (!queue.isEmpty()) {
      QueueRecord top = queue.poll();
      GraphNode<Integer> parent = top.parent();
      GraphNode<Integer> node = top.node();
      int currDist = top.distance();

      // Already part of MST
      if (mstNodes.contains(node)) continue;

      // Add to MST
      mstNodes.add(node);
      if (parent != null) {
        for (AdjNode adjNode : graph.get(parent)) {
          if (adjNode.node().equals(node)) {
            mst.add(new GraphEdge<>(adjNode.weight(), parent, node));
            break;
          }
        }
      }

      // Relax the adj node distances
      for (AdjNode adj : graph.get(node)) {
        int adjDist = distanceMap.get(adj.node());
        if (adjDist > currDist + adj.weight()) {
          distanceMap.put(adj.node(), currDist + adj.weight());
          queue.offer(new QueueRecord(node, adj.node(), distanceMap.get(adj.node())));
        }
      }
    }

    if (mst.size() != graph.size() - 1) {
      throw new IllegalStateException("Could not build a Minimum Spanning Tree");
    }

    return mst;
  }

  private Map<GraphNode<Integer>, List<AdjNode>> buildGraph(List<GraphEdge<Integer>> edges) {
    // Build Graph from edges
    Map<GraphNode<Integer>, List<AdjNode>> graph = new HashMap<>();
    for (GraphEdge<Integer> e : edges) {
      GraphNode<Integer> from = e.from();
      GraphNode<Integer> to = e.to();
      graph.putIfAbsent(from, new ArrayList<>());
      graph.putIfAbsent(to, new ArrayList<>());
      graph.get(from).add(new AdjNode(to, e.weight()));
      graph.get(to).add(new AdjNode(from, e.weight()));
    }
    return graph;
  }

  private record AdjNode(GraphNode<Integer> node, int weight) {}
  ;

  private record QueueRecord(GraphNode<Integer> parent, GraphNode<Integer> node, int distance) {}
  ;
}
