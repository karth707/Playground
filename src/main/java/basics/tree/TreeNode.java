package basics.tree;

public record TreeNode<T>(T data, TreeNode<T> left, TreeNode<T> right) {}
