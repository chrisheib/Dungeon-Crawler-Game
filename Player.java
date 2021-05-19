/**
 * Spielerklasse.
 * @author Christoph Heibutzki 4573456 Gruppe 7c
 * @coauthor: Julia Meier-Grotrian 4506300 Gruppe 7
 */
public class Player extends Character {
    /**  aktuell verbleibende Tranknutzungen.  */
    private int remainingItemUses;
    /** Heilwert eines Trankes, unveraendert.  */
    private int healingPower;
    /** Maximale AP, unveraendert.  */
    private int maxAp;
    /** aktuelle ap */
    private int ap;
    /** Ap-Regenerationsrate, unveraendert */
    private int apRegen;
    /** Schmiedenwert, unveraendert */
    private int forgePower;
    /** Maximale Trankzahl, unveraendert. */
    private int maxItemUses;
// Konstruktor

    /**
     * Ausfuehrlicher Konstruktor fuer Spieler.
     * @param maxHp Maximales Leben und startleben.
     * @param atk Grundangriffswert.
     * @param healingPower Heilungskraft eines Trankes.
     * @param remainingItemUses Trankanzahl.
     * @param ap Maximale und anfaengliche AP.
     * @param apRegen Pro Runde regenerierte AP.
     * @param hitChance Trefferrate.
     * @param forgePower Wert, um den die atk erhoeht wird, wenn der Spieler eine Schmiede betritt.
     */
    public Player(int maxHp, int atk, int healingPower, int remainingItemUses,
        int ap, int apRegen, double hitChance, int forgePower) {

        super(maxHp, atk, hitChance, "Spieler");
        this.healingPower = healingPower;
        this.maxItemUses = remainingItemUses;
        this.remainingItemUses = remainingItemUses;
        this.maxAp = ap;
        this.ap = this.maxAp;
        this.apRegen = apRegen;
        this.forgePower = forgePower;
    }

// Funktionen

    /**
     * Regeniert die AP um apRegen.
     * @return Tatsaechlich wiederhergestellte AP.
     */
    public int regenerateAp() {
        int apRechnung = this.maxAp - this.ap;
        if (apRechnung > 0) {
            if (apRechnung > this.apRegen) {
                this.ap += this.apRegen;
                return this.apRegen;
            } else {
                this.ap = this.maxAp;
                return apRechnung;
            }
        } else {
            return 0;
        }
    }

    /**
     * Spezialfaehigkeit: Stelle AP wieder her.
     * @return False, wenn AP bereits voll.
     */
    public boolean saMeditation() {
        if (this.ap < this.maxAp) {
            this.ap = this.maxAp;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Spezialfaehigkeit Notration.
     * @return false, wenn zu wenig AP
     */
    public boolean saNotration() {
        if (this.ap >= 50) {
            this.ap -= 50;
            this.setHp( this.getHp() + this.healingPower);
            if (this.getHp() > this.getMaxHp()) {
                this.setHp(this.getMaxHp());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Spezialfaehigkeit Harter Schlag.
     * @param c Charakter, der angegriffen werden soll
     * @return angerichteter Schaden
     */
    public int saHarterSchlag(Character c) {
        int schaden = 0;
        if (this.ap >= 50) {
            schaden = this.getAtk() * 2;
            c.takeDamage(schaden);
            this.ap -= 50;
            return schaden;
        } else {
            return -1;
        }
    }

    /**
     * Gibt allgemeine Daten aus.
     * @return SpielerHP, ATK, Items, AP
     */
    public String toString() {
        return "Player: HP: " + this.getHp() + ", ATK: " + this.getAtk() + ", Items: " + this.remainingItemUses
                + ", AP: " + this.ap;
    }

    /**
     * Trankeinnahme.
     * @return false wenn zu wenig AP.
     */
    public boolean heal() {
        if (this.remainingItemUses > 0) {
            this.remainingItemUses -= 1;
            this.setHp(this.getHp() + this.healingPower);
            if (this.getHp() > this.getMaxHp()) {
                this.setHp(this.getMaxHp());
            }
            return true;
        } else { 
            return false;
        }
    }

    /**
     * Gibt Heilkraft zurueck.
     * @return healingPower.
     */
    public int getHealingPower() {
        return this.healingPower;
    }

    /**
     * Gibt uebrige Traenke zurueck.
     * @return Trankanzahl.
     */
    public int getRemainingItemUses() {
        return this.remainingItemUses;
    }

    /**
     * Gib AP zurueck.
     * @return Ap.
     */
    public int getAp() {
        return this.ap;
    }

    /**
     * Fuelle Ap, Hp und Items auf.
     */
    public void useHealingWell() {
        this.setHp(this.getMaxHp());
        this.ap = this.maxAp;
        this.remainingItemUses = this.maxItemUses;
    }

    /**
     * Erhoehe den Angriffswert in einer Schmiede.
     */
    public void useForge() {
        this.setAtk(this.getAtk() + this.forgePower);
    }
}
