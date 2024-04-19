"use client";

import { use, useCallback, useEffect, useState } from "react";
import { getPlayerHand, submitSelectedCard, getPlayers, getTrumpSuit, getCurrentPlayer } from "@/lib/gameService";
import Hand from "../../components/Hand";
import Card from "../../components/Card";
import './css_files_game/buttonStyles.css';
import { useSocket } from "@/lib/useSocket";
import MessagePanel from "../../components/MessagePanel"


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

  // const onMessage = useCallback((message: MessageEvent) => {
  //   const json_response = JSON.parse(message.data);
  //   if (json_response.header === "username") {
  //     console.log(json_response.content);
  //     setUsername(json_response.content);
  //   }
  //   if (json_response.header === "players") {
  //     const playerString = json_response.content;
  //     let playerList = playerString
  //       .substring(1, playerString.length - 1)
  //       .split(",");
  //     // sort players (the player corresponding to this client should go first)
  //     playerList = sortPlayerList(playerList, username);
  //     setPlayers2(playerList);
  //     console.log(players2.toString());
  //   }
  // }, []);

  // function sortPlayerList(playerList: string[], username: string) {
  //   if (!playerList.includes(username)) {
  //     return playerList;
  //   }

  //   const ownIndex = playerList.indexOf(username);
  //   const sortedList = playerList
  //     .slice(ownIndex)
  //     .concat(playerList.slice(0, ownIndex));
  //   return sortedList;
  // }


  // create socket hook
  const socket = useSocket();

  // fetch initial data
  // useEffect(() => {
  //   socket.send(JSON.stringify({ header: "setup" }));
  // }, []);

  // states for all player names as well as this clients name
  const [username, setUsername] = useState<string>("");
  const [players2, setPlayers2] = useState<string[]>([]);

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
  const [turnNotification, setTurnNotification] = useState({ show: false, player: "player3" });

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

    if(json_response.header == "players") {
      setPlayers2(json_response.content);
    }

    if(json_response.header === "userName") {
      setUsername(json_response.content);
    }

    if(json_response.header === "currentPlayer") {
      setCurrentPlayerTurn(json_response.content);
      showTurnNotification(json_response.content);
    }

    if(json_response.header === "playersData") {
      setPlayers(json_response.content);
    }

    if(json_response.header === "playerHand") {
      setPlayerHand(json_response.content);
    }
    
    if(json_response.header === "trumpSuit") {
      setTrumpSuit(json_response.content);
    }

    if(json_response.header === "pointsToWin") {
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

  // notification handler given by current player message
  const showTurnNotification = (player: string) => {
    setTurnNotification({ show: true, player });
    setTimeout(() => setTurnNotification({ show: false, player: "" }), 1000);  // Hide the popup after 1 second
  };

  // useEffect hook to trigger turn notification when currentPlayerTurn changes
  useEffect(() => {
    if(currentPlayerTurn == username) {
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
    if(selectedCard !== null) {
        socket.send(JSON.stringify({ suit: selectedCard.suit, value: selectedCard.value }));
    }
  };

  return (
    <div className="h-screen flex justify-center items-center relative bg-cover bg-center" style={{ backgroundImage: 'url("/textures/wood3.jpg")' }}>

        {/* Conditional rendering for turn notification */}
        {turnNotification.show && (
          <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 z-50">
            <div className="p-6 text-lg text-black rounded-lg" style={{background: 'linear-gradient(to right, red, orange)'}} role="alert">
              It Is Your Turn!
            </div>
          </div>
        )}

      <div className="flex justify-center items-center relative rounded-2xl" style={{ width: '90%', height: '85%', backgroundColor: 'rgba(74, 160, 74)', border: '3px solid #c0c0c0' }}>

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
        <div
          className="absolute left-10 pb-10"
          style={{ transform: "rotate(90deg)" }}
        >
          <Hand cards={opponents} onCardSelect={handleOppSelect} />
          <div
            className="text-center mt-10"
            style={{ transform: "rotate(-90deg)" }}
          >
            <div
              onMouseEnter={() => toggleTooltip(players.player2.name)}
              onMouseLeave={() => toggleTooltip(players.player2.name)}
              className="inline-block bg-white rounded-full px-4 py-1 text-sm font-semibold text-gray-700 relative cursor-pointer"
            >
              {players.player2.name}
              {tooltipVisible.player2 && (
                <div className="absolute z-10 w-40 p-2 -mt-30 left-1/2 transform -translate-x-1/2 text-white bg-gray-900 rounded-lg shadow-md">
                  Wins: {players.player2.wins}
                  <br />
                  Games Played: {players.player2.gamesPlayed}
                </div>
              )}
            </div>
            <h2 className="text-white mt-2">Score: {players.player2.score}</h2>
          </div>
        </div>

        {/* Render player 3 */}
        <div className="absolute top-5" style={{ transform: "rotate(180deg)" }}>
          <Hand cards={opponents} onCardSelect={handleOppSelect} />
          <div
            className="text-center mt-5"
            style={{ transform: "rotate(-180deg)" }}
          >
            <div
              onMouseEnter={() => toggleTooltip(players.player3.name)}
              onMouseLeave={() => toggleTooltip(players.player3.name)}
              className="inline-block bg-white rounded-full px-4 py-1 text-sm font-semibold text-gray-700 relative cursor-pointer"
            >
              {players.player3.name}
              {tooltipVisible.player3 && (
                <div className="absolute z-10 w-40 p-2 -mt-30 left-1/2 transform -translate-x-1/2 text-white bg-gray-900 rounded-lg shadow-md">
                  Wins: {players.player3.wins}
                  <br />
                  Games Played: {players.player3.gamesPlayed}
                </div>
              )}
            </div>
            <h2 className="text-white mt-2">Score: {players.player3.score}</h2>
          </div>
        </div>

        {/* Render player 4 */}
        <div
          className="absolute right-10 pb-10"
          style={{ transform: "rotate(270deg)" }}
        >
          <Hand cards={opponents} onCardSelect={handleOppSelect} />
          <div
            className="text-center mt-10"
            style={{ transform: "rotate(90deg)" }}
          >
            <div
              onMouseEnter={() => toggleTooltip(players.player4.name)}
              onMouseLeave={() => toggleTooltip(players.player4.name)}
              className="inline-block bg-white rounded-full px-4 py-1 text-sm font-semibold text-gray-700 relative cursor-pointer"
            >
              {players.player4.name}
              {tooltipVisible.player4 && (
                <div className="absolute z-10 w-40 p-2 -mt-30 left-1/2 transform -translate-x-1/2 text-white bg-gray-900 rounded-lg shadow-md">
                  Wins: {players.player4.wins}
                  <br />
                  Games Played: {players.player4.gamesPlayed}
                </div>
              )}
            </div>
            <h2 className="text-white mt-2">Score: {players.player4.score}</h2>
          </div>
        </div>

        {/* Form for card submission */}
        <form onSubmit={handleSubmit} className="absolute bottom-0 right-0">
          <button
            type="submit"
            className={`baseButton ${
              !selectedCard ? "disabled" : ""
            } mb-20 mr-80`}
            disabled={!selectedCard}
            onClick={() => console.log("Submit action")}
          >
            Play Card
          </button>
        </form>

        {/* Render Trump Suit */}
        <div
          style={{
            position: "absolute",
            top: "47.5%",
            right: "calc(50% + 200px)",
            transform: "translateY(-50%)",
          }}
        >
          <h1 className="font-bold font-germania pb-3">Trump Suit</h1>
          <Card suit={trumpSuit} value="" isSelected={false} />
        </div>

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

      <MessagePanel/>
    </div>
  );
}
