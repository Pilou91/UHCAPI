package fr.pilou.uhcapi.module.role;

import fr.pilou.powerapi.PowerAPI;
import fr.pilou.powerapi.powers.Power;
import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.game.IGameHandler;
import fr.pilou.uhcapi.game.player.IGamePlayer;
import fr.pilou.uhcapi.module.camp.Camp;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.UUID;

@Getter
public abstract class Role {
    private final String name;
    private final String identifier;
    private final Class<? extends Camp> campClass;

    @SneakyThrows
    public Role(){
        RoleRegistry registry = getClass().getDeclaredAnnotation(RoleRegistry.class);
        if (registry == null) {
            throw new Exception("Scenario data must not be null");
        }

        this.name = registry.name();
        this.identifier = registry.identifier();
        this.campClass = registry.campClass();
    }

    public void addPower(Power... powers){
        UUID uuid = getOwner().getUniqueId();
        for (Power power : powers) {
            PowerAPI.addPower(uuid, power);
        }
    }

    public IGamePlayer getOwner(){
        IGameHandler gameHandler = API.get().getGameHandler();
        for (IGamePlayer gamePlayer : gameHandler.getGamePlayers()) {
            Role role = gamePlayer.getRole();
            if(role != null && role.equals(this)){
                return gamePlayer;
            }
        }

        return null;
    }

    public void onDescription(IGamePlayer gamePlayer){

    }

    public void onRole(IGamePlayer gamePlayer){

    }
}
