// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import fam.badger_ken.matchmaker.Filterer;
import fam.badger_ken.matchmaker.HoldingLevel;
import fam.badger_ken.matchmaker.Person;
import fam.badger_ken.matchmaker.ResultMaker;
import fam.badger_ken.matchmaker.SwingGui;
import fam.badger_ken.matchmaker.widget.AnyDropdownable;

/**
 * The 'HoldingLevelFilter' filters for a minimal holding level.
 */
public class HoldingLevelFilter implements Filterer {
  private ResultMaker resultMaker;

  private boolean onNow = false;
  private Integer threshold = null;
  private boolean isMinFilter;

  public HoldingLevelFilter(ResultMaker resultMaker, boolean isMinFilter) {
    this.resultMaker = resultMaker;
    this.isMinFilter = isMinFilter;
  }

  @Override
  public void notify(JComponent changee, JComponent backgrounder) {
    JComboBox box = (JComboBox) changee;
    AnyDropdownable dropdown = (AnyDropdownable) box.getSelectedItem();
    Integer newValue = (dropdown == null || dropdown.isAny()) ? null : ((HoldingLevel) dropdown.infoForFilter()).weight;
    boolean toBeOn = (newValue != null);
    // a change is either changing null-ness, or changing value.
    boolean change = (toBeOn != onNow)
        || (onNow && !newValue.equals(threshold));
    onNow = toBeOn;
    threshold = newValue;
    if (change && backgrounder != null) {
      backgrounder.setBackground(onNow ? SwingGui.FILTER_ON_COLOR : SwingGui.FILTER_OFF_COLOR);
      resultMaker.makeResults();
    }
  }

  @Override
  public boolean passes(Person person) {
    if (!onNow) return true;
    int level;
    if (person.getHoldings() == null || person.getHoldings().isEmpty()) {
      level = 0;
    } else {
      level = person.getHoldings().iterator().next().holdingLevel.weight;
    }
    return isMinFilter ? (level >= threshold) : (level <= threshold);
  }

  @Override
  public boolean isOn() {
    return onNow;
  }
}
