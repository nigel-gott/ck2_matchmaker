package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.GenderHandler;
import fam.badger_ken.matchmaker.cell.TraitsHandler;
import fam.badger_ken.matchmaker.filter.TraitFilter;
import fam.badger_ken.matchmaker.widget.AnyDropdownable;
import fam.badger_ken.matchmaker.widget.UnsettableComboBox;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.Optional;

import static fam.badger_ken.matchmaker.SwingGui.mustNotHaveRow;
import static fam.badger_ken.matchmaker.SwingGui.populateDropdownPane;


public class TraitsColumn extends Column {

    public TraitsColumn(Matchmaker matchmaker) {
        super("Traits", new TraitsHandler(matchmaker));
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return Util.csvEscape(winner.getDisplayTraits(gameConfig, saveState));
    }

    // See fam.badger_ken.matchmaker.SwingGui.initialize
    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {

        JPanel traitsFilterPanel = new JPanel();
        traitsFilterPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent evt) {
                // the first time the traits tab is shown, populate the traits
                // drop-downs - they have no content until the load happens.
                if (shouldRepopulateData) {
                    populateDropdownPane((JComponent) evt.getSource(),
                            new DropdownMakerHelper() {
                                @Override
                                public Filterer makeFilter(boolean isInclusion) {
                                    return new TraitFilter(resultMaker, isInclusion);
                                }

                                @Override
                                public Collection<? extends AnyDropdownable> getOriginalItems() {
                                    return matchmaker.gameConfig.traits.values();
                                }

                                @Override
                                public boolean passes(Filterer filterer) {
                                    return !(filterer instanceof TraitFilter);
                                }
                            }, matchmaker);
                }
                shouldRepopulateData = false;
                //populateTraitsDropdowns((JComponent) evt.getSource(), matchmaker.gameConfig);
            }
        });
        traitsFilterPanel.setLayout(new BoxLayout(traitsFilterPanel, BoxLayout.X_AXIS));

        JPanel traitsYesPanel = new JPanel();
        traitsYesPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        traitsFilterPanel.add(traitsYesPanel);
        traitsYesPanel.setLayout(new BoxLayout(traitsYesPanel, BoxLayout.Y_AXIS));

        JLabel lblNewLabel_6 = new JLabel("Must have:");
        traitsYesPanel.add(lblNewLabel_6);

        for (int i = 0; i < 5; i++) {
            traitsYesPanel.add(new UnsettableComboBox());
        }

        JPanel traitsNoPanel = new JPanel();
        traitsNoPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        traitsFilterPanel.add(traitsNoPanel);
        traitsNoPanel.setLayout(new BoxLayout(traitsNoPanel, BoxLayout.Y_AXIS));

        traitsNoPanel.add(mustNotHaveRow());

        for (int i = 0; i < 5; i++) {
            traitsNoPanel.add(new UnsettableComboBox());
        }

        return Optional.of(traitsFilterPanel);
    }


}
