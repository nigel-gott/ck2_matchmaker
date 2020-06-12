// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import fam.badger_ken.matchmaker.Person;
import fam.badger_ken.matchmaker.ResultMaker;

/**
 * The 'GenderFilter' filters for gender.
 */
public class HasArtifactFilter extends YesNoAnyFilter {

  public HasArtifactFilter(ResultMaker resultMaker) {
    super(resultMaker, "Has Artifact", "No Artifacts");
  }

  @Override
  public boolean passes(Person person) {
    if (!isOn()) return true;
    return !person.artifacts.isEmpty() == wantsYes();
  }
}
