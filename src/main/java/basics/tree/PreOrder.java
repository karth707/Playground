package basics.tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Useful to create a copy of the tree.
 */
public class PreOrder<T> implements TreeTraversal<T> {

    @Override
    public List<T> recursiveTraversal(TreeNode<T> root) {
        List<T> result = new ArrayList<>();
        traverse(result, root);
        return result;
    }

    private void traverse(List<T> result, TreeNode<T> node) {
        if (node == null) return;
        result.add(node.data());
        traverse(result, node.left());
        traverse(result, node.right());
    }

    @Override
    public List<T> iterativeTraversal(TreeNode<T> root) {
        List<T> result = new ArrayList<>();
        Deque<TreeNode<T>> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode<T> top = stack.pop();
            result.add(top.data());
            if (top.right() != null) stack.push(top.right());
            if (top.left() != null) stack.push(top.left());
        }
        return result;
    }
}
