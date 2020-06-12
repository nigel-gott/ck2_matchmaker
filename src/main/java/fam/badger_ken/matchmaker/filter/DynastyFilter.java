// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextArea;

import fam.badger_ken.matchmaker.Dynasty;
import fam.badger_ken.matchmaker.Filterer;
import fam.badger_ken.matchmaker.Person;
import fam.badger_ken.matchmaker.ResultMaker;
import fam.badger_ken.matchmaker.SwingGui;
import fam.badger_ken.matchmaker.widget.AnyDropdownable;

/**
 * The 'DynastyFilter' filters for presence/absence of a given dynasty.
 */
public class DynastyFilter implements Filterer {
  private ResultMaker resultMaker;
  private boolean isPresenceChecker;

  private boolean onNow = false;
  private Integer value = null;

  public DynastyFilter(ResultMaker resultMaker, boolean isPresenceChecker) {
    this.resultMaker = resultMaker;
    this.isPresenceChecker = isPresenceChecker;
  }

  @Override
  public void notify(JComponent changee, JComponent backgrounder) {
    JComboBox box = (JComboBox) changee;
    AnyDropdownable dropdown = (AnyDropdownable) box.getSelectedItem();
    Integer newValue = dropdown == null ? null : (Integer) dropdown.infoForFilter();
    boolean toBeOn = (newValue != null);
    // a change is either changing null-ness, or changing value.
    boolean change = (toBeOn != onNow)
        || (onNow && !newValue.equals(value));
    onNow = toBeOn;
    value = newValue;
    if (change && backgrounder != null) {
      backgrounder.setBackground(onNow ? SwingGui.FILTER_ON_COLOR : SwingGui.FILTER_OFF_COLOR);
      resultMaker.makeResults();
    }
  }

  @Override
  public boolean passes(Person person) {
    if (!onNow) return true;
    return isPresenceChecker == value.equals(person.dynastyKey);
  }

  @Override
  public boolean isOn() {
    return onNow;
  }

  /*
   * A special version for the dynasty filter, as the component only
   * contains the display name of the dynasty, and its wasteful of space
   * to make a hash table that maps from that back to the object, when we have it.
   */
  public void notify(Dynasty winner, JTextArea textArea) {
    Integer newValue = winner == null ? null : winner.key;
    boolean toBeOn = (newValue != null);
    // a change is either changing null-ness, or changing value.
    boolean change = (toBeOn != onNow)
        || (onNow && !newValue.equals(value));
    onNow = toBeOn;
    value = newValue;
    if (change && textArea != null) {
      textArea.setBackground(onNow ? SwingGui.FILTER_ON_COLOR : SwingGui.FILTER_OFF_COLOR);
      resultMaker.makeResults();
    }
    
  }  
}
