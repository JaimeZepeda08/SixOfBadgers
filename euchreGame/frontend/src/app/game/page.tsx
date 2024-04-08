"use client";

import { useEffect, useState } from "react";
import { getPlayerHand, submitSelectedCard, getPlayers } from "@/lib/gameService";
import Hand from "../../components/Hand";
import './css_files_game/buttonStyles.css';


/**
 * Game component representing the main page of the game. Each person
 * will have a set of cards that are fanned out. All opponents will be hidden while
 * the users will be shown to them.
 * 
 * @returns {JSX.Element} The Home component.
 */

export default function Page() {
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
  const [selectedCard, setSelectedCard] = useState<{ suit: string; value: string } | null>(null);

  // state to handle all player named given at start of game
  const [players, setPlayers] = useState(
    {
      player1: {
        name:"player1",
        wins: "25",
        gamesPlayed: "101",
        score: "1"
      },
      player2: {
        name:"player2",
        wins: "26",
        gamesPlayed: "101",
        score: "2"
      },
      player3: {
        name:"player3",
        wins: "27",
        gamesPlayed: "102",
        score: "3"
      },
      player4: {
        name:"player4",
        wins: "28",
        gamesPlayed: "103",
        score: "4"
      },
    }
  )

  const [tooltipVisible, setTooltipVisible] = useState({ player1: false, player2: false, player3: false, player4: false });

  useEffect(() => {
    async function fetchPlayers() {
      const players = await getPlayers();
      if (players != null) {
        setPlayerHand(players);
      } else {
        console.error("Players are undefined");
      }
    }
    getPlayers();
  }, []);

  // Effect hook to fetch player's hand when the component mounts
  useEffect(() => {
    async function fetchPlayerHand() {
      // Fetch player's hand from the server
      const hand = await getPlayerHand();
      if (hand && hand.length > 0) {
        // Update state with the fetched player's hand
        setPlayerHand(hand);
      } else {
        console.error("Player hand is undefined");
      }
    }
    fetchPlayerHand();
  }, []);

  // Parse the player's hand JSON string into an object
  const cards = JSON.parse(playerHand);

  // placeholder for opponents cards
  const opponents = [
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
  ];

type PlayerIdentifier = 'player1' | 'player2' | 'player3' | 'player4';

const toggleTooltip = (player: PlayerIdentifier) => {
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

  useEffect(() => {
    if (selectedCard !== null) {
      console.log("Suit: " + selectedCard.suit + " Value: " + selectedCard.value);
    }
  }, [selectedCard]);

  // placeholder for opponents cards
  const handleOppSelect = (suit: string | null, value: string | null) => {
    console.log("Remove option of touching others cards")
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // if (selectedCard && selectedCard.suit !== "?" && selectedCard.value !== "?") {
    if(selectedCard !== null) {
        const formData = new FormData();
        formData.append("suit", selectedCard.suit);
        formData.append("value", selectedCard.value);
        await submitSelectedCard(formData);
    }
  };


  return (
    <div className="h-screen flex justify-center items-center relative bg-cover bg-center" style={{ backgroundImage: 'url("/textures/wood3.jpg")' }}>
      <div className="flex justify-center items-center relative rounded-2xl" style={{ width: '90%', height: '85%', backgroundColor: 'rgba(74, 160, 74)', border: '3px solid #c0c0c0' }}>
        <div className="absolute bottom-10" style={{ transform: "rotate(0deg)" }}>
          <Hand cards={cards} onCardSelect={handleCardSelect}/>
          <div className="text-center mt-10">
            <div 
              onMouseEnter={() => toggleTooltip('player1')} 
              onMouseLeave={() => toggleTooltip('player1')} 
              className="inline-block bg-white rounded-full px-4 py-1 text-sm font-semibold text-gray-700 relative cursor-pointer"
            >
              {players.player1.name}
              {tooltipVisible.player1 && (
                <div className="absolute z-10 w-40 p-2 -mt-30 left-1/2 transform -translate-x-1/2 text-white bg-gray-900 rounded-lg shadow-md">
                  Wins: {players.player1.wins}<br/>
                  Games Played: {players.player1.gamesPlayed}
                </div>
              )}
            </div>
            <h2 className="text-white mt-2">Score: {players.player1.score}</h2>
          </div>
        </div>

        <div className="absolute left-10 pb-10" style={{ transform: "rotate(90deg)" }}>
          <Hand cards={opponents} onCardSelect={handleOppSelect}/>
          <div className="text-center mt-10" style={{transform: "rotate(-90deg)"}}>
            <div 
              onMouseEnter={() => toggleTooltip('player2')} 
              onMouseLeave={() => toggleTooltip('player2')} 
              className="inline-block bg-white rounded-full px-4 py-1 text-sm font-semibold text-gray-700 relative cursor-pointer"
            >
              {players.player2.name}
              {tooltipVisible.player2 && (
                <div className="absolute z-10 w-40 p-2 -mt-30 left-1/2 transform -translate-x-1/2 text-white bg-gray-900 rounded-lg shadow-md">
                  Wins: {players.player2.wins}<br/>
                  Games Played: {players.player2.gamesPlayed}
                </div>
              )}
            </div>
            <h2 className="text-white mt-2">Score: {players.player2.score}</h2>
          </div>
        </div>

        <div className="absolute top-10" style={{ transform: "rotate(180deg)" }}>
          <Hand cards={opponents} onCardSelect={handleOppSelect}/>  
          <div className="text-center mt-10" style={{transform: "rotate(-180deg)"}}>
            <div 
              onMouseEnter={() => toggleTooltip('player3')} 
              onMouseLeave={() => toggleTooltip('player3')} 
              className="inline-block bg-white rounded-full px-4 py-1 text-sm font-semibold text-gray-700 relative cursor-pointer"
            >
              {players.player3.name}
              {tooltipVisible.player3 && (
                <div className="absolute z-10 w-40 p-2 -mt-30 left-1/2 transform -translate-x-1/2 text-white bg-gray-900 rounded-lg shadow-md">
                  Wins: {players.player3.wins}<br/>
                  Games Played: {players.player3.gamesPlayed}
                </div>
              )}
            </div>
            <h2 className="text-white mt-2">Score: {players.player3.score}</h2>
          </div>
        </div>

        <div className="absolute right-10 pb-10" style={{ transform: "rotate(270deg)" }}>
          <Hand cards={opponents} onCardSelect={handleOppSelect}/>
          <div className="text-center mt-10" style={{transform: "rotate(90deg)"}}>
            <div 
              onMouseEnter={() => toggleTooltip('player4')} 
              onMouseLeave={() => toggleTooltip('player4')} 
              className="inline-block bg-white rounded-full px-4 py-1 text-sm font-semibold text-gray-700 relative cursor-pointer"
            >
              {players.player4.name}
              {tooltipVisible.player4 && (
                <div className="absolute z-10 w-40 p-2 -mt-30 left-1/2 transform -translate-x-1/2 text-white bg-gray-900 rounded-lg shadow-md">
                  Wins: {players.player4.wins}<br/>
                  Games Played: {players.player4.gamesPlayed}
                </div>
              )}
            </div>
            <h2 className="text-white mt-2">Score: {players.player4.score}</h2>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="absolute bottom-0 right-0">
          <button
            type="submit"
            className={`baseButton ${!selectedCard ? 'disabled' : ''} mb-20 mr-80`}
            disabled={!selectedCard}
            onClick={() => console.log('Submit action')}
          >
            Play Card
          </button>
        </form>
      </div>
    </div>
  );
}
