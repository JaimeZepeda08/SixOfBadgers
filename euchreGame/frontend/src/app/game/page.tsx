"use client";

import { useEffect, useState } from "react";
import { getPlayerHand } from "@/lib/gameService";
import Hand from "../../components/Hand";

export default function Home() {
  const [playerHand, setPlayerHand] = useState<string>("");

  useEffect(() => {
    async function fetchPlayerHand() {
      try {
        const hand = await getPlayerHand();
        if (hand) {
          setPlayerHand(hand);
        } else {
          console.error("Player hand is undefined");
        }
      } catch (error) {
        console.error(error);
      }
    }
    fetchPlayerHand();
  }, []);

  const cards = [
    { suit: "hearts", value: "A" },
    { suit: "clubs", value: "Q" },
    { suit: "spades", value: "J" },
    { suit: "diamonds", value: "K" },
    { suit: "clubs", value: "Q" },
    { suit: "spades", value: "J" },
  ];

  const opponents = [
    { suit: "?", value: "?" },
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
