package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.GenderHandler;
import fam.badger_ken.matchmaker.filter.GenderFilter;
import fam.badger_ken.matchmaker.widget.TwoPlusAnyComboBox;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import fam.badger_ken.matchmaker.widget.UnsettableTextField;



public class CopyColumn extends Column {

    // See fam.badger_ken.matchmaker.ViewTable.setColumnWidths to check width
    public CopyColumn() {
        super("", new GenderHandler(), SHORT_COLUMN_WIDTH);
    }

    // See fam.badger_ken.matchmaker.Matchmaker.writeWinners to check
    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return "";
    }

    // See fam.badger_ken.matchmaker.SwingGui.initialize
    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {


        return null;
    }


}
