'use client';

import React, { use, useState, useEffect } from "react";
import Card from "./Card";
import clsx from "clsx";

interface Hand {
  cards: Card[];
  onCardSelect: (suit: string | null, value: string | null) => void;
}


interface Hand {
  cards: Card[];
}

/**
 * Hand Component
 *
 * Renders a visual representation of a hand of cards. This component is designed to display a sequence of player cards
 * in a fan-like spread. It dynamically adjusts the layout based on the number of cards, making sure that the hand is visually
 * appealing and that individual cards are distinguishable and selectable.
 *
 * @param {object} Hand The component's props
 * @param {Card[]} cards An array of cards
 * @returns HTML representation of a hand of cards
 *
 * @author jaime zepeda
 */
const Hand = ({ cards, onCardSelect }: Hand) => {
  const spreadAngle = 5; // Angle by which each card is spread out
  const spreadOffset = ((cards.length - 1) * spreadAngle) / 2; // Offset for centering the spread
  const padding = 30; // Padding between cards
  const totalWidth = (cards.length - 1) * padding; // Total width of the hand

   // change selected card when we click on another card
   const [selectedCard, setSelectedCard] = useState<number | null>(null);

   // selected card will have the values sent to game otherwise when
   // deselected it will be null values
   const handleCardClick = (index: number) => {
     if(index === selectedCard) {
       setSelectedCard(null);
       onCardSelect(null, null);
     } else {
       setSelectedCard(index);
       onCardSelect(cards[index].suit, cards[index].value);
     }
   };
 
   // hook to correctly display current values of the selected card
   useEffect(() => {
     if (selectedCard !== null) {
       console.log("Suit: " + cards[selectedCard].suit + " " + "Value: " + cards[selectedCard].value);
     }
   }, [selectedCard, cards]);
 

  return (
    <div
      className="relative flex justify-center items-center"
      style={{ width: `${totalWidth}px` }}
    >
      {/* map out all cards and make the card hover if we're over it or we already selected it */}
      {cards.map((card, index) => {
        const rotation = index * spreadAngle - spreadOffset;
        const translateX = padding * index - totalWidth / 2;
        return (
          // when we click on this card it will be handled as a selected card
          <div
            key={index}
            className="absolute cursor-pointer"
            onClick={() => handleCardClick(index)}
            style={{
              transform: `rotate(${rotation}deg) translateX(${translateX}px) translateY(-50%)`
            }}
          >
            <Card suit={card.suit} value={card.value} isSelected={index === selectedCard} />
          </div>
        );
      })}
    </div>
  );
};

export default Hand;
