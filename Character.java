import java.util.Scanner;

/**
 * @author Christoph Heibutzki 4573456 Gruppe 7c
 */
public class Character {
    /** aktuelle hp */
    private int hp;
    /** maxphp, unveraendert */
    private int maxHp;
    /** aktuelle atk */
    private int atk;
    /** hitchance, unveraendert */
    private double hitChance;
    /** name */
    private String name;
    /** Ist es ein Spezialmonster? */
    private boolean special;

    /**
     * Konstruktor.
     * @param maxHp maximale Lebenspunkte.
     * @param atk Grundangriffswert.
     * @param hitChance Trefferchance.
     * @param name Name, der im Kampf verwendet wird.
     */
    public Character(int maxHp, int atk, double hitChance, String name) {
        this.atk = atk;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.hitChance = hitChance;
        this.name = name;
        this.special = false;
    }

    /**
     * Dummyfunktion fuer erbende Klassen.
     * @param sp Spieler.
     * @param mo Monster.
     * @param sc Scanner.
     * @param c Kampf.
     */
    public void special(Character sp, Character mo, Scanner sc, Combat c) {

    }

    /**
     * Ueberprueft, ob Monster tot.
     * @return hp == 0.
     */
    public boolean isDefeated() {
        return (hp == 0);
    }

    /**
     * Grundangriffsmethode.
     * @param mo Anzugreifender Charakter.
     * @return Angerichteter Schaden, -1 wenn daneben.
     */
    public int attack(Character mo) {
        int schaden;
        if (Math.random() < this.hitChance) {
            schaden = (int) (this.atk + (Math.random() * ( this.atk + 1)));
            mo.takeDamage(schaden);
            return schaden;
        } else {
            return -1;
        }
    }

    /**
     * Nimm Schaden.
     * @param dmg zu nehmender Schaden.
     */
    public void takeDamage(int dmg) {
        this.hp -= dmg;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    /**
     * Ueberpruefung, ob besonders.
     * @return Besonderheit.
     */
    public boolean isSpecial() {
        return special;
    }
    // isDefeated, hp, maxHp, atk, attack, takeDmg, hasSpecials

    /**
     * Setze HP.
     * @param hp Neue HP.
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Hole Max-HP.
     * @return Max-HP.
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Hole aktuelle HP.
     * @return aktuelle HP.
     */
    public int getHp() {
        return this.hp;
    }

    /**
     * Gebe Namen aus.
     * @return Name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gebe aktuellen Angriffswert aus.
     * @return Angriffswert.
     */
    public int getAtk() {
        return this.atk;
    }

    /**
     * Setze Besonderheit.
     * @param special Status.
     */
    public void setSpecial(boolean special) {
        this.special = special;
    }

    /**
     * Setze Angriffswert.
     * @param atk Neuer Angriffswert.
     */
    public void setAtk(int atk) {
        this.atk = atk;
    }

    /**
     * Gebe den Status aus.
     * @return Name + HP + ATK.
     */
    public String toString() {
        return (this.name + ": HP: " + this.hp + " ATK: " + this.atk);
    }

}

