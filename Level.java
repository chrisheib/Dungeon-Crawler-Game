import java.util.Arrays;

/**
 * @author Christoph Heibutzki 4573456 Gruppe 7c
 * @coauthor: Julia Meier-Grotrian 4506300 Gruppe 7
 */
public class Level {
    /** Kartenzeichen */
    private char[][] mapData;
    /** Hoehe der Karte */
    private int height;
    /** Startposition des Spielers */
    private int[] start = new int[2];
    /** Monsterverknuepfungsdaten, mit int[x] = Monster und = int[][x] = koordinate. */
    private int[][] monsterData;
    /** monsterAnzahl */
    private int monsterAnzahl;
    /** Ist die Zahl der Kampffelder = monsterZahl? */
    private boolean monsterResolved = false;

    /**
     * Erstellt ein Level.
     * @param mapData Kartenzeichen, die die Karte darstellen.
     * @param monsterAnzahl erwartete Monseterzahl.
     */
    public Level(char[][] mapData, int monsterAnzahl) {
        this.mapData = mapData;
        this.monsterAnzahl = monsterAnzahl;
        this.monsterData = new int[monsterAnzahl][2];
        this.height = this.mapData.length;
        int monsterZaehler = 0;
        int[] check = new int[2];
        for (check[0] = 0; check[0] < this.mapData.length; check[0]++) {
            for (check[1] = 0; check[1] < this.mapData[check[0]].length; check[1]++) {
                char checkLetter = this.mapData[check[0]][check[1]];
                if (checkLetter == 'S') {
                    this.start = check.clone();
                }
                if (checkLetter == 'B') {
                    monsterData[monsterZaehler] = check.clone();
                    monsterZaehler++;
                }
            }
        }
        if (monsterZaehler == this.monsterAnzahl) {
            this.monsterResolved = true;
        }
    }

    /**
     * Gibt die Art eines angeforderten Feldes zurueck.
     * @param position Koordinaten des zu ueberpruefenden Feldes mit [0] = y and [1] = x.
     * @return <ul>
     *     <li>-2: field is of unknown type.</li>
     *     <li>-1: invalid field is checked.</li>
     *     <li>0: field is blocked.</li>
     *     <li>1: field is empty</li>
     *     <li>2: field is combat</li>
     *     <li>3: field is healing well.</li>
     *     <li>4: field is forge.</li>
     *     <li>5: field is target.</li>
     * </ul>
     */
    public int getPositionType(int[] position) {
        char pos;
        try {
            pos = this.mapData[position[0]][position[1]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
        switch (pos) {
            case '.':
                return 1;
            case 'T':
                return 4;
            case 'O':
                return 3;
            case 'B':
                return 2;
            case 'x':
                return 0;
            case 'Z':
                return 5;
            case '#':
                return 0;
            default:
                return -2;

        }

    }

    /**
     * Returns start position.
     * @return int[2] with [0] being y and [1] being x.
     */
    public int[] getStart() {
        return this.start;
    }

    /**
     * Getter for height.
     * @return Arraylength of this.mapData.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns a graphical version of the mapData as String[this.getHeight].
     * @param playerPosition Playerpos with [0] = y and [1] = x.
     * @return this.mapData as String[].
     */
    public String[] showField(int[] playerPosition) {
        String[] returnString = new String[this.height];
        for (int check = 0; check < this.height; check++) {
            returnString[check] = "";
            for (int check2 = 0; check2 < this.mapData[check].length; check2++) {
                if (playerPosition[0] == check && playerPosition[1] == check2) {
                    returnString[check] += 'P';
                    continue;
                }
                returnString[check] += this.mapData[check][check2];
            }
        }
        return returnString;
    }

    /**
     * 'Leere' das angegebene Feld.
     * @param position Koordinaten des zu leerenden Felder.
     */
    public void clear(int[] position) {
        try {
            this.mapData[position[0]][position[1]] = '.';
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ungueltiges Feld soll geleert werden!!!");
        }
    }

    /**
     * Hole den Index des Monsters, das zu einer Position gehoert.
     * @param position Zu pruefende Koordinaten.
     * @return Index des Monsters, -1 wenn kein passendes Monster fuer die Position.
     */
    public int getMonsterData(int[] position) {
        for (int i = 0; i < this.monsterAnzahl; i++) {
            if (Arrays.equals(this.monsterData[i], position)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Getter fuer Monsterressolved.
     * @return True, wenn jedes Monster ein Kampffeld hat.
     */
    public boolean isMonsterResolved() {
        return this.monsterResolved;
    }

}
