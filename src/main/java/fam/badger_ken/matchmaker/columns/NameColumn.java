package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.NameHandler;
import fam.badger_ken.matchmaker.cell.NameHandler;
import fam.badger_ken.matchmaker.filter.NameFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class NameColumn extends Column {


    public NameColumn(Matchmaker matchmaker) {
        super("Name", new NameHandler(matchmaker), NameFilter.class);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return Util.csvEscape(winner.getDisplayName(gameConfig));
    }

    @Override
    public Component setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final NameFilter nameFilter = new NameFilter(resultMaker, matchmaker.gameConfig);
        matchmaker.addFilter(nameFilter);
        JPanel nameFilterPanel = new JPanel();
        nameFilterPanel.setLayout(new BoxLayout(nameFilterPanel, BoxLayout.Y_AXIS));

        JPanel panel_7 = new JPanel();
        nameFilterPanel.add(panel_7);

        JLabel lblNewLabel_11 = new JLabel("Character Name: ");
        panel_7.add(lblNewLabel_11);

        UnsettableTextField nameField = new UnsettableTextField(20, nameFilter);
        panel_7.add(nameField);

        return nameFilterPanel;
    }


}
