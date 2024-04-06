'use client';

import React, { use, useState, useEffect } from "react";
import Hand from "../../components/Hand";
import { submitSelectedCard } from '@/lib/gameService';

/**
 * Home component representing the main page of the game. Each person
 * will have a set of cards that are fanned out. All opponents will be hidden while
 * the users will be shown to them.
 * 
 * @returns {JSX.Element} The Home component.
 */
export default function Home() {
  // placeholder for user cards
  const cards = [
    { suit: "HEARTS", value: "A" },
    { suit: "CLUBS", value: "Q" },
    { suit: "SPADES", value: "J" },
    { suit: "DIAMONDS", value: "K" },
    { suit: "CLUBS", value: "Q" },
    { suit: "SPADES", value: "J" },
  ];

  // placeholder for opponents cards
  const opponents = [
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
  ];

  // use state to keep track of currently selected card
  const [selectedCard, setSelectedCard] = useState<{ suit: string; value: string } | null>(null);

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
      <form onSubmit={handleSubmit} className="absolute bottom-0 right-0 m-4">
        <button
          type="submit"
          className={`px-4 py-2 font-bold text-white rounded ${
            selectedCard ? 'bg-blue-500 hover:bg-blue-700' : 'bg-gray-400 cursor-not-allowed'
          }`}
          disabled={!selectedCard}
          onClick={() => console.log('Submit action')}
        >
          Submit Selected Card
        </button>
      </form>
      
    </div>
  );
}
