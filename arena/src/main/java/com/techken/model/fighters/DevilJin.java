package com.techken.model.fighters;

import com.techken.model.BaseCharacter;
import com.techken.skills.BaseSkill;
import com.techken.skills.actions.AttackAction;
import com.techken.skills.actions.HealthStealDamageAction;

public class DevilJin extends BaseCharacter {

    public DevilJin() {
        super("Devil Jin", 110, 20, 15);

        this.skills[0] = new AttackAction("Laser Scraper", 20);
        this.skills[1] = new AttackAction("Demon Paw", 25);
        this.skills[2] = new HealthStealDamageAction("Infernal Destruction", 15);
    }
}
