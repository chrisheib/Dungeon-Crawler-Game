import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * @author Christoph Heibutzki 4573456 Gruppe 7c
 * @coauthor: Julia Meier-Grotrian 4506300 Gruppe 7
 */
public class Combat {

    /** variable Fehler */
    private int fehler = 0;

    /** variable BattleOver */
    private boolean battleOver = false;

    /**
     * Constructor fuer Combat, erstellt einen Kampf inclusive der Konsolenausgabe.
     * @param sp Der kaempfende Spieler.
     * @param mo Das angegriffene Monster.
     * @param sc Der Scanner, der fuer die Eingabe genutzt wird.
     */
    public Combat(Player sp, Character mo, Scanner sc) {
        Integer runde = 1;
        int eingabe, spApReg;
        System.out.println();
        System.out.println(mo.getName() + " erscheint!!!");
        System.out.println();
    kampf : 
        while (true) {
            rundenAnfang(sp, mo, runde);
            System.out.print("Eingabe: ");
            try {
                eingabe = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Fehler, keine Zahl! (" + sc.next() + ")");
                Crawler.enter();
                warte(1000);
                continue;
            }
            switch (eingabe) {
                case 1:
                    System.out.println();
                    System.out.print("Angriff! ");
                    warte(500);
                    int spDmg = sp.attack(mo);
                    if (spDmg > 0) {
                        System.out.print("Treffer an " + mo.getName() + "! Schaden: " + spDmg);
                        Crawler.enter();
                        if (mo.isDefeated()) {
                            warte(1000);
                            break kampf;
                        }
                        warte(500);
                    } else {
                        System.out.print("Du verfehlst!");
                        warte(500);
                        Crawler.enter();
                    }
                    break;
                case 2:
                    System.out.print("Trank! ");
                    boolean trankeinnahme = sp.heal();
                    if (trankeinnahme) {
                        System.out.print("Du hast wieder " + sp.getHp() + " HP!");
                        Crawler.enter();
                    } else {
                        System.out.print("Du hast keine Traenke mehr!");
                        Crawler.enter();
                    }
                    warte(500);
                    break;
                case 3:
                    boolean tempStatus = spezialfaehigkeitsauswahl(sp, mo, sc);
                    if (this.battleOver) {
                        break kampf;
                    }
                    if (!tempStatus) {
                        continue kampf;
                    }

                    break;
                case 1337:
                    mo.takeDamage(mo.getMaxHp());
                    System.out.print("IMBAZOOOOO0000R");
                    Crawler.enter();
                    break kampf;
                default:
                    System.out.print("Fehler, falscher Wert!");
                    Crawler.enter();
                    System.out.println();
                    warte(500);
                    continue kampf;
            }

            //Monster zieht
            if (mo.isSpecial()) {
                mo.special(sp, mo, sc, this);
                if (this.battleOver) {
                    break;
                }
            } else {
                if (monsterAttack(sp, mo)) {
                    break;
                }
            }
            //Rundenende
            
            spApReg = sp.regenerateAp();
            if (spApReg > 0) {
                System.out.print("Du hast " + spApReg + " AP wiederhergestellt.");
                Crawler.enter();
            }

            System.out.println();
            warte(750);

            runde++;
        }
        System.out.println();
        System.out.print("Kampf vorbei! ");
        
        warte(1000);
           
        if (mo.isDefeated()) {
            System.out.print("Du hast gewonnen! ");
            warte(500);
        } else if (sp.isDefeated()) {
            System.out.print(mo.getName() + " gewinnt! ");
            warte(500);
            System.out.print("Du wirst ohnmaechtig...");
        } else {
            System.out.print("Fehler, beide noch am Leben o.0");
        }
        Crawler.enter();
    }

    /**
     * Wartet zeit ms und erhoeht fehler, falls einer vorliegt..
     * @param zeit Zeit in ms, die gewartet werden soll.
     */
    public void warte(int zeit) {
        /*try {
            Thread.sleep(zeit);
        } catch (InterruptedException e) {
            fehler++;
        }*/
    }

    /**
     * Methode, die die Ausgabe der Werte der Kampfteilnehmer uebernimmt.
     * @param sp Spieler, der Kaempft.
     * @param mo Monster, das kaempft.
     * @param runde Rundenzahl, die ausgegeben wird.
     */
    private void rundenAnfang(Player sp, Character mo, Integer runde) {
        System.out.println("Runde " + runde + "!");
        System.out.println();
        System.out.println(sp.toString());
        System.out.println(mo.toString());
        System.out.println();
        warte(1250);
        System.out.println("Was wirst du tun?");
        System.out.println("\t1: Angriff mit " + sp.getAtk() + " ATK");
        System.out.println("\t2: Trank (" + sp.getRemainingItemUses() + " verbleibend, heilt: "
                + sp.getHealingPower() + ")");
        System.out.println("\t3: Spezialangriff" );
    }

    /**
     * Methode, die die Spezialfaehigkeitswahl inklusive ein und Ausgabe uebernimmt.
     * @param sp Kaempfender Spieler.
     * @param mo Kaempfendes monster.
     * @param sc Zu nutzender Scanner.
     * @return <ul>
     *     <li>true: Faehigkeit wurde genutzt.</li>
     *     <li>false: rueckkehr ins vorige Menu.</li>
     * </ul>
     */
    public boolean spezialfaehigkeitsauswahl(Player sp, Character mo, Scanner sc) {
        int eingabe;
        boolean aktion;
    faehigkeit :
        while (true) {
            System.out.println();
            System.out.println("Spezialfaehigkeit auswaehlen, '0' zum Abbrechen. Du hast "
                    + sp.getAp() + " AP.");
            System.out.println("\t1: Notration: Heilung um " + sp.getHealingPower() + " HP, Kosten: 50 AP");
            System.out.println("\t2: Harter Schlag: Schlag mit 2 * ATK, Kosten: 50 AP");
            System.out.println("\t3: Meditation: AP auffrischen, Kosten: 0 AP");
            System.out.print("Eingabe: ");
            try {
                eingabe = sc.nextInt();
            } catch (InputMismatchException fe) {
                System.out.println("Falsche Eingabe: " + sc.next() );
                continue;
            }
            System.out.println();
            switch (eingabe) {
                case 0:
                    return false;
                case 1:
                    aktion = sp.saNotration();
                    if (aktion) {
                        System.out.print("Du hast wieder " + sp.getHp() + " HP!");
                        Crawler.enter();
                        warte(500);
                        break faehigkeit;
                    } else {
                        System.out.print("Nicht genug AP!");
                        Crawler.enter();
                        continue faehigkeit;
                    }
                case 2:
                    int spDmg = sp.saHarterSchlag(mo);
                    if (spDmg > 0) {
                        System.out.print("Das ist sehr effektiv!!! " + spDmg + " Schaden angerichtet!");
                        Crawler.enter();
                        if (mo.isDefeated()) {
                            warte(500);
                            this.setBattleOver(true);
                            break faehigkeit;
                        }
                        warte(500);
                        break faehigkeit;
                    } else {
                        System.out.print("Nicht genug AP!");
                        Crawler.enter();
                        continue faehigkeit;
                    }
                case 3:
                    aktion = sp.saMeditation();
                    if (aktion) {
                        System.out.print("Du hast wieder volle AP!");
                        Crawler.enter();
                        break faehigkeit;
                    } else {
                        System.out.print("Du hattest bereits volle AP!");
                        Crawler.enter();
                        continue faehigkeit;
                    }
                default:
                    System.out.print("Fehler, falscher Wert!");
                    Crawler.enter();
                    break;
            }
        }
        return true;
    }

    /**
     * Setter fuer battleOver
     * @param battleOver setzt den neuen Zustand.
     */
    public void setBattleOver(boolean battleOver) {
        this.battleOver = battleOver;
    }

    /**
     * Ausgabemethode fuer einen standard-Monsterangriff.
     * @param sp kaempfender Spieler.
     * @param mo angreifendes Monster.
     * @return Spieler besiegt.
     */
    public boolean monsterAttack(Character sp, Character mo) {
        System.out.print(mo.getName() + " greift an! ");
        warte(500);
        int moDmg = mo.attack(sp);
        if (moDmg > 0) {
            System.out.print("Du bist getroffen! Schaden: " + moDmg);
            if (sp.isDefeated()) {
                Crawler.enter();
                return true;
            }
        } else {
            System.out.print(mo.getName() + " verfehlt!");
        }
        Crawler.enter();
        return false;
    }
}


