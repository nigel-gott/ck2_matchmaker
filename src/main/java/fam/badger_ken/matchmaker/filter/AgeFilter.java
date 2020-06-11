// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import fam.badger_ken.matchmaker.Person;
import fam.badger_ken.matchmaker.ResultMaker;

/**
 * The 'AgeFilter' filters for either min age, or max age.
 */
public class AgeFilter extends IntegerFilter {

  public AgeFilter(ResultMaker resultMaker, boolean isMinFilter) {
    super(resultMaker, isMinFilter);
  }

  @Override
  public Integer evaluatePerson(Person person) {
    return person.ageInYears;
  }
}
