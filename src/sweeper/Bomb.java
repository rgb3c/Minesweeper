package sweeper;

import java.util.Random;

class Bomb {
    private Matrix bombMap;
    private int totalBombs;

    Bomb (int totalBombs) {
        this.totalBombs = totalBombs;
    }
    void start() {
        bombMap = new Matrix(Box.ZERO);
        for (int i = 0; i < totalBombs; i++) {
            placeBomb();
        }

    }

    Box get (Coord coord) {
        return bombMap.get(coord);
    }

    private void placeBomb() {
        Coord coord = Ranges.getRandomCord();
        bombMap.set(coord, Box.BOMB);
        incNumbersAroundBomb(coord);
    }
    private void incNumbersAroundBomb(Coord coord) {
        for (Coord around : Ranges.getCoordAround(coord)) {
            if (Box.BOMB != bombMap.get(around)) {
                bombMap.set(around, bombMap.get(around).getNextNumberBox());
            }
        }
    }
}
