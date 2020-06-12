package fam.badger_ken.matchmaker;

import fam.badger_ken.matchmaker.filter.FiltererFilter;
import fam.badger_ken.matchmaker.widget.AnyDropdownable;

import java.util.Collection;

/**
 * This helper class is a bit of a 'factory', it helps all the
 * various panes that generate a set of drop-down boxes with an 'any'
 * value to rule things in/out share the same logic.
 */
public interface DropdownMakerHelper extends FiltererFilter {
    /**
     * @return the original set of items that are copied into each dropdown.
     * They are copied because otherwise Swing requires that they share the
     * same selection value.
     */
    public Collection<? extends AnyDropdownable> getOriginalItems();

    /**
     * @param isInclusion whether the dropdown box is inclusive.
     * @return a new filter to attach to a new dropdown box.
     */
    public Filterer makeFilter(boolean isInclusion);
}
