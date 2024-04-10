import React from "react";
import { SimpleButtonLink } from "@/components/SimpleButton";

export default function Page() {
  return (
    <div className="flex flex-col justify-center items-center">
      <h1 className="text-6xl font-bold text-red-900 mb-40 mt-36">
        Euchre With Friends
      </h1>
      <SimpleButtonLink
        text="Singleplayer"
        href="home/singleplayer"
        className="my-6"
      />
      <SimpleButtonLink
        text="Multiplayer"
        href="home/multiplayer"
        className="my-6"
      />
      <SimpleButtonLink
        text="Go to Saved Games"
        href="home/saved_games"
        className="my-6"
      />
    </div>
  );
}
