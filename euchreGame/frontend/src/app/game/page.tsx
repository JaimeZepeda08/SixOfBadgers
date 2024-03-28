'use client';

import React, { use, useState, useEffect } from "react";
import Hand from "../../components/Hand";
import { currentSelectedCard } from '@/lib/userService';

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
    { suit: "hearts", value: "A" },
    { suit: "clubs", value: "Q" },
    { suit: "spades", value: "J" },
    { suit: "diamonds", value: "K" },
    { suit: "clubs", value: "Q" },
    { suit: "spades", value: "J" },
  ];

  // placeholder for opponents cards
  const opponents = [
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
  ];

  // use state to keep track of currently selected card
  const [selectedCard, setSelectedCard] = useState<{ suit: string; value: string } | null>(null);

  const handleCardSelect = (suit: string, value: string) => {
    setSelectedCard({ suit, value });
  };

  useEffect(() => {
    if (selectedCard !== null) {
      console.log("Suit: " + selectedCard.suit + " Value: " + selectedCard.value);
    }
  }, [selectedCard]);

  // placeholder for opponents cards
  const handleOppSelect = (suit: string, value: string) => {
    console.log("Remove option of touching others cards")
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (selectedCard && selectedCard.suit !== "?" && selectedCard.value !== "?") {
        const formData = new FormData();
        formData.append("suit", selectedCard.suit);
        formData.append("value", selectedCard.value);
        await currentSelectedCard(formData);
    }
};


  return (
    <div className="h-screen flex justify-center items-center relative">
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
        <button type="submit" className="p-2 bg-blue-500 text-white rounded hover:bg-blue-700">
            Submit Selected Card
        </button>
        </form>
    </div>
  );
}
