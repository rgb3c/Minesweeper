package sweeper;

public class Game {

    private Bomb bomb;
    private Flag flag;
    private GameState state;

    public GameState getState() {
        return state;
    }

    public Game (int cols, int rows, int bombs) {
        Ranges.setSize(new Coord(cols, rows));
        bomb = new Bomb(bombs);
        flag = new Flag();
    }

    public void start() {
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
        openBox(coord);
    }

    private void openBox(Coord coord) {
        switch (flag.get(coord)) {
            case OPENED: return;
            case FLAGED: return;
            case CLOSED:
                switch (bomb.get(coord)) {
                    case ZERO: return;
                    case BOMB: return;
                    default: flag.setOpenedToBox(coord); return;
                }
        }
    }

    public void pressRightButton(Coord coord) {
        flag.toggleFlagedToBox(coord);
    }
}
