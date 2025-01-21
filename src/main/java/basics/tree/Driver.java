package basics.tree;

import java.util.List;

public class Driver {

  public static void main(String[] args) {

    /*
     *         1
     *        / \
     *       4   5
     *      / \
     *     7   9
     */
    TreeNode<Integer> root =
        new TreeNode<>(
            1,
            new TreeNode<>(4, new TreeNode<>(7, null, null), new TreeNode<>(9, null, null)),
            new TreeNode<>(5, null, null));

    TreeTraversal<Integer> inOrder = new InOrder<>();
    TreeTraversal<Integer> preOrder = new PreOrder<>();
    TreeTraversal<Integer> postOrder = new PostOrder<>(() -> true);
    TreeTraversal<Integer> levelOrder = new LevelOrder<>();

    List<Integer> eInOrder = List.of(7, 4, 9, 1, 5);
    System.out.println(
        "Expected: "
            + eInOrder
            + "; Actual - inOrder recursive: "
            + inOrder.recursiveTraversal(root));
    System.out.println(
        "Expected: "
            + eInOrder
            + "; Actual - inOrder iterative: "
            + inOrder.iterativeTraversal(root));

    List<Integer> ePreOrder = List.of(1, 4, 7, 9, 5);
    System.out.println(
        "Expected: "
            + ePreOrder
            + "; Actual - preOrder recursive: "
            + preOrder.recursiveTraversal(root));
    System.out.println(
        "Expected: "
            + ePreOrder
            + "; Actual - preOrder iterative: "
            + preOrder.iterativeTraversal(root));

    List<Integer> ePostOrder = List.of(7, 9, 4, 5, 1);
    System.out.println(
        "Expected: "
            + ePostOrder
            + "; Actual - postOrder recursive: "
            + postOrder.recursiveTraversal(root));
    System.out.println(
        "Expected: "
            + ePostOrder
            + "; Actual - postOrder iterative: "
            + postOrder.iterativeTraversal(root));

    List<Integer> eLevelOrder = List.of(1, 4, 5, 7, 9);
    System.out.println(
        "Expected: "
            + eLevelOrder
            + "; Actual - levelOrder iterative: "
            + levelOrder.iterativeTraversal(root));
  }
}
