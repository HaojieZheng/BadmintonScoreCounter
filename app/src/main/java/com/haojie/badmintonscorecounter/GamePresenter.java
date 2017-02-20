package com.haojie.badmintonscorecounter;

/**
 * Created by Haojie on 2/20/2017.
 */
public class GamePresenter {
    static String getAnnouncementText(Game game, boolean includeServiceCalls)
    {
        String result = "";

        if (game.getWinner() != 0)
        {
            if (game.getWinner() == 1)
            {
                return "Team 1 wins " + game.getTeam1Score() + " " + game.getTeam2Score();
            }
            else
            {
                return "Team 2 wins " + game.getTeam2Score() + " " + game.getTeam1Score();
            }
        }

        if (includeServiceCalls && game.isServiceChanged())
            result += "Service Over ";

        if (game.getTeam1Score() == game.getTeam2Score())
        {
            result += game.getTeam1Score() + " all ";
        }
        else
        {
            Game.PlayerPosition currentServer = game.getCurrentServer();
            String team1Score = game.getTeam1Score() + " ";
            if (game.isGamePoint(1))
                team1Score+="Game Point ";

            String team2Score = game.getTeam2Score() + " ";
            if (game.isGamePoint(2))
                team2Score+="Game Point ";


            if (currentServer == Game.PlayerPosition.Team1Left || currentServer == Game.PlayerPosition.Team1Right)
                result += team1Score + team2Score;
            else
                result += team2Score + team1Score;

        }

        return result;
    }

    static CourtView.Position PlayerPositionToPosition(Game.PlayerPosition p)
    {
        switch (p)
        {
            case Team1Left:
                return CourtView.Position.TopRight;
            case Team1Right:
                return CourtView.Position.TopLeft;
            case Team2Left:
                return CourtView.Position.BottomLeft;
            case Team2Right:
                return CourtView.Position.BottomRight;
        }

        return CourtView.Position.None;
    }
}
