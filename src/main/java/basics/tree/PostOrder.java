package basics.tree;

import java.util.*;
import java.util.function.Supplier;

/** Useful to delete the tree. */
public class PostOrder<T> implements TreeTraversal<T> {

  Supplier<Boolean> useTwoStacks;

  public PostOrder() {
    this(() -> false);
  }

  public PostOrder(Supplier<Boolean> useTwoStacks) {
    this.useTwoStacks = useTwoStacks;
  }

  @Override
  public List<T> recursiveTraversal(TreeNode<T> root) {
    List<T> result = new ArrayList<>();
    traverse(result, root);
    return result;
  }

  private void traverse(List<T> result, TreeNode<T> node) {
    if (node == null) return;
    traverse(result, node.left());
    traverse(result, node.right());
    result.add(node.data());
  }

  @Override
  public List<T> iterativeTraversal(TreeNode<T> root) {
    return (useTwoStacks.get())
        ? iterativeTraversalWithTwoStacks(root)
        : iterativeTraversalWithHashSet(root);
  }

  private List<T> iterativeTraversalWithHashSet(TreeNode<T> root) {
    List<T> result = new ArrayList<>();
    Deque<TreeNode<T>> stack = new ArrayDeque<>();
    Set<TreeNode<T>> processed = new HashSet<>();
    TreeNode<T> currNode = root;
    while (currNode != null || !stack.isEmpty()) {
      if (currNode != null) {
        stack.push(currNode);
        currNode = currNode.left();
      } else {
        TreeNode<T> top = stack.peek();
        if (top.right() == null || processed.contains(top.right())) {
          stack.pop();
          result.add(top.data());
          processed.add(top);
        } else {
          currNode = top.right();
        }
      }
    }
    return result;
  }

  private List<T> iterativeTraversalWithTwoStacks(TreeNode<T> root) {
    List<T> result = new ArrayList<>();
    Deque<TreeNode<T>> activeStack = new ArrayDeque<>();
    Deque<TreeNode<T>> resultStack = new ArrayDeque<>();

    activeStack.push(root);
    while (!activeStack.isEmpty()) {
      TreeNode<T> top = activeStack.pop();
      resultStack.push(top);
      if (top.left() != null) activeStack.push(top.left());
      if (top.right() != null) activeStack.push(top.right());
    }

    while (!resultStack.isEmpty()) {
      result.add(resultStack.pop().data());
    }
    return result;
  }
}
