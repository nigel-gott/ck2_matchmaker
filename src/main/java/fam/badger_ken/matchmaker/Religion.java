// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import fam.badger_ken.matchmaker.widget.AnyDropdownable;

public class Religion implements AnyDropdownable {
  public String label;  // e.g. 'sunni'
  public String displayName;  // e.g. 'Sunni'
  public ReligionGroup group;  // e.g. 'Muslim'

  public Religion(ReligionGroup group, String label, String displayName) {
    this.group = group;
    this.label = label;
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    // displayed in the drop-down boxes.
    return displayName;
  }

  @Override
  public boolean isAny() {
    return false;
  }

  @Override
  public Object infoForFilter() {
    return label;  // that's all the filter needs
  }
}
