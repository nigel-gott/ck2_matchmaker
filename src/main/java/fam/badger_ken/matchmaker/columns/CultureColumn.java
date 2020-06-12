package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.CultureHandler;
import fam.badger_ken.matchmaker.filter.CultureFilter;
import fam.badger_ken.matchmaker.widget.AnyDropdownable;
import fam.badger_ken.matchmaker.widget.UnsettableComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.Optional;

import static fam.badger_ken.matchmaker.SwingGui.mustNotHaveRow;
import static fam.badger_ken.matchmaker.SwingGui.populateDropdownPane;


public class CultureColumn extends Column {

    public CultureColumn(Matchmaker matchmaker) {
        super("Culture", new CultureHandler(matchmaker));
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return Util.csvEscape(winner.getDisplayCulture(gameConfig));
    }

    // See fam.badger_ken.matchmaker.SwingGui.initialize
    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {

        JPanel cultureFilterPanel = new JPanel();
        cultureFilterPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent evt) {
                if (shouldRepopulateData) {
                    // the first time the traits tab is shown, populate the traits
                    // drop-downs - they have no content until the load happens.
                    populateDropdownPane((JComponent) evt.getSource(),
                            new DropdownMakerHelper() {
                                @Override
                                public Filterer makeFilter(boolean isInclusion) {
                                    return new CultureFilter(resultMaker, isInclusion);
                                }

                                @Override
                                public Collection<? extends AnyDropdownable> getOriginalItems() {
                                    return matchmaker.gameConfig.culturesByLabel.values();
                                }

                                @Override
                                public boolean passes(Filterer filterer) {
                                    return !(filterer instanceof CultureFilter);
                                }
                            }, matchmaker);
                }
                shouldRepopulateData = false;
                //populateTraitsDropdowns((JComponent) evt.getSource(), matchmaker.gameConfig);
            }
        });

        cultureFilterPanel.setLayout(new GridLayout(0, 2, 5, 0));

        JPanel cultureYesPanel = new JPanel();
        cultureFilterPanel.add(cultureYesPanel);
        cultureYesPanel.setLayout(new BoxLayout(cultureYesPanel, BoxLayout.Y_AXIS));

        JLabel label_11 = new JLabel("Must have:");
        label_11.setHorizontalAlignment(SwingConstants.CENTER);
        cultureYesPanel.add(label_11);

        JPanel cultureNoPanel = new JPanel();
        cultureFilterPanel.add(cultureNoPanel);
        cultureNoPanel.setLayout(new BoxLayout(cultureNoPanel, BoxLayout.Y_AXIS));
        cultureNoPanel.add(mustNotHaveRow());

        UnsettableComboBox yesbox = new UnsettableComboBox();
        cultureYesPanel.add(yesbox);
        for (int i = 0; i < 5; i++) {
            cultureNoPanel.add(new UnsettableComboBox());
        }

        return Optional.of(cultureFilterPanel);
    }


}
