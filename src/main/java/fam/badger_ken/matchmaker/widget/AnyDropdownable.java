// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.widget;

/**
 * To be 'AnyDropdownable' means that you can be a value in a dropdown,
 * which also has the special value of 'Any'.
 */
public interface AnyDropdownable {
  /**
   * @return whether you are the 'Any' item.
   */
  boolean isAny();
  /**
   * @return yourself in String form, this is used to display the label
   * in the drop-down
   */
  String toString();
  
  /**
   * @return a perspective on yourself that the filter needs to determine
   * whether or not a person passes a filter.
   */
  Object infoForFilter();

}
