import React from "react";
import Card from "./Card";

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

  return (
    <div
      className="relative flex justify-center items-center"
      style={{ width: `${totalWidth}px` }}
    >
      {cards.map((card, index) => {
        const rotation = index * spreadAngle - spreadOffset;
        const translateX = padding * index - totalWidth / 2;
        return (
          <div
            key={index}
            className="absolute hover:z-50"
            style={{
              transform: `rotate(${rotation}deg) translateX(${translateX}px) translateY(-50%)`,
            }}
          >
            <Card suit={card.suit} value={card.value} />
          </div>
        );
      })}
    </div>
  );
};

export default Hand;
