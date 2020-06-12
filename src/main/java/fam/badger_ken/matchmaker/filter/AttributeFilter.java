// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import javax.swing.JComponent;
import javax.swing.JTextField;

import fam.badger_ken.matchmaker.Filterer;
import fam.badger_ken.matchmaker.Matchmaker;
import fam.badger_ken.matchmaker.Person;
import fam.badger_ken.matchmaker.ResultMaker;
import fam.badger_ken.matchmaker.SwingGui;
import fam.badger_ken.matchmaker.Util;

/**
 * The 'AttributeFilter' filters for either min or max value of an attribute.
 */
public class AttributeFilter implements Filterer {
  private final Matchmaker matchmaker;
  private final ResultMaker resultMaker;
  private final boolean isMinFilter;
  private final int ordinality;  // of the attribute

  private boolean onNow = false;
  private Integer value = null;

  public AttributeFilter(ResultMaker resultMaker, Matchmaker matchmaker,
      int ordinality, boolean isMinFilter) {
    this.resultMaker = resultMaker;
    this.matchmaker = matchmaker;
    this.ordinality = ordinality;
    this.isMinFilter = isMinFilter;
  }

  @Override
  public void notify(JComponent changee, JComponent backgrounder) {
    JTextField textField = (JTextField) changee;
    String val = textField.getText();
    Integer newValue = Util.toInt(val, null);
    // a change is either changing null-ness, or changing value.
    boolean change = !Util.guardedEquals(value, newValue);
    onNow = (newValue != null);
    value = newValue;
    if (change && backgrounder != null) {
      backgrounder.setBackground(onNow ? SwingGui.FILTER_ON_COLOR : SwingGui.FILTER_OFF_COLOR);
      resultMaker.makeResults();
    }
  }

  @Override
  public boolean passes(Person person) {
    if (!onNow) return true;
    int val = person.getAdjustedAttribute(matchmaker.gameConfig, ordinality);
    return isMinFilter ? (val >= value) : (val <= value);
  }

  @Override
  public boolean isOn() {
    return onNow;
  }


}
