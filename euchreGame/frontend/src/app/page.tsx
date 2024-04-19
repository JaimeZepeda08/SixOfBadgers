import React from "react";
import { SimpleButtonLinkRed } from "@/components/SimpleButton";

/**
 * Page component representing a page with a link to navigate to saved games.
 *
 * @returns {JSX.Element} The page component.
 */
export default function Page() {
  return (
    <div
      className="flex flex-col justify-center items-center"
      style={{ zoom: 0.8 }}
    >
      <h1 className="text-6xl font-bold text-red-900 mb-40 mt-36">
        Euchre With Friends
      </h1>
      {/* List of buttons for all game options */}
      <SimpleButtonLinkRed
        text="Singleplayer"
        href="home/singleplayer"
        className="my-6"
      />
      <SimpleButtonLinkRed
        text="Multiplayer"
        href="home/multiplayer"
        className="my-6"
      />
      <SimpleButtonLinkRed
        text="Go to Saved Games"
        href="home/saved_games"
        className="my-6"
      />
    </div>
  );
}
