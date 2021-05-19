/**
 * @author Christoph Heibutzki 4573456 Gruppe 7
 * @coauthor: Julia Meier-Grotrian 4506300 Gruppe 7
 */
public class Raum {
    /** Feld besucht? */
    private boolean visited;
    /** Position auf der Karte. */
    private int[] position;
    /** Aufrufender Backtracker, der zur erkennung der Nachbarn genutzt wird. */
    private RecursiveBacktracker mg;
    /** Zeichen. */
    private char value;
    /** Gueltig oder Abfall. */
    private boolean valid;
    /** Anzahl der abfuehrenden Wege */
    private int connections;
    /** Distanz vom Startknoten = Iterationsstufen des Rekursiven Aufrufs. */
    private int distance;
    /** Zugewiesenes Monster. */
    private Monster mo;

    /**
     * Standardkonstruktor.
     * @param position Koordinaten.
     * @param mg Aufrufender Backtracker, dient der Nach barfindung.
     * @param value Zeichen, das Dargestellt wird.
     */
    public Raum(int[] position, RecursiveBacktracker mg, char value) {
        this.position = position;
        this.mg = mg;
        this.value = value;
        this.visited = false;
        this.valid = true;
        this.connections = 0;
        this.distance = 0;
    }

    /**
     * Konstruktor fuer Abfallraum.
     */
    public Raum() {
        this.valid = false;
        this.visited = true;
        this.distance = -1;
    }

    /**
     * Getter Connections.
     * @return Abfuehrende Wege.
     */
    public int getConnections() {
        return this.connections;
    }

    /**
     * Erhoeht Connection-Zahl um 1.
     */
    public void incrementConnections() {
        this.connections++;
    }

    /**
     * Setter fuer Connections.
     * @param connections Connection-Anzahl.
     */
    public void setConnections(int connections) {
        this.connections = connections;
    }

    /**
     * Setter Visited
     * @param visited Raum besucht?
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Getter visited.
     * @return Raum besucht?
     */
    public boolean isVisited() {
        return this.visited;
    }

    /**
     * Getter Positionskoordinaten.
     * @return Positionskoordinaten.
     */
    public int[] getPosition() {
        return this.position;
    }

    /**
     * Setter Zeichen.
     * @param value Zeichen.
     */
    public void setValue(char value) {
        this.value = value;
    }

    /**
     * Raum gueltig?
     * @return Abfallraum?
     */
    public boolean isValid() {
        return this.valid;
    }

    /**
     * Getter Zeichen.
     * @return Zeichen.
     */
    public char toChar() {
        return this.value;
    }

    /**
     * Gibt Nachbarn des Raumes auf der beim construieren erstellten Karte aus.
     * @return <ul>
     *     <li>[0] = oben</li>
     *     <li>[1] = rechts</li>
     *     <li>[2] = unten</li>
     *     <li>[3] = links</li>
     * </ul>
     */
    public Raum[] getNeighbours() {
        Raum[] toReturn = new Raum[4];
        if (position == null) {
            System.out.println("fehler");
        }
        int y = this.position[0];
        int x = this.position[1];
        toReturn[0] = this.mg.getMapData(y - 2, x);
        toReturn[1] = this.mg.getMapData(y, x + 2);
        toReturn[2] = this.mg.getMapData(y + 2, x);
        toReturn[3] = this.mg.getMapData(y, x - 2);

        return toReturn;
    }

    /**
     * Verlaengert ein Raumarray um einen Raum.
     * @param source Array, das erweitert wird.
     * @param toAdd Element, um den erweitert wird.
     * @return Fertiges Raumarray.
     */
    public static Raum[] raumArrayAppend(Raum[] source, Raum toAdd) {
        Raum[] neu = new Raum[source.length + 1];
        for (int i = 0; i < source.length; i++) {
            neu[i] = source[i];
        }
        neu[neu.length - 1] = toAdd;
        return neu;
    }

    /**
     * Erstelle ein Monster, dessen Staerke mit der relativen Entfernung des Raumes zum Start skaliert.
     * @param gesamtLaenge Maximaler Weglaenge.
     */
    public void createMonster(int gesamtLaenge) {
        int random = (int) (Math.random() * 3);
        int hp = 80 + (int) ((Math.random() * 40) + 1 + (((double) this.distance / (double) gesamtLaenge) * 50));
        int atk = 10 + (int) ((Math.random() * 4) + 1 + (((double) this.distance / (double) gesamtLaenge) * 5));
        int heal = 3 + (int) ((Math.random() * 2) + 1 + (((double) this.distance / (double) gesamtLaenge) * 3));
        double hC = 0.4 + ((Math.random() * 0.15) + 0.01 + (((double) this.distance / (double) gesamtLaenge) * 0.2));
        int rage = 3;
        switch (random) {
            case 0:
                this.mo = new Monster(hp, atk, hC, "RandomMonster");
                break;
            case 1:
                this.mo = new Healer(hp, atk, hC, heal, "RandomHealer");
                break;
            case 2:
                this.mo = new Warlord(hp, atk, hC, rage, "RandomWarlord");
                break;
            default:
                System.out.println("FAIL");
        }
    }

    /**
     * Getter Monster.
     * @return Monster, das vorher mit create Monster erstellt werden muss.
     */
    public Monster getMonster() {
        return this.mo;
    }

    /**
     * Getter fuer Distanz zu Start.
     * @return Distanz zu Start.
     */
    public int getDistance() {
        return this.distance;
    }

    /**
     * Setter fuer Distanz zu start.
     * @param distance Distanz zu Start.
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Loesche eine Raumreferenz aus einem Array.
     * @param source Zu bearbeitendes Array.
     * @param toDelete Zu loeschender Raum.
     * @return Raumarray ohne Raumreferenz des zu loeschenden.
     */
    public static Raum[] deleteFromRaumArray(Raum[] source, Raum toDelete) {
        Raum[] neu = new Raum[source.length - 1];
        for (int i = 0, j = 0; i < source.length; i++) {
            if (source[i] != toDelete) {
                neu[j] = source[i];
                j++;
            }
        }
        return neu;
    }

}
