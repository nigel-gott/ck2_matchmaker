// c(2012) i.am.badger.ken@gmail.com
// You may re-use, so long as I am credited, and you don't charge
// for your work that uses this.
package fam.badger_ken.matchmaker.filter;

import fam.badger_ken.matchmaker.Filterer;

/**
 * A FiltererFilter takes filterers and indicates if they pass
 * a filter. whew!
 */
public interface FiltererFilter {
  boolean passes(Filterer filterer);
}
