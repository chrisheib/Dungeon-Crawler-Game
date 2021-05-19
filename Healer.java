import java.util.Scanner;

/**
 * @author Christoph Heibutzki 4573456 Gruppe 7c
 * @coauthor: Julia Meier-Grotrian 4506300 Gruppe 7
 */
public class Healer extends Monster {
    /** Wert, um den Regeneriert wird. Unveraendert. */
    private int heilWert;

    /**
     * Konstruktor.
     * @param maxHp MaxHP.
     * @param atk Grundangriffswert.
     * @param hitChance Trefferchance.
     * @param heilWert Regeneration pro Runde.
     * @param name Name.
     */
    public Healer(int maxHp, int atk, double hitChance, int heilWert, String name) {
        super(maxHp, atk, hitChance, name);
        this.heilWert = heilWert;
        this.setSpecial(true);
    }

    /**
     * Spezialangriff: Regeneration vor dem Angriff.
     * @param sp Spieler.
     * @param mo Monster.
     * @param sc Scanner.
     * @param c Kampf.
     */
    @Override
    public void special(Character sp, Character mo, Scanner sc, Combat c) {
        System.out.print(mo.getName() + " regeneriert sich!");
        Crawler.enter();
        this.setHp(this.getHp() + this.heilWert);
        if (this.getHp() > this.getMaxHp()) {
            this.setHp(this.getMaxHp());
        }
        c.setBattleOver(c.monsterAttack(sp, mo));
    }

}
