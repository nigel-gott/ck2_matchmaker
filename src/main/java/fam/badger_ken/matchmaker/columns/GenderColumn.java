package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.GenderHandler;
import fam.badger_ken.matchmaker.filter.GenderFilter;
import fam.badger_ken.matchmaker.widget.TwoPlusAnyComboBox;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class GenderColumn extends Column {

    public GenderColumn() {
        super("Gender", new GenderHandler(), SHORT_COLUMN_WIDTH);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return winner.isMale ? "m" : "f";
    }

    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final GenderFilter genderFilter = new GenderFilter(resultMaker);
        matchmaker.addFilter(genderFilter);
        JPanel genderFilterPanel = new JPanel();

        TwoPlusAnyComboBox genderComboBox = new TwoPlusAnyComboBox("Male", "Female", genderFilter);
        genderFilterPanel.add(genderComboBox);
        return Optional.of(genderFilterPanel);
    }


}
