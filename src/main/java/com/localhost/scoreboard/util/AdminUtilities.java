package com.localhost.scoreboard.util;

import com.localhost.scoreboard.model.Game;

public class AdminUtilities {

    private static String ADMIN_HASH = "GOD";

    public static boolean isAdmin(String hash) {
        return hash != null && hash.equals(ADMIN_HASH);
    }

    public static boolean isCurrent(Game game, String hash) {
        return game.getCurrentPlayer() != null && game.getCurrentPlayer().getHash().equals(hash);
    }
}
