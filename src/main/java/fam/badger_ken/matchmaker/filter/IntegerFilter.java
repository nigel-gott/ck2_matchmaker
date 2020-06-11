// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import javax.swing.JComponent;
import javax.swing.JTextField;

import fam.badger_ken.matchmaker.Filterer;
import fam.badger_ken.matchmaker.Person;
import fam.badger_ken.matchmaker.ResultMaker;
import fam.badger_ken.matchmaker.SwingGui;
import fam.badger_ken.matchmaker.Util;

/**
 * An IntegerFilter is an abstract base class that can filter integer-valued
 * quantities in a person, with either a 'min' or 'max' focus.
 * Subclasses only need implement the 'evaluatePerson()' method that, given
 * a person, indicates the present value of the quantity in question.
 */
public abstract class IntegerFilter implements Filterer {
  private ResultMaker resultMaker;
  private boolean isMinFilter;

  private boolean onNow = false;
  private Integer value = null;
  
  public IntegerFilter(ResultMaker resultMaker, boolean isMinFilter) {
    this.resultMaker = resultMaker;
    this.isMinFilter = isMinFilter;
    this.onNow = false;
    this.value = null;
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
  
  public abstract Integer evaluatePerson(Person person);

  @Override
  public boolean passes(Person person) {
    if (!onNow) return true;
    Integer personVal = evaluatePerson(person);
    return isMinFilter ? (personVal >= value) : (personVal <= value);
  }

  @Override
  public boolean isOn() {
    return onNow;
  }

}
