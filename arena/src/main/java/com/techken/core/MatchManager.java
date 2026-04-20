package com.techken.core;

import com.techken.model.BaseCharacter;
import com.techken.ai.AILogic;
import com.techken.utils.InputHandler;
import com.techken.utils.CombatMath;
import com.techken.skills.BaseSkill;
import com.techken.skills.actions.AttackAction;
import com.techken.skills.actions.GuardAction;
import com.techken.skills.actions.HealthStealDamageAction;
import com.techken.utils.AnsiColors;

public class MatchManager {

    private final InputHandler inputHandler;
    private final AILogic aiLogic;
    private final CombatMath combatMath;

    public MatchManager() {
        this.inputHandler = new InputHandler();
        this.aiLogic = new AILogic();
        this.combatMath = new CombatMath();
    }


    // Main match loop mo run until someone's HP hits 0
    public void startMatch(BaseCharacter player1, BaseCharacter player2) {
        if (player1 == null || player2 == null) {
            System.out.println("MatchManager: A character is not implemented yet. Returning to menu.");
            return;
        }

        System.out.println(AnsiColors.RED_BRIGHT + "\n================== " + player1.getName() + " VS " + player2.getName() + " ==================" + AnsiColors.RESET);

        int turnCounter = 1;
        while (player1.getHealth() > 0 && player2.getHealth() > 0) {
            System.out.println(AnsiColors.YELLOW + "\n-------------------- Turn " + turnCounter + " --------------------" + AnsiColors.RESET);
            displayHealth(player1, player2);

             // Faster character ang mo attack first
            BaseCharacter first;
            if (player1.getSpeed() >= player2.getSpeed()) {
                first = player1;    
            } else {
                first = player2;
            }

            BaseCharacter second;
            if (first == player1) {
                second = player2;
            } else {
                second = player1;
            }

            System.out.println(AnsiColors.WHITE_DIM + "\n(" + first.getName() + " is faster and goes first.)" + AnsiColors.RESET);


            // Each character takes their turn, stop early if someone dies
            processTurn(first, second, first == player1);
            if (checkGameOver(player1, player2)) break;

            processTurn(second, first, second == player1);
            if (checkGameOver(player1, player2)) break;

            turnCounter++;
        }

        declareWinner(player1, player2);
    }

    // Handles one character's turn player gets a menu, CPU uses AI or katong random generated chooser
    private void processTurn(BaseCharacter attacker, BaseCharacter defender, boolean isPlayer) {
        String turnOwner = isPlayer ? "(Player)" : "(CPU)";
        System.out.println(AnsiColors.BOLD + "\nIt's " + attacker.getName() + "'s turn. " + turnOwner + AnsiColors.RESET);
        attacker.resetDefense();

        BaseSkill chosenSkill;
        if (isPlayer) {
               // Shows skill options
            System.out.println("Choose your skill:");
            for (int i = 0; i < attacker.getSkills().length; i++) {
                System.out.println("  [" + (i + 1) + "] " + attacker.getSkills()[i].getSkillName());
            }
            System.out.print(AnsiColors.YELLOW_BRIGHT + ">> Select move (1-3): " + AnsiColors.RESET);
            int skillIndex = inputHandler.getIntInput(1, 3) - 1;
            chosenSkill = attacker.getSkills()[skillIndex];
        } else {
            chosenSkill = aiLogic.selectSkill(attacker);
        }

        System.out.println(AnsiColors.BOLD + attacker.getName() + " uses " + chosenSkill.getSkillName() + "!" + AnsiColors.RESET);

        // Handle attack or lifesteal skill
        if (chosenSkill instanceof AttackAction || chosenSkill instanceof HealthStealDamageAction) {
            int damage = combatMath.calculateDamage(chosenSkill, defender);
            defender.takeDamage(damage);
            System.out.println(AnsiColors.RED + "It deals " + damage + " damage to " + defender.getName() + "!" + AnsiColors.RESET);

            // Lifesteal: attacker heals for 50% of damage dealt, capped at max HP
            if (chosenSkill instanceof HealthStealDamageAction) {
                int healthStolen = damage / 2; 
                int newHealth = Math.min(attacker.getHealth() + healthStolen, attacker.getMaxHealth());
                attacker.setHealth(newHealth);
                System.out.println(AnsiColors.YELLOW_BRIGHT + attacker.getName() + " recovers " + healthStolen + " health!" + AnsiColors.RESET);
            }
        } else if (chosenSkill instanceof GuardAction) {
            // Guard: temporarily raises the attacker's own defense
            int defenseBoost = ((GuardAction) chosenSkill).getDefenseBoost();
            attacker.setDefense(attacker.getDefense() + defenseBoost);
            System.out.println(AnsiColors.YELLOW_BRIGHT + attacker.getName() + " boosts their defense by " + defenseBoost + "!" + AnsiColors.RESET);
        }
    }

    // Prints current HP for both characters
    private void displayHealth(BaseCharacter p1, BaseCharacter p2) {
        System.out.println(AnsiColors.GREEN + "--> Player: " + p1.getName() + " HP: " + Math.max(0, p1.getHealth()) + "/" + p1.getMaxHealth() + AnsiColors.RESET);
        System.out.println(AnsiColors.GREEN + "--> CPU:    " + p2.getName() + " HP: " + Math.max(0, p2.getHealth()) + "/" + p2.getMaxHealth() + AnsiColors.RESET);
    }

     // Returns true if either character is dead
    private boolean checkGameOver(BaseCharacter p1, BaseCharacter p2) {
        return p1.getHealth() <= 0 || p2.getHealth() <= 0;
    }

      // Shows final HP and announces the winner
    private void declareWinner(BaseCharacter p1, BaseCharacter p2) {
        System.out.println(AnsiColors.RED_BRIGHT + "\n================== FIGHT OVER ==================" + AnsiColors.RESET);
        displayHealth(p1, p2);
        
        String winnerMessage;
        if (p1.getHealth() <= 0) { 
            winnerMessage = "CPU WINS!";  // Player 1 (user) died
        } else { 
            winnerMessage = "PLAYER WINS!";  // Player 2 (CPU) died
        }
        System.out.println(AnsiColors.BOLD + AnsiColors.YELLOW_BRIGHT + "\n" + winnerMessage + AnsiColors.RESET);
    }
}
