"use client";

import { useEffect, useState } from "react";
import { getPlayerHand, getOpponents } from "@/lib/gameService";
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

  const [opponentHand, setOpponentHand] = useState(
    `[{"suit":"?","value":"?"}, {"suit":"?","value":"?"}, {"suit":"?","value":"?"}, {"suit":"?","value":"?"}, {"suit":"?","value":"?"}]`
  );

  useEffect(() => {
    async function fetchOpponents() {
      const hand = await getOpponents();
      if (hand && hand.length > 0) {
        setOpponentHand(hand);
      } else {
        console.error("Player hand is undefined");
      }
    }
    fetchOpponents();
  }, []);

  const opponents = JSON.parse(opponentHand);

  return (
    <div className="h-screen flex justify-center items-center relative">
      <div className="absolute bottom-8" style={{ transform: "rotate(0deg)" }}>
        <Hand cards={cards} />
      </div>
      {opponents.map((element: string, index: number) => {
        const opponentCards = [];
        for (let i = 0; i < Number(element); i++) {
          opponentCards.push({ suit: "?", value: "?" });
        }
        const rotation = (index + 1) * 90;
        const dir = ["left-8", "top-8", "right-8"];
        return (
          <div
            key={index}
            className={`absolute ${dir[index]}`}
            style={{ transform: `rotate(${rotation}deg)` }}
          >
            <Hand cards={opponentCards} />
          </div>
        );
      })}
    </div>
  );
}
