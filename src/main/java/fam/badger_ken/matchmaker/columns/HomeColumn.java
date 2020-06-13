package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.HomeHandler;
import fam.badger_ken.matchmaker.filter.HomeFilter;
import fam.badger_ken.matchmaker.widget.UnsettableTextField;

import javax.swing.*;
import java.awt.*;


public class HomeColumn extends Column {

    public HomeColumn(Matchmaker matchmaker) {
        super("Home", new HomeHandler(matchmaker), HomeFilter.class);
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return winner.getHomeName(gameConfig, saveState);
    }

    @Override
    public Component setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        final HomeFilter homeFilter = new HomeFilter(resultMaker, matchmaker.gameConfig, matchmaker.saveState);
        matchmaker.addFilter(homeFilter);
        JPanel homeFilterPanel = new JPanel();
        homeFilterPanel.setLayout(new BoxLayout(homeFilterPanel, BoxLayout.Y_AXIS));

        JPanel panel_7 = new JPanel();
        homeFilterPanel.add(panel_7);

        JLabel lblNewLabel_11 = new JLabel("Home County: ");
        panel_7.add(lblNewLabel_11);

        UnsettableTextField homeTextField = new UnsettableTextField(20, homeFilter);
        panel_7.add(homeTextField);

        return homeFilterPanel;
    }


}
