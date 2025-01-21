package basics.graph;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Driver {

  public static void main(String[] args) {

    /*
     * Directed Graph:
     * Dir: 1->2; 3->6; 3->2; 4->3; 4->2; 4->1; 5->4; 5->1; 6->2
     *
     *      {4} - 5 - {3}
     *     / | \       | \
     *   9   |  \      |  8
     *  /    |   \     |   \
     * {5}   1    3    3   {6}
     *  \    |     \   |   /
     *   4   |      \  |  7
     *    \  |       \ | /
     *      {1} - 2 - {2}
     */

    GraphNode<Integer> node1 = new GraphNode<>(1);
    GraphNode<Integer> node2 = new GraphNode<>(2);
    GraphNode<Integer> node3 = new GraphNode<>(3);
    GraphNode<Integer> node4 = new GraphNode<>(4);
    GraphNode<Integer> node5 = new GraphNode<>(5);
    GraphNode<Integer> node6 = new GraphNode<>(6);

    List<GraphEdge<Integer>> edges =
        List.of(
            new GraphEdge<>(9, node5, node4),
            new GraphEdge<>(4, node5, node1),
            new GraphEdge<>(5, node4, node3),
            new GraphEdge<>(3, node4, node2),
            new GraphEdge<>(1, node4, node1),
            new GraphEdge<>(2, node1, node2),
            new GraphEdge<>(8, node3, node6),
            new GraphEdge<>(3, node3, node2),
            new GraphEdge<>(7, node6, node2));

    Kruskal<Integer> kruskal = new Kruskal<>(edges, Comparator.comparingInt(a -> a));
    System.out.println("Running Kruskal for MST:");
    printMST(kruskal.buildMST());

    Prims<Integer> prims = new Prims<>(edges);
    System.out.println("Running Prims for MST:");
    printMST(prims.buildMST());
  }

  private static void printMST(Set<GraphEdge<Integer>> mst) {
    System.out.println("MST Edges: ");
    int mstWeight = 0;
    for (GraphEdge<Integer> mstEdge : mst) {
      System.out.println(
          "{"
              + mstEdge.from().data()
              + "}"
              + " - "
              + mstEdge.weight()
              + " - "
              + "{"
              + mstEdge.to().data()
              + "}");
      mstWeight += mstEdge.weight();
    }
    System.out.println("MST Weight: " + mstWeight);
  }
}
