// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import fam.badger_ken.matchmaker.*;

import javax.swing.*;

/**
 * The 'HomeFilter' filters for the home county of each character
 */
public class HomeFilter implements Filterer {

  private final ResultMaker resultMaker;
  private final GameConfig gameConfig;
  private boolean onNow;
  private String value;
  private final SaveState saveState;

  public HomeFilter(ResultMaker resultMaker, GameConfig gameConfig, SaveState saveState) {
    this.resultMaker = resultMaker;
    this.gameConfig = gameConfig;
    this.saveState = saveState;
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
    String homeName = person.getHomeName(gameConfig, saveState);
    return value == null && homeName == null || homeName != null && value != null && homeName.toLowerCase().contains(value.toLowerCase());
  }

  @Override
  public boolean isOn() {
    return onNow;
  }
}
