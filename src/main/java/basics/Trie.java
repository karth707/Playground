package basics;

import java.util.Optional;

public class Trie {

    private final TrieNode root;

    public Trie() {
        this.root = TrieNode.buildRoot();
    }

    public void insert(String word) {
        TrieNode node = root;
        for (char c: word.toCharArray()) {
            if (!node.hasChild(c)) {
                node.addChild(c);
            }
            node = node.getChild(c).orElseThrow(
                    () -> new IllegalStateException("Illegal state of Trie"));
        }
        node.setIsWord();
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char c: word.toCharArray()) {
            if (!node.hasChild(c)) return false;
            node = node.getChild(c).orElseThrow(
                    () -> new IllegalStateException("Illegal state of Trie"));
        }
        return node.getIsWord();
    }

    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c: prefix.toCharArray()) {
            if (!node.hasChild(c)) return false;
            node = node.getChild(c).orElseThrow(
                    () -> new IllegalStateException("Illegal state of Trie"));
        }
        return true;
    }

    private static class TrieNode {
        private final TrieNode[] children;
        private boolean isWord;

        private TrieNode () {
            this.children = new TrieNode[128];
            this.isWord = false;
        }

        public static TrieNode buildRoot() {
            return new TrieNode();
        }

        public boolean hasChild(char c) {
            return children[c] != null;
        }

        public void addChild(char c) {
            if (!hasChild(c)) {
                children[c] = new TrieNode();
            }
        }

        public Optional<TrieNode> getChild(char c) {
            return (hasChild(c)) ? Optional.of(children[c]) : Optional.empty();
        }

        public void setIsWord() {
            isWord = true;
        }

        public boolean getIsWord() {
            return isWord;
        }
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        trie.insert("ball");
        trie.insert("bat");

        System.out.println("Search for apple, found: " + trie.search("apple"));
        System.out.println("Search for bat, found: " + trie.search("bat"));
        System.out.println("Search for bal, found: " + trie.search("bal"));
        System.out.println("Prefix Search for bal, found: " + trie.search("bal"));
    }
}
