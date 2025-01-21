package basics.graph;

public record GraphEdge<T>(T weight, GraphNode<T> from, GraphNode<T> to) {}
