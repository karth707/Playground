package basics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unchecked")
public class Heap<T> {

  private int capacity;
  private Object[] array;
  private int size;
  private final Comparator<T> cmp;

  public Heap(Comparator<T> comparator) {
    this.capacity = 1000;
    this.array = new Object[capacity];
    this.size = 0;
    this.cmp = comparator;
  }

  public void add(Object element) {
    if (size == capacity) {
      capacity = 2 * capacity;
      array = Arrays.copyOf(array, capacity);
    }
    array[size] = element;
    heapifyUp(size);
    size++;
  }

  public T remove() {
    if (size == 0) {
      throw new IllegalStateException("Heap is empty");
    }
    Object root = array[0];
    swap(0, size - 1);
    size--;
    heapifyDown(0);
    return (T) root;
  }

  public T peek() {
    if (size == 0) {
      throw new IllegalStateException("Heap is empty");
    }
    Object root = array[0];
    return (T) root;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  private void heapifyUp(int index) {
    int pIndex = (index - 1) / 2;
    if (index > 0 && cmp.compare((T) array[index], (T) array[pIndex]) < 0) {
      swap(pIndex, index);
      heapifyUp(pIndex);
    }
  }

  private void heapifyDown(int index) {
    int lcIndex = 2 * index + 1;
    int rcIndex = 2 * index + 2;
    int candidate = index;
    if (lcIndex < size && cmp.compare((T) array[lcIndex], (T) array[candidate]) < 0) {
      candidate = lcIndex;
    }
    if (rcIndex < size && cmp.compare((T) array[rcIndex], (T) array[candidate]) < 0) {
      candidate = rcIndex;
    }
    if (candidate != index) {
      swap(index, candidate);
      heapifyDown(candidate);
    }
  }

  private void swap(int left, int right) {
    Object temp = array[left];
    array[left] = array[right];
    array[right] = temp;
  }

  public static void main(String[] args) {
    Heap<Integer> minHeap = new Heap<>((a, b) -> a - b);
    minHeap.add(4);
    minHeap.add(7);
    minHeap.add(2);
    minHeap.add(9);
    minHeap.add(1);

    List<Integer> sortedList = new ArrayList<>();
    while (!minHeap.isEmpty()) {
      sortedList.add(minHeap.remove());
    }
    System.out.println("Sorted: " + sortedList);
  }
}
