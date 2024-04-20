import React from "react";
import Card from "@/components/Card";

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
