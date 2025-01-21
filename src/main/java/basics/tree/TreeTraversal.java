package basics.tree;

import java.util.List;

public interface TreeTraversal<T> {

    List<T> recursiveTraversal(TreeNode<T> root);

    List<T> iterativeTraversal(TreeNode<T> root);
}
