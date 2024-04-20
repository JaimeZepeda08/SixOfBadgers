import React from "react";
import Card from "@/components/Card";

/**
 * TrumpSuit component.
 * 
 * @param {object} props - Component props.
 * @param {string} props.trumpSuit - The current trump suit.
 * @returns {JSX.Element} TrumpSuit component.
 */
const TrumpSuit = ({ trumpSuit }) => {
  return (
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
  );
}

export default TrumpSuit;
