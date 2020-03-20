import java.util.*;

//
// An implementation of a binary search tree.
//
// This tree stores both keys and values associated with those keys.
//
// More information about binary search trees can be found here:
//
// http://en.wikipedia.org/wiki/Binary_search_tree
//
// Note: Wikipedia is using a different definition of
//       depth and height than we are using.  Be sure
//       to read the comments in this file for the
//	 	 height function.
//
class BinarySearchTree <K extends Comparable<K>, V>  {

	public static final int BST_PREORDER  = 1;
	public static final int BST_POSTORDER = 2;
	public static final int BST_INORDER   = 3;

	// These are package friendly for the TreeView class
	BSTNode<K,V>	root;
	int		count;

	int		findLoops;
	int		insertLoops;

	public BinarySearchTree () {
		root = null;
		count = 0;
		resetFindLoops();
		resetInsertLoops();
	}

	public int getFindLoopCount() {
		return findLoops;
	}

	public int getInsertLoopCount() {
		return insertLoops;
	}

	public void resetFindLoops() {
		findLoops = 0;
	}
	public void resetInsertLoops() {
		insertLoops = 0;
	}


	// Recursive function for inserting into BST
	public BSTNode<K,V> recursiveInsert(BSTNode<K,V> temp, K k, V v){
		BSTNode<K,V> newNode = new BSTNode<K,V>(k,v);

		if(temp == null){
			count++;
			return newNode;
		}
		
		int comp = k.compareTo(temp.key);

		if(comp == 0){
			insertLoops++;
			temp.value = v;
		}
		else if(comp < 0){
			insertLoops++;
			temp.left = recursiveInsert(temp.left, k, v);
		}
		else{
			insertLoops++;
			temp.right = recursiveInsert(temp.right, k, v);
		}
		return temp;
	}

	//
	// Purpose:
	//
	// Insert a new Key:Value Entry into the tree.  If the Key
	// already exists in the tree, update the value stored at
	// that node with the new value.
	//
	// Pre-Conditions:
	// 	the tree is a valid binary search tree
	//
	public void insert(K k, V v){
		root = recursiveInsert(root, k, v);
	}


	// Recursive function to find in BST
	public V recursiveFind(BSTNode<K,V> temp, K key) throws KeyNotFoundException{
		if(temp == null){
			throw new KeyNotFoundException();
		}

		int comp = key.compareTo(temp.key);

		if(comp == 0){
			findLoops++;
			return temp.value;
		}
		else if(comp < 0){
			findLoops++;
			return recursiveFind(temp.left, key);
		}
		else{
			findLoops++;
			return recursiveFind(temp.right, key);
		}
	}

	//
	// Purpose:
	//
	// Return the value stored at key.  Throw a KeyNotFoundException
	// if the key isn't in the tree.
	//
	// Pre-conditions:
	//	the tree is a valid binary search tree
	//
	// Returns:
	//	the value stored at key
	//
	// Throws:
	//	KeyNotFoundException if key isn't in the tree
	//
	public V find (K key) throws KeyNotFoundException{
		return recursiveFind(root, key);
	}


	//
	// Purpose:
	//
	// Return the number of nodes in the tree.
	//
	// Returns:
	//	the number of nodes in the tree.
	public int size(){
		return count;
	}


	//
	// Purpose:
	//	Remove all nodes from the tree.
	//
	public void clear(){
		root = null;
		count = 0;
	}


	// Recursive function to find the max height of BST
	public int recursiveHeight(BSTNode<K,V> temp){
		int left = 0;
		int right = 0;

		if(temp.left != null){
			left = recursiveHeight(temp.left);
		}

		if(temp.right != null){
			right = recursiveHeight(temp.right);
		}

		if(left > right){
			return left+1;
		}

		else{
			return right+1;
		}
	}

	//
	// Purpose:
	//
	// Return the height of the tree.  We define height
	// as being the number of nodes on the path from the root
	// to the deepest node.
	//
	// This means that a tree with one node has height 1.
	//
	// Examples:
	//	See the assignment PDF and the test program for
	//	examples of height.
	//
	public int height(){
		if(root == null){
			return 0;
		}
		else{
			return recursiveHeight(root);
		}
	}


	//
	// Purpose:
	//
	// Return a list of all the key/value Entrys stored in the tree
	// The list will be constructed by performing a level-order
	// traversal of the tree.
	//
	// Level order is most commonly implemented using a queue of nodes.
	//
	//  From wikipedia (they call it breadth-first), the algorithm for level order is:
	//
	//	levelorder()
	//		q = empty queue
	//		q.enqueue(root)
	//		while not q.empty do
	//			node := q.dequeue()
	//			visit(node)
	//			if node.left != null then
	//			      q.enqueue(node.left)
	//			if node.right != null then
	//			      q.enqueue(node.right)
	//
	// Note that we will use the Java LinkedList as a Queue by using
	// only the removeFirst() and addLast() methods.
	//
	public List<Entry<K,V>> entryList() {
		List<Entry<K, V>> 			l = new LinkedList<Entry<K,V> >(); // new list
		LinkedList<BSTNode<K,V>> q = new LinkedList<BSTNode<K,V>>(); // new queue
		BSTNode<K,V> temp = null;
		q.addLast(root); //addLast = enqueue

		while(!q.isEmpty()){
			temp = q.removeFirst(); //removeFirst = dequeue
			l.add(new Entry<K,V>(temp.key, temp.value)); //adds the Entry to the list

			if(temp.left != null){
				q.addLast(temp.left);
			}
			if(temp.right != null){
				q.addLast(temp.right);
			}
		}
		return l;
	}

	private void doInOrder(BSTNode<K,V> n, List<Entry<K,V>> l){
		if(root == null){
			System.out.println("Tree does not exist.");
		}
		else{
			if(n.left != null){
				doInOrder(n.left, l);
			}
			l.add(new Entry<K,V>(n.key, n.value));
			if(n.right != null){
				doInOrder(n.right, l);
			}
		}
	}

	private void doPreOrder (BSTNode<K,V> n, List <Entry<K,V>> l){
		if(root == null){
			System.out.println("Tree does not exist.");
		}
		else{
			l.add(new Entry<K,V>(n.key, n.value));
			if(n.left != null){
			doPreOrder(n.left, l);
			}
			if(n.right != null){
				doPreOrder(n.right, l);
			}	
		}
	}

	private void doPostOrder (BSTNode<K,V> n, List <Entry<K,V>> l){
		if(root == null){
			System.out.println("Tree does not exist.");
		}
		else{
			if(n.left != null){
				doPostOrder(n.left, l);
			}
			if(n.right != null){
				doPostOrder(n.right, l);
			}	
			l.add(new Entry<K,V>(n.key, n.value));
		}
	}



	//
	// Purpose:
	//
	// Return a list of all the key/value Entrys stored in the tree
	// The list will be constructed by performing a traversal 
	// specified by the parameter which.
	//
	// If which is:
	//	BST_PREORDER	perform a pre-order traversal
	//	BST_POSTORDER	perform a post-order traversal
	//	BST_INORDER	perform an in-order traversal
	//
	public List<Entry<K,V> > entryList (int which) {
		List<Entry<K,V> > l = new LinkedList<Entry<K,V> >();

		if(which == BST_PREORDER){
			doPreOrder(root, l);
		}		
		else if(which == BST_POSTORDER){
			doPostOrder(root, l);
		}
		else if(which == BST_INORDER){
			doInOrder(root, l);
		}
		return l;
	}

	// Your instructor had the following private methods in his solution:
	// private void doInOrder (BSTNode<K,V> n, List <Entry<K,V> > l);
	// private void doPreOrder (BSTNode<K,V> n, List <Entry<K,V> > l);
	// private void doPostOrder (BSTNode<K,V> n, List <Entry<K,V> > l);
	// private int doHeight (BSTNode<K,V> t)
}
