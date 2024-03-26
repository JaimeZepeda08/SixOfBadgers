'use client';

import React, { use, useState } from "react";
import Card from "./Card";
import clsx from "clsx";

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
 */
const Hand = ({ cards }: Hand) => {
  const spreadAngle = 5;
  const spreadOffset = ((cards.length - 1) * spreadAngle) / 2;
  const padding = 30;
  const totalWidth = (cards.length - 1) * padding;

  // change selected card when we click on another card
  const [selectedCard, setSelectedCard] = useState<number | null>(null);

  const handleCardClick = (index: number) => {
    setSelectedCard(index === selectedCard ? null : index); // Toggle selection
  };

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
