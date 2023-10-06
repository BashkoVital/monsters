package com.bashko.controllers;

import com.bashko.entity.Dice;
import com.bashko.entity.Monster;
import com.bashko.entity.Player;
import com.bashko.service.MonsterService;
import com.bashko.service.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Scanner;

@Log4j2
@AllArgsConstructor
public class PlayController {

    private PlayerService playerService;
    private MonsterService monsterService;
    private Scanner scanner;

    public void play(List<Monster> monsters, Player player, Dice dice) {
        String playCommand;
        do {
            playerService.printPlayer(player);
            System.out.println("-------------------------------------------------------------------------------------------------------");
            monsterService.printMonsters(monsters);
            System.out.println("Ваш ход...");

            playCommand = scanner.next();
            if (playCommand.equalsIgnoreCase("б")) {
                playerService.attackToMonster(player, monsters, dice);
                if (!monsterService.isMonstersAlive(monsters)) {
                    log.info("Player {} won!", player.getName());
                    System.out.println("Вы победили!");
                    return;
                }
                monsterService.attackToPlayer(monsters, player, dice);
                if (!playerService.isPlayerAlive(player)) {
                    log.info("Player {} lost!", player.getName());
                    System.out.println("Вы проиграли!");
                    return;
                }
            } else if (playCommand.equalsIgnoreCase("з")) {
                if ((player.getCountOfHealing() == 0)) {
                    log.info("Healing impossible for player {}", player.getName());
                    System.out.println("Вы израсходовали свою возможность восстановить здоровье.");
                } else {
                    log.info("Player {} healed himself", player.getName());
                    playerService.getMoreHealthForPlayer(player);
                }
            } else if (!playCommand.equalsIgnoreCase("выход")) System.out.println("Неверный ввод");
        } while (!playCommand.equalsIgnoreCase("выход"));
    }
}
