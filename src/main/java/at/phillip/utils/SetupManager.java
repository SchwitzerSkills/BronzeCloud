package at.phillip.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SetupManager {

    public void createFolders(){
        File staticFolder = new File("./static");

        if(!staticFolder.exists()){
            if (!staticFolder.mkdirs()) {
                System.out.println("Failed to create the folder.");
                return;
            }
        }

        File databaseFolder = new File("./database");

        if(!databaseFolder.exists()){
            if (!databaseFolder.mkdirs()) {
                System.out.println("Failed to create the folder.");
                return;
            }
        }

        File softwareFolder = new File("./software");

        if(!softwareFolder.exists()){
            if (!softwareFolder.mkdirs()) {
                System.out.println("Failed to create the folder.");
                return;
            }
        }

        makeConfigYML();

    }

    public void makeConfigYML(){
        String filePath = "./software/config.yml";
        String content =
                "enforce_secure_profile: false\n" +
                        "forge_support: false\n" +
                        "player_limit: -1\n" +
                        "ip_forward: true\n" +
                        "permissions:\n" +
                        "  default:\n" +
                        "  - bungeecord.command.server\n" +
                        "  - bungeecord.command.list\n" +
                        "  admin:\n" +
                        "  - bungeecord.command.alert\n" +
                        "  - bungeecord.command.end\n" +
                        "  - bungeecord.command.ip\n" +
                        "  - bungeecord.command.reload\n" +
                        "  - bungeecord.command.kick\n" +
                        "  - bungeecord.command.send\n" +
                        "  - bungeecord.command.find\n" +
                        "timeout: 30000\n" +
                        "log_commands: false\n" +
                        "online_mode: true\n" +
                        "servers:\n" +
                        "  fallback:\n" +
                        "    motd: '&1Just another BungeeCord - Forced Host'\n" +
                        "    address: localhost:25565\n" +
                        "    restricted: false\n" +
                        "listeners:\n" +
                        "- query_port: 25577\n" +
                        "  motd: '&1Another Bungee server'\n" +
                        "  tab_list: GLOBAL_PING\n" +
                        "  query_enabled: false\n" +
                        "  proxy_protocol: false\n" +
                        "  forced_hosts:\n" +
                        "    pvp.md-5.net: pvp\n" +
                        "  ping_passthrough: false\n" +
                        "  priorities:\n" +
                        "  - fallback\n" +
                        "  bind_local_address: true\n" +
                        "  host: 0.0.0.0:25577\n" +
                        "  max_players: 1\n" +
                        "  tab_size: 60\n" +
                        "  force_default_server: true\n" +
                        "log_pings: true\n" +
                        "remote_ping_timeout: 5000\n" +
                        "disabled_commands:\n" +
                        "- disabledcommandhere\n" +
                        "network_compression_threshold: 256\n" +
                        "reject_transfers: false\n" +
                        "remote_ping_cache: -1\n" +
                        "connection_throttle: 4000\n" +
                        "stats: 929918be-da36-43ec-aea0-cc2c9c4512b2\n" +
                        "connection_throttle_limit: 3\n" +
                        "groups:\n" +
                        "  md_5:\n" +
                        "  - admin\n" +
                        "prevent_proxy_connections: false";

        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                Files.write(path, content.getBytes());

                System.out.println("File created successfully!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String paperPath = "./software/spigot.yml";
        String contentPaper =
                "messages:\n" +
                        "  whitelist: You are not whitelisted on this server!\n" +
                        "  unknown-command: Unknown command. Type \"/help\" for help.\n" +
                        "  server-full: The server is full!\n" +
                        "  outdated-client: Outdated client! Please use {0}\n" +
                        "  outdated-server: Outdated server! I'm still on {0}\n" +
                        "  restart: Server is restarting\n" +
                        "world-settings:\n" +
                        "  default:\n" +
                        "    below-zero-generation-in-existing-chunks: true\n" +
                        "    view-distance: default\n" +
                        "    simulation-distance: default\n" +
                        "    thunder-chance: 100000\n" +
                        "    merge-radius:\n" +
                        "      item: 0.5\n" +
                        "      exp: -1.0\n" +
                        "    mob-spawn-range: 8\n" +
                        "    item-despawn-rate: 6000\n" +
                        "    arrow-despawn-rate: 1200\n" +
                        "    trident-despawn-rate: 1200\n" +
                        "    zombie-aggressive-towards-villager: true\n" +
                        "    nerf-spawner-mobs: false\n" +
                        "    enable-zombie-pigmen-portal-spawns: true\n" +
                        "    wither-spawn-sound-radius: 0\n" +
                        "    end-portal-sound-radius: 0\n" +
                        "    hanging-tick-frequency: 100\n" +
                        "    unload-frozen-chunks: false\n" +
                        "    growth:\n" +
                        "      cactus-modifier: 100\n" +
                        "      cane-modifier: 100\n" +
                        "      melon-modifier: 100\n" +
                        "      mushroom-modifier: 100\n" +
                        "      pumpkin-modifier: 100\n" +
                        "      sapling-modifier: 100\n" +
                        "      beetroot-modifier: 100\n" +
                        "      carrot-modifier: 100\n" +
                        "      potato-modifier: 100\n" +
                        "      torchflower-modifier: 100\n" +
                        "      wheat-modifier: 100\n" +
                        "      netherwart-modifier: 100\n" +
                        "      vine-modifier: 100\n" +
                        "      cocoa-modifier: 100\n" +
                        "      bamboo-modifier: 100\n" +
                        "      sweetberry-modifier: 100\n" +
                        "      kelp-modifier: 100\n" +
                        "      twistingvines-modifier: 100\n" +
                        "      weepingvines-modifier: 100\n" +
                        "      cavevines-modifier: 100\n" +
                        "      glowberry-modifier: 100\n" +
                        "      pitcherplant-modifier: 100\n" +
                        "    entity-activation-range:\n" +
                        "      animals: 32\n" +
                        "      monsters: 32\n" +
                        "      raiders: 64\n" +
                        "      misc: 16\n" +
                        "      water: 16\n" +
                        "      villagers: 32\n" +
                        "      flying-monsters: 32\n" +
                        "      wake-up-inactive:\n" +
                        "        animals-max-per-tick: 4\n" +
                        "        animals-every: 1200\n" +
                        "        animals-for: 100\n" +
                        "        monsters-max-per-tick: 8\n" +
                        "        monsters-every: 400\n" +
                        "        monsters-for: 100\n" +
                        "        villagers-max-per-tick: 4\n" +
                        "        villagers-every: 600\n" +
                        "        villagers-for: 100\n" +
                        "        flying-monsters-max-per-tick: 8\n" +
                        "        flying-monsters-every: 200\n" +
                        "        flying-monsters-for: 100\n" +
                        "      villagers-work-immunity-after: 100\n" +
                        "      villagers-work-immunity-for: 20\n" +
                        "      villagers-active-for-panic: true\n" +
                        "      tick-inactive-villagers: true\n" +
                        "      ignore-spectators: false\n" +
                        "    entity-tracking-range:\n" +
                        "      players: 128\n" +
                        "      animals: 96\n" +
                        "      monsters: 96\n" +
                        "      misc: 96\n" +
                        "      display: 128\n" +
                        "      other: 64\n" +
                        "    ticks-per:\n" +
                        "      hopper-transfer: 8\n" +
                        "      hopper-check: 1\n" +
                        "    hopper-amount: 1\n" +
                        "    hopper-can-load-chunks: false\n" +
                        "    dragon-death-sound-radius: 0\n" +
                        "    seed-village: 10387312\n" +
                        "    seed-desert: 14357617\n" +
                        "    seed-igloo: 14357618\n" +
                        "    seed-jungle: 14357619\n" +
                        "    seed-swamp: 14357620\n" +
                        "    seed-monument: 10387313\n" +
                        "    seed-shipwreck: 165745295\n" +
                        "    seed-ocean: 14357621\n" +
                        "    seed-outpost: 165745296\n" +
                        "    seed-endcity: 10387313\n" +
                        "    seed-slime: 987234911\n" +
                        "    seed-nether: 30084232\n" +
                        "    seed-mansion: 10387319\n" +
                        "    seed-fossil: 14357921\n" +
                        "    seed-portal: 34222645\n" +
                        "    seed-ancientcity: 20083232\n" +
                        "    seed-trailruins: 83469867\n" +
                        "    seed-trialchambers: 94251327\n" +
                        "    seed-buriedtreasure: 10387320\n" +
                        "    seed-mineshaft: default\n" +
                        "    seed-stronghold: default\n" +
                        "    hunger:\n" +
                        "      jump-walk-exhaustion: 0.05\n" +
                        "      jump-sprint-exhaustion: 0.2\n" +
                        "      combat-exhaustion: 0.1\n" +
                        "      regen-exhaustion: 6.0\n" +
                        "      swim-multiplier: 0.01\n" +
                        "      sprint-multiplier: 0.1\n" +
                        "      other-multiplier: 0.0\n" +
                        "    max-tnt-per-tick: 100\n" +
                        "    max-tick-time:\n" +
                        "      tile: 50\n" +
                        "      entity: 50\n" +
                        "    verbose: false\n" +
                        "settings:\n" +
                        "  bungeecord: true\n" +
                        "  save-user-cache-on-stop-only: false\n" +
                        "  sample-count: 12\n" +
                        "  player-shuffle: 0\n" +
                        "  user-cache-size: 1000\n" +
                        "  moved-wrongly-threshold: 0.0625\n" +
                        "  moved-too-quickly-multiplier: 10.0\n" +
                        "  timeout-time: 60\n" +
                        "  restart-on-crash: true\n" +
                        "  restart-script: ./start.sh\n" +
                        "  netty-threads: 4\n" +
                        "  attribute:\n" +
                        "    maxAbsorption:\n" +
                        "      max: 2048.0\n" +
                        "    maxHealth:\n" +
                        "      max: 1024.0\n" +
                        "    movementSpeed:\n" +
                        "      max: 1024.0\n" +
                        "    attackDamage:\n" +
                        "      max: 2048.0\n" +
                        "  log-villager-deaths: true\n" +
                        "  log-named-deaths: true\n" +
                        "  debug: false\n" +
                        "commands:\n" +
                        "  tab-complete: 0\n" +
                        "  send-namespaced: true\n" +
                        "  log: true\n" +
                        "  spam-exclusions:\n" +
                        "  - /skill\n" +
                        "  silent-commandblock-console: false\n" +
                        "  replace-commands:\n" +
                        "  - setblock\n" +
                        "  - summon\n" +
                        "  - testforblock\n" +
                        "  - tellraw\n" +
                        "advancements:\n" +
                        "  disable-saving: false\n" +
                        "  disabled:\n" +
                        "  - minecraft:story/disabled\n" +
                        "players:\n" +
                        "  disable-saving: false\n" +
                        "config-version: 12\n" +
                        "stats:\n" +
                        "  disable-saving: false\n" +
                        "  forced-stats: {}\n";

        try {
            Path path = Paths.get(paperPath);
            if (!Files.exists(path)) {
                Files.write(path, contentPaper.getBytes());

                System.out.println("File created successfully!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String propertiesPath = "./software/server.properties";
        String contentProperties =
                "#Minecraft server properties\n" +
                        "#Mon Jan 20 17:41:30 CET 2025\n" +
                        "accepts-transfers=false\n" +
                        "allow-flight=false\n" +
                        "allow-nether=true\n" +
                        "broadcast-console-to-ops=true\n" +
                        "broadcast-rcon-to-ops=true\n" +
                        "bug-report-link=\n" +
                        "debug=false\n" +
                        "difficulty=easy\n" +
                        "enable-command-block=false\n" +
                        "enable-jmx-monitoring=false\n" +
                        "enable-query=false\n" +
                        "enable-rcon=false\n" +
                        "enable-status=true\n" +
                        "enforce-secure-profile=true\n" +
                        "enforce-whitelist=false\n" +
                        "entity-broadcast-range-percentage=100\n" +
                        "force-gamemode=false\n" +
                        "function-permission-level=2\n" +
                        "gamemode=survival\n" +
                        "generate-structures=true\n" +
                        "generator-settings={}\n" +
                        "hardcore=false\n" +
                        "hide-online-players=false\n" +
                        "initial-disabled-packs=\n" +
                        "initial-enabled-packs=vanilla\n" +
                        "level-name=world\n" +
                        "level-seed=\n" +
                        "level-type=minecraft:normal\n" +
                        "log-ips=true\n" +
                        "max-chained-neighbor-updates=1000000\n" +
                        "max-players=20\n" +
                        "max-tick-time=60000\n" +
                        "max-world-size=29999984\n" +
                        "motd=A Minecraft Server\n" +
                        "network-compression-threshold=256\n" +
                        "online-mode=false\n" +
                        "op-permission-level=4\n" +
                        "pause-when-empty-seconds=-1\n" +
                        "player-idle-timeout=0\n" +
                        "prevent-proxy-connections=false\n" +
                        "pvp=true\n" +
                        "query.port=25565\n" +
                        "rate-limit=0\n" +
                        "rcon.password=\n" +
                        "rcon.port=25575\n" +
                        "region-file-compression=deflate\n" +
                        "require-resource-pack=false\n" +
                        "resource-pack=\n" +
                        "resource-pack-id=\n" +
                        "resource-pack-prompt=\n" +
                        "resource-pack-sha1=\n" +
                        "server-ip=localhost\n" +
                        "server-port=25565\n" +
                        "simulation-distance=10\n" +
                        "spawn-monsters=true\n" +
                        "spawn-protection=16\n" +
                        "sync-chunk-writes=true\n" +
                        "text-filtering-config=\n" +
                        "text-filtering-version=0\n" +
                        "use-native-transport=true\n" +
                        "view-distance=10\n" +
                        "white-list=false\n";

        try {
            Path path = Paths.get(propertiesPath);
            if (!Files.exists(path)) {
                Files.write(path, contentProperties.getBytes());

                System.out.println("File created successfully!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createProxyDefaultTemplate(File workDirectory){
        File plugins = new File(workDirectory.getAbsolutePath() + "/plugins/");

        if(!plugins.exists()){
            plugins.mkdir();
        }

        File HOME = new File(workDirectory.getAbsolutePath() + "/plugins/BungeeBronzeModule/");

        if(!HOME.exists()){
            HOME.mkdir();
        }
    }

    public void createServerDefaultTemplate(File workDirectory){
        File plugins = new File(workDirectory.getAbsolutePath() + "/plugins/");

        if(!plugins.exists()){
            plugins.mkdir();
        }

        File HOME = new File(workDirectory.getAbsolutePath() + "/plugins/SpigotBronzeModule/");

        if(!HOME.exists()){
            HOME.mkdir();
        }
    }

}
