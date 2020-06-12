package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.AttributeHandler;
import fam.badger_ken.matchmaker.cell.MarriageHandler;
import fam.badger_ken.matchmaker.filter.AttributeFilter;
import fam.badger_ken.matchmaker.filter.SpousesFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class AttributeColumn extends Column {

    public final int attributeOrd;
    public final String attributeName;

    public AttributeColumn(String attributeName, Matchmaker matchmaker, int attributeOrd) {
        super(attributeName, new AttributeHandler(matchmaker, attributeOrd), 65);
        this.attributeOrd = attributeOrd;
        this.attributeName = attributeName;
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return String.valueOf(winner.getAdjustedAttribute(gameConfig, attributeOrd));
    }

    // Setup by manual methods in SwingGui as we have one tab for all 5 attributes hence we can't make a tab for each
    // one in here
    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {



        return Optional.empty();
    }


}
