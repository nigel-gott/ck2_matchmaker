package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.GenderHandler;
import fam.badger_ken.matchmaker.cell.HoldingsHandler;
import fam.badger_ken.matchmaker.cell.RulerHandler;
import fam.badger_ken.matchmaker.filter.HoldingLevelFilter;
import fam.badger_ken.matchmaker.filter.RulerFilter;
import fam.badger_ken.matchmaker.widget.AnyDropdownable;
import fam.badger_ken.matchmaker.widget.AnyItemForDropdown;
import fam.badger_ken.matchmaker.widget.UnsettableComboBox;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.Vector;


public class HoldingsColumn extends Column {

    public HoldingsColumn(Matchmaker matchmaker) {
        super("Holdings", new HoldingsHandler(matchmaker));
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return Util.csvEscape(winner.getDisplayableHoldings(gameConfig));
    }

    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        JPanel rulerFilterPanel = new JPanel();
        rulerFilterPanel.setLayout(new BoxLayout(rulerFilterPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < 2; i++) {
            JPanel row1Panel = new JPanel();
            row1Panel.setLayout(new BoxLayout(row1Panel, BoxLayout.X_AXIS));

            //TwoPlusAnyComboBox rulerComboBox = new TwoPlusAnyComboBox("Yes", "No", rulerFilter);
            //rulerFilterPanel.add(rulerComboBox);
            UnsettableComboBox holdingsBox = new UnsettableComboBox();
            Vector<AnyDropdownable> entries = new Vector<AnyDropdownable>();
            entries.add(new AnyItemForDropdown());
            entries.add(HoldingLevel.NONE);
            entries.add(HoldingLevel.BARONY);
            entries.add(HoldingLevel.COUNTY);
            entries.add(HoldingLevel.DUCHY);
            entries.add(HoldingLevel.KINGDOM);
            entries.add(HoldingLevel.EMPIRE);
            boolean isMin = (i == 0);
            Filterer filter = new HoldingLevelFilter(resultMaker, isMin);
            matchmaker.addFilter(filter);
            holdingsBox.populate(entries, filter);
            //holdingsBox.setBorder(BorderFactory.createEmptyBorder(0,50,0,150));
            holdingsBox.setMaximumSize(new Dimension(500, 30));

            row1Panel.add(new JLabel(isMin ? "minimum holdings level: " : "maximum holdings level"));
            row1Panel.add(holdingsBox);
            JLabel spacer = new JLabel("");
            spacer.setMinimumSize(new Dimension(250, 0));
            row1Panel.add(spacer);
            rulerFilterPanel.add(row1Panel);
        }

        return Optional.of(rulerFilterPanel);
    }


}
