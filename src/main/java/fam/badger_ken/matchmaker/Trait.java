// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import java.util.ArrayList;
import java.util.List;

import fam.badger_ken.matchmaker.widget.AnyDropdownable;

public class Trait implements AnyDropdownable {
  public Integer key;
  public String label;
  public String displayName;
  public List<AttributeImpact> impacts;

  public static class AttributeImpact {
    public AttributeInfo impactee;
    public int impact;


    public AttributeImpact(AttributeInfo impactee, int impact) {
      this.impactee = impactee;
      this.impact = impact;
    }
  }


  public Trait(Integer key, String label) {
    this.key = key;
    this.label = label;
    this.displayName = label;  // a default.
    this.impacts = new ArrayList<AttributeImpact>();
  }

  public void addImpact(AttributeInfo impactee, int impact) {
    impacts.add(new AttributeImpact(impactee, impact));
  }
  
  // implement AnyDropdownable:
  @Override
  public boolean isAny() {
    return false;
  }
  @Override
  public String toString() {
    return displayName;
  }

  @Override
  public Object infoForFilter() {
    return this;
  }
}
