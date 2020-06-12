// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import fam.badger_ken.matchmaker.widget.AnyDropdownable;

public class Culture implements AnyDropdownable {
  public final String label;  // e.g. 'maghreb_arabic'
  public final String displayName;  // e.g. 'Maghreb'

  public Culture(String label, String displayName) {
    this.label = label;
    this.displayName = displayName;
  }

  @Override
  public boolean isAny() {
    return false;
  }

  @Override
  public Object infoForFilter() {
    return label;
  }

  @Override
  public String toString() {
    return displayName;
  }

}
