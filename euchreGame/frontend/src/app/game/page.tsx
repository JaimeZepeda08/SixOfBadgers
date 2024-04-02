"use client";

import { useEffect, useState } from "react";
import { getPlayerHand } from "@/lib/gameService";
import Hand from "../../components/Hand";

/**
 * React component representing the Game page
 *
 * @returns JSX element
 *
 * @author jaime zepeda
 */
export default function Page() {
  // State variable to hold the player's hand, initialized with dummy data
  const [playerHand, setPlayerHand] = useState(
    `[{"suit":"?","value":"?"}, {"suit":"?","value":"?"}, {"suit":"?","value":"?"}, {"suit":"?","value":"?"}, {"suit":"?","value":"?"}]`
  );

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

  // Dummy opponent's hand
  const opponents = [
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
    { suit: "?", value: "?" },
  ];

  return (
    <div className="h-screen flex justify-center items-center relative">
      <div className="absolute bottom-8" style={{ transform: "rotate(0deg)" }}>
        <Hand cards={cards} />
      </div>
      <div className="absolute left-0" style={{ transform: "rotate(90deg)" }}>
        <Hand cards={opponents} />
      </div>
      <div className="absolute top-8" style={{ transform: "rotate(180deg)" }}>
        <Hand cards={opponents} />
      </div>
      <div className="absolute right-0" style={{ transform: "rotate(270deg)" }}>
        <Hand cards={opponents} />
      </div>
    </div>
  );
}
