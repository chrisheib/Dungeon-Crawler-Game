import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Crawler-Klasse.
 * @author Christoph Heibutzki 4573456 Gruppe 7c
 * @coauthor: Julia Meier-Grotrian 4506300 Gruppe 7
 */
public class Crawler {

    /** Scanner */
    public static Scanner sc = new Scanner(System.in);

    /**
     * Mainmethode fuer das Spiel
     * @param args ungenutzt.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Player sp;
        char[][] mapData;
        Monster[] mo;
        int monsterAnzahl;
    method:
        while (true) {
            System.out.println("Welches Eingabesystem soll genutzt werden?");
            System.out.println("\t1: Zufall.");
            System.out.println("\t2: Datei/Default");
            try {
                int input = sc.nextInt();
                switch (input) {
                    case 1:
                        int height, width;
                        MazeGenerator gen = new RecursiveBacktracker();
                    enterHeight:
                        while (true) {
                            System.out.print("Bitte Hoehe der Karte eingeben: ");
                            try {
                                height = sc.nextInt();
                                break enterHeight;
                            } catch (InputMismatchException e) {
                                System.out.println(sc.next() + " ist ungueltig, bitte gueltige Eingabe machen.");
                            }
                        }
                    enterWidth:
                        while (true) {
                            System.out.print("Bitte Breite der Karte eingeben: ");
                            try {
                                width = sc.nextInt();
                                break enterWidth;
                            } catch (InputMismatchException e) {
                                System.out.println(sc.next() + " ist ungueltig, bitte gueltige Eingabe machen.");
                            }
                        }

                        try {
                            mapData = gen.generate(height, width);
                            mo = gen.getMonsters();
                        } catch (IllegalArgumentException e) {
                            System.out.println("Ungueltige Groesse. Bitte Groesse ueber 7x7 "
                                    + "und ungerade Hoehe und Breite nutzen.");
                            continue method;
                        }
                        sp = new Player(100, 10, 30, 3, 100, 10, 0.75, 5);
                        break method;
                    case 2: {
                        //Karte aus Datei
                        Path mapPath = Paths.get(".", "map.txt");
                        try {
                            List<String> mapRaw = Files.readAllLines(mapPath, Charset.forName("UTF-8"));
                            Iterator<String> mapIterator = mapRaw.iterator();
                            mapData = new char[mapRaw.size()][];
                            for (int i = 0; mapIterator.hasNext(); i++) {
                                mapData[i] = mapIterator.next().trim().toCharArray();
                            }
                        } catch (IOException z) {
                            mapData = new char[][]{
                                {'S', 'O', '.'},
                                {'T', 'x', 'B'},
                                {'B', 'x', 'T'},
                                {'B', 'Z', 'x'}
                            };
                        }

                        //Monster aus Datei
                        Path monsterPath = Paths.get(".", "mons.txt");
                        try {
                            List<String> monsRaw = Files.readAllLines(monsterPath, Charset.forName("UTF-8"));
                            Iterator<String> monsIterator = monsRaw.iterator();
                            monsterAnzahl = monsRaw.size();
                            mo = new Monster[monsterAnzahl];
                            for (int i = 0; monsIterator.hasNext(); i++) {
                                String[] tempStringArray = monsIterator.next().trim().split(" ");
                                switch (tempStringArray[0]) {
                                    case "Monster":
                                        mo[i] = new Monster(Integer.parseInt(tempStringArray[1]),
                                                Integer.parseInt(tempStringArray[2]),
                                                Double.parseDouble(tempStringArray[3]), tempStringArray[4]);
                                        break;
                                    case "Healer":
                                        mo[i] = new Healer(Integer.parseInt(tempStringArray[1]),
                                                Integer.parseInt(tempStringArray[2]),
                                                Double.parseDouble(tempStringArray[3]),
                                                Integer.parseInt(tempStringArray[4]), tempStringArray[5]);
                                        break;
                                    case "Warlord":
                                        mo[i] = new Warlord(Integer.parseInt(tempStringArray[1]),
                                                Integer.parseInt(tempStringArray[2]),
                                                Double.parseDouble(tempStringArray[3]),
                                                Integer.parseInt(tempStringArray[4]), tempStringArray[5]);
                                        break;
                                    default:
                                        break;
                                }
                                System.out.println("Monster found: " + mo[i].toString());
                            }
                        } catch (IOException e) {
                            monsterAnzahl = 3;
                            mo = new Monster[monsterAnzahl];
                            mo[0] = new Monster(10, 150, 0.7, "Hugo");
                            mo[1] = new Healer(100, 10, 0.65, 5, "Karl");
                            mo[2] = new Warlord(120, 10, 0.7, 3, "Eleonora");
                        }

                        //Spieler aus Datei

                        Path playerPath = Paths.get(".", "play.txt");
                        try {
                            List<String> playRaw = Files.readAllLines(playerPath, Charset.forName("UTF-8"));
                            String[] tempStringArray = playRaw.get(0).trim().split(" ");
                            sp = new Player(Integer.parseInt(tempStringArray[0]), Integer.parseInt(tempStringArray[1]),
                                    Integer.parseInt(tempStringArray[2]), Integer.parseInt(tempStringArray[3]),
                                    Integer.parseInt(tempStringArray[4]), Integer.parseInt(tempStringArray[5]),
                                    Double.parseDouble(tempStringArray[6]), Integer.parseInt(tempStringArray[7]));
                        } catch (IOException e) {
                            sp = new Player(100, 10, 30, 3, 100, 10, 0.75, 5);
                        }
                        break method;
                    }
                    default:
                        System.out.println("Bitte gueltige Eingabe machen!");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Bitte guelitge Eingabe machen! (" + sc.next() + ")");
            }
        }




        Level m = new Level(mapData, mo.length);

        int[] playerPosition = m.getStart();
        int[] nextPosition;
        m.clear(playerPosition);

    crawl:
        while (true) {
            if (!m.isMonsterResolved()) {
                System.out.print("Monsterzahl stimmt nicht mit Kampffeldern ueberein!");
                enter();
                break;
            }
            nextPosition = new int[2];
            String eingabe;
            char[] eingabeCharArray;
            String[] mapStringArray = m.showField(playerPosition);
            System.out.println();
            for (int i = 0; i < m.getHeight(); i++) {
                System.out.println(mapStringArray[i]);
            }
            System.out.println();
            String tempBewegung = getMovement(playerPosition, m);
            System.out.print(tempBewegung);
            try {
                eingabe = sc.next();
            } catch (InputMismatchException e) {
                System.out.print("Fehler, korrekte Richtung eingeben! (" + sc.next() + ")");
                enter();
                continue;
            }
            eingabeCharArray = eingabe.toCharArray();
            if (eingabeCharArray.length != 1) {
                System.out.print("Bitte korrekte Richtung eingeben!");
                enter();
                continue;
            }
            char eingabeChar;
            eingabeChar = eingabeCharArray[0];
            switch (eingabeChar) {
                case 'n':
                    nextPosition[0] = playerPosition[0] - 1;
                    nextPosition[1] = playerPosition[1];
                    break;
                case 's':
                    nextPosition[0] = playerPosition[0] + 1;
                    nextPosition[1] = playerPosition[1];
                    break;
                case 'w':
                    nextPosition[0] = playerPosition[0];
                    nextPosition[1] = playerPosition[1] - 1;
                    break;
                case 'e':

                case 'o':
                    nextPosition[0] = playerPosition[0];
                    nextPosition[1] = playerPosition[1] + 1;
                    break;
                default:
                    System.out.print("Bitte korrekte Richtung eingeben!");
                    Crawler.enter();
                    continue crawl;
            }
            int fieldCheck = m.getPositionType(nextPosition);
            switch (fieldCheck) {
                case -2:
                    System.out.print("FEHLER! Unbekanntes Feld.");
                    enter();
                    break crawl;
                case -1:
                    System.out.print("Bitte auf ein gueltiges Feld bewegen.");
                    enter();
                    continue crawl;
                case 0:
                    System.out.print("Feld ist nicht zugaenglich.");
                    enter();
                    continue crawl;
                case 1:
                    playerPosition = nextPosition;
                    continue crawl;
                case 2:
                    int tempMonsterData = m.getMonsterData(nextPosition);
                    Combat c = new Combat(sp, mo[tempMonsterData], sc);
                    if (sp.isDefeated()) {
                        System.out.print("Du hast verloren! Spiel vorbei!");
                        enter();
                        break crawl;
                    } else if (mo[tempMonsterData].isDefeated()) {
                        playerPosition = nextPosition;
                        m.clear(playerPosition);
                        continue crawl;
                    }
                case 3:
                    sp.useHealingWell();
                    m.clear(nextPosition);
                    playerPosition = nextPosition;
                    System.out.print("Du bist wieder vollgeheilt!");
                    enter();
                    continue crawl;
                case 4:
                    sp.useForge();
                    m.clear(nextPosition);
                    playerPosition = nextPosition;
                    System.out.print("Du hast eine Schmiede genutzt! Neue ATK: " + sp.getAtk());
                    enter();
                    continue crawl;
                case 5:
                    System.out.print("Du hast dein Ziel erreicht!!! Herzlichen Glueckwunsch.");
                    enter();
                    break crawl;
                default:
                    break;
            }
        }

    }

    /**
     * Prueft Bewegungsmoeglichkeiten eines Spielers.
     * @param playerPosition Spieler-koordinaten.
     * @param m zu ueberpruefende Karte.
     * @return "Bitte Bewegungsrichtung eingeben: ( < Richtungen > ).
     */
    public static String getMovement(int[] playerPosition, Level m) {

        int[] nextPosition = new int[2];
        String returnString = "Bitte Bewegungsrichtung eingeben: (";

        nextPosition[0] = playerPosition[0] - 1;
        nextPosition[1] = playerPosition[1];
        if (m.getPositionType(nextPosition) > 0) {
            returnString += "n, ";
        }
        nextPosition[0] = playerPosition[0];
        nextPosition[1] = playerPosition[1] + 1;
        if (m.getPositionType(nextPosition) > 0) {
            returnString += "o, ";
        }
        nextPosition[0] = playerPosition[0] + 1;
        nextPosition[1] = playerPosition[1];
        if (m.getPositionType(nextPosition) > 0) {
            returnString += "s, ";
        }
        nextPosition[0] = playerPosition[0];
        nextPosition[1] = playerPosition[1] - 1;
        if (m.getPositionType(nextPosition) > 0) {
            returnString += "w, ";
        }
        returnString += "): ";
        return returnString;
    }

    /**
     * Aufruf laesst die Konsole auf Eingabe von Enter warten.
     */
    public static void enter() {
        try {
            sc.nextLine();
        } catch (InputMismatchException ignored) {

        }

    }

}
