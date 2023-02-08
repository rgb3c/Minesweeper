import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import sweeper.Box;
import sweeper.Coord;
import sweeper.Game;
import sweeper.Ranges;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class JavaSweeper extends JFrame {

    private Game game;
    private JPanel panel;
    private JLabel label;
    private JMenuBar menu;
    private static int COLS = 9;
    private static int ROWS = 9;
    private static int BOMBS = 10;
    private final int IMAGE_SIZE = 50;
    private static JavaSweeper javaSweeper;

    public static void main(String[] args) {
        javaSweeper = new JavaSweeper();
    }

    private JavaSweeper() {
        menu = new JMenuBar();
        menu.add(createMenu());
        setJMenuBar(menu);
        game = new Game(COLS, ROWS, BOMBS);
        game.start();
        setImages();
        initLabel();
        initPanel();
        initFrame();
    }

    private Component createMenu() {
        JMenu sizeMenu = new JMenu("Settings");
        JMenuItem small = new JMenuItem("9х9");
        JMenuItem normal = new JMenuItem("16х16");
        JMenuItem big = new JMenuItem("16х30");
        JMenuItem setBombs = new JMenuItem("Amount bombs");

        sizeMenu.add(small);
        sizeMenu.add(normal);
        sizeMenu.add(big);
        sizeMenu.addSeparator();
        sizeMenu.add(setBombs);

        small.addActionListener(a -> {
            resizeGame(9,9, 10);
        });

        normal.addActionListener(a -> {
                resizeGame(16,16,40);
        });

        big.addActionListener(a -> {
                resizeGame(30,16,99);
        });

        setBombs.addActionListener(a -> {
            setBombsAction();
        });

        return sizeMenu;
    }

    private void setBombsAction() {
        JDialog jDialog = new JDialog(javaSweeper, "Amount", true);
        JButton jButton1 = new JButton("Ok");
        JTextField jTextField = new JTextField();
        jDialog.add(jButton1, BorderLayout.SOUTH);
        jDialog.add(jTextField, BorderLayout.NORTH);

        jButton1.addActionListener(b -> {

                char[] chars = jTextField.getText().toCharArray();
                StringBuilder sb = new StringBuilder();
                for (char char1 : chars) {
                    if (Character.isDigit(char1)) {
                        sb.append(char1);
                    }
                }
                BOMBS = Integer.parseInt(sb.toString());
                jDialog.setVisible(false);
                resizeGame(COLS, ROWS, BOMBS);
        });

        jDialog.pack();
        jDialog.setResizable(false);
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }

    private static void resizeGame(int newCols, int newRows, int newBombs) {
        COLS = newCols;
        ROWS = newRows;
        BOMBS = newBombs;
        javaSweeper.setVisible(false);
        javaSweeper = new JavaSweeper();
    }

    private void initLabel() {
        label = new JLabel("Welcome!");
        add(label, BorderLayout.SOUTH);
    }

    private void initPanel () {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Coord coord : Ranges.getAllCoords()) {
                    g.drawImage((Image) game.getBox(coord).image,
                            coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE, this);
                }
            }
        };

        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x,y);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    game.pressLeftButton(coord);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    game.pressRightButton(coord);
                } panel.repaint();
                if (e.getButton() == MouseEvent.BUTTON2) {
                    game.start();
                } panel.repaint();
                label.setText(getMessage());
                panel.repaint();
            }
        });

        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x * IMAGE_SIZE,
                Ranges.getSize().y * IMAGE_SIZE));
        add(panel);
    }

    private String getMessage() {
        switch (game.getState()) {
            case PLAYED:
                return "Let's play";
            case BOMBED:
                return "Game over";
            case WINNER:
                return "Win";
            default:
                return "Hello!";
        }
    }

    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Sweeper");
        setResizable(false);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
        setIconImage(getImage("icon"));
    }

    private void setImages() {
        for (Box box : Box.values()) {
            box.image = getImage(box.name());
        }
    }

    private Image getImage (String name) {
        String filename = "img/" + name.toLowerCase(Locale.ROOT) + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }
}
