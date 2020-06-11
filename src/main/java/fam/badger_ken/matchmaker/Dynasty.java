// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import fam.badger_ken.matchmaker.widget.AnyDropdownable;

public class Dynasty implements AnyDropdownable {
  public Integer key;
  public String name;
  public String culture;
  public int size;  // how many nobles are in this dynasty.

  public Dynasty(Integer key, String name, String culture) {
    this.key = key;
    this.name = name;
    this.culture = culture;
    this.size = 0;
  }
  
  public void addNoble() {
    size++;
  }
  
  @Override
  public String toString() {
    return name + " (" + size + " noble" + ((size == 1) ? "" : "s") + ")";
  }

  @Override
  public boolean isAny() {
    return false;
  }

  @Override
  public Object infoForFilter() {
    return this.key;
  }
}
