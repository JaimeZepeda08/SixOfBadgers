import React from "react";
import Card from "./Card";

const Hand = ({ cards }) => {
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
