// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

/**
 * To save space, the parser can be told to prune things as it goes.
 */
public interface ParserPruner {
  /**
   * The Node (child) is being assigned as a child of Node (parent).
   * (child) is complete - (parent) may have more children yet to come
   * (siblings of (child)). Should the parser discard (child)?
   * @param parent the parent node
   * @param child the child node
   * @return true if the node should be discarded.
   */
  boolean discard(Node parent, Node child);
}
