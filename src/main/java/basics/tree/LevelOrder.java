package basics.tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Useful when need to process one level at a time
 */
public class LevelOrder<T> implements TreeTraversal<T> {

    @Override
    public List<T> recursiveTraversal(TreeNode<T> root) {
        throw new IllegalStateException("Recursive traversal for level order does not exist");
    }

    @Override
    public List<T> iterativeTraversal(TreeNode<T> root) {
        List<T> result = new ArrayList<>();
        Queue<TreeNode<T>> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode<T> node = queue.poll();
            result.add(node.data());
            if (node.left() != null) queue.offer(node.left());
            if (node.right() != null) queue.offer(node.right());
        }
        return result;
    }
}
