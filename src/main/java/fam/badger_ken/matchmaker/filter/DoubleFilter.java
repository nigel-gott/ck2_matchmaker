// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import fam.badger_ken.matchmaker.*;

import javax.swing.*;

/**
 * An IntegerFilter is an abstract base class that can filter double-valued
 * quantities in a person, with either a 'min' or 'max' focus.
 * Subclasses only need implement the 'evaluatePerson()' method that, given
 * a person, indicates the present value of the quantity in question.
 */
public abstract class DoubleFilter implements Filterer {
  private final ResultMaker resultMaker;
  private final boolean isMinFilter;

  private boolean onNow;
  private Double value;

  public DoubleFilter(ResultMaker resultMaker, boolean isMinFilter) {
    this.resultMaker = resultMaker;
    this.isMinFilter = isMinFilter;
    this.onNow = false;
    this.value = null;
  }
   

  @Override
  public void notify(JComponent changee, JComponent backgrounder) {
    JTextField textField = (JTextField) changee;
    String val = textField.getText();
    Double newValue = Util.toDouble(val, null);
    // a change is either changing null-ness, or changing value.
    boolean change = !Util.guardedEquals(value, newValue);
    onNow = (newValue != null);
    value = newValue;
    if (change && backgrounder != null) {
      backgrounder.setBackground(onNow ? SwingGui.FILTER_ON_COLOR : SwingGui.FILTER_OFF_COLOR);
      resultMaker.makeResults();
    }
  }
  
  public abstract Double evaluatePerson(Person person);

  @Override
  public boolean passes(Person person) {
    if (!onNow) return true;
    Double personVal = evaluatePerson(person);
    return isMinFilter ? (personVal >= value) : (personVal <= value);
  }

  @Override
  public boolean isOn() {
    return onNow;
  }

}
