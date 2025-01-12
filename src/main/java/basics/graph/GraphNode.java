package basics.graph;

import java.util.Objects;

public record GraphNode<T> (T data) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GraphNode<?> graphNode)) return false;
        return Objects.equals(data, graphNode.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }
}
