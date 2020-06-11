// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

/**
 * A ResultMaker is something that knows how to make,
 * and display, the results of the current filters.
 */
public interface ResultMaker {
  void makeResults();

  /**
   * Enable/disable yourself - default is to be enabled.
   */
  void setEnabled(boolean b);
}
