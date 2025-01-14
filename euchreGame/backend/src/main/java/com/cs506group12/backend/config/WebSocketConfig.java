package com.cs506group12.backend.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.cs506group12.backend.interfaces.Player;
import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.Client;
import com.cs506group12.backend.models.EuchreGame;
import com.cs506group12.backend.models.GameSession;
import com.cs506group12.backend.models.GameState;
import com.cs506group12.backend.models.HumanPlayerDecorator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration class for WebSocket communication between frontent and backend
 * 
 * @author jaime zepeda
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    // Concurrent HashMap to store active WebSocket sessions clients
    private final Map<WebSocketSession, Client> sessions = new ConcurrentHashMap<>();
    // Concurrent HashMap to store active game sessions
    private final Map<String, GameSession> games = new ConcurrentHashMap<>();

    /**
     * Registers WebSocket handlers.
     *
     * @param registry The WebSocketHandlerRegistry to register handlers.
     */
    @SuppressWarnings("null")
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/ws").setAllowedOrigins("*");
    }

    /**
     * Creates a WebSocketHandler bean.
     *
     * @return WebSocketHandler bean.
     */
    @Bean
    public WebSocketHandler myWebSocketHandler() {
        return new WebSocketHandler() {
            @SuppressWarnings("null")
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                System.out.println("Connection established with session id: " + session.getId()); // debug

                // add client to hashmap
                sessions.put(session, new Client(session));
            }

            @SuppressWarnings("null")
            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                System.out.println("Received message: " + message.getPayload()); // debug

                // get client sending the message
                Client client = sessions.get(session);
                GameSession game = client.getGame();

                // Assuming the message is in JSON format
                ObjectMapper mapper = new ObjectMapper();
                Object payload = message.getPayload();
                JsonNode jsonNode = mapper.readTree(payload.toString());

                // manages different types of messages from clients
                if (jsonNode.has("header")) {
                    String messageHeader = jsonNode.get("header").asText();

                    switch (messageHeader) {
                        /************** Multiplayer Game Lobby **************/

                        // called when a player creates a new game
                        case "create":
                            handleCreateMessage(client, jsonNode.get("name").asText());
                            break;
                        // called when a players joins a game
                        case "join":
                            handleJoinMessage(client, jsonNode.get("gameID").asText(), jsonNode.get("name").asText());
                            break;
                        // called when a player leaves a game
                        case "leave":
                            handleLeaveMessage(client);
                            break;
                        // called when a player attempts to start a game
                        case "start":
                            handleStartGame(game, client);
                            break;

                        /************** In Game **************/

                        // called when the players have loaded the game screen
                        case "ready":
                            handleGameSetUp(game, client);
                            break;
                        // called when an in-game event occurs
                        case "gameEvent":
                            handleGameEvent(game, client, jsonNode);
                            break;
                        // called whenever a players sends a message in the chat
                        case "message":
                            handleChatMessages(game, client, jsonNode.get("message").asText());
                            break;
                        default:
                            break;
                    }
                }
            }

            @SuppressWarnings("null")
            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                System.out.println("Error in WebSocket session with session id: " + session.getId()); // debug

                // some error happened in a client session
                handleErrorInSession(session);
            }

            @SuppressWarnings("null")
            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                System.out.println("Connection closed with session id: " + session.getId()); // debug

                // a client closes the browser
                handleErrorInSession(session);
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };
    }

    /*
     * The following functions correspond the the handling of messages or events in
     * the multiplayer game lobby. Most of the data will be sent to all clients in a
     * game session
     */

    /**
     * This function handles clients closing sessions. It will remove them from
     * their current game and alert other players
     * 
     * @param session the client session that was lost
     */
    private void handleErrorInSession(WebSocketSession session) {
        // Remove session from the sessions map
        Client client = sessions.get(session);
        sessions.remove(session);

        // remove from current game
        GameSession game = client.getGame();
        game.removeClient(client);

        // alert players
        game.sendSessionToAllClients();
    }

    /**
     * Handles the creation of a new game session.
     *
     * @param client The client creating the game.
     */
    public void handleCreateMessage(Client client, String name) {
        // create a new game session with the client as the host
        if(!name.equals("guest")) {
            client.setClientId(name);
        }

        GameSession game = new EuchreGame(client);
        // store game session
        games.put(game.getGameId(), game);
        // send message to all players in game that a new client has joined
        game.sendSessionToAllClients();
    }

    /**
     * Handles new clients joining an existing game session.
     *
     * @param client client that is trying to join a game
     */
    public void handleJoinMessage(Client client, String id, String name) {
        if(!name.equals("guest")) {
            client.setClientId(name);
        }
        // check that the game with the id sent by the client exists
        if (games.containsKey(id)) {
            // get game with corresponding id
            GameSession game = games.get(id);
            // check that the client is not already in this game
            if (!game.hasClient(client)) {
                // add player to game
                if (game.addClient(client)) {
                    // send message to all players in game that a new client has joined
                    game.sendSessionToAllClients();
                } else {
                    client.reportError("Game " + id + " is already full");
                }
            } else {
                client.reportError("You are already in game " + id);
            }
        } else {
            client.reportError("Game " + id + " does not exist");
        }
    }

    /**
     * Handles players leaving a game session
     * 
     * @param client the client leaving the game
     */
    public void handleLeaveMessage(Client client) {
        if (client.isInGame()) {
            // remove from current game
            GameSession game = client.getGame();
            game.removeClient(client);

            // alert players
            game.sendSessionToAllClients();

            // alert client that they can leave the session
            client.sendMessage("leave", "You can now leave the game");
        } else {
            client.reportError("You are currently not in a game");
        }
    }

    /**
     * Handles starting a game session.
     *
     * @param game   The GameSession to start.
     * @param client the client that started the game
     */
    public void handleStartGame(GameSession game, Client client) {
        // cast variables
        EuchreGame euchreGame = (EuchreGame) game;

        // check if the game can be started
        if (euchreGame != null) {
            if (euchreGame.isHost(client)) {
                // start the game
                euchreGame.startGame();
            } else {
                client.reportError("Only the host can start the game");
            }
        } else {
            client.reportError("Please create or join a game first");
        }
    }

    /*
     * The following functions will handle in-game events such as the set up of
     * client side variables, and other game loop related events.
     */

    /**
     * Handles client-side set up of the game.
     * 
     * This class does the following things:
     * 1. Send player ID to their corresponding client
     * 2. Send all the player IDs to client
     * 3. Send the initial player's hand to the client
     * 
     * @param client the client requesting the data
     */
    public void handleGameSetUp(GameSession game, Client client) {
        client.setReady();
        // make sure that all clients are ready before sending data
        if (game.areClientsReady()) {
            EuchreGame euchreGame = (EuchreGame) game;
            for (Client c : euchreGame.getConnectedClients()) {
                c.sendClient();
                //player.sendPlayer(); //TODO move this out of player class?
            }
            euchreGame.sendSessionToAllClients();
        }
    }

    /**
     * Handles events that happen during a game, and controls the overall flow of
     * the state-machine
     * 
     * Events handled by this function:
     * 1.
     * 
     * @param game    the game corresponding to the event
     * @param client  the client that triggered the event
     * @param payload contains the JSON data of the game event
     */
    public void handleGameEvent(GameSession game, Client client, JsonNode payload) {
        // implement game logic
        EuchreGame euchreGame = (EuchreGame) game;
        GameState.PHASE currentPhase = euchreGame.getCurrentPhase();

        Player activePlayer = euchreGame.getActivePlayer();
        Client activeClient;
        HumanPlayerDecorator humanPlayer;
        //Make sure active player is a human player
        if(!activePlayer.getClass().equals(HumanPlayerDecorator.class)){
            return;
        }
        humanPlayer = (HumanPlayerDecorator) activePlayer;
        activeClient = humanPlayer.getClient();

        //If the client we received the message from isn't the active player, return
        if(client != activeClient){
            return;
        }

        Card c;
        Card.SUIT suit;
        boolean proceed;
        
        switch (currentPhase) {
            case PICKTRUMP1:
            case PICKTRUMP2:
                suit = Card.stringToSuit(payload.get("Suit").asText());
                proceed = euchreGame.pickTrump(suit);
                if(proceed){
                    euchreGame.playTrick(null);
                }
                break;

            case REPLACECARD:
                c = Card.fromJSON(payload.get("Card").asText());
                euchreGame.replaceCard(c);
                euchreGame.playTrick(null);
                break;

            case PLAYTRICK:
                c = Card.fromJSON(payload.get("Card").asText());
                proceed = euchreGame.playTrick(c);
                if(proceed){
                    euchreGame.scoreTrick();
                }
                break;
        
            default:
                break;
        }
        
        
    }

    /**
     * Handles the processing of an in-game chat message
     * 
     * @param game    the game session that the message was sent in
     * @param sender  the client that sent the message
     * @param message the string representation of the message
     */
    public void handleChatMessages(GameSession game, Client sender, String message) {
        game.processMessage(sender, message);
    }
}