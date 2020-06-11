// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import fam.badger_ken.matchmaker.Person;
import fam.badger_ken.matchmaker.ResultMaker;

/**
 * The 'SpousesFilter' filters for min/max # of spouses.
 */
public class SpousesFilter extends IntegerFilter {
  public SpousesFilter(ResultMaker resultMaker, boolean isMinFilter) {
    super(resultMaker, isMinFilter);
  }
  
  @Override
  public Integer evaluatePerson(Person person) {
    return person.getNumSpouses();
  }
}
