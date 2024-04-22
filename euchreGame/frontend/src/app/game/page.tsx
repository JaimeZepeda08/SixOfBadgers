"use client";

import { use, useCallback, useEffect, useState } from "react";
import {
  getPlayerHand,
  submitSelectedCard,
  getPlayers,
  getTrumpSuit,
  getCurrentPlayer,
} from "@/lib/gameService";
import Hand from "../../components/Hand";
import Card from "../../components/Card";
import "./css_files_game/buttonStyles.css";
import { useSocket } from "@/lib/useSocket";
import MessagePanel from "../../components/MessagePanel";
import Opponent from "./game_components/Opponent";
import CardSubmissionForm from "./game_components/CardSubmissionForm";
import TrumpSuit from "./game_components/TrumpSuit";
import Link from "next/link";
import GameModal from "./game_components/GameModal";

/**
 * Game component representing the main page of the game. Each person
 * will have a set of cards that are fanned out. All opponents will be hidden while
 * the users will be shown to them.
 *
 * This component fetches the player's hand, allows the user to select a card to play,
 * and displays the current trump suit. It also renders the player's name, wins, games played,
 * and score, along with tooltips for additional information.
 *
 * @returns {JSX.Element} The Game component, which includes player hands, trump suit, and other game elements.
 */
export default function Page() {
  // create socket hook
  const socket = useSocket();

  // fetch initial data
  // useEffect(() => {
  //   socket.send(JSON.stringify({ header: "setup" }));
  // }, []);

  // states for all player names as well as this clients name
  const [username, setUsername] = useState<string>("player1");
  const [players2, setPlayers2] = useState<string[]>([
    "player1",
    "player2",
    "player3",
    "player4",
  ]);

  // State variable to hold the player's hand, initialized with dummy data
  const [playerHand, setPlayerHand] = useState(
    JSON.stringify([
      { suit: "HEARTS", value: "A" },
      { suit: "CLUBS", value: "Q" },
      { suit: "SPADES", value: "J" },
      { suit: "DIAMONDS", value: "K" },
      { suit: "CLUBS", value: "Q" },
    ])
  );

  // use state to keep track of currently selected card
  const [selectedCard, setSelectedCard] = useState<{
    suit: string;
    value: string;
  } | null>(null);

  // state to handle new trump suit
  const [trumpSuit, setTrumpSuit] = useState("SPADES");

  // state to handle all player named given at start of game
  const [players, setPlayers] = useState({
    player1: {
      name: "player1",
      wins: "25",
      gamesPlayed: "101",
      score: "1",
    },
    player2: {
      name: "player2",
      wins: "26",
      gamesPlayed: "101",
      score: "2",
    },
    player3: {
      name: "player3",
      wins: "27",
      gamesPlayed: "102",
      score: "3",
    },
    player4: {
      name: "player4",
      wins: "28",
      gamesPlayed: "103",
      score: "4",
    },
  });

  // state to for current player (who is playing their card)
  const [currentPlayerTurn, setCurrentPlayerTurn] = useState("player1");

  // state to handle who recieves the notification
  const [turnNotification, setTurnNotification] = useState({
    show: false,
    player: "player3",
  });

  // state for which tooltip is active for a player
  const [tooltipVisible, setTooltipVisible] = useState({
    player1: false,
    player2: false,
    player3: false,
    player4: false,
  });

  // state for score to win a round
  const [pointsToWin, setPointsToWin] = useState(0);

  // placeholder for opponents cards
  const opponents = [
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
  ];

  // Parse the player's hand JSON string into an object
  const cards = JSON.parse(playerHand);

  // case statements for handling incoming messages
  const onMessage = useCallback((message: MessageEvent) => {
    const json_response = JSON.parse(message.data);

    if (json_response.header === "players") {
      const playerString = json_response.content;
      let playerList = playerString
        .substring(1, playerString.length - 1)
        .split(",");
      // sort players (the player corresponding to this client should go first)
      playerList = sortPlayerList(playerList, username);
      setPlayers2(playerList);
    }

    if (json_response.header === "userName") {
      setUsername(json_response.content);
    }

    if (json_response.header === "currentPlayer") {
      setCurrentPlayerTurn(json_response.content);
      showTurnNotification(json_response.content);
    }

    if (json_response.header === "playersData") {
      setPlayers(json_response.content);
    }

    if (json_response.header === "playerHand") {
      setPlayerHand(json_response.content);
    }

    if (json_response.header === "trumpSuit") {
      setTrumpSuit(json_response.content);
    }

    if (json_response.header === "pointsToWin") {
      setPointsToWin(json_response.content);
    }
  }, []);

  // hook for grabbing messages being transmitted
  useEffect(() => {
    socket.addEventListener("message", onMessage);
    return () => {
      socket.removeEventListener("message", onMessage);
    };
  }, [socket, onMessage]);

  // sorts usernames so that the clients name comes first in the list
  function sortPlayerList(playerList: string[], username: string) {
    if (!playerList.includes(username)) {
      return playerList;
    }

    const ownIndex = playerList.indexOf(username);
    const sortedList = playerList
      .slice(ownIndex)
      .concat(playerList.slice(0, ownIndex));
    return sortedList;
  }

  // notification handler given by current player message
  const showTurnNotification = (player: string) => {
    setTurnNotification({ show: true, player });
    setTimeout(() => setTurnNotification({ show: false, player: "" }), 2000); // Hide the popup after 1 second
  };

  // useEffect hook to trigger turn notification when currentPlayerTurn changes
  useEffect(() => {
    if (currentPlayerTurn == username) {
      showTurnNotification(currentPlayerTurn);
    }
  }, [currentPlayerTurn]);

  // hook to correctly show updated selected card
  useEffect(() => {
    if (selectedCard !== null) {
      console.log(
        "Suit: " + selectedCard.suit + " Value: " + selectedCard.value
      );
    }
  }, [selectedCard]);

  // based on which name we are hovering that tooltip will be visible
  const toggleTooltip = (player: string) => {
    setTooltipVisible((prev) => ({ ...prev, [player]: !prev[player] }));
  };

  // selected card will either have values or be null for sending
  // data to the backend
  const handleCardSelect = (suit: string | null, value: string | null) => {
    if (suit === null || value === null) {
      setSelectedCard(null);
    } else {
      setSelectedCard({ suit, value });
    }
  };

  // placeholder for opponents cards
  const handleOppSelect = (suit: string | null, value: string | null) => {
    console.log("Remove option of touching others cards");
  };

  // submits selected card to play
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (selectedCard !== null) {
      console.log(selectedCard.suit + " " + selectedCard.value);
      socket.send(
        JSON.stringify({ suit: selectedCard.suit, value: selectedCard.value })
      );
    }
  };

  // state to handle the modal when the game finishes
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    const isGameOver = Object.values(players).some(
      (player) => parseInt(player.score) === pointsToWin
    );
    if (isGameOver) {
      openModal();
    }
  }, [players, pointsToWin]);

  const openModal = () => {
    setIsOpen(true);
  };

  return (
    <div
      className="h-screen flex justify-center items-center relative bg-cover bg-center"
      style={{ backgroundImage: 'url("/textures/wood3.jpg")' }}
    >
      {/* Conditional rendering for turn notification */}
      {turnNotification.show && (
        <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 z-50">
          <div
            className="p-6 text-lg text-black rounded-lg"
            style={{ background: "linear-gradient(to right, red, orange)" }}
            role="alert"
          >
            It Is Your Turn!
          </div>
        </div>
      )}

      <div
        className="flex justify-center items-center relative rounded-2xl"
        style={{
          width: "90%",
          height: "85%",
          backgroundColor: "rgba(74, 160, 74)",
          border: "3px solid #c0c0c0",
        }}
      >
        {/* Render player 1 */}
        <div
          className="absolute bottom-5"
          style={{ transform: "rotate(0deg)" }}
        >
          <Hand cards={cards} onCardSelect={handleCardSelect} />
          <div className="text-center mt-5">
            <div
              onMouseEnter={() => toggleTooltip(players.player1.name)}
              onMouseLeave={() => toggleTooltip(players.player1.name)}
              className="inline-block bg-white rounded-full px-4 py-1 text-sm font-semibold text-gray-700 relative cursor-pointer"
            >
              {players.player1.name}
              {tooltipVisible.player1 && (
                <div className="absolute z-10 w-40 p-2 -mt-30 left-1/2 transform -translate-x-1/2 text-white bg-gray-900 rounded-lg shadow-md">
                  Wins: {players.player1.wins}
                  <br />
                  Games Played: {players.player1.gamesPlayed}
                </div>
              )}
            </div>
            <h2 className="text-white mt-2">Score: {players.player1.score}</h2>
          </div>
        </div>

        {/* Render player 2 */}
        <Opponent
          name={players2[1]}
          player={{
            ...players[players2[1]],
            position: "left-10 pb-10",
            rotation: 90,
            key: players2[1],
          }}
          cards={opponents}
          onCardSelect={handleOppSelect}
          tooltipVisible={tooltipVisible}
          toggleTooltip={toggleTooltip}
          padding="pr-10"
        />

        {/* Render player 3 */}
        <Opponent
          name={players2[2]}
          player={{
            ...players[players2[2]],
            position: "top-5",
            rotation: 180,
            key: "player3",
          }}
          cards={opponents}
          onCardSelect={handleOppSelect}
          tooltipVisible={tooltipVisible}
          toggleTooltip={toggleTooltip}
          padding=""
        />

        {/* Render player 4 */}
        <Opponent
          name={players2[3]}
          player={{
            ...players[players2[3]],
            position: "right-10 pb-10",
            rotation: 270,
            key: "player4",
          }}
          cards={opponents}
          onCardSelect={handleOppSelect}
          tooltipVisible={tooltipVisible}
          toggleTooltip={toggleTooltip}
          padding="pl-10"
        />

        {/* Form for card submission */}
        <CardSubmissionForm
          handleSubmit={handleSubmit}
          selectedCard={selectedCard}
        />

        {/* Render Trump Suit */}
        <TrumpSuit trumpSuit={"CLUBS"} />

        {/* Render played cards */}
        <div
          className="flex flex-row"
          style={{
            position: "absolute",
            top: "50%",
            right: "calc(50% - 250px)",
            transform: "translateY(-50%)",
          }}
        >
          {/* players hands */}
          <Card suit="CLUBS" value="1" isSelected={false} />
          <Card suit="SPADES" value="2" isSelected={false} />
          <Card suit="DIAMONDS" value="3" isSelected={false} />
          <Card suit="HEARTS" value="4" isSelected={false} />
        </div>
      </div>

      {/* modal for when the game finishes */}
      <GameModal isOpen={isOpen} />

      <MessagePanel />
    </div>
  );
}
