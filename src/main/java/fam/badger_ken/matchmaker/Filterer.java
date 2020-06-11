// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import javax.swing.JComponent;

/**
 * A 'Filterer' is something that can Filter.
 * A Filter is notified whenever the component it is watching
 * changes, and, if so
 *     a) indicates that in the component visually,
 *     b) notifies whoever it wants to of this change. 
 *     
 * It is then asked to evaluate a Person to see whether the person
 * now passes the filter.
 */
public interface Filterer {
  /**
   * Notified when a value may have changed.
   * @param changee the component that may have changed
   * @param backgrounder the component whose background color should change
   *     to show status, if any.
   */
  void notify(JComponent changee, JComponent backgrounder);

  boolean passes(Person person);

  /**
   * am I on?
   */
  boolean isOn();
}
