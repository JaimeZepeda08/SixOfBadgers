"use client";

import { useEffect, useState } from "react";
import { getPlayerHand } from "@/lib/gameService";
import Hand from "../../components/Hand";
import { submitSelectedCard } from '@/lib/gameService';
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
      { suit: "SPADES", value: "J" },
    ])
  );

  // use state to keep track of currently selected card
  const [selectedCard, setSelectedCard] = useState<{ suit: string; value: string } | null>(null);


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
    <div className="h-screen flex justify-center items-center relative" style={{ backgroundColor: 'rgb(74, 160, 74)' }}>
      <div className="absolute bottom-8" style={{ transform: "rotate(0deg)" }}>
        <Hand cards={cards} onCardSelect={handleCardSelect}/>
      </div>
      <div className="absolute left-0" style={{ transform: "rotate(90deg)" }}>
        <Hand cards={opponents} onCardSelect={handleOppSelect}/>
      </div>
      <div className="absolute top-8" style={{ transform: "rotate(180deg)" }}>
        <Hand cards={opponents} onCardSelect={handleOppSelect}/>  
      </div>
      <div className="absolute right-0" style={{ transform: "rotate(270deg)" }}>
        <Hand cards={opponents} onCardSelect={handleOppSelect}/>
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
  );
}
