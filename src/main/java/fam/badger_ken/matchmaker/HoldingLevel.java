// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker;

import fam.badger_ken.matchmaker.widget.AnyDropdownable;

/**
 * The levels of holding, used in the UI as a filtering level.
 */
public class HoldingLevel implements AnyDropdownable, Comparable<HoldingLevel> {
  public String displayLabel; // 'Barony', 'County', ...
  public int weight;
  
  public static final HoldingLevel BARONY = new HoldingLevel('b', "Barony", 1);
  public static final HoldingLevel COUNTY = new HoldingLevel('c', "County", 2);
  public static final HoldingLevel DUCHY = new HoldingLevel('d', "Duchy", 3);
  public static final HoldingLevel KINGDOM = new HoldingLevel('k', "Kingdom", 4);
  public static final HoldingLevel EMPIRE = new HoldingLevel('e', "Empire", 5);
  
  @Override
  public boolean isAny() {
    return false;
  }
  @Override
  public Object infoForFilter() {
    return this;
  }
  public HoldingLevel(char prefix, String displayLabel, int weight) {
    this.displayLabel = displayLabel;
    this.weight = weight;
  }
  
  public static HoldingLevel fromPrefix(char prefix) {
    switch (prefix) {
    case 'b': return BARONY;
    case 'c': return COUNTY;
    case 'd': return DUCHY;
    case 'e': return EMPIRE;
    case 'k': return KINGDOM;
    default: return null;
    }
  }
  @Override
  public int compareTo(HoldingLevel arg0) {
   return arg0.weight - weight;
  }
  
  @Override
  public String toString() {
    // this gets displayed in the drop-down box.
    return displayLabel;
  }
  
  
}


