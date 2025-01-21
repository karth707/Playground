package basics;

import java.util.function.BiFunction;

@SuppressWarnings("unchecked")
public class SegmentTree<T> {

  private final Object[] tree; // Segment Tree array
  private final int size; // Size of the original array
  private final BiFunction<T, T, T> segmentFunction;

  public SegmentTree(T[] elements, BiFunction<T, T, T> segmentFunction) {
    this.segmentFunction = segmentFunction;
    this.size = elements.length;

    // Max size is 4 * n for complete binary tree where n is power of 2
    // - Ex: n = 4 = 2^2 -> nodes needed = (2*4)-1 = 7
    // - If n = 6 (not power of 2) safe to use 8 = (2*8)-1 = 15
    this.tree = new Object[4 * size];

    // 0 -> root and for element i -> left child = (2*i)+1 and right child = (2*i)+2
    buildTree(elements, 0, 0, size - 1);
  }

  private void buildTree(T[] elements, int nodeIndex, int start, int end) {
    if (start == end) {
      // child node is the element node
      tree[nodeIndex] = elements[start];
    } else {
      int mid = (start + end) / 2;
      int leftChildIndex = (2 * nodeIndex) + 1;
      int rightChildIndex = (2 * nodeIndex) + 2;

      // Split current range of the array into two halves for left and right children
      buildTree(elements, leftChildIndex, start, mid);
      buildTree(elements, rightChildIndex, mid + 1, end);

      // Calculate and set parent
      tree[nodeIndex] = segmentFunction.apply((T) tree[leftChildIndex], (T) tree[rightChildIndex]);
    }
  }

  public void update(int index, T value) {
    update(0, 0, size - 1, index, value);
  }

  private void update(int nodeIndex, int start, int end, int index, T value) {
    if (start == end) {
      tree[nodeIndex] = value;
    } else {
      int mid = (start + end) / 2;
      int leftChildIndex = (2 * nodeIndex) + 1;
      int rightChildIndex = (2 * nodeIndex) + 2;

      if (index <= mid) {
        update(leftChildIndex, start, mid, index, value);
      } else {
        update(rightChildIndex, mid + 1, end, index, value);
      }

      // Re-calculate the parent node value
      tree[nodeIndex] = segmentFunction.apply((T) tree[leftChildIndex], (T) tree[rightChildIndex]);
    }
  }

  public T query(int left, int right) {
    return query(0, 0, size - 1, left, right);
  }

  private T query(int nodeIndex, int start, int end, int left, int right) {

    // No overlap with the current range
    if (right < start || left > end) {
      return null;
    }

    // Total overlap with current range.
    // Segment tree node range is fully within the required range
    if (left <= start && end <= right) {
      return (T) tree[nodeIndex];
    }

    // Partial overlap, look for both sides
    int mid = (start + end) / 2;
    int leftChildIndex = (2 * nodeIndex) + 1;
    int rightChildIndex = (2 * nodeIndex) + 2;

    T leftResult = query(leftChildIndex, start, mid, left, right);
    T rightResult = query(rightChildIndex, mid + 1, end, left, right);

    if (leftResult == null && rightResult == null) return null;
    if (leftResult == null) return rightResult;
    if (rightResult == null) return leftResult;
    return segmentFunction.apply(leftResult, rightResult);
  }

  public void print() {
    for (Object node : tree) {
      System.out.print(node + " ");
    }
    System.out.println();
  }

  public static void main(String[] args) {
    Integer[] arr = {1, 3, 5, 7, 9, 11};
    SegmentTree<Integer> st = new SegmentTree<>(arr, Integer::sum);
    st.print();

    System.out.println("Initial Range Sum (1-3): " + st.query(1, 3)); // Output: 15
    st.update(1, 10); // Update index 1 to 10
    System.out.println("Updated Range Sum (1-3): " + st.query(1, 3)); // Output: 22
  }
}
