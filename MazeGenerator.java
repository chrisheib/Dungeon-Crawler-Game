/**
 * @author Christoph Heibutzki 4573456 Gruppe 7
 * @coauthor: Julia Meier-Grotrian 4506300 Gruppe 7
 */
public interface MazeGenerator {
    /** Definition fuer Wallzeichen */
    char WALL = '#';
    /** Definition fuer Frei-Zeichen */
    char FREE = '.';
    /** Definition fuer Startzeichen */
    char START = 'S';
    /** Definition fuer Kampfzeichen */
    char BATTLE = 'B';
    /** Definition fuer Schmiedenzeichen */
    char SMITHY = 'T';
    /** Definition fuer Brunnenzeichen */
    char WELL = 'O';
    /** Definition fuer Zielzeichen */
    char GOAL = 'Z';

    /**
     * Abstrakte Kartengenerationsklasse.
     * @param height Hoehe der zu generierenden Karte.
     * @param width Weite der Karte.
     * @return char[][], das die Karte darstellt.
     * @throws IllegalArgumentException Fehlerabfang, wenn die Laenge ein Problem darstellt.
     */
    char[][] generate(int height, int width) throws IllegalArgumentException;

    /**
     * Abstrake Monsteruebergabeklasse.
     * @return Monsterarray, das mit der Karte synchronisiert ist.
     */
    Monster[] getMonsters();
}
