// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

/**
 * Quick-and-dirty approximation of an xml node, done this way for
 * speed/simplicity.
 * 
 * @author badger_ken
 * 
 */
public class Node {
  public final String tag;
  public String value;
  public Vector<Node> children;
  public Node parent;
  // a string can have a _set_ of values. however, this puts memory use WAY up.
  // so have a regular map of the 'first' attribute, and then another map
  // for just the ones that are repeated.
  public Map<String, String> attributes;
  public Map<String, HashSet<String>> repeatedAttributes;

  public Node(String p_tag) {
    parent = null;
    tag = p_tag;
    value = null;
    children = null;
    attributes = null;
    repeatedAttributes = null;
  }

  private void makeChildren() {
    if (children == null) {
      children = new Vector<>();
    }
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void addChild(Node child) {
    child.parent = this;
    // System.out.println("adding " + child.tag + " as child of " + tag);
    makeChildren();
    children.add(child);
  }

  private void makeAttributes() {
    if (attributes == null) {
      attributes = new HashMap<>();
    }
  }

  public void setAttribute(String key, String val) {
    // is it already in the repeated attrs?
    if (repeatedAttributes != null && repeatedAttributes.containsKey(key)) {
      HashSet<String> vals = repeatedAttributes.get(key);
      vals.add(val);
      return;
    }
    makeAttributes();
    // is it already in the single-attrs? If so, make it multi:
    if (attributes.containsKey(key)) {
      if (repeatedAttributes == null)
        repeatedAttributes = new HashMap<>();
      HashSet<String> vals = new HashSet<>();
      vals.add(attributes.get(key));
      vals.add(val);
      repeatedAttributes.put(key, vals);
      // note that the first one stays in (attributes)
      return;
    }
    // here for the first time
    attributes.put(key, val);
  }

  @SuppressWarnings({"StringConcatenationInsideStringBufferAppend"})
  public String PrettyPrint() {
    StringBuilder buf = new StringBuilder();
    buf.append("<" + tag);
    /*
     * for (Entry<String, String> entry: attributes.entrySet().entrySet()) {
     * buf.append(" " + entry.getKey() + "=" + entry.getValue()); }
     */
    buf.append(">");
    for (int i = 0; i < children.size(); i++) {
      buf.append("\n  " + i + ":" + children.elementAt(i).tag + "...");
    }
    if (value != null && !value.isEmpty()) {
      buf.append("\n" + value);
    }
    buf.append("\n</" + tag + ">");
    return buf.toString();
  }

  /**
   * Finds the value for the given attribute, if it exists.
   * 
   * @param key
   *          the attribute key
   * @return the value, null if not found.
   */
  public String findAttribute(String key) {
    if (attributes != null && attributes.containsKey(key))
      return attributes.get(key);
    // is it in the multi?
    if (repeatedAttributes != null) {
      HashSet<String> vals = repeatedAttributes.get(key);
      if (vals != null)
        return vals.iterator().next(); // the first one
    }
    return null;
  }

  public Set<String> findAttributes(String key) {
    // it might be repeated, and it might not....
    if (repeatedAttributes != null) {
      Set<String> repeatedAnswer = repeatedAttributes.get(key);
      if (repeatedAnswer != null)
        return repeatedAnswer;
    }
    // its not in the repeated set, it might be in the singles set
    if (attributes == null || !attributes.containsKey(key))
      return null;
    HashSet<String> wrap = new HashSet<>();
    wrap.add(attributes.get(key));
    return wrap;
  }

  /**
   * Finds the child of the given node, with the given tag, if one exists. Does
   * so breadth-first.
   * 
   * @param tag
   *          the desired tag
   * @return the descendant, null if none.
   */
  public Node findDescendant(String tag) {
    if (children == null)
      return null;
    // is it in an immediate child?
    for (Node child : children) {
      if (tag.equals(child.tag)) {
        return child;
      }
    }
    // ok, try descendants:
    for (Node child : children) {
      Node descendant = child.findDescendant(tag);
      if (descendant != null)
        return descendant;
    }
    return null;
  }

  /**
   * Utility routine that sees if a node has a certain real-valued
   * attribute.
   */
  public Double getDouble(String attrName) {
    String sVal = findAttribute(attrName);
    if (sVal == null) return null;
    try {
      return Double.parseDouble(sVal);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Utility debug procedure
   */
  public void dumpAttributes() {
    if (attributes != null) {
      for (Entry<String, String> entry : attributes.entrySet()) {
        System.out.println(entry.getKey() + " --> " + entry.getValue());
      }
    }
    if (repeatedAttributes != null) {
      for (Entry<String, HashSet<String>> entry : repeatedAttributes.entrySet()) {
        System.out.print("[r] " + entry.getKey() + " -->");
        int i = 0;
        for (String attr : entry.getValue()) {
          if (i != 0)
            System.out.print(", ");
          System.out.print(attr);
          i++;
        }
      }
    }

  }

  /**
   * Tries to convert the tag to an integer.
   * @return the integer, null if it isn't.
   */
  public Integer tagAsInt() {
    int provinceId;
    try {
      // province tags are just the numeric IDs
      provinceId = Integer.parseInt(tag);
    } catch (NumberFormatException e) {
      return null;
    }
    return provinceId;
  }

}
