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
 *
 * @author jaime zepeda
 */
const Hand = ({ cards }: Hand) => {
  const spreadAngle = 5; // Angle by which each card is spread out
  const spreadOffset = ((cards.length - 1) * spreadAngle) / 2; // Offset for centering the spread
  const padding = 30; // Padding between cards
  const totalWidth = (cards.length - 1) * padding; // Total width of the hand

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
            className="absolute"
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
