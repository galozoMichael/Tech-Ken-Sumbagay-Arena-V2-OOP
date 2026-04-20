package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.BaseSkill;
import com.techken.skills.actions.AttackAction;
import com.techken.skills.actions.HealthStealDamageAction;

public class DevilJin extends BaseCharacter {

    private boolean isBerserk = false;

    public DevilJin() {
        super("Devil Jin", 120, 20, 15);

        this.skills[0] = new AttackAction("Laser Scraper", 25);
        this.skills[1] = new AttackAction("Demon Paw", 30);
        this.skills[2] = new HealthStealDamageAction("Infernal Destruction", 20);
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);

        //kani kay polymorphism (get angry deviljin mode)
        if (!isBerserk && this.getHealth() <= (this.getMaxHealth() * 0.3)) {
            isBerserk = true;
            System.out.println(">>> DEVIL JIN ENTERS BERSERK MODE! DAMAGE BUFFED! <<<");

            for (BaseSkill skill : this.skills) {
                if (skill instanceof AttackAction) {
                    AttackAction attackSkill = (AttackAction) skill;
                    attackSkill.setDamage(attackSkill.getDamage() + 15);
                } else if (skill instanceof HealthStealDamageAction) {
                    HealthStealDamageAction lifestealSkill = (HealthStealDamageAction) skill;
                    lifestealSkill.setDamage(lifestealSkill.getDamage() + 15);
                }
            }
        }
    }
}
