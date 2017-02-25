package com.haojie.badmintonscorecounter;

import android.util.Pair;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Haojie on 2/25/2017.
 */
public class GameStatisticsPresenterTest {
    @Test
    public void getTopNPlayers_empty_database() throws Exception {

        Database database = new Database();
        GameStatisticsPresenter presenter = new GameStatisticsPresenter(database);

        presenter.calculate();

        ArrayList<Pair<Player, Integer>> result = presenter.getTopNPlayers(1);

        assertTrue(result.isEmpty());

    }

}