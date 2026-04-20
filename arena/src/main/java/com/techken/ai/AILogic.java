package com.techken.ai;

import com.techken.model.BaseCharacter;
import com.techken.skills.BaseSkill;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AILogic {

    private final Random random;

    public AILogic() {
        this.random = new Random();
    }

    public BaseSkill selectSkill(BaseCharacter aiCharacter) {
        List<BaseSkill> availableSkills = new ArrayList<>();
        for (BaseSkill skill : aiCharacter.getSkills()) {
            if (skill != null) {
                availableSkills.add(skill);
            }
        }

        if (availableSkills.isEmpty()) {
            return null; 
        }

        int skillIndex = random.nextInt(availableSkills.size());
        return availableSkills.get(skillIndex);
    }
}