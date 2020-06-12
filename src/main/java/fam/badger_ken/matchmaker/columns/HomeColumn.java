package fam.badger_ken.matchmaker.columns;

import fam.badger_ken.matchmaker.*;
import fam.badger_ken.matchmaker.cell.GenderHandler;
import fam.badger_ken.matchmaker.cell.HomeHandler;

import java.awt.*;
import java.util.Optional;


public class HomeColumn extends Column {

    public HomeColumn(Matchmaker matchmaker) {
        super("Home", new HomeHandler(matchmaker));
    }

    @Override
    public String convertPersonToCsv(Person winner, GameConfig gameConfig, SaveState saveState) {
        return winner.getHomeName(gameConfig, saveState);
    }

    @Override
    public Optional<Component> setupFiltersAndMakeTab(Matchmaker matchmaker, ResultMaker resultMaker) {
        return Optional.empty();

    }


}
