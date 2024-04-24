import React from "react";
import { SimpleButtonLinkRed } from "@/components/SimpleButton";

/**
 * Page component representing a page with a link to navigate to saved games.
 *
 * @returns {JSX.Element} The page component.
 */
export default function Page() {
  // Load test decision context
  return (
    <div
      className={"flex flex-col justify-center items-center"}
    >
      <h1 className="text-6xl font-bold text-red-900 mb-40 mt-36">
        Euchre With Friends
      </h1>
      {/* List of buttons for all game options */}
      <div style={{ zoom: 1.2 }}>
        <SimpleButtonLinkRed
          text="Singleplayer"
          href="/singleplayer"
          className="my-6"
        />
        <SimpleButtonLinkRed
          text="Multiplayer"
          href="/multiplayer"
          className="my-6"
        />
      </div>
    </div>
  );
}
