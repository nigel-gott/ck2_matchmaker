// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import fam.badger_ken.matchmaker.*;

import javax.swing.*;

/**
 * The 'HomeFilter' filters for the home county of each character
 */
public class NameFilter implements Filterer {

  private final ResultMaker resultMaker;
  private final GameConfig gameConfig;
  private boolean onNow;
  private String value;

  public NameFilter(ResultMaker resultMaker, GameConfig gameConfig) {
    this.resultMaker = resultMaker;
    this.gameConfig = gameConfig;
  }

  @Override
  public void notify(JComponent changee, JComponent backgrounder) {
    JTextField textField = (JTextField) changee;
    String newValue = textField.getText();
    // a change is either changing null-ness, or changing value.
    boolean change = !Util.guardedEquals(value, newValue);
    onNow = (newValue != null && !newValue.isEmpty());
    value = newValue;
    if (change && backgrounder != null) {
      backgrounder.setBackground(onNow ? SwingGui.FILTER_ON_COLOR : SwingGui.FILTER_OFF_COLOR);
      resultMaker.makeResults();
    }

  }

  @Override
  public boolean passes(Person person) {
    if (!onNow) return true;
    return value != null && person.getDisplayName(gameConfig).toLowerCase().contains(value.toLowerCase());
  }

  @Override
  public boolean isOn() {
    return onNow;
  }
}
