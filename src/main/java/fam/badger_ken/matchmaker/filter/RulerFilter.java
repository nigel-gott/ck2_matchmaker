// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import fam.badger_ken.matchmaker.Person;
import fam.badger_ken.matchmaker.ResultMaker;

/**
 * The 'RulerFilter' filters for whether they are a ruler.
 */
public class RulerFilter extends YesNoAnyFilter {
  public RulerFilter(ResultMaker resultMaker) {
    super(resultMaker, "Yes", "No");
  }

  @Override
  public boolean passes(Person person) {
    if (!isOn()) return true;
    return person.isRuler() == wantsYes();
  }	
}
