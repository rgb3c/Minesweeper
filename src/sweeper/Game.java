package sweeper;

public class Game {
    public static final String OPEN = "res/sound/open.wav";
    public static final String FLAG = "res/sound/flag.wav";
    public static final String BOMB = "res/sound/bomb.wav";
    public static final String WIN = "res/sound/levelup.wav";

    private Bomb bomb;
    private Flag flag;
    private GameState state;
    private boolean collision;
    private int collisionCounter = 0;
    Audio audioOpen = new Audio();
    Audio audioFlag = new Audio();
    Audio audioBomb = new Audio();
    Audio audioWin = new Audio();

    public GameState getState() {
        return state;
    }

    public Game (int cols, int rows, int bombs) {
        Ranges.setSize(new Coord(cols, rows));
        bomb = new Bomb(bombs);
        flag = new Flag();
        initAudio();
    }

    private void initAudio() {
        audioOpen.setFile(OPEN);
        audioFlag.setFile(FLAG);
        audioBomb.setFile(BOMB);
        audioWin.setFile(WIN);
        audioOpen.setVolume(-12.0f);
        audioBomb.setVolume(-15.0f);
        audioWin.setVolume(-12.0f);
    }

    public void start() {
        collision = true;
        bomb.start();
        flag.start();
        state = GameState.PLAYED;
    }

    public Box getBox (Coord coord) {
        if (flag.get(coord) == Box.OPENED) {
            return bomb.get(coord);
        }
        return flag.get(coord);
    }

    public void pressLeftButton(Coord coord) {
        if (gameOver()) {
            return;
        }
        openBox(coord);
        checkWinner();
    }

    private void checkWinner() {
        if (state == getState().PLAYED) {
            if (flag.getCountOfClosedBoxes() == bomb.getTotalBombs()) {
                audioWin.play(WIN);
                state = GameState.WINNER;
            }
        }
    }

    private void openBox(Coord coord) {
        switch (flag.get(coord)) {
            case OPENED: setOpenedToClosedBoxesAroundNumber(coord); return;
            case FLAGED: return;
            case CLOSED:
                switch (bomb.get(coord)) {
                    case ZERO: openBoxAround(coord); return;
                    case BOMB: openBombs(coord); return;
                    default:
                        collision = false;
                        flag.setOpenedToBox(coord); return;
                }
        }
    }

    private void setOpenedToClosedBoxesAroundNumber(Coord coord) {
            if (bomb.get(coord) != Box.BOMB) {
                if (flag.getCountOfFlagedBoxesAround(coord) == bomb.get(coord).getNumber()) {
                    for (Coord around : Ranges.getCoordAround(coord)) {
                        if (flag.get(around) == Box.CLOSED) {
                            openBox(around);
                        }
                    }
                }
            }
    }

    private void openBombs(Coord bombed) {
        while (collision) {
            System.out.println("collision" + collisionCounter++);
            start();
            openBox(bombed);
            return;
        }

        state = GameState.BOMBED;
        audioBomb.play(BOMB);
        flag.setBombedToBox(bombed);
        for (Coord coord : Ranges.getAllCoords()) {
            if (bomb.get(coord) == Box.BOMB) {
                flag.setOpenedToClosedBombBox(coord);
            } else {
                flag.setNobombToFlagedSafeBox(coord);
            }
        }
    }

    private void openBoxAround(Coord coord) {
        collision = false;
        audioOpen.play(OPEN);
        flag.setOpenedToBox(coord);
        for (Coord around : Ranges.getCoordAround(coord)) {
            openBox(around);
        }
    }

    public void pressRightButton(Coord coord) {
        if (gameOver()) {
            return;
        }
        audioFlag.play(FLAG);
        flag.toggleFlagedToBox(coord);
    }

    private boolean gameOver() {
        if (state == GameState.PLAYED) {
            return false;
        } else {
            start();
            return true;
        }
    }
    public void muteAll() {
        audioOpen.volumeMute();
        audioFlag.volumeMute();
        audioBomb.volumeMute();
        audioWin.volumeMute();
    }
}
