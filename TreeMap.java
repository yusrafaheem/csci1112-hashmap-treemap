/*--------------------------------------------------------------------------
GWU CSCI 1112 Spring 2025
author: Yusra Faheem, James Taylor

This class implements the Map interface using a binary search tree keyed
lexicographically on the key string. Deletion is implemented lazily: a
deleted node is marked with its delete flag rather than being physically
unlinked, so put and get treat a marked node as absent while the tree
structure underneath it is preserved. If the rebalance flag is set, the
tree is rebuilt into a balanced shape (dropping any marked nodes) after
every insertion.
--------------------------------------------------------------------------*/

// used only in balancing utilities
import java.util.List;
import java.util.ArrayList;

public class TreeMap implements Map {

    // The root of the binary search tree; null when the tree is empty
    private TreeNode root;
    // Whether the tree should rebuild itself into a balanced shape after
    // every insertion
    private boolean rebalance;

    /// Creates an empty binary search tree map.
    /// @param rebalance if true, the tree rebalances itself after every put
    public TreeMap( boolean rebalance ) {
        this.root = null;
        this.rebalance = rebalance;
    }

    /// Inserts a key-value pair into the tree, or updates the value if the
    /// key already exists. Walks the tree comparing the key lexically at
    /// each node; if a matching node is found (even one previously marked
    /// deleted) its value is updated and its delete flag is cleared so no
    /// duplicate node is ever created. Otherwise a new node is attached as
    /// a leaf in the correct position. If this tree rebalances, the tree
    /// is rebuilt after the insertion completes.
    /// @param key the key to insert or update
    /// @param value the value to associate with the key
    /// @param profile a single-element array; profile[0] is incremented by
    ///        the number of key comparisons performed (rebalancing is not
    ///        counted)
    public void put( String key, String value, int[] profile ) {
        if (root == null) {
            root = new TreeNode(key, value, null);
        } else {
            TreeNode current = root;
            while (true) {
                profile[0]++;
                int cmp = key.compareTo(current.getKey());
                if (cmp == 0) {
                    current.updateValue(value);
                    current.delete = false;
                    break;
                } else if (cmp < 0) {
                    if (current.left == null) {
                        current.left = new TreeNode(key, value, current);
                        break;
                    }
                    current = current.left;
                } else {
                    if (current.right == null) {
                        current.right = new TreeNode(key, value, current);
                        break;
                    }
                    current = current.right;
                }
            }
        }
        if (rebalance) {
            balance();
        }
    }

    /// Looks up the value associated with a key by walking the tree,
    /// comparing the key lexically at each node.
    /// @param key the key to search for
    /// @param profile a single-element array; profile[0] is incremented by
    ///        the number of key comparisons performed
    /// @return the value associated with the key if found and not deleted;
    ///         otherwise, null
    public String get(String key, int[] profile) {
        TreeNode current = root;
        while (current != null) {
            profile[0]++;
            int cmp = key.compareTo(current.getKey());
            if (cmp == 0) {
                if (current.delete) {
                    return null;
                }
                return current.getValue();
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    /// Removes a key from the tree by marking its node deleted rather than
    /// physically unlinking it, so the tree's shape is left intact.
    /// @param key the key to remove
    /// @param profile a single-element array; profile[0] is incremented by
    ///        the number of key comparisons performed
    /// @return true if the key was found and not already deleted;
    ///         otherwise, false
    public boolean delete(String key, int[] profile) {
        TreeNode current = root;
        while (current != null) {
            profile[0]++;
            int cmp = key.compareTo(current.getKey());
            if (cmp == 0) {
                if (current.delete) {
                    return false;
                }
                current.delete = true;
                return true;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    /// Discards the entire tree, leaving the map empty.
    public void clear(){
        root = null;
    }


    //-------------------------------------------------------------------
    // Utilities
    //-------------------------------------------------------------------
    /// The entry point for balancing the entire tree to maintain optimal
    /// insert and search efficiency
    private void balance() {
        root = balance(root);
    }

    /// Rebalance a given subtree given a local root node
    /// Note: This algorithm focuses on correctness and is not the most
    /// efficient algorithm available. Please look up different algorithms
    /// that solve the balancing problem.
    /// @param root the root of the subtree to balance
    /// @return the new root of the subtree after balancing
    private TreeNode balance(TreeNode root) {
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        //Sorts tree from given root
        populate(root, nodes);
        //Return null if root has no children
        if(nodes.size() == 0) return null;

        return balance(nodes, root, 0, nodes.size() - 1);
    }


    /// Recursive helper in the balancing operation to support balance.
    /// @param nodes a list of nodes
    /// @param parent the parent node of this subtree
    /// @param start the start index within the list
    /// @param end the end index within the list
    /// @return the local root after balancing is performed on the subtree
    private TreeNode balance(List<TreeNode> nodes, TreeNode parent, int start, int end) {
        int mid = (start + end) / 2;
        TreeNode node = nodes.get(mid);
        node.parent = parent;
        if(start == end){
            node.left = null;
            node.right = null;
            return node;
        }
        //Recursively balance tree on left and right children using
        //middle node as root
        if(!(mid - 1 < start)) {
            node.left = balance(nodes, node, start, mid - 1);
        } else {
            node.left = null;
        }

        if(!(mid + 1 > end)) {
            node.right = balance(nodes, node, mid + 1, end);
        } else {
            node.right = null;
        }

        return node;
    }

    /// Recursive helper in the balancing operation to put listitems into
    /// the tree
    /// @param the root of the subtree to balance
    /// @param the list of nodes to balance
    private void populate(TreeNode node, List<TreeNode> list) {
        if(node == null) return;
        populate(node.left, list);
        if( !node.delete ) {
            list.add(node);
        }
        populate(node.right, list);
    }
}
