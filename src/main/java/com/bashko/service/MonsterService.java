package com.bashko.service;

import com.bashko.entity.Dice;
import com.bashko.entity.Monster;
import com.bashko.entity.Player;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Log4j2
public class MonsterService {
    private final Random random;

    public MonsterService(Random random) {
        this.random = random;
    }

    public void attackToPlayer(List<Monster> monsters, Player player, Dice dice) {
        Monster monster = monsters.get(random.nextInt(monsters.size()));
        log.debug("{} attack to player", monster.getName());
        int modifierOfAttack = monster.getAttack() - player.getProtection() + 1;

        if (modifierOfAttack <= 1) {
            int result = random.nextInt(1, dice.getMaxValue() + 1);
            log.debug("{} throw {} on dice", monster.getName(), result);
            if (result == 5 || result == 6) {
                player.setHealth(player.getHealth() - monster.getDamage()[random.nextInt(monster.getDamage().length)]);
            }
        } else {
            for (int i = 1; i <= modifierOfAttack; i++) {
                int result = random.nextInt(1, dice.getMaxValue() + 1);
                log.debug("{} throw {} on dice {}", monster.getName(), result, i);
                if (result == 5 || result == 6) {
                    player.setHealth(player.getHealth() - monster.getDamage()[random.nextInt(monster.getDamage().length)]);
                    return;
                }
            }
        }
    }

    public boolean isMonstersAlive(List<Monster> monsters) {
        int countAliveMonsters = 0;
        for (Monster monster : monsters) {
            if (monster.getHealth() > 0) countAliveMonsters++;
        }
        return countAliveMonsters != 0;
    }

    public List<Monster> createMonsters(int countOfMonsters) {
        List<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < countOfMonsters; i++) {
            monsters.add(Monster.builder()
                    .name("Monster#" + (i + 1))
                    .health(100)
                    .attack(1)
                    .protection(1)
                    .damage(new Integer[]{1, 2, 3, 4, 5, 6})
                    .build());
            log.debug(" Monster#{} was created", (i + 1));
        }
        return monsters;
    }

    public void printMonsters(List<Monster> monsters) {
        for (Monster monster : monsters) {
            System.out.print(monster.getName() + " : ");
            printHealth(monster.getHealth());
        }
    }

    private void printHealth(Integer health) {
        StringBuilder sb = new StringBuilder();
        sb.append("*".repeat(Math.max(0, health)));
        System.out.println(sb + "  (" + sb.length() + "%)");
    }
}
