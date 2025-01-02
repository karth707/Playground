package basics.tree;

import java.util.*;

/**
 * In the case of binary search trees (BST), gives nodes in non-decreasing order.
 */
public class InOrder<T> implements TreeTraversal<T> {

    @Override
    public List<T> recursiveTraversal(TreeNode<T> root) {
        List<T> result = new ArrayList<>();
        traverse(result, root);
        return result;
    }

    private void traverse(List<T> result, TreeNode<T> node) {
        if (node == null) return;
        traverse(result, node.left());
        result.add(node.data());
        traverse(result, node.right());
    }

    @Override
    public List<T> iterativeTraversal(TreeNode<T> root) {
        List<T> result = new ArrayList<>();
        Deque<TreeNode<T>> stack = new ArrayDeque<>();
        TreeNode<T> currNode = root;
        while (currNode != null || !stack.isEmpty()) {
            if (currNode != null) {
                stack.push(currNode);
                currNode = currNode.left();
            } else {
                TreeNode<T> topNode = stack.pop();
                result.add(topNode.data());
                currNode = topNode.right();
            }
        }
        return result;
    }
}
