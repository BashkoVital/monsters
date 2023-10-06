package com.bashko.service;

import com.bashko.entity.Dice;
import com.bashko.entity.Monster;
import com.bashko.entity.Player;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Log4j2
public class PlayerService {
    private final Random random;
    public static final Map<String, Player> thePersonagesOfTheGame = new HashMap<>();

    static {
        thePersonagesOfTheGame.put("Человек", Player.builder()
                .name("Человек")
                .attack(2)
                .protection(4)
                .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                .build());
        thePersonagesOfTheGame.put("Эльф", Player.builder()
                .name("Эльф")
                .attack(4)
                .protection(2)
                .damage(new Integer[]{3, 4, 5, 6, 7, 8})
                .build());
    }

    public PlayerService(Random random) {
        this.random = random;
    }

    public void attackToMonster(Player player, List<Monster> monsters, Dice dice) {
        Monster monster = monsters.get(random.nextInt(monsters.size()));
        log.debug("Player {} attack to {}", player.getName(), monster.getName());
        int modifierOfAttack = player.getAttack() - monster.getProtection() + 1;

        if (modifierOfAttack <= 1) {
            int result = random.nextInt(1, dice.getMaxValue() + 1);
            log.debug("{} throw {} on the dice", player.getName(), result);
            if (result == 5 || result == 6) {
                monster.setHealth(monster.getHealth() - player.getDamage()[random.nextInt(player.getDamage().length)]);
            }
            checkMonsterKill(monsters, monster);
        } else {
            for (int i = 1; i <= modifierOfAttack; i++) {
                int result = random.nextInt(1, dice.getMaxValue() + 1);
                log.debug("{} throw {} on the dice {}", player.getName(), result, i);
                if (result == 5 || result == 6) {
                    monster.setHealth(monster.getHealth() - player.getDamage()[random.nextInt(player.getDamage().length)]);
                    checkMonsterKill(monsters, monster);
                    return;
                }
            }
        }
    }

    private void checkMonsterKill(List<Monster> monsters, Monster monster) {
        if (monster.getHealth() <= 0) {
            System.out.println("Вы убили монстра " + monster.getName());
            log.info("Player killed {}", monster.getName());
            monsters.remove(monster);
        }
    }

    public void getMoreHealthForPlayer(Player player) {
        if (player.getCountOfHealing() > 0) {
            int health = Math.round((float) (player.getHealth() + 30));
            player.setHealth(Math.min(health, 100));
            player.setCountOfHealing(player.getCountOfHealing() - 1);
        }
    }

    public boolean isPlayerAlive(Player player) {
        return player.getHealth() > 0;
    }


    public Player createNewPlayer(Player personage, String name) {
        return Player.builder()
                .name(name)
                .health(100)
                .attack(personage.getAttack())
                .protection(personage.getProtection())
                .countOfHealing(4)
                .damage(personage.getDamage())
                .build();
    }

    public void printPlayer(Player player) {
        System.out.print(player.getName() + " : ");
        printHealth(player.getHealth());
    }

    private void printHealth(Integer health) {
        StringBuilder sb = new StringBuilder();
        sb.append("*".repeat(Math.max(0, health)));
        System.out.println(sb + "  (" + sb.length() + "%)");
    }

    public String toStringPersonage(Player personage) {
        return "\"" + personage.getName() + "\": "
               + "атака - " + personage.getAttack()
               + ", защита - " + personage.getProtection()
               + ", урон от " + personage.getDamage()[0]
               + " до " + personage.getDamage()[5] + ".";
    }
}
